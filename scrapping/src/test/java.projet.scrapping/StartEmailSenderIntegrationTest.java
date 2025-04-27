

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import projet.scrapping.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ScrappingApplication.class)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)  // Pour garantir que chaque test démarre dans un état propre
public class StartEmailSenderIntegrationTest {

    @Autowired
    private StartEmailSender startEmailSender;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private TendanceRepository tendanceRepository;

    @Autowired
    private NotificationEnvoyeeRepository notificationEnvoyeeRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private SiteRepository siteRepository;

    private Article article;


    @BeforeEach
    void setup() {


        // Récupère un article existant par son nom
        article = articleRepository.findByNomA("iphone 15");

        // Setup tendance (prix < seuil)
        Tendance tendance = new Tendance();
        tendance.setPrix("299.99");
        tendance.setDate(Timestamp.valueOf(LocalDateTime.now()));
        tendanceRepository.save(tendance);
    }

    @Test
    void shouldSendEmailAndPush_WhenNotifTypeIsDeux_AndPriceBelowThreshold() {
        // WHEN: Appel de la méthode checkLastTendancesAndSendEmail
        startEmailSender.checkLastTendancesAndSendEmail();

        // THEN: Vérifier que les notifications ont été envoyées
        List<NotificationEnvoyee> notifs = notificationEnvoyeeRepository.findAll();

        // Vérifier qu'il y a bien deux notifications envoyées (email et push)
      //  assertEquals(2, notifs.size(), "Deux notifications doivent être envoyées");

        // Vérifier qu'une notification de type email a été envoyée
        assertTrue(notifs.stream().anyMatch(n -> n.getTypeNotif().equals("email")), "Une notification email doit avoir été envoyée");

        // Vérifier qu'une notification de type push a été envoyée
        assertTrue(notifs.stream().anyMatch(n -> n.getTypeNotif().equals("push")), "Une notification push doit avoir été envoyée");
    }
}
