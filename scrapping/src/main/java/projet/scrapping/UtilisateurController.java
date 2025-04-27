package projet.scrapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class UtilisateurController {
    private final UtilisateurService utilisateurService;
    private  final JwtUtil JwtUtil;
    @Autowired
    private UtilisateurController(UtilisateurService utilisateurService, JwtUtil JwtUtil) {
        this.utilisateurService = utilisateurService;
        this.JwtUtil= JwtUtil;
    }

    public static UtilisateurController createUtilisateurController(UtilisateurService utilisateurService, JwtUtil JwtUtil) {
        return new UtilisateurController(utilisateurService, JwtUtil);
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> inscrireUtilisateur(@RequestBody Utilisateur utilisateurDTO) {
        Map<String, String> response = new HashMap<>();
        System.out.println("utilisateur"+utilisateurDTO);
        try {
            if (utilisateurService.verifierSiUtilisateurExiste(utilisateurDTO.getEmail())) {
                response.put("message", "Un utilisateur avec cet email existe déjà.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            System.out.println("Nom reçu : " + utilisateurDTO.getNomU());
            System.out.println("Mot de passe reçu : " + utilisateurDTO.getMotDePasse());
            System.out.println("Email reçu : " + utilisateurDTO.getEmail());
            System.out.println("prenomU reçu : " + utilisateurDTO.getPrenom());
            utilisateurService.insererUtilisateur(
                    utilisateurDTO.getNomU(),
                    utilisateurDTO.getPrenom(),
                    utilisateurDTO.getEmail(),
                    utilisateurDTO.getMotDePasse()
            );
            response.put("message", "Utilisateur inscrit avec succès bravo ");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Erreur lors de l'inscription de l'utilisateur");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> connecterUtilisateur(@RequestBody Utilisateur utilisateurDTO) {
        Map<String, String> response = new HashMap<>();
        System.out.println("Utilisateur reçu : " + utilisateurDTO);
        System.out.println("Email reçu : " + utilisateurDTO.getEmail());
        System.out.println("Mot de passe reçu : " + utilisateurDTO.getMotDePasse());
        try {
            Map<String, String> utilisateur = utilisateurService.connecterUtilisateur(
                    utilisateurDTO.getEmail(),
                    utilisateurDTO.getMotDePasse()
            );

                if ("Connexion réussie.".equals(utilisateur.get("message"))) {
                    // Ajout d'un log avant de générer le token
                    System.out.println("Essai de génération du token pour l'email : " + utilisateurDTO.getEmail());

                    try {
                        String token = JwtUtil.generateToken(utilisateurDTO.getEmail());
                        System.out.println("Token généré avec succès : " + token);
                        response.put("message", "Connexion réussie.");
                        response.put("token", token);  // Ajout du token à la réponse
                        return ResponseEntity.ok(response);
                    } catch (Exception e) {
                        // Capturer et loguer l'erreur si la génération du token échoue
                        System.err.println("Erreur lors de la génération du token : " + e.getMessage());
                        response.put("message", "Erreur lors de la génération du token.");
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
                    }

            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(utilisateur);
            }
        } catch (Exception e) {
            response.put("message", "Erreur lors de la connexion de l'utilisateur");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@RequestHeader("Authorization") String token) {
        Map<String, String> response = new HashMap<>();
        try {
            System.out.println("token a supprimer"+token);
            // Vérifier que le token est valide
            if (token != null && !token.isEmpty()) {
                // Ajouter le token à la blacklist
                JwtUtil.addToBlacklist(token);
                response.put("message", "Déconnexion réussie.");
                return ResponseEntity.ok(response);
            } else {
                response.put("message", "Token manquant ou invalide.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception e) {
            response.put("message", "Erreur lors de la déconnexion.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}


