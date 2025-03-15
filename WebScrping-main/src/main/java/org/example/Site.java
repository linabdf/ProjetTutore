package org.example;

public class Site {
    private int numS;
    private String urlarticle;
    private String nomSite;
    private int numA;

    public Site(int numS, String urlarticle, String nomSite, int numA){
        this.numS = numS;
        this.urlarticle = urlarticle;
        this.nomSite = nomSite;
        this.numA = numA;
    }

    public int getNumS() {
        return numS;
    }

    public void setNumS(int numS) {
        this.numS = numS;
    }

    public String getUrlarticle() {
        return urlarticle;
    }

    public void setUrlarticle(String urlarticle) {
        this.urlarticle = urlarticle;
    }

    public String getNomsite() {
        return nomSite;
    }

    public void setNomsite(String nomsite) {
        this.nomSite = nomsite;
    }

    public int getNumA() {
        return numA;
    }

    public void setNumA(int numA) {
        this.numA = numA;
    }
}
