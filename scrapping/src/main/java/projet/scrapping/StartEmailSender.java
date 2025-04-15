package projet.scrapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.management.Notification;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class StartEmailSender {
@Autowired
private  NotificationController notificationController;
    @Autowired
    private EmailService emailService;
    @Autowired
    private TendanceRepository tendanceRepository;

    @Autowired
    private NotificationEnvoyeeRepository notificationEnvoyeeRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired NotificationService notificationService;
    @Scheduled(fixedRate = 60000)
    public void checkLastTendancesAndSendEmail() {
        List<Article> articles = articleRepository.findAll();
        for (Article article : articles) {
            List<Site> sites = article.getSites();
            for (Site site : sites) {
                // On récupère la dernière tendance pour ce site
                Optional<Tendance> lastTendance = tendanceRepository
                        .findTopBySiteOrderByNumTDesc(site); // ou par date si tu as un champ date
                if (lastTendance.isPresent()) {
                    Tendance tendance = lastTendance.get();
                    String prixS = tendance.getPrix();
                    System.out.println("Tendance"+tendance.getPrix()+"article"+article.getNomA()+"site"+site.getNomSite()+"Article"+article.getSeuil());
                    if (prixS != null && !prixS.isEmpty()) {
                        // Nettoyage du prix pour enlever les caractères non numériques (autres que , ou .)
                        prixS = prixS.trim().replace("€", ".").replaceAll("[^0-9,\\.]", ""); // Supprimer "€" et autres caractères
                        double prixDecimal = Double.parseDouble(prixS.replace(",", "."));
                        System.out.println("prixDecimal: " + prixDecimal);
                        System.out.println("Seuil de l'article (" + article.getNomA() + "): " + article.getSeuil());
                        if (prixDecimal < article.getSeuil()) {
                            Utilisateur utilisateur = article.getUtilisateur();
                            String typeNotif = article.getNotif(); // <-- vérifie ici le type

                            if ("email".equalsIgnoreCase(typeNotif)) {
                                NotificationEnvoyee derniereNotif = article.getDerniereNotification();
                                if (derniereNotif != null) {
                                    // Si le prix actuel est plus élevé ou égal à celui de la dernière notification, ne rien faire
                                    if (prixDecimal >= derniereNotif.getPrix()) {
                                        // Le prix n'a pas baissé, donc on ne fait rien
                                        System.out.println("❌ Le prix n'a pas baissé, pas d'envoie de mail.");
                                        return; // Sortir de la méthode sans envoyer de notification
                                    }
                                }
                                // Envoi d'email
                                String to = utilisateur.getEmail();
                                String subject = "🔔 Prix en baisse pour " + article.getNomA();
                                String content = "Bonjour !\n\n" +
                                        "Le dernier prix observé pour " + article.getNomA() +
                                        " sur le site " + site.getNomSite() + " est de " +
                                        tendance.getPrix() + ".\n" +
                                        "C'est en dessous de votre seuil de " + article.getSeuil() + " €.";

                                emailService.sendEmail(to, subject, content);
                                System.out.println("✅ Email envoyé à " + to);
                                NotificationEnvoyee notif = new NotificationEnvoyee();
                                notif.setMessage(content);
                                notif.setPrix(prixDecimal); // Prix actuel
                                notif.setDateEnvoi(LocalDateTime.now()); // Date et heure d'envoi de la notification
                                notif.setArticle(article); // Associe la notification à l'article

                                // Persister la nouvelle notification
                                notificationEnvoyeeRepository.save(notif);

                                // Met à jour l'article avec la dernière notification envoyée
                                article.setDerniereNotification(notif);
                                articleRepository.save(article);
                            } /*if ("push".equalsIgnoreCase(typeNotif)) {

                                notificationService.envoyerNotif(utilisateur.getEmail(), article.getNomA(),  prixDecimal,site.getNomSite());


                            System.out.println("📲 Notification push envoyée à l'utilisateur : " + utilisateur.getEmail());
                            }

                            break;
*/
                            if ("push".equalsIgnoreCase(typeNotif)) {
                                String message = "📉 Le prix de " + article.getNomA() + " est tombé à " + prixDecimal + "€ !";

                                // Vérifier si une notification a déjà été envoyée
                                NotificationEnvoyee derniereNotif = article.getDerniereNotification();
                                if (derniereNotif != null) {
                                    // Si le prix actuel est plus élevé ou égal à celui de la dernière notification, ne rien faire
                                    if (prixDecimal >= derniereNotif.getPrix()) {
                                        // Le prix n'a pas baissé, donc on ne fait rien
                                        System.out.println("❌ Le prix n'a pas baissé, pas de nouvelle notification.");
                                        return; // Sortir de la méthode sans envoyer de notification
                                    }
                                }

                                // Envoie la notif vers le front via SSE
                                notificationController.sendNotificationToAll(message); // Envoie au front via SSE

                                // ✅ Sauvegarde la notification
                                NotificationEnvoyee notif = new NotificationEnvoyee();
                                notif.setMessage(message);
                                notif.setPrix(prixDecimal); // Prix actuel
                                notif.setDateEnvoi(LocalDateTime.now()); // Date et heure d'envoi de la notification
                                notif.setArticle(article); // Associe la notification à l'article

                                // Persister la nouvelle notification
                                notificationEnvoyeeRepository.save(notif);

                                // Met à jour l'article avec la dernière notification envoyée
                                article.setDerniereNotification(notif);
                                articleRepository.save(article);
                            }



                            break;}
                    }
                }
            }
        }
        System.out.println("✅ Vérification des prix terminée. Emails envoyés si nécessaire.");
    }
}
