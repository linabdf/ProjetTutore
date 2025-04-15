package projet.scrapping;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationEnvoyeeRepository
   extends JpaRepository<NotificationEnvoyee, Long> {
        boolean existsByArticleAndPrix(Article article, double prix);
    }


