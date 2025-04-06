package projet.scrapping;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleService
{

    @Autowired
    private final ArticleRepository articleRepository;
    @Autowired
    private final UtilisateurRepository utilisateurRepository;
    @Autowired
    public ArticleService( ArticleRepository articleRepository, UtilisateurRepository utilisateurRepository) {

        this.articleRepository = articleRepository;
        this.utilisateurRepository = utilisateurRepository;

    }

@Transactional
public Article insererArticle(String nomA, double seuil, Utilisateur utilisateur, String notif, Integer frequence, Timestamp currentTimestamp) {
    // Création de l'objet Article
    Article article = new Article(nomA, seuil, utilisateur, notif, frequence,currentTimestamp);

    System.out.println("Article à insérer : " + article);

    try {
        // Sauvegarde de l'article dans la base de données
        Article savedArticle = articleRepository.save(article);  // Sauvegarde de l'article
        articleRepository.flush(); // Forcer la synchronisation avec la base de données

        System.out.println("Article inséré avec succès dans la base : " + savedArticle);

        // Recherche de l'utilisateur dans la base de données à partir de l'email
        Utilisateur fetchedUtilisateur = utilisateurRepository.findByEmail(utilisateur.getEmail());

        // Vérifier si l'utilisateur existe
        if (fetchedUtilisateur != null) {
            System.out.println("Utilisateur trouvé : " + fetchedUtilisateur);

            // Récupérer tous les articles associés à cet utilisateur
            List<Article> articles = articleRepository.findByUtilisateur(fetchedUtilisateur);

            // Afficher la liste des articles associés à cet utilisateur
            System.out.println("Liste des articles de l'utilisateur :");
            for (Article articl : articles) {
                System.out.println(articl.getNomA());  // Afficher le nom de l'article
            }
        } else {
            System.out.println("Utilisateur non trouvé avec l'e-mail : " + utilisateur.getEmail());
        }

        // Retourner l'article inséré
        return savedArticle;

    } catch (Exception e) {
        e.printStackTrace();
        System.out.println("Erreur lors de l'insertion de l'article.");
        return null;  // En cas d'erreur, retourner null ou gérer l'exception selon votre logique.
    }
}
@Transactional
public  List<Article>getArticlesByUtilisateur(Utilisateur utilisateur){
    return articleRepository.findByUtilisateur(utilisateur);
}

@Transactional
public Optional<Article> getArticleById(int id) {
        return articleRepository.findById(id);
    }
}
