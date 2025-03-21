package projet.scrapping.Tendance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projet.scrapping.*;
import projet.scrapping.Site.Site;
import projet.scrapping.Site.SiteRepository;
import projet.scrapping.Utilisateur.Utilisateur;
import projet.scrapping.Utilisateur.UtilisateurRepository;

import java.sql.Timestamp;
import java.util.*;

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
        Optional<Tendance> tendance = tendanceService.getTendanceById(id);
        if (tendance != null) {
            return ResponseEntity.ok(tendance);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tendance non trouvé.");
        }
    }

    @GetMapping("/site/{siteId}")
    public ResponseEntity<?> getTendancesBySiteId(@PathVariable int siteId) {
        List<Tendance> tendances = tendanceService.getTendanceBySiteId(siteId);
        System.out.println(tendances);
        if (!tendances.isEmpty()) {
            return ResponseEntity.ok(tendances);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aucune tendance trouvée pour ce site.");
        }
    }

}