package projet.scrapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

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
        boolean skipNotification ;
        boolean skipEmailNotification;
        boolean skipPushNotification;

        for (Article article : articles) {

            List<Site> sites = article.getSites();

            for (Site site : sites) {
                skipNotification = false;
                skipEmailNotification=false;
                skipPushNotification=false;
                Optional<Tendance> lastTendance = tendanceRepository
                        .findTopBySiteOrderByNumTDesc(site);

                if (lastTendance.isPresent()) {
                    Tendance tendance = lastTendance.get();
                    String prixS = tendance.getPrix();
                    System.out.println("Tendance " + tendance.getPrix() + " pour l'article " + article.getNomA() + " sur le site " + site.getNomSite());

                    if (prixS != null && !prixS.isEmpty()) {
                        prixS = prixS.trim()
                                .replaceFirst("^€", "");
                        prixS = prixS.replaceAll("€", ".");
                        prixS = prixS.replaceAll("[^0-9.]", "");
                        int firstDotIndex = prixS.indexOf('.');
                        if (firstDotIndex != -1) {
                            // S'il y a un point, on enlève tous les autres après le premier
                            prixS = prixS.substring(0, firstDotIndex + 1) +
                                    prixS.substring(firstDotIndex + 1).replace(".", "");
                        }
                        double prixDecimal = Double.parseDouble(prixS.replace(",", "."));
                        System.out.println("prixDecimal: " + prixDecimal);
                        System.out.println("Seuil de l'article (" + article.getNomA() + "): " + article.getSeuil());
                        if (prixDecimal < article.getSeuil()) {
                            Utilisateur utilisateur = article.getUtilisateur();
                            String typeNotif = article.getNotif();
                            if (("email".equalsIgnoreCase(typeNotif))) {
                            String content = "Bonjour !\n\n" +
                                        "Le dernier prix observé pour " + article.getNomA() +
                                        " sur le site " + site.getNomSite() + " est de " +
                                        tendance.getPrix() + ".\n le " + tendance.getDate() +
                                        "C'est en dessous de votre seuil de " + article.getSeuil() + " €.";
                                Optional<NotificationEnvoyee> optionalNotif = notificationEnvoyeeRepository
                                        .findTopByArticleAndTypeNotifOrderByDateEnvoiDesc(article, "email");

                                // Vérification si une notification a déjà été envoyée
                                if (optionalNotif.isPresent()) {
                                    NotificationEnvoyee derniereNotif = optionalNotif.get();
                                    System.out.println("⬇️ Prix actuel : " + prixDecimal + " / Dernier prix notifié : " + derniereNotif.getPrix());
                                    String dernierType = derniereNotif.getTypeNotif();
                                    if (derniereNotif != null && dernierType.equalsIgnoreCase(typeNotif)) {
                                        if (prixDecimal >= derniereNotif.getPrix() && "email".equals(derniereNotif.getTypeNotif())) {
                                            System.out.println("❌ Le prix n'a pas baissé, pas d'envoi d'email.");
                                            skipNotification = true;
                                            skipEmailNotification=true;
                                        } else {
                                            System.out.println("📉 Le prix a baissé, envoi d'email autorisé.");
                                        }
                                    } else {
                                        System.out.println("🔁 Ancienne notification d’un type différent (" + dernierType + "), envoi autorisé.");
                                    }
                                } else {
                                    System.out.println("📨 Première notification pour cet article, envoi autorisé.");
                                }

                                // Envoi si aucune condition de blocage
                                if (/*!skipNotification*/!skipEmailNotification) {
                                    System.out.println("📤 Envoi d'email...");
                                    String to = utilisateur.getEmail();
                                    String subject = "🔔 Prix en baisse pour " + article.getNomA();

                                    emailService.sendEmail(to, subject, content);
                                    System.out.println("✅ Email envoyé à " + to);

                                    // Sauvegarde de la notification
                                    NotificationEnvoyee notif = new NotificationEnvoyee();
                                    notif.setMessage(content);
                                    notif.setPrix(prixDecimal);
                                    notif.setTypeNotif("email");
                                    notif.setDateEnvoi(LocalDateTime.now());
                                    notif.setArticle(article);
                                    notificationEnvoyeeRepository.save(notif);
                                }
                            }
                            // Si la notification push est demandée
                            if (("push".equalsIgnoreCase(typeNotif))) {
                                String message = "Le prix de " + article.getNomA() + " est tombé à " + prixDecimal + "€ !.\n le " + tendance.getDate() +
                                        "C'est en dessous de votre seuil de " + article.getSeuil() + " €.";
                                Optional<NotificationEnvoyee> optionalNotif = notificationEnvoyeeRepository
                                        .findTopByArticleAndTypeNotifOrderByDateEnvoiDesc(article, "push");
                                if (optionalNotif.isPresent() ) {
                                    NotificationEnvoyee derniereNotif = optionalNotif.get();
                                    System.out.println("⬇️ Prix actuel : " + prixDecimal + " / Dernier prix notifié : " + derniereNotif.getPrix());
                                    String dernierType = derniereNotif.getTypeNotif();
                                    if (derniereNotif != null && dernierType.equalsIgnoreCase(typeNotif)) {
                                       if (prixDecimal >= derniereNotif.getPrix() && "push".equals(derniereNotif.getTypeNotif())) {
                                          System.out.println("❌ Le prix n'a pas baissé, pas d'envoi de notification.");
                                          skipNotification = true;
                                          skipPushNotification=true;
                                       }
                                       else {
                                           System.out.println("🔁 Ancienne notification d’un type différent (" + dernierType + ")");

                                       }
                                    }

                                }else  {
                                    System.out.println("📨 Première notification pour cet article, envoi autorisé.");
                                    // forçage explicite (utile si jamais une valeur par défaut traîne)
                                }

                                if (/*!skipNotification*/!skipPushNotification) {
                                    notificationController.sendNotificationToAll(message);
                                    NotificationEnvoyee notif = new NotificationEnvoyee();
                                    notif.setPrix(prixDecimal);
                                    notif.setMessage(message);
                                    notif.setDateEnvoi(LocalDateTime.now());
                                    notif.setTypeNotif("push");
                                    notif.setArticle(article);
                                    notificationEnvoyeeRepository.save(notif);
                                    System.out.println("✅ Notification envoyée et sauvegardée.");
                            }

                        }
                            if ("deux".equalsIgnoreCase(typeNotif)) {
                                Optional<NotificationEnvoyee> lastNotifEmail = notificationEnvoyeeRepository
                                        .findTopByArticleAndTypeNotifOrderByDateEnvoiDesc(article, "email");

                                Optional<NotificationEnvoyee> lastNotifPush = notificationEnvoyeeRepository
                                        .findTopByArticleAndTypeNotifOrderByDateEnvoiDesc(article, "push");

                                boolean canSendEmail = true;
                                boolean canSendPush = true;

// Vérification pour l'email
                                if (lastNotifEmail.isPresent()) {
                                    double lastEmailPrice = lastNotifEmail.get().getPrix();
                                    if (prixDecimal >= lastEmailPrice) {
                                        canSendEmail = false;
                                        System.out.println("❌ Email pas envoyé : prix (" + prixDecimal + "€) >= dernier email (" + lastEmailPrice + "€)");
                                    }
                                }

// Vérification pour le push
                                if (lastNotifPush.isPresent()) {
                                    double lastPushPrice = lastNotifPush.get().getPrix();
                                    if (prixDecimal >= lastPushPrice) {
                                        canSendPush = false;
                                        System.out.println("❌ Push pas envoyé : prix (" + prixDecimal + "€) >= dernier push (" + lastPushPrice + "€)");
                                    }
                                }

// Si on peut envoyer l'email
                                if (canSendEmail) {
                                    String emailContent = "Bonjour !\n\n" +
                                            "Le prix de " + article.getNomA() + " est tombé à " + tendance.getPrix() +
                                            " sur " + site.getNomSite() + " le " + tendance.getDate() + ".\n" +
                                            "Ce prix est en dessous de votre seuil de " + article.getSeuil() + "€.";

                                    emailService.sendEmail(utilisateur.getEmail(),
                                            "🔔 Prix en baisse pour " + article.getNomA(),
                                            emailContent);

                                    NotificationEnvoyee emailNotif = new NotificationEnvoyee();
                                    emailNotif.setArticle(article);
                                    emailNotif.setDateEnvoi(LocalDateTime.now());
                                    emailNotif.setTypeNotif("email");
                                    emailNotif.setPrix(prixDecimal);
                                    emailNotif.setMessage(emailContent);
                                    notificationEnvoyeeRepository.save(emailNotif);

                                    System.out.println("✅ Email envoyé.");
                                }

// Si on peut envoyer le push
                                if (canSendPush) {
                                    String pushMessage = "📉 Le prix de " + article.getNomA() + " est tombé à " + prixDecimal + "€ sur " + site.getNomSite() + " le " +
                                            tendance.getDate()+".C'est en dessous de votre seuil de " + article.getSeuil() + " €.";;

                                    notificationController.sendNotificationToAll(pushMessage);

                                    NotificationEnvoyee pushNotif = new NotificationEnvoyee();
                                    pushNotif.setArticle(article);
                                    pushNotif.setDateEnvoi(LocalDateTime.now());
                                    pushNotif.setTypeNotif("push");
                                    pushNotif.setPrix(prixDecimal);
                                    pushNotif.setMessage(pushMessage);
                                    notificationEnvoyeeRepository.save(pushNotif);

                                    System.out.println("✅ Notification push envoyée.");
                                }


                                if (!canSendEmail && !canSendPush) {
                                    System.out.println("❌ Aucune notification envoyée.");
                                }
                            }}}
                }
            }
        }

        System.out.println("✅ Vérification des prix terminée. Emails envoyés si nécessaire.");
    }
}
