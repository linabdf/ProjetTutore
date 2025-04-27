package projet.scrapping;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationEnvoyeeRepository
   extends JpaRepository<NotificationEnvoyee, Long> {
        boolean existsByArticleAndPrix(Article article, double prix);
         NotificationEnvoyee findByMessageContaining(String keyword);
         List<NotificationEnvoyee> findAll() ;
         Optional<NotificationEnvoyee> findTopByArticle(Article article);
         Optional<NotificationEnvoyee> findTopByArticleAndTypeNotifOrderByDateEnvoiDesc(Article article, String typeNotif);


}


