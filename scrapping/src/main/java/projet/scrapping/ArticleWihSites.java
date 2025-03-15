package projet.scrapping;

import java.util.List;

public class ArticleWihSites {
    private Article article;  // L'article
    private List<Site> sites; // Liste des sites associés

    // Getters and Setters
    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public List<Site> getSites() {
        return sites;
    }

    public void setSites(List<Site> sites) {
        this.sites = sites;
    }
}
