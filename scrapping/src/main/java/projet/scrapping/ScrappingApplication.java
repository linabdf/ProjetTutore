package projet.scrapping;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcTransactionManagerAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {
		R2dbcAutoConfiguration.class,
		R2dbcTransactionManagerAutoConfiguration.class
})
@ComponentScan(basePackages = "projet.scrapping")
public class ScrappingApplication/* implements CommandLineRunner {
*/
/*	@Autowired
	private UtilisateurService utilisateurService;
   @Autowired
   private ArticleService articleService;*/
{
	@Value("${spring.datasource.url}")
	private String dbUrl;

	@PostConstruct
	public void init() {
		System.out.println("✅ Connected to database: " + dbUrl);
	}
	public static void main(String[] args) {
		SpringApplication.run(ScrappingApplication.class, args);
	}
/*
	@Override
	public void run(String... args) throws Exception {
		//utilisateurService.insererUtilisateur( "kenza", "tiourtit", "tiouartitKenza@gmail.com", "shsghgsghhg");
	/*ArticleService articleService = null;*/
		//ArticleService.insererArticle( "Ordddzi", "dddddd", String.valueOf(222), "U263","notifMail",12);
	//ArticleService.insererArticle( "Orssdzi", "ssssbbsbsbbs", String.valueOf(222), "U258");
		//System.out.println("jhghjs");
		// Article article=ArticleService.createArticle("1243");
		// Exemple d'utilisation de utilisateurService
	    /*System.out.println("📌 Liste des utilisateurs en base :");
		utilisateurService.getAllUtilisateurs().forEach(utilisateur -> {
			System.out.println(utilisateur);
		});
		//utilisateurService.supprimerUtilisateur("U256");
	//	articleService.supprimerArtcile("ordi","U263");
	//	utilisateurService.modifierUtilisateur("kenza","tiourtit","kenzatiourtit@gmmail.com");
	}
}*/
}