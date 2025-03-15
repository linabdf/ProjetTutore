package org.example;



import java.sql.Timestamp;
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
        Run runInstance = new Run(mainInstance);
        runInstance.start(20,20);
    }

    public void start(int delay,int delay2) {
      scheduleTask(delay , false); //Tache principale pour le scraping
        checkUpdate(delay2); //Tache secondaire qui verifie si des mise a jour sont effectué
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
                System.out.println("je suis la ");
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
}

