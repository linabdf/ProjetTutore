package projet.scrapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/article")
public class ArticleController {


    private final ArticleService ArticleService;
    private final JwtUtil JwtUtil;
    private final UtilisateurRepository UtilisateurRepository;
    private final SiteRepository  SiteRepository;
    @Autowired
    public ArticleController(ArticleService ArticleService, JwtUtil JwtUtil, UtilisateurRepository UtilisateurRepository, SiteRepository siteRepository) {
        this.ArticleService = ArticleService;
        this.JwtUtil = JwtUtil;
        this.UtilisateurRepository = UtilisateurRepository;
        this.SiteRepository = siteRepository;
    }

    @PostMapping("/addSite")
    public ResponseEntity<Map<String, String>> addArticle(@RequestBody Map<String, Object> requestBody,
                                                          @RequestHeader("Authorization") String token) {
        Map<String, String> response = new HashMap<>();

        try {
            // Extraire l'email du token (par exemple via un utilitaire JWT)
            String tokenStr = token.replace("Bearer ", "");
            String email = JwtUtil.extractEmail(tokenStr);  // Utiliser un utilitaire pour extraire l'email

            if (email == null) {
                response.put("message", "Token invalide.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
           System.out.println("je suis la ");
            // Récupérer l'utilisateur par email (utilisez votre service pour récupérer l'utilisateur)
            Utilisateur utilisateur = UtilisateurRepository.findByEmail(email);

            if (utilisateur == null) {
                response.put("message", "Utilisateur non trouvé.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            // Extraire les informations de l'article du corps de la requête
            Map<String, Object> articleMap = (Map<String, Object>) requestBody.get("article");

            String nomA = (String) articleMap.get("nom");

            String seuilStr = (String) articleMap.get("seuil");  // Seuil envoyé comme String
            double seuil = 0;
            try {
                seuil = Double.parseDouble(seuilStr);  // Conversion explicite
            } catch (NumberFormatException e) {
                response.put("message", "Seuil invalide.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            System.out.println("je suis la3 "+seuil);
            String notif = (String) articleMap.get("notif");
            System.out.println("nomArt"+nomA);

            String frequenceStr = (String) articleMap.get("frequence");
            Integer frequence = 0;
            try {
                frequence = Integer.parseInt(frequenceStr);  // Conversion explicite
            } catch (NumberFormatException e) {
                response.put("message", "Fréquence invalide.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            Timestamp currentTimestamp=new Timestamp(System.currentTimeMillis());

            Article article =  ArticleService.insererArticle(nomA, seuil, utilisateur,notif,frequence,currentTimestamp);
            System.out.println("c'est bon ");
            // Créer un nouvel article

            // Récupérer les sites sélectionnés
            Map<String, Boolean> selectedSites = (Map<String, Boolean>) requestBody.get("selectedSites");

            // Parcourir les sites sélectionnés
            System.out.println("Sites sélectionnés :");
            for (Map.Entry<String, Boolean> entry : selectedSites.entrySet()) {
                if (entry.getValue()) {
                    Site site=new Site();
                    site.setNomSite(entry.getKey());  // Assigner le nom du site
                    site.setArticle(article);  // Associer l'article à ce site
                    SiteRepository.save(site); // Sauvegarder le site dans la base de données
                    System.out.println("Site ajouté: " + entry.getKey());
                }
                    System.out.println(entry.getKey() + ": " + entry.getValue());
            }
            // Récupérer les sites associés à l'article

            List<Site> sites = SiteRepository.findByArticle(article);
            List<String> siteNames = new ArrayList<>();

            for (Site site : sites) {
                siteNames.add(site.getNomSite());  // Ajouter le nom de chaque site
            }
            System.out.println("");
            // Ajouter les sites récupérés à la réponse
            response.put("message", "Article et sites ajoutés avec succès.");
            response.put("sites", String.valueOf(siteNames));
            // Affichage des sites associés à cet article
            /*  List<Site> sites = siteService.findSitesByArticle(article);
             */response.put("message", "Article ajouté avec succès.");
            /*response.put("sites", sites.toString());*/  // Afficher les sites associés à l'article (si vous avez besoin)

            // Retourner la réponse avec un message et les sites associés
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("message", "Erreur lors de l'ajout de l'article.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

  /*  @PostMapping("/addArticle")
    public ResponseEntity<Map<String, String>> addArticle(@RequestBody Article article,
                                                          @RequestHeader("Authorization") String token) {
        Map<String, String> response = new HashMap<>();
       System.out.println("articleSite" +article.getSites());

        try {
            // Extraire le token du header "Authorization"
            String tokenStr = token.replace("Bearer ", "");

            // Extraire l'email du token
            String email = JwtUtil.extractEmail(tokenStr);
            System.out.println("EMAIL extrait : " + email);

            if (email != null) {
                // Trouver l'utilisateur par email
                Utilisateur utilisateur = UtilisateurRepository.findByEmail(email);

                if (utilisateur != null) {
                    // Récupérer le numU de l'utilisateur
                     Long numU = Long.valueOf(utilisateur.getNumU());
                    System.out.println("numU de l'utilisateur : " + numU);

                    // Ajouter l'article et associer numU à l'article
                  //  article.setNumU(numU); // Associer l'ID utilisateur à l'article
                    //Article savedArticle = ArticleService.addArticle(article);

                    System.out.println("JSJJSK");
                    System.out.println("JSJJSK");
                    article.setUtilisateur(utilisateur);
                    System.out.println("JSJJSK");
                    ArticleService.insererArticle(article.getNomA(), article.getSeuil(), utilisateur,article.getNotif(),article.getFrequence());
                  System.out.println("INSERTRION");

                    response.put("message", "Article ajouté avec succès.");
                    //response.put("articleId", String.valueOf(savedArticle.getId()));
                    return ResponseEntity.ok(response);
                } else {
                    response.put("message", "Utilisateur non trouvé.");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
                }
            } else {
                response.put("message", "Token invalide.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            response.put("message", "Erreur lors de l'ajout de l'article.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }*/
  @GetMapping("/myArticles")
  public ResponseEntity<?> getMyArticles(@RequestHeader("Authorization") String token) {
      try {
          System.out.println("TOKEN OFF"+token);
          // Extraire le token
          String tokenStr = token.replace("Bearer ", "");
          String email = JwtUtil.extractEmail(tokenStr);
          System.out.println(" je suis la email"+email);
          if (email != null) {
              // Trouver l'utilisateur
              Utilisateur utilisateur = UtilisateurRepository.findByEmail(email);
              System.out.println(" je suis la email"+utilisateur);
              if (utilisateur != null) {
                  // Récupérer les articles de l'utilisateur

                  List<Article> articles = ArticleService.getArticlesByUtilisateur(utilisateur);

                  // Formatter la réponse avec seulement nomA, seuil et frequence
                  List<Map<String, Object>> formattedArticles = new ArrayList<>();
                  System.out.println(" je suis la 2");
                  for (Article article : articles) {
                      Map<String, Object> articleMap = new HashMap<>();

                      articleMap.put("nom", article.getNomA());
                      System.out.println("nonA"+article.getNomA());
                      articleMap.put("seuil", article.getSeuil());
                      articleMap.put("frequence", article.getFrequence());
                      articleMap.put("notif", article.getNotif());

                      formattedArticles.add(articleMap);
                     // System.out.println(" je suis la email1"+formattedArticles);
                  }

                  return ResponseEntity.ok(formattedArticles);
              } else {
                  return ResponseEntity.status(401).body("Utilisateur non trouvé.");
              }
          } else {
              return ResponseEntity.status(401).body("Token invalide.");
          }
      } catch (Exception e) {
          return ResponseEntity.status(500).body("Erreur interne du serveur.");
      }
  }

}