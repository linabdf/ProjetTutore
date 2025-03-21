package projet.scrapping.Article;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projet.scrapping.Utilisateur.Utilisateur;

import java.util.List;

@Repository
public interface ArticleRepository  extends JpaRepository<Article,Integer> {
   List<Article> findByUtilisateur(Utilisateur utilisateur);
   List<Article> findAll();

}
