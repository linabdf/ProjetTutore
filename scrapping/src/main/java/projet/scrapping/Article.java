package projet.scrapping;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "article")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "numA")
    private Integer numA;

    @Column(name = "nomA")
    @JsonProperty("nom")
    private String nomA;

    @Column(name = "seuil")
    @JsonProperty("seuil")
    private double seuil;

    @ManyToOne
    @JoinColumn(name = "numU", referencedColumnName = "numU")
    private Utilisateur utilisateur;

    @Column(name = "notif")
    @JsonProperty("notif")
    private String notif;

    @Column(name = "frequence")
    @JsonProperty("frequence")
    private Integer frequence;

    @Lob
    @Column(name = "Description",columnDefinition = "TEXT")
    private String Description;

    @Column(name = "urlImage")
    private String urlImage;

    @Column(name = "derniereupdate")
    private Timestamp derniereupdate;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Site> sites;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<NotificationEnvoyee> latestNotification = new ArrayList<>();

    // --- Constructeurs ---
    public Article() {}

    public Article(String nomA, double seuil, Utilisateur utilisateur, String notif, Integer frequence, Timestamp currentTimestamp) {
        this.nomA = nomA;
        this.seuil = seuil;
        this.utilisateur = utilisateur;
        this.notif = notif;
        this.frequence = frequence;
        this.derniereupdate = currentTimestamp;
        this.latestNotification = new ArrayList<>();
    }

    // --- Getters et Setters ---
    public Integer getNumA() {
        return numA;
    }

    public String getNomA() {
        return nomA;
    }

    public double getSeuil() {
        return seuil;
    }

    public int getFrequence() {
        return frequence;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public String getNotif() {
        return notif;
    }

    public String getDescription() {
        return Description;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setDerniereupdate(Timestamp currentTimestamp) {
        this.derniereupdate = currentTimestamp;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public void setTitre(String titre) {
        this.nomA = titre;
    }

    public void setContenu(String contenu) {
        this.Description = contenu;
    }

    public void setSeuil(double seuil) {
        this.seuil = seuil;
    }

    public void setFrequence(int frequence) {
        this.frequence = frequence;
    }

    public void setNotif(String notif) {
        this.notif = notif;
    }

    public boolean setNumu(String numuConstant) {
        if (this.utilisateur != null) {
            this.utilisateur.SetNumU(Integer.valueOf(numuConstant));
        } else {
            throw new RuntimeException("L'utilisateur est null. Impossible de définir numU.");
        }
        return false;
    }

    public List<Site> getSites() {
        return sites;
    }

    public void setSites(List<Site> sites) {
        this.sites = sites;
    }

    public List<NotificationEnvoyee> getDerniereNotification() {
        if (this.latestNotification == null) {
            this.latestNotification = new ArrayList<>(); // Assure-toi que la liste n'est jamais nulle.
        }
        return this.latestNotification; // Renvoie bien toutes les notifications
    }


    public void setDerniereNotification(NotificationEnvoyee notif) {
        if (this.latestNotification != null) {
            this.latestNotification.add(notif);
        }
    }
}
