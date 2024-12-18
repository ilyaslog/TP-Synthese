package com.example.tp7.Service;

import com.example.tp7.DAO.DevelopeurRepository;
import com.example.tp7.entity.Developeur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DevelopeurServiceImpl implements DevelopeurService {
    @Autowired
    private DevelopeurRepository developeurRepository;

    public DevelopeurServiceImpl() {
        super();
    }
    @Autowired
    public DevelopeurServiceImpl(DevelopeurRepository developeurRepository) {
        this.developeurRepository = developeurRepository;
    }
    @Override
    public List<Developeur> findAll() {
        return developeurRepository.findAll();
    }

    @Override
    public Developeur findById(int id) {
        return developeurRepository.findById(id).orElse(null);
    }

    @Override
    public Developeur save(Developeur developeur) {
        System.out.println("Saving developer: " + developeur);
        return developeurRepository.save(developeur);
    }

    @Override
    public void deleteById(int id) {
         developeurRepository.deleteById(id);
    }

    @Override
    public Developeur validateLogin(String login, String password) {
        Developeur developeur = developeurRepository.findByLogin(login);
        if (developeur != null && developeur.getPassword().equals(password)) {
            return developeur; // Credentials are correct
        }
        return null; // Credentials are incorrect
    }
    @Override
    public Developeur updateByid(int id, Developeur developeur) {
        return developeurRepository.findById(id).map(existingDevelopeur -> {
            existingDevelopeur.setName(developeur.getName());
            existingDevelopeur.setLogin(developeur.getLogin());
            existingDevelopeur.setPassword(developeur.getPassword());
            existingDevelopeur.setCompetences(developeur.getCompetences());
            existingDevelopeur.setBio(developeur.getBio());
            return developeurRepository.save(existingDevelopeur);
        }).orElseThrow(() -> new RuntimeException("Developer with ID " + id + " not found."));
    }
    @Override
    public List<Developeur> searchByName(String name) {
        return developeurRepository.findByNameContainingIgnoreCase(name);
    }


    @Override
    public Developeur findByLogin(String login) {
        return developeurRepository.findByLogin(login);  // Find Developeur by login
    }
    @Override
    public Developeur updatePassword(String login, String newPassword) {
        Developeur developeur = findByLogin(login);  // Find Developeur by login
        if (developeur != null) {
            developeur.setPassword(newPassword);  // Set new password
            return developeurRepository.save(developeur);  // Save the updated entity
        }
        return null;
    }

}
