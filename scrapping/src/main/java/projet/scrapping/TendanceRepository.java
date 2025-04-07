package projet.scrapping;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface TendanceRepository extends JpaRepository<Tendance,Integer> {
     List<Tendance> findBySiteNumS(int numS); // Modification ici

     List<Tendance> findAll();

     List<Tendance> findBySite(Site site);

     Optional<Tendance> findTopBySiteOrderByNumTDesc(Site site);
}
