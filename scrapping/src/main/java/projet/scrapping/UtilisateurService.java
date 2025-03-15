package projet.scrapping;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import  projet.scrapping.JwtUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;



@Service
public class UtilisateurService {

    @Autowired
    private final UtilisateurRepository utilisateurRepository;
    private  final JwtUtil jwtUtil;
    @Autowired
    public UtilisateurService(UtilisateurRepository utilisateurRepository, JwtUtil jwtUtil) {
        this.utilisateurRepository = utilisateurRepository;
        this.jwtUtil = jwtUtil;
    }
/*
    // Générer le prochain ID utilisateur
    public String generateNextArticle() {
        String newId = "U001";
        String query = "SELECT MAX(numU) AS lastId FROM utilisateur";
        try (Connection connection = dm.getConnexion();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                String lastId = rs.getString("lastId");
                if (lastId != null) {
                    int numPart = Integer.parseInt(lastId.substring(1));  // Extraire la partie numérique
                    newId = "U" + String.format("%03d", numPart + 1);  // Incrémenter et formater
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newId;
    }
*/
    // Insérer un utilisateur
    /*public void insererUtilisateur(String nomU, String prenom, String email, String motdepasse) {
        String id = generateNextArticle();
       // Crypter le mot de passe
        try (Connection connection = dm.getConnexion()) {
            if (connection != null) {
                String query = "INSERT INTO utilisateur (numU, nomU, prenomU, emailU, motdepasse) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, id);
                    preparedStatement.setString(2, nomU);
                    preparedStatement.setString(3, prenom);
                    preparedStatement.setString(4, email);
                    preparedStatement.setString(5, motdepasse); // Insérer le mot de passe crypté
                    preparedStatement.executeUpdate();
                    System.out.println("Utilisateur inséré avec succès.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
*/
  /*  // Récupérer tous les utilisateurs
    public List<Utilisateur> getAllUtilisateurs() {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String query = "SELECT * FROM utilisateur";
        try (Connection connection = dm.getConnexion();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Utilisateur utilisateur = new Utilisateur();
                utilisateur.SetNumU(resultSet.getString("numU"));
                utilisateur.SetNomU(resultSet.getString("nomU"));
                utilisateur.setEmail(resultSet.getString("emailU"));
                utilisateurs.add(utilisateur);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utilisateurs;
    }

    // Supprimer un utilisateur
    public void supprimerUtilisateur(String numU) {
        String query = "DELETE FROM utilisateur WHERE numU = ?";
        try (Connection connection = dm.getConnexion();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, numU);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Utilisateur supprimé avec succès.");
            } else {
                System.out.println("Aucun utilisateur trouvé avec le numéro : " + numU);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Modifier un utilisateur
    public void modifierUtilisateur(String nomU, String prenom, String email) {
        String query = "UPDATE utilisateur SET emailU = ? WHERE nomU = ? AND prenomU = ?";
        try (Connection connection = dm.getConnexion();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, email);
            preparedStatement.setString(2, nomU);
            preparedStatement.setString(3, prenom);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Utilisateur mis à jour avec succès.");
            } else {
                System.out.println("Aucun utilisateur trouvé avec le nom : " + nomU + " et le prénom : " + prenom);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
*/

    @Transactional
    public void insererUtilisateur(String nomU, String prenom, String email, String motDePasse) {
        Utilisateur utilisateur = new Utilisateur(nomU, prenom, email, motDePasse);
        System.out.println("Utilisateur à insérer : " + utilisateur);

        try {
            Utilisateur savedUtilisateur = utilisateurRepository.save(utilisateur);
            utilisateurRepository.flush();
            System.out.println("Utilisateur inséré avec succès DANS LA BASE : " + savedUtilisateur);
          //  Utilisateur fetchedUtilisateur = utilisateurRepository.findByEmail("suusuusuusuus");
            List<Utilisateur> utilisateurs = utilisateurRepository.findAll();
            //System.out.println("Liste des utilisateur" + utilisateurs);
            /*if (fetchedUtilisateur != null) {
                System.out.println("Utilisateur récupéré : " + fetchedUtilisateur);
            } else {
                System.out.println("Utilisateur non trouvé dans la base de données.");
            }*/
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur lors de l'insertion de l'utilisateur.");
        }
    }

    @Transactional
    public void ConnecterUtilisateur(String nomU, String prenom, String email, String motDePasse) {
        Utilisateur utilisateur = new Utilisateur(nomU, prenom, email, motDePasse);
        System.out.println("Utilisateur à insérer : " + utilisateur);

        try {
            Utilisateur savedUtilisateur = utilisateurRepository.save(utilisateur);
            utilisateurRepository.flush();
            System.out.println("Utilisateur inséré avec succès DANS LA BASE : " + savedUtilisateur);
            Utilisateur fetchedUtilisateur = utilisateurRepository.findByEmail("suusuusuusuus");
            List<Utilisateur> utilisateurs = utilisateurRepository.findAll();
            System.out.println("Liste des utilisateur" + utilisateurs);
            if (fetchedUtilisateur != null) {
                System.out.println("Utilisateur récupéré : " + fetchedUtilisateur);
            } else {
                System.out.println("Utilisateur non trouvé dans la base de données.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur lors de l'insertion de l'utilisateur.");
        }
    }

    @Transactional
    public Map<String, String> connecterUtilisateur(String email, String motdepasse) {
        Map<String, String> response = new HashMap<>();

        Optional<Utilisateur> utilisateur = Optional.ofNullable(utilisateurRepository.findByEmail(email));
        if (utilisateur.isEmpty()) {

            response.put("message", "Utilisateur non trouvé ");
            return response;
        }
        System.out.println("utilisateur trouvé");
        Utilisateur utilisateur1 = utilisateur.get();
        if (!utilisateur1.getMotDePasse().equals(motdepasse)) {
            response.put("message", "Mot de passe incorrect.");
            System.out.println("mot de passe incorrect");
            return response;
        }
        System.out.println("mot de passe correct");
        response.put("message", "Connexion réussie.");
        return response;
    }

}



