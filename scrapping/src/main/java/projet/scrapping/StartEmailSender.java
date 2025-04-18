package projet.scrapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class StartEmailSender {
    @Autowired
    private NotificationController notificationController;

    @Autowired
    private EmailService emailService;

    @Autowired
    private TendanceRepository tendanceRepository;

    @Autowired
    private NotificationEnvoyeeRepository notificationEnvoyeeRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private NotificationService notificationService;

    @Scheduled(fixedRate = 60000)
    public void checkLastTendancesAndSendEmail() {
        List<Article> articles = articleRepository.findAll();
        boolean skipNotification = false;

        for (Article article : articles) {

            List<Site> sites = article.getSites();

            for (Site site : sites) {
                // On récupère la dernière tendance pour ce site
                Optional<Tendance> lastTendance = tendanceRepository
                        .findTopBySiteOrderByNumTDesc(site); // ou par date si tu as un champ date

                if (lastTendance.isPresent()) {
                    Tendance tendance = lastTendance.get();
                    String prixS = tendance.getPrix();
                    System.out.println("Tendance " + tendance.getPrix() + " pour l'article " + article.getNomA() + " sur le site " + site.getNomSite());

                    if (prixS != null && !prixS.isEmpty()) {
                        // Nettoyage du prix pour enlever les caractères non numériques (autres que , ou .)
                        prixS = prixS.trim().replace("€", ".").replaceAll("[^0-9,\\.]", ""); // Supprimer "€" et autres caractères
                        double prixDecimal = Double.parseDouble(prixS.replace(",", "."));
                        System.out.println("prixDecimal: " + prixDecimal);
                        System.out.println("Seuil de l'article (" + article.getNomA() + "): " + article.getSeuil());

                        // Si le prix est inférieur au seuil, on envoie la notification
                        if (prixDecimal < article.getSeuil()) {
                            Utilisateur utilisateur = article.getUtilisateur();
                            String typeNotif = article.getNotif();
                            Optional<NotificationEnvoyee> optionalNotif = notificationEnvoyeeRepository.findTopByArticle(article);

                            if ("email".equalsIgnoreCase(typeNotif)) {
                                // Si une notification précédente existe, on vérifie le prix
                                if (optionalNotif.isPresent()) {
                                    NotificationEnvoyee derniereNotif = optionalNotif.get();

                                    if (derniereNotif != null) {
                                        // Si le prix actuel est plus élevé ou égal à celui de la dernière notification, ne rien faire
                                        if (prixDecimal >= derniereNotif.getPrix() &&   "email".equals(derniereNotif.getTypeNotif())) {
                                            System.out.println("❌ Le prix n'a pas baissé, pas d'envoi de mail.");
                                            skipNotification = true;
                                        }
                                    }
                                } else {
                                    System.out.println("Aucune notification trouvée pour cet article.");
                                }

                                if (!skipNotification) {
                                    // Envoi de l'email
                                    String to = utilisateur.getEmail();
                                    String subject = "🔔 Prix en baisse pour " + article.getNomA();
                                    String content = "Bonjour !\n\n" +
                                            "Le dernier prix observé pour " + article.getNomA() +
                                            " sur le site " + site.getNomSite() + " est de " +
                                            tendance.getPrix() + ".\n le " + tendance.getDate() +
                                            "C'est en dessous de votre seuil de " + article.getSeuil() + " €.";

                                    emailService.sendEmail(to, subject, content);
                                    System.out.println("✅ Email envoyé à " + to);

                                    // Créer et persister la nouvelle notification
                                    NotificationEnvoyee notif = new NotificationEnvoyee();
                                    notif.setMessage(content);
                                    notif.setPrix(prixDecimal);
                                    notif.setTypeNotif("email"); // Prix actuel
                                    notif.setDateEnvoi(LocalDateTime.now()); // Date et heure d'envoi de la notification
                                    notif.setArticle(article); // Associe la notification à l'article

                                    // Persister la nouvelle notification
                                    notificationEnvoyeeRepository.save(notif);


                                }
                            }
                            // Si la notification push est demandée
                            if ("push".equalsIgnoreCase(typeNotif)) {
                                String message = "Le prix de " + article.getNomA() + " est tombé à " + prixDecimal + "€ !.\n le " + tendance.getDate() +
                                        "C'est en dessous de votre seuil de " + article.getSeuil() + " €.";

                                // Vérification si une notification a déjà été envoyée
                                if (optionalNotif.isPresent() ) {
                                    NotificationEnvoyee derniereNotif = optionalNotif.get();
                                    System.out.println("⬇️ Prix actuel : " + prixDecimal + " / Dernier prix notifié : " + derniereNotif.getPrix());
                                    if (derniereNotif != null) {
                                    if (prixDecimal >= derniereNotif.getPrix() && "push".equals(derniereNotif.getTypeNotif())) {
                                        System.out.println("❌ Le prix n'a pas baissé, pas d'envoi de notification.");
                                        skipNotification = true;

                                    }}
                                }
                                if (!skipNotification) {
                                // Envoi vers le front
                                notificationController.sendNotificationToAll(message);

                                // Création et persistance de la notification push
                                NotificationEnvoyee notif = new NotificationEnvoyee();
                                notif.setPrix(prixDecimal);
                                notif.setMessage(message);
                                notif.setDateEnvoi(LocalDateTime.now());
                                    notif.setTypeNotif("push");
                                    notif.setArticle(article);

                                // Sauvegarder la notification push
                                notificationEnvoyeeRepository.save(notif);



                                System.out.println("✅ Notification envoyée et sauvegardée.");
                            }

                        }}
                    }
                }
            }
        }

        System.out.println("✅ Vérification des prix terminée. Emails envoyés si nécessaire.");
    }
}
