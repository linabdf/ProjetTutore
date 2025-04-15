package projet.scrapping;
import io.jsonwebtoken.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class NotificationService {

        private Map<String, List<String>> notifications = new ConcurrentHashMap<>();

        public void ajouterNotificationPourUtilisateur(String email, String message) {
            notifications.computeIfAbsent(email, k -> new ArrayList<>()).add(message);
        }

        public List<String> getNotifications(String email) {
            return notifications.getOrDefault(email, new ArrayList<>());
        }

        public void clearNotifications(String email) {
            notifications.remove(email);
        }
   }



