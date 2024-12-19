package com.example.tp7.Service;

import com.example.tp7.DAO.ProjDevRepository;
import com.example.tp7.entity.ProjDev;
import com.example.tp7.entity.Projet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjDevService {

    private final ProjDevRepository projDevRepository;

    @Autowired
    public ProjDevService(ProjDevRepository projDevRepository) {
        this.projDevRepository = projDevRepository;
    }

    // Save a review (ProjDev entity)
    public void saveReview(ProjDev projDev) {
        projDevRepository.save(projDev);
    }

    // Get all ProjDev records for a given project ID
    public List<ProjDev> getProjDevsByProjetId(Integer idProj) {
        return projDevRepository.findByProjet_IdProj(idProj);
    }

    // Get a specific ProjDev by project ID and developer ID
    public Optional<ProjDev> getProjDevByProjetIdAndDeveloperId(Integer projectId, Integer developerId) {
        return projDevRepository.findByProjet_IdProjAndDeveloppeur_Id(projectId, developerId);
    }

    // Get the count of finished projects by Chef (project manager) ID
    public long getFinishedProjectsByChef(Integer idChef) {
        return projDevRepository.countFinishedProjectsByChef(idChef);
    }

    // Get the count of unfinished projects by Chef ID
    public long getUnfinishedProjectsByChef(Integer idChef) {
        return projDevRepository.countUnfinishedProjectsByChef(idChef);
    }

    // Get the average rating of projects managed by Chef ID
    public Double getAverageRatingByChef(Integer idChef) {
        return projDevRepository.findAverageRatingByChef(idChef);
    }

    public Double findAverageRatingByChef( Integer idChef){
        return projDevRepository.findAverageRatingByChef(idChef);
    }

  public long countFinishedProjectsByDeveloper(Integer idDev) {
    return projDevRepository.countFinishedProjectsByDeveloper(idDev);
}

public long countUnfinishedProjectsByDeveloper(Integer idDev) {
    return projDevRepository.countUnfinishedProjectsByDeveloper(idDev);
}

public Double findAverageRatingByDeveloper(Integer idDev) {
    return projDevRepository.findAverageRatingByDeveloper(idDev);
}
public List<Projet> findBystatusAndDeveloppeur_Id(int statut, Integer idDev) {
    return projDevRepository.findByStatutAndDeveloppeur_Id(statut, idDev);
}
}
