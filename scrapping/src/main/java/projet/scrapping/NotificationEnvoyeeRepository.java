package projet.scrapping;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationEnvoyeeRepository
   extends JpaRepository<NotificationEnvoyee, Long> {
        boolean existsByArticleAndPrix(Article article, double prix);
       NotificationEnvoyee findByMessageContaining(String keyword);

    Optional<NotificationEnvoyee> findTopByArticle(Article article);

}


