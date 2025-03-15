package org.example;

import java.sql.Timestamp;

public class Article {
    private int numA;
    private String nomarticle;
    private String description;
    private String image;
    private int seuil;
    private int frequence;
    private Timestamp derniereupdate;
    private int numU;

    public Article(int numA, String nomarticle, String description, String image, int seuil, int frequence, Timestamp derniereupdate, int numU){
        this.numA = numA;
        this.nomarticle = nomarticle;
        this.description = description;
        this.image = image;
        this.seuil = seuil;
        this.frequence = frequence;
        this.derniereupdate = derniereupdate;
        this.numU = numU;
    }

    public int getNumA() {
        return numA;
    }

    public void setNumA(int numA) {
        this.numA = numA;
    }

    public String getNomarticle() {
        return nomarticle;
    }

    public void setNomarticle(String nomarticle) {
        this.nomarticle = nomarticle;
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

    public int getSeuil() {
        return seuil;
    }

    public void setSeuil(int seuil) {
        this.seuil = seuil;
    }

    public int getFrequence() {
        return frequence;
    }

    public void setFrequence(int frequence) {
        this.frequence = frequence;
    }

    public Timestamp getDerniereupdate() {
        return derniereupdate;
    }

    public void setDerniereupdate(Timestamp derniereupdate) {
        this.derniereupdate = derniereupdate;
    }

    public int getNumU() {
        return numU;
    }

    public void setNumU(int numU) {
        this.numU = numU;
    }
}
