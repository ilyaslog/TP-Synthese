package com.example.tp7.Service;

import com.example.tp7.entity.Projet;
import com.example.tp7.DAO.ProjetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjetService {

    private final ProjetRepository projetRepository;

    public ProjetService(ProjetRepository projetRepository) {
        this.projetRepository = projetRepository;
    }

    public Projet saveProjet(Projet projet) {
        return projetRepository.save(projet);
    }

    public List<Projet> getAllProjets() {
        return projetRepository.findAll();
    }
}
