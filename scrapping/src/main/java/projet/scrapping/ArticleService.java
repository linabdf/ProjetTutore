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
/*
    @Autowired
    public ArticleService(DatabaseManager dm, ArticleRepository articleRepository, UtilisateurRepository utilisateurRepository) {
        this.dm = dm;
        this.articleRepository = articleRepository;
        this.utilisateurRepository = utilisateurRepository;
        dm.connexion();
    }
    public  String generateNextArticle() {
        String newId = "A001";
        String query = "SELECT MAX(numA)AS lastId FROM Article";
        try (Statement stmt = dm.getConnexion().createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                String lastId = rs.getString("lastId");
                if (lastId != null) {
                    int numPart = Integer.parseInt(lastId.substring(1));  // Extraire la partie numérique
                    newId = "A" + String.format("%03d", numPart + 1);  // Incrémenter et formater
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newId;
    }
    public  void insererArticle(String nomA, String Descrption, String seuil, String numU,String notif ,Integer frequence) {
        Connection connection = dm.getConnexion();
        String numA= generateNextArticle();
        if (connection != null) {

            try {
                String checkQuery="SELECT COUNT(numU) FROM Article WHERE  nomA = ? AND numU = ?";
                PreparedStatement checkStmt=connection.prepareStatement(checkQuery);
                checkStmt.setString(1,nomA);
                checkStmt.setString(2,numU);
                ResultSet resultSet = checkStmt.executeQuery();
                resultSet.next();
                int count = resultSet.getInt(1);
                if (count > 0) {
                    System.out.println("L'article avec ce nom existe déjà pour cet utilisateur !");
                } else {
                String query = "INSERT INTO Article (numA,nomA,Descrption,seuil,numU,notif,frequence) VALUES (?, ? , ?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, numA);
                preparedStatement.setString(2, nomA);
                preparedStatement.setString(3, Descrption);
                preparedStatement.setString(4, seuil);
                preparedStatement.setString(5, numU);
                preparedStatement.setString(6,notif);
                preparedStatement.setInt(7,frequence);
                preparedStatement.executeUpdate();
                System.out.println(" insertion  de l'article  .");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public static final String NUMU_CONSTANT = "1255"; // Valeur constante pour numu

    public Article createArticle(String id) {

        Utilisateur utilisateur = utilisateurRepository.findById(NUMU_CONSTANT).orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));  // Crée un nouvel objet Article avec l'ID d'article (passé en paramètre)
        Article article = new Article();
        article.setId(id);  // Définir l'ID de l'article
        article.setTitre("Exemple Titre");
        article.setContenu("Exemple Contenu");
        article.setSeuil(100.0);
        article.setUtilisateur(utilisateur);


        // Assigner la constante numu à l'article
    //    article.setNumu(NUMU_CONSTANT); // Assigner la constante à l'article

        // Sauvegarde l'article dans la base de données
        return articleRepository.save(article);
    }
    public void supprimerArtcile(String nomA,String numU) {
        Connection connection = dm.getConnexion();
        if (connection != null) {
            try {
                String query = "DELETE FROM Article WHERE nomA = ? AND numU = ? ";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, nomA);
                preparedStatement.setString(2, numU);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Utilisateur supprimé avec succès.");
                } else {
                    System.out.println("Aucun utilisateur trouvé avec le numéro : " + nomA);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
    public void modifierSeuilArticle(String numA, String nouveauSeuil) {
        Connection connection = dm.getConnexion();
        if (connection != null) {
            try {
                // Requête pour mettre à jour le seuil d'un article
                String query = "UPDATE Article SET seuil = ? WHERE numA = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, nouveauSeuil);
                preparedStatement.setString(2, numA);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Seuil de l'article mis à jour avec succès.");
                } else {
                    System.out.println("Aucun article trouvé avec le numéro : " + numA);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
*/
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
