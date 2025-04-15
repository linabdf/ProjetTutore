package projet.scrapping;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class NotificationEnvoyee {



        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        private String typeNotif; // email, push, etc.
        private double prix;

        private LocalDateTime dateEnvoi;

    @OneToOne
    private Article article;
    public void setMessage(String message) {
        this.dateEnvoi=dateEnvoi;
    }

    public void setDateEnvoi(LocalDateTime now) {
        this.dateEnvoi=dateEnvoi;
    }

    public void setArticle(Article article) {
        this.article=article;
    }
    public void setPrix(double prix) {
        this.prix=prix;
    }

    public double getPrix() {
        return  prix;
    }
    // Getters, setters, constructeur
    }

