package projet.scrapping;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonProperty;
@Entity
@Table(name="utilisateur")
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "numU")
    private Integer  numU;
    @Column(name="nomU")
    @JsonProperty("username")
    private String nomU;
    @Column(name="prenomU")
    @JsonProperty("firstname")
    private String prenomU;
    @Column(name="emailU")
    @JsonProperty("email")
    private String emailU;
    @Column(name="motdepasse")
    @JsonProperty("password")
    private String motdepasse;

    public Utilisateur(String lina, String boudefoua, String dndkkdkdk,String motdepasse) {

        this.nomU=lina;
        this.prenomU=boudefoua;
        this.emailU=dndkkdkdk;
        //this.notificationU= NotificationType.valueOf(notifemail);
        this.motdepasse=motdepasse;
    }
    // Constructeur par défaut
    public Utilisateur() {}

    public Utilisateur(String email, String motdepasse) {
        this.emailU=email;
        this.motdepasse=motdepasse;
    }
    public Integer getNumU(){
        return numU;
    }
    public void SetNumU(Integer numU){
        this.numU=numU;
    }
    public String getNomU(){
        return nomU;
    }
    public String getPrenom(){
        return prenomU;
    }
    public String getEmail(){
        return emailU;
    }
    public String getMotDePasse(){
        return motdepasse;
    }
    public void SetNomU(String nomU){
        this.nomU=nomU;
    }
    public void setEmail(String mail) {
        this.emailU=mail;
    }
    @Override
    public String toString() {
        return "Utilisateur nomU='" + nomU + "', prenomU='" + prenomU + "', emailU='" + emailU + "', motDepasse='" + motdepasse+"'}";
    }
}

