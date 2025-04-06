package projet.scrapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component
public class StartEmailSender {

    @Autowired
    private EmailService emailService;
    @Autowired TendanceRepository tendanceRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Scheduled(fixedRate = 600000000)
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
                            System.out.println("utilisateur"+utilisateur.getEmail());
                            System.out.println("je commence");
                            String to = utilisateur.getEmail();
                            String subject = "🔔 Prix en baisse pour " + article.getNomA();
                            String content = "Bonjour !" +
                                    "Le dernier prix observé pour " + article.getNomA() + "sur le site " + site.getNomSite() + " est de " +
                                    tendance.getPrix() +"." +
                                    "C'est en dessous de votre seuil de " + article.getSeuil() + " €.";

                            emailService.sendEmail(to, subject, content);
                            // Afficher un message dans la console pour indiquer que l'email a été envoyé
                            System.out.println("✅ Email envoyé pour l'article: " + article.getNomA() + " à l'adresse: " + to);

                            break;
                        }
                    }
                }
            }
        }
        System.out.println("✅ Vérification des prix terminée. Emails envoyés si nécessaire.");
    }
}
