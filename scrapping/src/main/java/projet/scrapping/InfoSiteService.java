package projet.scrapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class InfoSiteService {
    @Autowired
    private InfoSiteRepository infoSiteRepository;

    public InfoSite addInfoSite(InfoSite infoSite) {
        return infoSiteRepository.save(infoSite);
    }

    public List<InfoSite> getAllInfoSites() {
        return infoSiteRepository.findAll();
    }

    public InfoSite getInfoSiteById(Integer id) {
        return infoSiteRepository.findById(id).orElse(null);
    }

}
