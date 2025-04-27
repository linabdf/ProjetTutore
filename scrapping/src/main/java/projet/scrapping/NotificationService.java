package projet.scrapping;
import io.jsonwebtoken.io.IOException;
import jakarta.transaction.Transactional;
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
        @Autowired
         private static NotificationEnvoyeeRepository notificationEnvoyeeRepository = null;
        private Map<String, List<String>> notifications = new ConcurrentHashMap<>();
        public NotificationService(NotificationEnvoyeeRepository notificationEnvoyeeRepository) {
                this.notificationEnvoyeeRepository = notificationEnvoyeeRepository;
        }
        public void ajouterNotificationPourUtilisateur(String email, String message) {
            notifications.computeIfAbsent(email, k -> new ArrayList<>()).add(message);
        }

        public List<String> getNotifications(String email) {
            return notifications.getOrDefault(email, new ArrayList<>());
        }

        public void clearNotifications(String email) {
            notifications.remove(email);
        }
        @Transactional
        public static int getUnreadNotification(){
            List<NotificationEnvoyee> allNotifications=notificationEnvoyeeRepository.findAll();
            int unreadCount = 0;
            for (NotificationEnvoyee notif:allNotifications){
                if(!notif.getlue()&&("push".equals(notif.getTypeNotif()))){unreadCount++;}
            }
            return unreadCount;
        }
   }




