import org.junit.jupiter.api.Test;
import projet.scrapping.Article;
import projet.scrapping.Utilisateur;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Timestamp;

public class ArticleTest {

    @Test
    public void testConstructorWithParameters() {
        // Création d'un utilisateur pour l'article
        Utilisateur user = new Utilisateur();
        user.setEmail("user@example.com");

        // Création d'un article avec le constructeur complet
        String name = "Article X";
        double seuil = 100.0;
        String notif = "email";
        Integer frequence = 30;
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        Article article = new Article(name, seuil, user, notif, frequence, timestamp);

        // Vérifications
        assertEquals(name, article.getNomA(), "Le nom de l'article devrait correspondre.");
        assertEquals(seuil, article.getSeuil(), "Le seuil de l'article devrait correspondre.");
        assertEquals(notif, article.getNotif(), "La notification de l'article devrait correspondre.");
        assertEquals(frequence, article.getFrequence(), "La fréquence de l'article devrait correspondre.");
        assertEquals(user, article.getUtilisateur(), "L'utilisateur associé à l'article devrait correspondre.");
    }

    // Ajoute les autres tests ici...
}
