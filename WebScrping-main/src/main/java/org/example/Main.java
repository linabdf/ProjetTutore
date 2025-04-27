package org.example;


import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static Main INSTANCE;
    public DatabaseManager dm;
    private final API api = new API();
    private final WebScrapping ws = new WebScrapping();

    private Scraping scraping;


    public Main() {
        INSTANCE = this;
        dm = new DatabaseManager("jdbc:mysql://", "localhost", "scrapping", "root", "your_new_password");
        dm.connexion();
    }


    //Permet de trouver le prochain article à scraper
    public Scraping run() {
        try {
            String nomsite = "";
            String urlarticle = "";
            int numA = 0;
            int numS = 0;
            int frequence;
            Timestamp dernieredate;
            Timestamp min = Timestamp.valueOf("2030-01-01 23:59:59");


            // liste qui contient tout les articles de la table article
            ArrayList<Integer> list = api.getNumArtile();

            for (int listarticle : list) {

                Article a = api.getArticle(listarticle);
                ArrayList<Site> s = api.getSite(listarticle);

                for (Site listsite : s) {
                    Tendance t = api.getTendance(listsite.getNumS());

                    int seuil = a.getSeuil();

                    // frequence a laquelle il faut lancer le scraping
                    frequence = a.getFrequence();
                    frequence = frequence * 60 * 1000;

                    // date a laquelle le dernier scraping a été fait
                    dernieredate = t.getDate();

                    //heure du prochain scraping
                    long totalMillis = dernieredate.getTime() + frequence - Time.valueOf("01:00:00").getTime();
                    Timestamp result = new Timestamp(totalMillis);

                    System.out.println("La derniere mise a jour de l'article " + listarticle + " été le " + dernieredate);
                    System.out.println("La frequence de mise à jour de l'article " + listarticle + " est de " + frequence / (1000 * 60) + " minutes");
                    System.out.println("La prochaine mise à jour aura lieu à " + result);
                    System.out.println("L'article " + listarticle + " a un seuil de " + seuil);
                    System.out.println("L'article " + listarticle + " est sur " + listsite.getNomsite() + " avec comme lien " + listsite.getUrlarticle());


                    //on garde les information de l'article qui est le plus proche du scraping
                    if (result.before(min)) {
                        min = result;
                        numA = listarticle;
                        nomsite = listsite.getNomsite();
                        urlarticle = listsite.getUrlarticle();

                    }
                }
            }

            scraping = new Scraping(numA, nomsite, urlarticle, min);

        } catch (Exception e) {
            System.out.println("[Main] Erreur dans la fonction run() (" + e.getMessage() + ")");
            dm.deconnexion();
        }
        return scraping;
    }


    //Permet de lancer le scraping
    public void scraping(boolean newarticle){
        try {
            if (!newarticle) { //Si l'article est deja present dans la base


                ArrayList<Site> site = api.getSite(scraping.getNumA());
                //Temp actuel
                Timestamp currentTime2 = Timestamp.valueOf(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

                for (Site listsite : site) {
                    InfoSite is = api.getInfoSite(listsite.getNomsite());
                    String selecteurprix = is.getPrix2();
                    String s = api.getSiteUrl(scraping.getNumA(),listsite.getNomsite());

                    //WebScapping
                    Scraping result = ws.PremierScraping(false,"",s,"", selecteurprix,"", "", "","","");

                    //Mise à jour de la tendance du prix dans la BD
                    api.setTendance(result.getPrix(), listsite.getNumS(), currentTime2);
                }



            } else { //Nouvel article
                List<Integer> newarticles = api.getNewArticle();

                for (int article : newarticles) {
                    System.out.println("L'article " + article + " vient d'etre ajouté");

                    ArrayList<Site> s = api.getSite(article); // Liste des site pour un article

                    //Temp actuel
                    Timestamp currentTime2 = Timestamp.valueOf(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

                    for (Site site : s) {
                        Article a = api.getArticle(article);
                        InfoSite is = api.getInfoSite(site.getNomsite());

                        String input = a.getNomarticle(); //article recherché
                        String url = is.getUrl(); //url de recherche
                        String lienproduit = is.getListeproduits(); // selecteur liste article
                        String selecteurArticle = is.getArticle(); // selecteur article
                        String selecteurTitre = is.getTitre(); //selecteur titre
                        String selecteurprix = is.getPrix2(); //selecteur prix
                        String description = is.getDescription(); //selecteur description
                        String image = is.getImage(); //selecteur image


                        //Creation de L'url de rechere
                        String finalurl = ws.makeUrl(url, input);

                        //Creation de l'input
                        String finalinput = ws.makerechere(input);

                        //WebScapping
                        Scraping result = ws.PremierScraping(true,finalurl,"", lienproduit, selecteurprix, selecteurTitre, selecteurArticle, finalinput,image,description);

                        if(result == null){
                            //Mise à jour de la description dans la BD
                            String description2 = api.getDescription(article);
                            if(description2 == null || description2.isEmpty()){
                                api.setDescription(article,"Aucun article trouvé");
                            }
                        }else{
                            //Mise à jour de la description dans la BD
                            String description2 = api.getDescription(article);
                            if(description2 == null || description2.isEmpty()){
                                api.setDescription(article,result.getDescription());
                            }


                            //Mise à jour de l'image dans la BD
                            api.setImage(article,result.getImage());

                            //Mise à jour de l'url de l'article dans la BD
                            api.setSiteUrl(result.getUrlarticle(), article,site.getNomsite());



                            // Mise à jour de la tendance du prix dans la BD
                            api.setTendance(result.getPrix(), site.getNumS(), currentTime2);
                        }

                    }
                }
            }
        }catch(Exception e){
            System.out.println("[Main] Erreur dans la fonction scraping() (" + e.getMessage() + ")");
            dm.deconnexion();
        }
    }


    //Verifie si il y a des mise à jour
    public boolean update(){
        return api.getDerniereUpdate();
    }
    //Verifie si il y a des mise à jour à faire immediatement
    public List<Integer> updateNow(){
        return api.getUpdateNow();
    }
}