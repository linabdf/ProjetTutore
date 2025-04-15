package org.example;


import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.regex.Pattern;



public class WebScrapping {


    //Fabriquer l'url de recherche
    public  String makeUrl(String url, String input){
        String result = input.replaceAll("\\s","+");
        String result2;

        if(url.contains(" ")){
            result2 = url.replaceAll("\\s",result);
        }else{
            result2 = url+result;
        }
        System.out.println(result2);
        return result2;
    }

    //Formalisme pour la recherche de l'article
    public  String makerechere(String input){
        String result = input.replaceAll("\\s",".*");
        String finalinput = ".*"+result+".*";
        System.out.println(finalinput);
        return finalinput;
    }

    public boolean isValidUrl(String url) {
        try {
            new java.net.URL(url).toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }




    public Scraping PremierScraping(boolean newarticle,String urlRecherche,String urlArticle,String url2, String cssSelectorprix,
                                    String cssSelectortitre,String cssSelector,String finalinput,String imageS,String descriptionS) {
        String prix = "";
        System.setProperty("webdriver.gecko.driver", "/snap/bin/geckodriver");


        // Configurer Firefox en mode headless
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--headless"); // Active le mode headless

        // Crée une instance du navigateur
        WebDriver driver = new FirefoxDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        if (newarticle) {
            String url7 = "";
            String image = "";
            String description2="";
            Scraping site = null;
            Boolean trouve = false;

            try {
                // Ouvrir l'URL
                driver.get(urlRecherche);
                String url4 = driver.getCurrentUrl();
                System.out.println("Title: " + url4);



                Thread.sleep(5000);
                // Récupérer la liste des articles affichés
                List<WebElement> produitstitre = driver.findElements(By.cssSelector(cssSelector));
                System.out.println("Nombre d'articles trouvés : " + produitstitre.size());

                for (WebElement produit : produitstitre) {

                    Thread.sleep(5000);

                    String titre2 = produit.findElement(By.cssSelector(cssSelectortitre)).getText();
                    System.out.println("Titre: " + titre2);


                    url7 = produit.findElement(By.cssSelector(url2)).getAttribute("href");
                    System.out.println("urlProduit: " + url7);
                    if(url7 == null || url7.isEmpty()) {

                        String quary3 = "return document.querySelector('" + url2 + "')?.href;";
                        JavascriptExecutor js3 = (JavascriptExecutor) driver;
                        url7 = (String) js3.executeScript(quary3);
                        System.out.println("urlProduit: " + url7);

                    }

                    // Vérifier si le titre contient la recherche"
                    if (Pattern.compile(finalinput, Pattern.CASE_INSENSITIVE).matcher(titre2).matches()) {
                        System.out.println("Produit trouvé: " + titre2);
                        trouve = true;
                        break;
                    }


                }

                if(!trouve){
                    return null;
                }

                driver.get(url7);


                Thread.sleep(5000);
                prix = driver.findElement(By.cssSelector(cssSelectorprix)).getText().replace("\n", ",");
                prix = prix.replace(",", "€");
                prix = prix.replace(".", "€");
                prix = prix.replace("€€€€", "€");
                prix = prix.split(",")[0].trim().split(" ")[0];
                if (prix.endsWith("€") || prix.endsWith("*")) { // Vérifie si le dernier caractère est "!"
                    prix = prix.substring(0, prix.length() - 1);

                }
                System.out.println("Prix: " + prix);


                Thread.sleep(5000);

                String quary = "return document.querySelector('"+ descriptionS + "')?.innerText.trim();";
                JavascriptExecutor js = (JavascriptExecutor) driver;
                description2 = (String) js.executeScript(quary);


                System.out.println("description: " + description2);


                image = driver.findElement(By.cssSelector(imageS)).getAttribute("src");

                if (image.isEmpty()) {
                    image = driver.findElement(By.cssSelector(imageS)).getAttribute("srcset");
                    image = image.split(",")[0].trim().split(" ")[0];

                }
                System.out.println("image: " + image);
                driver.quit();



            } catch (Exception e) {
                System.out.println("[WebScrapping] Erreur dans la fonction PremierScraping() --> if (" + e.getMessage() + ")");
                driver.quit();
            }
            return new Scraping(url7,prix,image,description2);
        } else {
            try {
                driver.get(urlArticle);

                System.out.println(driver.getCurrentUrl());



                Thread.sleep(5000);

                prix = driver.findElement(By.cssSelector(cssSelectorprix)).getText().replace("\n", ",");
                prix = prix.replace(",", "€");
                prix = prix.replace(".", "€");
                prix = prix.replace("€€€€", "€");
                prix = prix.split(",")[0].trim().split(" ")[0];
                if (prix.endsWith("€") || prix.endsWith("*")) { // Vérifie si le dernier caractère est "!"
                    prix = prix.substring(0, prix.length() - 1);

                }
                System.out.println("Prix: " + prix);
                driver.quit();
            } catch (Exception e) {
                System.out.println("[WebScrapping] Erreur dans la fonction PremierScraping() --> else (" + e.getMessage() + ")");
                driver.quit();
            }
            return new Scraping(prix);
        }
    }
}