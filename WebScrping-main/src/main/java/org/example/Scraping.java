package org.example;

import java.sql.Timestamp;

public class Scraping {

    private String prix;
    private String urlarticle;
    private String nomsite;
    private int numA;
    private int numS;
    private Timestamp min;
    private String image;
    private String description;



    public Scraping(String prix){
        this.prix = prix;
    }

    public Scraping(String urlarticle,String prix, String image, String description){
        this.urlarticle = urlarticle;
        this.prix = prix;
        this.image = image;
        this.description = description;

    }

    public Scraping(int numA, String nomsite, String urlarticle, Timestamp min,int numS){
        this.numA = numA;
        this.nomsite = nomsite;
        this.urlarticle = urlarticle;
        this.min = min;
        this.numS = numS;
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
        return nomsite;
    }

    public void setNomsite(String nomsite) {
        this.nomsite = nomsite;
    }

    public int getNumA() {
        return numA;
    }

    public void setNumA(int numA) {
        this.numA = numA;
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

    public int getNumS() {
        return numS;
    }

    public void setNumS(int numS) {
        this.numS = numS;
    }
}