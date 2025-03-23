package projet.scrapping;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "site")
public class Site {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer numS;

    @Column(name = "urlarticle")
    @JsonProperty("urlArticle")  // Assurez-vous que le JSON utilise cette propriété
    private String urlArticle;
//nomsite
    @Column(name = "nomSite")
    @JsonProperty("nomSite")  // Assurez-vous que le JSON utilise cette propriété
    private String nomSite;

    // Relation ManyToOne avec Article
    @ManyToOne
    @JoinColumn(name = "numA", referencedColumnName = "numA")  // La clé étrangère vers l'article
    private Article article;
    @OneToMany(mappedBy = "site", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tendance> tendances;
    // Constructeur par défaut
    public Site() {
    }

    // Constructeur avec paramètres
    public Site(String urlArticle, String nomSite, Article article) {
        this.urlArticle = urlArticle;
        this.nomSite = nomSite;
        this.article = article;
    }

    // Getters et Setters
    public Integer getNumS() {
        return numS;
    }

    public void setNumS(Integer numS) {
        this.numS = numS;
    }

    public String getUrlArticle() {
        return urlArticle;
    }

    public void setUrlArticle(String urlArticle) {
        this.urlArticle = urlArticle;
    }

    public String getNomSite() {
        return nomSite;
    }

    public void setNomSite(String nomSite) {
        this.nomSite = nomSite;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }
}
