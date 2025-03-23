package projet.scrapping;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name="Tendance")
public class Tendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer numT;

    @Column(name="prix")
    private String prix;
    @Column(name="date")
    private Timestamp date;
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "numS",referencedColumnName = "numS")
    private Site site ;
    public Tendance(String prix,Timestamp date,Site site){
        this.prix=prix;
        this.date=date;
        this.site=site;
    }
    public  Tendance(){}

    public Tendance(Integer numT, String prix, Timestamp date) {
        this.prix=prix;
        this.date=date;
        this.numT=numT;
    }

    public Integer getNumT() {
        return numT;
    }

    public void setNumT(Integer numT) {
        this.numT = numT;
    }

    public String getPrix() {
        return prix;
    }

    public void setPrix(String prix) {
        this.prix = prix;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public Site getNumS() {
        return site;
    }

    public void setNumS(Site site) {
        this.site =site ;
    }
}



