package projet.scrapping;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Component
public class JwtUtil {

    private static final SecretKey SECRET_KEY =  Keys.secretKeyFor(SignatureAlgorithm.HS256); // Utilisation d'une clé sécurisée de 256 bit

    // Méthode pour générer un token
    public String generateToken(String email) {
        try {
            System.out.println("Génération du token pour l'email : " + email);
            long expirationTime = 10* 60 * 60 * 1000; // 10 heures

            return Jwts.builder()
                    .setSubject(email)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                    .signWith(SECRET_KEY) // Utilisation de la clé secrète sécurisée
                    .compact();
        } catch (Exception e) {
            System.out.println("Erreur lors de la génération du token : " + e.getMessage());
            return null;
        }
    }

    // Méthode pour extraire l'email du token
    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }


    // Méthode pour valider le token
    public boolean validateToken(String token, String email) {
        try {
            String extractedEmail = extractEmail(token);
            return (extractedEmail.equals(email) && !isTokenExpired(token));
        } catch (Exception e) {
            return false;
        }
    }

    // Méthode pour vérifier si le token est expiré
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Méthode pour extraire la date d'expiration du token
    private Date extractExpiration(String token) {
        return Jwts.parserBuilder()
                .setSigningKey( SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }
    private static Set<String> blacklistedTokens = new HashSet<>();

    // Méthode pour ajouter un token à la blacklist
    public static void addToBlacklist(String token) {
        blacklistedTokens.add(token);
    }

    // Méthode pour vérifier si un token est dans la blacklist
    public static boolean isBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }
}