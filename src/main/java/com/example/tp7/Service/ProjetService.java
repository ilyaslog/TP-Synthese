package com.example.tp7.Service;

import com.example.tp7.entity.ChefProjet;
import com.example.tp7.entity.ProjDev;
import com.example.tp7.entity.Projet;
import com.example.tp7.DAO.ProjetRepository;
import com.example.tp7.DAO.ProjDevRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjetService {

    private final ProjetRepository projetRepository;
    private final ProjDevRepository projDevRepository;

    @Autowired
    public ProjetService(ProjetRepository projetRepository, ProjDevRepository projDevRepository) {
        this.projetRepository = projetRepository;
        this.projDevRepository = projDevRepository;
    }

    @Transactional
    public Projet saveProjetWithDevelopers(Projet projet, List<ProjDev> developers) {
        // Save the project first
        Projet savedProject = projetRepository.save(projet);

        // Link each ProjDev to the saved project and save them
        for (ProjDev dev : developers) {
            dev.setProjet(savedProject);
            projDevRepository.save(dev);
        }

        return savedProject;
    }

    public List<Projet> getAllProjets() {
        return projetRepository.findAll();
    }
    // Retrieve a project by ID
    public Projet findById(int id) {
        return projetRepository.findById(id).orElse(null);
    }

    // Update an existing project
    public void update(Projet projet) {
        projetRepository.save(projet);
    }




    public void deleteProjectById(Integer idProj) {
        projetRepository.deleteById(idProj);
    }


    // Save review (ProjDev)
    public void saveReview(ProjDev projDev) {
        projDevRepository.save(projDev);
    }

    // Retrieve a project by its ID
    public Projet findById(Integer projetId) {
        return projetRepository.findById(projetId).orElse(null);
    }


}