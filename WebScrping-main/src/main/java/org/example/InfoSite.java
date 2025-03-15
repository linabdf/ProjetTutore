package org.example;

public class InfoSite {
    private String nom;
    private String url;
    private String listeproduits;
    private String article;
    private String titre;
    private String prix;
    private String prix2;
    private String description;
    private String image;

    public InfoSite(String nom,String url,String listeproduits,String article,String titre,String prix,String prix2,String description,String image){
        this.nom = nom;
        this.url = url;
        this.listeproduits = listeproduits;
        this.article = article;
        this.titre = titre;
        this.prix = prix;
        this.prix2 = prix2;
        this.description = description;
        this.image = image;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getListeproduits() {
        return listeproduits;
    }

    public void setListeproduits(String listeproduits) {
        this.listeproduits = listeproduits;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getPrix() {
        return prix;
    }

    public void setPrix(String prix) {
        this.prix = prix;
    }

    public String getPrix2() {
        return prix2;
    }

    public void setPrix2(String prix2) {
        this.prix2 = prix2;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
