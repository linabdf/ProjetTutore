package projet.scrapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    @Autowired
    private NotificationEnvoyeeRepository notificationRepository;

    @CrossOrigin(origins = "http://localhost:4200")

    @GetMapping("/unreadCount")
    public ResponseEntity<Integer> getUnreadCount() {
        int unreadCount = NotificationService.getUnreadNotification();  // Compter les notifications non lues
        return ResponseEntity.ok(unreadCount);
    }
    @PostMapping("/markAllAsRead")
    public ResponseEntity<String> markAllAsRead() {
        List<NotificationEnvoyee> notifications =notificationRepository.findAll();
        for (NotificationEnvoyee notif : notifications) {
            if (!notif.getlue()) {
                notif.setLue(true);
            }
        }
        notificationRepository.saveAll(notifications);
        return ResponseEntity.ok("✅ Toutes les notifications marquées comme lues !");
    }
    @GetMapping("/stream")
    public SseEmitter stream() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        return emitter;
    }

    public void sendNotificationToAll(String message) {
        System.out.println("📢 Envoi de notification : " + message);
        List<SseEmitter> deadEmitters = new ArrayList<>();

        int unreadCount = NotificationService.getUnreadNotification(); // <-- On récupère ici

        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event()
                        .name("notif")
                        .data(Map.of(
                                "message", message,
                                "unreadCount", unreadCount
                        ))
                );
                System.out.println("✅ Notification envoyée à un client.");
            } catch (IOException e) {
                System.out.println("⚠️ Erreur lors de l'envoi : " + e.getMessage());
                emitter.complete();
                deadEmitters.add(emitter);
            }
        }

        emitters.removeAll(deadEmitters);
    }


    @DeleteMapping("/supprimer")
    public ResponseEntity<String> supprimerNotifParMessage(@RequestBody Map<String, String> body) {
        String messageFront = body.get("message");
        System.out.println("🔍 Message reçu du front : [" + messageFront + "]");

        // Nettoyer le message (enlever espaces, sauts de ligne, etc.)
        String cleanedMessageFront = messageFront.replaceAll("\\s+", "").trim();

        List<NotificationEnvoyee> all = notificationRepository.findAll();
        for (NotificationEnvoyee notif : all) {
            String messageDB = notif.getMessage();
            String cleanedMessageDB = messageDB.replaceAll("\\s+", "").trim();

            if (cleanedMessageDB.equalsIgnoreCase(cleanedMessageFront)) {
                System.out.println("✅ Notification correspondante trouvée !");
                notif.setLue(true);
                notificationRepository.save(notif);
                return ResponseEntity.ok("✅ Notification marquée comme lue !");
            }
        }

        System.out.println("❌ Aucune notification trouvée avec ce message.");
        return ResponseEntity.status(404).body("❌ Notification non trouvée");
    }

}



