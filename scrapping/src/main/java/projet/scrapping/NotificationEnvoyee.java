package projet.scrapping;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class NotificationEnvoyee {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String typeNotif; // email, push, etc.

    private double prix;

    private LocalDateTime dateEnvoi;

    private String message;
    private boolean lue = false;
    @ManyToOne
    @JoinColumn(name = "numAnotif")
    private Article article;
    public NotificationEnvoyee(){
    }
    public NotificationEnvoyee(String message, boolean getlue) {
        this.message=message;
        this.lue=getlue;
    }

    // --- Getters et Setters ---
    public Integer getId() {
        return id;
    }
    public boolean getlue() {
        return lue;
    }
    public  void setLue(Boolean lue){
        this.lue=lue;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getTypeNotif() {
        return typeNotif;
    }

    public void setTypeNotif(String typeNotif) {
        this.typeNotif = typeNotif;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public LocalDateTime getDateEnvoi() {
        return dateEnvoi;
    }

    public void setDateEnvoi(LocalDateTime dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
