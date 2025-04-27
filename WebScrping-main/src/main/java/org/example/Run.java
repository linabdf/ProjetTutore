package org.example;



import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import java.time.Instant;



public class Run {

    private final Main main;
    private final ScheduledExecutorService scheduler;
    private ScheduledFuture<?> scheduledTask;


    public Run(Main main) {
        this.main = main;
        this.scheduler = Executors.newScheduledThreadPool(2);
    }

    public static void main(String[] args) {
        Main mainInstance = new Main();
        Run runInstance
                = new Run(mainInstance);
        runInstance.start(10,10,1);
    }

    public void start(int delay, int delay2, int delay3) {
        scheduleTask(delay , false); //Tache principale pour le scraping
        checkUpdate(delay2); //Tache secondaire qui verifie si des mise a jour sont effectué
        checkUpdateNow(delay3); //Tache pour ferifier si un article doit etre mis a jour immediatement
    }



    public void scheduleTask(int delay, boolean update) {
        System.out.println("Recherche de nouveaux articles...");

        try {
            // Planifier la tâche principale et la stocker
            scheduledTask = scheduler.schedule(() -> {
                if (update) { //On fait le scraping seulement si la tache arrive à la fin du delay
                    main.scraping(false);
                }

                Scraping scraping = main.run();

                if (scraping != null) {

                    Timestamp nextUpdate = scraping.getMin();

                    Timestamp currentTime = Timestamp.from(Instant.now());

                    long delayMillis = nextUpdate.getTime() - currentTime.getTime();
                    if (delayMillis < 0) delayMillis = 0; // Exécuter immédiatement si dépassé

                    int newDelay = (int) TimeUnit.MILLISECONDS.toSeconds(delayMillis);
                    System.out.println("Il est actuellement " + currentTime);
                    System.out.println("Prochaine mise à jour à " + nextUpdate);
                    System.out.println("Prochaine exécution dans " + newDelay + " secondes");

                    scheduleTask(newDelay, true);
                }

            }, delay, TimeUnit.SECONDS);

        }catch (Exception e){
            System.out.println("[Run] Erreur dans la fonction scheduleTask() (" + e.getMessage() + ")");
            main.dm.deconnexion();
        }
    }

    public void checkUpdate(int interval) {
        try {
            scheduler.scheduleAtFixedRate(() -> {

                boolean updated = main.update();
                if (updated) {
                    main.scraping(true);
                    System.out.println("Mise à jour détectée ! Exécution immédiate...");

                    // Annule la tâche planifiée si elle est en attente
                    if (scheduledTask != null && !scheduledTask.isDone()) {
                        scheduledTask.cancel(false);
                    }

                    // Exécuter immédiatement la mise à jour
                    scheduleTask(0, false);
                }

                System.out.println("Aucune mise à jour detecté!");

            }, 0, interval, TimeUnit.SECONDS);
        }catch (Exception e){
            System.out.println("[Run] Erreur dans la fonction checkUpdate() (" + e.getMessage() + ")");
            main.dm.deconnexion();
        }
    }
    public void checkUpdateNow(int interval) {
        try {
            scheduler.scheduleAtFixedRate(() -> {

                List<Integer> updated = main.updateNow();

                if (updated.isEmpty()) {
                    System.out.println("Aucune mise à jour detecté!");
                } else {
                    System.out.println("Mise à jour detecté dans le checkUpdateNow!");
                    for (Integer article : updated) {
                        WebScrapping ws = new WebScrapping();
                        API api = new API();
                        List<Site> site = api.getSite(article);
                        //Temp actuel
                        Timestamp currentTime2 = Timestamp.valueOf(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
                        for (Site listsite : site) {
                            InfoSite is = api.getInfoSite(listsite.getNomsite());
                            String selecteurprix = is.getPrix2();
                            String s = api.getSiteUrl(article, listsite.getNomsite());

                            //WebScapping
                            Scraping result = ws.PremierScraping(false, "", s, "", selecteurprix, "", "", "", "", "");

                            //Mise à jour de la tendance du prix dans la BD
                            api.setTendance(result.getPrix(), listsite.getNumS(), currentTime2);
                        }
                    }
                }


            }, 0, interval, TimeUnit.SECONDS);
        } catch (Exception e) {
            System.out.println("[Run] Erreur dans la fonction checkUpdateNow() (" + e.getMessage() + ")");
            main.dm.deconnexion();
        }
    }
}