package projet.scrapping;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository  extends JpaRepository<Article,Integer> {
   List<Article> findByUtilisateur(Utilisateur utilisateur);
   List<Article> findAll();

   @Query("SELECT a FROM Article a WHERE a.utilisateur = :utilisateur AND a.nomA = :nomA")
   Article findByUtilisateurAndNomA(@Param("utilisateur") Utilisateur utilisateur, @Param("nomA") String nomA);


   Article findByNomA(String tvSamsung);
}
