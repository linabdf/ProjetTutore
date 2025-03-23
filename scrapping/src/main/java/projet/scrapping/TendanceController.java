package projet.scrapping;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tendance")
public class TendanceController {




        private final TendanceService tendanceService;
        private final JwtUtil JwtUtil;
        private final SiteRepository siteRepository;
        @Autowired
        public TendanceController(TendanceService tendanceService, JwtUtil JwtUtil, SiteRepository siteRepository) {
            this.tendanceService = tendanceService;
            this.JwtUtil = JwtUtil;
            this.siteRepository = siteRepository;
        }


    @GetMapping("/{id}")
    public ResponseEntity<?> getTendanceById(@PathVariable int id) {
        try {


            Optional<Tendance> tendance = tendanceService.getTendanceById(id);
            if (tendance.isPresent()) {
                System.out.println("ccccdcccdcdc");
                return ResponseEntity.ok(tendance.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tendance non trouvée.");
            }

        } catch (NumberFormatException e) {
            // Si l'id n'est pas un nombre valide, retourner une réponse d'erreur
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID de tendance invalide.");
        }
    }@GetMapping("/site/{id}")
    public ResponseEntity<?> getTendancesBySiteId(@PathVariable int id) {
        try {
            List<Tendance> tendances = tendanceService.getTendanceBySiteId(id);

            // Vérifier si des tendances sont présentes
            if (!tendances.isEmpty()) {
                // Mapper les tendances pour envoyer des données correctes
                List<Map<String, Object>> result = tendances.stream()
                        .map(tendance -> {
                            Map<String, Object> tendanceData = new HashMap<>();
                            tendanceData.put("x", tendance.getDate());
                            tendanceData.put("y", (tendance.getPrix() != null) ? tendance.getPrix() : 0); // Assurer que y n'est pas NaN
                            return tendanceData;
                        })
                        .collect(Collectors.toList());

                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aucune tendance trouvée pour ce site.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur interne.");
        }
    }


}






