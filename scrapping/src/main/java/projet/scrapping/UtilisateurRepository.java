package projet.scrapping;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {
        @Query("SELECT u FROM Utilisateur u WHERE u.emailU = :email")
        Utilisateur findByEmail(@Param("email") String emailu);
        List<Utilisateur> findAll();
}

