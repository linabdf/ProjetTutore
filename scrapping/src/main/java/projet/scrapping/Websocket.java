package projet.scrapping;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class Websocket implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");  // Préfixe pour les messages envoyés aux clients
        config.setApplicationDestinationPrefixes("/app");  // Préfixe pour les messages envoyés par les clients
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")  // Déclare l'endpoint WebSocket
                .setAllowedOrigins("*")  // Permet toutes les origines
                .withSockJS();  // Assure une compatibilité avec SockJS
    }
}