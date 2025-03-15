package org.example;

import java.sql.Timestamp;

public class Tendance {

    private int numT;
    private String prix;
    private Timestamp date;
    private int numS;

    public Tendance(int numT, String prix, Timestamp date, int numS){
        this.numT = numT;
        this.prix = prix;
        this.date = date;
        this.numS = numS;
    }


    public int getNumT() {
        return numT;
    }

    public void setNumT(int numT) {
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

    public int getNumS() {
        return numS;
    }

    public void setNumS(int numS) {
        this.numS = numS;
    }
}
