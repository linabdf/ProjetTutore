package projet.scrapping.Site;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projet.scrapping.Article.Article;

import java.util.List;

@Repository
public interface SiteRepository extends JpaRepository<Site,Integer> {
    List<Site> findByArticle(Article article);
}
