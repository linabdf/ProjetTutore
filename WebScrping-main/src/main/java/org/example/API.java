package org.example;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;


public class API {

    //Permet de recuperer la liste des articles
    public ArrayList<Integer> getNumArtile() {
        int numA;
        ArrayList<Integer> list = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = Main.INSTANCE.dm.getConnexion().prepareStatement("SELECT numa FROM article a WHERE a.numa NOT IN (SELECT numa FROM site WHERE urlarticle is null)");

            ResultSet rs = preparedStatement.executeQuery();
            while (rs != null && rs.next()) {
                numA = rs.getInt("numa");
                list.add(numA);
            }
        } catch (SQLException e) {
            System.out.println("[Base de donnée] Erreur dans la fonction getNumArticle() (" + e.getMessage() + ")");
        }
        return list;
    }

    //Permet de savoir si une mise à jour a lieu pour les aricles
    public boolean getDerniereUpdate(){
        Timestamp frequence;
        Timestamp min = Timestamp.valueOf(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        Timestamp max = new Timestamp(Time.valueOf("00:00:00").getTime());
        boolean nouvelarticle = false;
        try{
            PreparedStatement preparedStatement = Main.INSTANCE.dm.getConnexion().prepareStatement("SELECT derniereupdate FROM article");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs != null && rs.next()) {
                frequence = rs.getTimestamp("derniereupdate");
                if(frequence.before(min)){
                    min = frequence;
                    System.out.println("La frequence min est:" + min);
                }
                if(frequence.after(max)){
                    max = frequence;
                    System.out.println("La frequence max est:" + max);
                }
            }
            setDerniereUpdate();
            if(min.before(max)){
                nouvelarticle = true;
            }
        }catch (SQLException e){
            System.out.println("[Base de donnée] Erreur dans la fonction getDerniereUpdate() (" + e.getMessage() + ")");
        }
        return nouvelarticle;
    }


    //Permet de mettre à jour l'attribut derniereupdate
    public void setDerniereUpdate(){
        Timestamp time = Timestamp.valueOf(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        try{
            PreparedStatement preparedStatement = Main.INSTANCE.dm.getConnexion().prepareStatement("UPDATE article SET derniereupdate = ?");
            preparedStatement.setTimestamp(1,time);
            preparedStatement.executeUpdate();

        }catch (SQLException e){
            System.out.println("[Base de donnée] Erreur dans la fonction setDerniereUpdate() (" + e.getMessage() + ")");
        }
    }

    //Recuperer les information d'un article
    public Article getArticle(int numA){
        Article a = null;
        try{
            PreparedStatement preparedStatement = Main.INSTANCE.dm.getConnexion().prepareStatement("SELECT * from article WHERE numa = ?");
            preparedStatement.setInt(1,numA);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs != null && rs.next()) {
                String nomarticle = rs.getString("noma");
                String desciption = rs.getString("description");
                String image = rs.getString("url_image");
                int seuil = rs.getInt("seuil");
                int frequence = rs.getInt("frequence");
                Timestamp derniereupdate = rs.getTimestamp("derniereupdate");
                int numU = rs.getInt("numu");

                a = new Article(numA,nomarticle,desciption,image,seuil,frequence,derniereupdate,numU);
            }
        }catch (SQLException e){
            System.out.println("[Base de donnée] Erreur dans la fonction getArticle() (" + e.getMessage() + ")");
        }
        return a;
    }

    //Recuperer les information d'un site
    public ArrayList<Site> getSite(int numA){
        ArrayList<Site> s = new ArrayList<>();
        try{
            PreparedStatement preparedStatement = Main.INSTANCE.dm.getConnexion().prepareStatement("SELECT * from site WHERE numa = ?");
            preparedStatement.setInt(1,numA);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs != null && rs.next()) {
                int numS = rs.getInt("numS");
                String urlarticle = rs.getString("urlarticle");
                String nomsite = rs.getString("nom_site");

                s.add(new Site(numS,urlarticle,nomsite,numA));
            }
        }catch (SQLException e){
            System.out.println("[Base de donnée] Erreur dans la fonction getSite() (" + e.getMessage() + ")");
        }
        return s;
    }



    //Recuperer les information de tendance
    public Tendance getTendance(int numS){
        Tendance t = null;
        try{
            PreparedStatement preparedStatement = Main.INSTANCE.dm.getConnexion().prepareStatement("SELECT * from tendance WHERE nums = ?");
            preparedStatement.setInt(1,numS);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs != null && rs.next()) {
                int numT = rs.getInt("numS");
                String prix = rs.getString("prix");
                Timestamp date = rs.getTimestamp("date");

                t = new Tendance(numT,prix,date,numS);
            }
        }catch (SQLException e){
            System.out.println("[Base de donnée] Erreur dans la fonction getTendance() (" + e.getMessage() + ")");
        }
        return t;
    }


    //Recuperer les information de infosite
    public InfoSite getInfoSite(String nom){
        InfoSite is = null;
        try{
            PreparedStatement preparedStatement = Main.INSTANCE.dm.getConnexion().prepareStatement("SELECT * from info_site WHERE nom = ?");
            preparedStatement.setString(1,nom);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs != null && rs.next()) {
                String url = rs.getString("url");
                String listeproduits = rs.getString("listeproduits");
                String article = rs.getString("article");
                String titre = rs.getString("titre");
                String prix = rs.getString("prix");
                String prix2 = rs.getString("prix2");
                String description = rs.getString("description");
                String image = rs.getString("image");

                is = new InfoSite(nom,url,listeproduits,article,titre,prix,prix2,description,image);
            }
        }catch (SQLException e){
            System.out.println("[Base de donnée] Erreur dans la fonction getInfoSite() (" + e.getMessage() + ")");
        }
        return is;
    }


    //Permet d'ajouter une tendance pour un article donnée
    public void setTendance(String prix,int numS, Timestamp date){
        try{
            PreparedStatement preparedStatement = Main.INSTANCE.dm.getConnexion().prepareStatement("INSERT INTO tendance (numT,prix,date,numS) VALUES(?,?,?,?)");
            preparedStatement.setInt(1,generateNextTendanceId());
            preparedStatement.setString(2,prix);
            preparedStatement.setTimestamp(3,date);
            preparedStatement.setInt(4,numS);
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            System.out.println("[Base de donnée] Erreur dans la fonction setTendance() (" + e.getMessage() + ")");
        }
    }


    // Génération d'un ID pour une nouvelle tendance
    public  int generateNextTendanceId() {
        int newId = 0;
        try {
            PreparedStatement preparedStatement = Main.INSTANCE.dm.getConnexion().prepareStatement("SELECT MAX(numT) AS lastId FROM tendance");
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                int lastId = rs.getInt("lastId");
                newId = lastId+1;
            }
        } catch (SQLException e) {
            System.out.println("[Base de donnée] Erreur dans la fonction generateNextTendanceId() (" + e.getMessage() + ")");
        }
        return newId;
    }


    //Permet d'inserer la description d'un article
    public void setDescription(int numA, String description){
        try{
            PreparedStatement preparedStatement = Main.INSTANCE.dm.getConnexion().prepareStatement("UPDATE article SET description = ? WHERE numa = ?");
            preparedStatement.setString(1,description);
            preparedStatement.setInt(2,numA);
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            System.out.println("[Base de donnée] Erreur dans la fonction setDescription() (" + e.getMessage() + ")");
        }
    }

    //Permet d'inserer l'url d'une image pour un article
    public void setImage(int numA, String image){
        try{
            PreparedStatement preparedStatement = Main.INSTANCE.dm.getConnexion().prepareStatement("UPDATE article SET url_image = ? WHERE numa = ?");
            preparedStatement.setString(1,image);
            preparedStatement.setInt(2,numA);
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            System.out.println("[Base de donnée] Erreur dans la fonction setImage() (" + e.getMessage() + ")");
        }
    }

    //Permet d'inserer l'url d'un article
    public void setSiteUrl(String urlarticle,int numA, String nomsite){
        try{
            PreparedStatement preparedStatement = Main.INSTANCE.dm.getConnexion().prepareStatement("UPDATE site set urlarticle = ? WHERE numa= ? and nom_site = ?");
            preparedStatement.setString(1,urlarticle);
            preparedStatement.setInt(2,numA);
            preparedStatement.setString(3,nomsite);
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            System.out.println("[Base de donnée] Erreur dans la fonction setSiteUrl() (" + e.getMessage() + ")");
        }
    }

    //Permet de recuperer l'url d'un article
    public String getSiteUrl(int numA, String nomsite){
        String urlarticle = "";
        try{
            PreparedStatement preparedStatement = Main.INSTANCE.dm.getConnexion().prepareStatement("SELECT urlarticle FROM site WHERE numa = ? and nom_site = ?");
            preparedStatement.setInt(1,numA);
            preparedStatement.setString(2,nomsite);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs != null && rs.next()) {
                urlarticle = rs.getString("urlarticle");
            }
        }catch (SQLException e){
            System.out.println("[Base de donnée] Erreur dans la fonction getSiteUrl() (" + e.getMessage() + ")");
        }
        return urlarticle;
    }


    //Permet de recuperer la liste des nouveaux articles
    public List<Integer> getNewArticle(){
        List<Integer> newarticle = new ArrayList<>();
        try{
            PreparedStatement preparedStatement = Main.INSTANCE.dm.getConnexion().prepareStatement("SELECT numa FROM article a WHERE a.numa NOT IN (SELECT numa FROM site WHERE urlarticle = ?)");
            preparedStatement.setString(1,null);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs != null && rs.next()) {
                newarticle.add(rs.getInt("numa"));
            }
        }catch (SQLException e){
            System.out.println("[Base de donnée] Erreur dans la fonction getNewArticle() (" + e.getMessage() + ")");
        }
        return newarticle;
    }
}
