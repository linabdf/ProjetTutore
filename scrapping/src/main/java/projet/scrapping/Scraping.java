package projet.scrapping;

import jakarta.persistence.*;

import java.sql.Timestamp;
@Entity
@Table(name="Scraping")
public class Scraping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "numSC")
    private Integer  numSC;
    private String prix;
    private String urlarticle;
    private String nomSite;
    private Timestamp min;
    private String image;
    private String description;
    @ManyToOne
    @JoinColumn(name = "numA",referencedColumnName = "numA")
    private Article Article ;
    @ManyToOne
    @JoinColumn(name = "numS",referencedColumnName = "numS")
    private Site site ;
  //  private int numA;
    //private int numS;
    public Scraping(String prix){
        this.prix = prix;
    }

    public Scraping(String urlarticle,String prix, String image, String description){
        this.urlarticle = urlarticle;
        this.prix = prix;
        this.image = image;
        this.description = description;

    }
 public  Scraping(){}
    public Scraping(Article Article, String nomsite, String urlarticle, Timestamp min,Site Site){
        this.Article = Article;
        this.nomSite = nomsite;
        this.urlarticle = urlarticle;
        this.min = min;
        this.site = Site;
    }

    public String getUrlarticle() {
        return urlarticle;
    }

    public void setUrlarticle(String urlarticle) {
        this.urlarticle = urlarticle;
    }


    public String getPrix() {
        return prix;
    }

    public void setPrix(String prix) {
        this.prix = prix;
    }


    public String getNomsite() {
        return nomSite;
    }

    public void setNomsite(String nomsite) {
        this.nomSite = nomsite;
    }

    public Article getNumA() {
        return Article;
    }

    public void setNumA(Article Article) {
        this.Article = Article;
    }

    public Timestamp getMin() {
        return min;
    }

    public void setMin(Timestamp min) {
        this.min = min;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Site getNumS() {
        return site;
    }

    public void setNumS(Site numS) {
        this.site = numS;
    }

}
