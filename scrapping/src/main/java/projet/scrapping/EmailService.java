package projet.scrapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
@Service
public class EmailService {
    @Autowired
    private JavaMailSender emailSender;

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("linaboudefoua12@gmail.com");  // L'adresse de l'expéditeur
        message.setTo(to);  // L'adresse du destinataire
        message.setSubject(subject);  // L'objet de l'email
        message.setText(text);  // Le corps de l'email
        emailSender.send(message);  // Envoi de l'email////
    }
}