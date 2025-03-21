package projet.scrapping.Tendance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TendanceRepository extends JpaRepository<Tendance,Integer> {
    List<Tendance> findBySite_NumS(int siteId); // Modification ici

    List<Tendance> findAll();

}
