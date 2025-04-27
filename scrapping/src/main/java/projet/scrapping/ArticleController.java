package projet.scrapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.*;

@RestController
@RequestMapping("/article")
public class ArticleController {

@Autowired
    private EmailService emailService;

    private final ArticleService ArticleService;
    private final JwtUtil JwtUtil;
    private final UtilisateurRepository UtilisateurRepository;
    private final SiteRepository  SiteRepository;
    private final ArticleRepository ArticleRepository ;
    private  final  TendanceRepository TendanceRepository;
    @Autowired
    public ArticleController(ArticleService ArticleService, JwtUtil JwtUtil, UtilisateurRepository UtilisateurRepository, SiteRepository siteRepository, ArticleRepository articleRepository, TendanceRepository tendanceRepository) {
        this.ArticleService = ArticleService;
        this.JwtUtil = JwtUtil;
        this.UtilisateurRepository = UtilisateurRepository;
        this.SiteRepository = siteRepository;
        ArticleRepository = articleRepository;
        TendanceRepository = tendanceRepository;
    }

    @PostMapping("/addSite")
    public ResponseEntity<Map<String, String>> addArticle(@RequestBody Map<String, Object> requestBody,
                                                          @RequestHeader("Authorization") String token) {
        Map<String, String> response = new HashMap<>();

        try {
            // Extraire l'email du token
            String tokenStr = token.replace("Bearer ", "");
            String email = JwtUtil.extractEmail(tokenStr);

            if (email == null) {
                response.put("message", "Token invalide.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
           System.out.println("je suis la ");

            Utilisateur utilisateur = UtilisateurRepository.findByEmail(email);

            if (utilisateur == null) {
                response.put("message", "Utilisateur non trouvé.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            // Extraire les informations de l'article du corps de la requête
            Map<String, Object> articleMap = (Map<String, Object>) requestBody.get("article");

            String nomA = (String) articleMap.get("nom");

            String seuilStr = (String) articleMap.get("seuil");
            double seuil = 0;
            try {
                seuil = Double.parseDouble(seuilStr);
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
                frequence = Integer.parseInt(frequenceStr);
            } catch (NumberFormatException e) {
                response.put("message", "Fréquence invalide.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            Timestamp currentTimestamp=new Timestamp(System.currentTimeMillis());
            Integer updateNow = 0;
            Article article =  ArticleService.insererArticle(nomA, seuil, utilisateur,notif,frequence,currentTimestamp,updateNow);
            System.out.println("c'est bon ");




            Map<String, Boolean> selectedSites = (Map<String, Boolean>) requestBody.get("selectedSites");

            // Parcourir les sites sélectionnés
            System.out.println("Sites sélectionnés :");
            for (Map.Entry<String, Boolean> entry : selectedSites.entrySet()) {
                if (entry.getValue()) {
                    Site site=new Site();
                    site.setNomSite(entry.getKey());
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


  @GetMapping("/myArticles")
  public ResponseEntity<?> getMyArticles(@RequestHeader("Authorization") String token) {
      try {

          System.out.println("TOKEN OFF"+token);
          // Extraire le token
          String tokenStr = token.replace("Bearer ", "");
          String email = JwtUtil.extractEmail(tokenStr);
          System.out.println(" je suis la email"+email);
          // Exemple d'envoi d'email

          if (email != null) {
              // Trouver l'utilisateur
              Utilisateur utilisateur = UtilisateurRepository.findByEmail(email);
              System.out.println(" je suis la email" + utilisateur);
              List<Map<String, Object>> formattedArticles = null;
              if (utilisateur != null) {
                  // Récupérer les articles de l'utilisateur

                  List<Article> articles = ArticleService.getArticlesByUtilisateur(utilisateur);

                  // Formatter la réponse avec seulement nomA, seuil et frequence
                  formattedArticles = new ArrayList<>();
                  System.out.println(" je suis la 2");
                  for (Article article : articles) {
                      Map<String, Object> articleMap = new HashMap<>();
                      articleMap.put("numA", article.getNumA());
                      articleMap.put("nom", article.getNomA());
                      System.out.println("nonA" + article.getNomA());
                      articleMap.put("seuil", article.getSeuil());
                      articleMap.put("frequence", article.getFrequence());
                      articleMap.put("notif", article.getNotif());
                      articleMap.put("Description",article.getDescription());
                      articleMap.put("urlimage",article.getUrlImage());


                      // System.out.println(" je suis la email1"+formattedArticles);
                      // Affichage des sites associés à l'article
                      System.out.println("Récupération des sites pour l'article: " + article.getNomA());
                      List<Site> sites = SiteRepository.findByArticle(article);
                      Map<String, Boolean> selectedSites = new HashMap<>();
                      selectedSites.put("amazon", false);
                      selectedSites.put("fnac", false);
                      selectedSites.put("ldlc", false);
                      selectedSites.put("rueducommerce", false);
                      selectedSites.put("boulanger", false);
                      selectedSites.put("leclerc", false);
                      selectedSites.put("alternate", false);
                      selectedSites.put("ebay", false);
                      selectedSites.put("cdiscount", false);
                      selectedSites.put("lego", false);

                      if (sites != null && !sites.isEmpty()) {
                          System.out.println("Sites associés trouvés: " + sites.size());
                          for (Site site : sites) {
                              System.out.println(" - " + site.getNomSite());
                              String siteName = site.getNomSite().toLowerCase(); // Convertir en minuscules pour correspondre aux clés
                              if (selectedSites.containsKey(siteName)) {
                                  selectedSites.put(siteName, true); // Mettre à true si le site est associé
                              }
                          }

                      } else {
                          System.out.println("Aucun site associé à cet article.");
                      }
                      articleMap.put("selectedSites", selectedSites);
                      System.out.println("selectedSite"+selectedSites);
                      formattedArticles.add(articleMap);
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

    @PutMapping("/updateArticle")
    public ResponseEntity<Map<String, String>> updateArticle(@RequestBody Map<String, Object> updatedArticle,
                                                             @RequestHeader("Authorization") String token) {
        Map<String, String> response = new HashMap<>();

        try {
            // Extraire l'email du token
            String tokenStr = token.replace("Bearer ", "");
            String email = JwtUtil.extractEmail(tokenStr);
            if (email == null) {
                response.put("message", "Token invalide.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            // Chercher l'utilisateur par email
            Utilisateur utilisateur = UtilisateurRepository.findByEmail(email);
            if (utilisateur == null) {
                response.put("message", "Utilisateur non trouvé.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            // Extraire les informations de l'article
            String nomA = (String) updatedArticle.get("nom");
            Integer frequenceStr = (Integer) updatedArticle.get("frequence");
            Integer seuilStr = (Integer) updatedArticle.get("seuil");
            String notif = (String) updatedArticle.get("notif");
            Object updateNowObj = updatedArticle.get("updateNow");
            // Validation des données
            double seuil = 0;
            try {
                seuil = seuilStr;
            } catch (NumberFormatException e) {
                response.put("message", "Seuil invalide.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            Integer frequence = 0;
            try {
                frequence = frequenceStr;
            } catch (NumberFormatException e) {
                response.put("message", "Fréquence invalide.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            int updateNow = 1;
            if (updateNowObj != null) {
                try {
                    if (updateNowObj instanceof Integer) {
                        updateNow = (Integer) updateNowObj;
                    } else if (updateNowObj instanceof String) {
                        updateNow = Integer.parseInt((String) updateNowObj);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Warning: Invalid format for updateNow, defaulting to 0.");
                }
            }
            // Chercher l'article à modifier pour cet utilisateur
            Article article = ArticleRepository.findByUtilisateurAndNomA(utilisateur, nomA);
            if (article == null) {
                response.put("message", "Article non trouvé pour cet utilisateur.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            } else {
                // Mettre à jour l'article
                article.setFrequence(frequence);
                article.setSeuil(seuil);
                article.setNotif(notif);
                article.setDerniereupdate(new Timestamp(System.currentTimeMillis()));
                article.setUpdateNow(updateNow);
                // Sauvegarder l'article mis à jour
                ArticleRepository.save(article);
            }
            // Récupérer les sites existants associés à cet article
            List<Site> existingSites = SiteRepository.findByArticle(article);
            // Supprimer les sites qui ne sont plus sélectionnés
            Map<String, Boolean> selectedSites = (Map<String, Boolean>) updatedArticle.get("selectedSites");
            for (Site site : existingSites) {
                String siteName = site.getNomSite();
                Boolean isSelected = selectedSites.get(siteName);
                // Log pour vérifier les valeurs récupérées
                System.out.println("Vérification du site : " + siteName);
                System.out.println("IsSelected: " + isSelected);
                // Si le site est désélectionné, le supprimer
                if (isSelected == null || !isSelected) {
                    System.out.println("Le site " + siteName + " est désélectionné. Suppression en cours...");
                    // Supprimer les tendances associées à ce site
                    List<Tendance> tendances = TendanceRepository.findBySite(site);
                    for (Tendance tendance : tendances) {
                        System.out.println("Tendance supprimée pour le site " + siteName);
                        TendanceRepository.delete(tendance);  // Supprimer la tendance
                    }
                    if (site != null){
                        site.setArticle(null);
                        SiteRepository.save(site);
                        // Supprimer le site
                    SiteRepository.delete(site);
                    System.out.println("Site supprimé : " + siteName);
                }
            }
            }
            // Ajouter les nouveaux sites sélectionnés
            for (Map.Entry<String, Boolean> entry : selectedSites.entrySet()) {
                if (entry.getValue()) {
                    String siteName = entry.getKey();
                    // Vérifier si le site est déjà associé à cet article
                    Site existingSite = SiteRepository.findByArticleAndNomSite(article, siteName);
                    if (existingSite == null) {
                        // Si le site n'existe pas, le créer et l'associer à l'article
                        Site site = new Site();
                        site.setNomSite(siteName);
                        site.setArticle(article);  // Associer l'article à ce site
                        SiteRepository.save(site); // Sauvegarder le site dans la base de données
                    } else {
                        System.out.println("Le site " + siteName + " existe déjà pour cet article.");
                    }
                }
            }
            response.put("message", "Article et sites mis à jour avec succès.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Erreur lors de la mise à jour de l'article.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @DeleteMapping("/supprimerArticle/{nomA}")
    public ResponseEntity<Map<String, String>> supprimerArticle(@PathVariable String nomA,
                                                                @RequestHeader("Authorization") String token) {
        Map<String, String> response = new HashMap<>();
        //emailService.sendEmail("alexiaalexi233@gmail.com", "Test Email", "Bonjour, ceci est un test d'email avec SES.");
        try {
            // Extraire l'email du token (par exemple avec JWT)
            String tokenStr = token.replace("Bearer ", "");
            String email = JwtUtil.extractEmail(tokenStr);

            if (email == null) {
                response.put("message", "Token invalide.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            // Chercher l'utilisateur par email
            Utilisateur utilisateur = UtilisateurRepository.findByEmail(email);
            if (utilisateur == null) {
                response.put("message", "Utilisateur non trouvé.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            // Chercher l'article par nomA pour cet utilisateur
            Article article = ArticleRepository.findByUtilisateurAndNomA(utilisateur, nomA);
            if (article == null) {
                response.put("message", "Article non trouvé.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            // Supprimer toutes les tendances associées à cet article
            List<Site> sites = SiteRepository.findByArticle(article);
            for (Site site : sites) {
                // Supprimer les tendances associées au site
                List<Tendance> tendances = TendanceRepository.findBySite(site);
                for (Tendance tendance : tendances) {
                    TendanceRepository.delete(tendance);  // Supprimer la tendance
                }
                // Supprimer le site
                SiteRepository.delete(site);
            }

            // Supprimer l'article
            ArticleRepository.delete(article);

            response.put("message", "Article et ses sites et tendances ont été supprimés avec succès.");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("message", "Erreur lors de la suppression de l'article.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getArticleById(@PathVariable int id) {
        try {
            Optional<Article> articleOpt = ArticleService.getArticleById(id);

            if (articleOpt.isPresent()) {
                Article article = articleOpt.get();
                // Créer une réponse structurée pour le front-end
                Map<String, Object> articleResponse = new HashMap<>();
                articleResponse.put("id", article.getNumA());  // ID de l'article
                articleResponse.put("nom", article.getNomA()); // Nom de l'article
                articleResponse.put("description", article.getDescription()); // Description de l'article
                articleResponse.put("seuil", article.getSeuil()); // Seuil
                articleResponse.put("frequence", article.getFrequence()); // Fréquence
                articleResponse.put("notif", article.getNotif()); // Notification
                articleResponse.put("urlimage", article.getUrlImage()); // Notification
                articleResponse.put("derniereupdate", article.getDerniereupdate()); // Date de la dernière mise à jour

                // Récupérer les sites associés à cet articleS
                List<Site> sites = SiteRepository.findByArticle(article);
                // Créer une liste pour stocker les sites
                List<Map<String, Object>> siteInfos = new ArrayList<>();

// Pour chaque site dans la liste 'sites'
                for (Site site : sites) {
                    // Créer un objet avec l'ID et le nom du site
                    Map<String, Object> siteInfo = new HashMap<>();
                    siteInfo.put("nomSite", site.getNomSite().toLowerCase()); // Nom du site en minuscule

                    siteInfo.put("numS", site.getNumS()); // ID du site

                    // Ajouter l'objet 'siteInfo' dans la liste 'siteInfos'
                    siteInfos.add(siteInfo);
                }

// Ajouter la liste des sites à la réponse
                articleResponse.put("sites", siteInfos);

                return ResponseEntity.ok(articleResponse);

            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Article non trouvé.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur interne du serveur.");
        }
    }


 /*   public ResponseEntity<?> getArticleWithSites(@PathVariable int id) {
        try {

            Optional<Article> articleOptional = ArticleRepository.findById(id);
            if (articleOptional.isPresent()) {
                Article article = articleOptional.get();

                // Récupérer les sites associés à cet article
                List<Site> sites = SiteRepository.findByArticleId(id);

                // Créer un objet de type ArticleDTO qui contient à la fois l'article et les sites
                Map<String, Object> response = new HashMap<>();
                response.put("article", article);
                response.put("sites", sites);

                return ResponseEntity.ok(response);  // Retourne l'article et les sites dans la même réponse
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Article non trouvé.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur interne du serveur.");
        }
    }
*/
    @PutMapping("/{id}")
    public Article replaceProject(@RequestBody Article newArticle, @PathVariable int id) {

        return ArticleRepository.findById(id)
                .map(article -> {
                    article.setSeuil(newArticle.getSeuil());
                    article.setFrequence(newArticle.getFrequence());
                    article.setUpdateNow(newArticle.getUpdateNow());
                    article.setDerniereupdate(newArticle.getDerniereupdate());
                    return ArticleRepository.save(article);
                })
                .orElseGet(() -> {
                    return ArticleRepository.save(newArticle);
                });
    }
    @GetMapping("/utilisateur/push")
    public ResponseEntity<List<NotificationEnvoyee>> getPushNotificationsUtilisateur(@RequestHeader("Authorization") String token) {
        try {
            System.out.println("🛡️ TOKEN reçu : " + token);
            String tokenStr = token.replace("Bearer ", "");
            String email = JwtUtil.extractEmail(tokenStr);
            System.out.println("📧 Email extrait du token : " + email);

            if (email != null) {
                Utilisateur utilisateur = UtilisateurRepository.findByEmail(email);
                if (utilisateur != null) {
                    List<Article> articles = ArticleService.getArticlesByUtilisateur(utilisateur);
                    System.out.println("article"+articles.size());
                    List<NotificationEnvoyee> notificationsDTO = new ArrayList<>();

                    for (Article article : articles) {
                        List<NotificationEnvoyee> notifs = article.getDerniereNotification();
                        System.out.println("notifarticle"+notifs.size()+ "article"+article.getNomA());



                        for (NotificationEnvoyee notif : article.getDerniereNotification()) {
                            if (notif.getTypeNotif().equalsIgnoreCase("push")) {
                            notificationsDTO.add(new NotificationEnvoyee(notif.getMessage(), notif.getlue()));
                        }}
                        System.out.println("Message : " + notificationsDTO);



                    }




                    return ResponseEntity.ok(notificationsDTO);
                }

            }

            return ResponseEntity.status(401).build(); // Non autorisé
        } catch (Exception e) {
            System.out.println("❌ Erreur lors de la récupération des notifications push : " + e.getMessage());
            return ResponseEntity.status(500).build(); // Erreur serveur
        }
    }
}
