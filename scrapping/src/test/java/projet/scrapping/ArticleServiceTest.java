/*package projet.scrapping;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import projet.scrapping.Article;
import projet.scrapping.Utilisateur;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Timestamp;
import java.util.List;
@SpringBootTest
    public class ArticleServiceTest {
        @Autowired
        private ArticleService articleService;

        @Autowired
        private ArticleRepository articleRepository;

        @Autowired
        private SiteRepository siteRepository;

        @Autowired
        private UtilisateurRepository utilisateurRepository;
        @Autowired
        private StartEmailSender startEmailSender;

    @Autowired
    private NotificationEnvoyeeRepository notificationEnvoyeeRepository;
    @Autowired
    private TendanceService tendanceService;

    @Test
    public void testAddSitesToArticle() {
        // Création d'un utilisateur
        Utilisateur utilisateur = new Utilisateur("Jeazn", "Dupzont", "userz@example.com", "motdepasse");
        utilisateur = utilisateurRepository.save(utilisateur);

        // Création et sauvegarde d'un article
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Article article = new Article("Article", 150.0, utilisateur, "email", 15, timestamp, 1);
        article = articleRepository.save(article);
        assertNotNull(article, "L'article ne doit pas être null après l'insertion.");

        // Création de sites
        Site site1 = new Site("", "amazon", article);
        Site site2 = new Site("", "fnac", article);

        // Sauvegarde des sites
        siteRepository.save(site1);
        siteRepository.save(site2);

        // Vérification des sites associés à l'article
        Article retrievedArticle = articleService.getArticleById(article.getNumA()).orElse(null);
        assertNotNull(retrievedArticle, "L'article récupéré ne doit pas être null.");
        List<Site> sites = retrievedArticle.getSites();
        assertNotNull(sites, "La liste des sites ne doit pas être null.");
        assertEquals(2, sites.size(), "L'article doit avoir 2 sites associés.");
        // WHEN: Appel de la méthode checkLastTendancesAndSendEmail
        Tendance tendance = tendanceService.ajouterTendanceAuSite(site1, "100",timestamp);
        startEmailSender.checkLastTendancesAndSendEmail();

        // THEN: Vérification des notifications envoyées
        List<NotificationEnvoyee> notifications = notificationEnvoyeeRepository.findAll();

        // Vérifier qu'une notification email a été envoyée
        assertTrue(notifications.stream().anyMatch(n -> n.getTypeNotif().equals("email")),
                "Une notification email doit avoir été envoyée.");

        // Vérifier qu'une notification push a été envoyée
        assertTrue(notifications.stream().anyMatch(n -> n.getTypeNotif().equals("push")),
                "Une notification push doit avoir été envoyée.");
    }
    }

        // Ajoute les autres tests ici...


*/