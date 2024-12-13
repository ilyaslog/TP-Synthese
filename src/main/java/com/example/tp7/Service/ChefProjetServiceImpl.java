package com.example.tp7.Service;

import com.example.tp7.DAO.ChefProjetRepository;
import com.example.tp7.entity.ChefProjet;
import com.example.tp7.entity.Developeur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChefProjetServiceImpl implements ChefProjetService {
    @Autowired
    private ChefProjetRepository chefProjetRepository;

    public ChefProjetServiceImpl() {
        super();
    }
    @Autowired
    private ChefProjetRepository ChefProjetRepository;
    @Override
    public java.util.List<ChefProjet> findall() {
       return ChefProjetRepository.findAll();
    }
    @Override
    public ChefProjet findById(int id) {
        return ChefProjetRepository.findById(id).orElse(null);
    }

    @Override
    public ChefProjet save(ChefProjet chefProjet) {
        return chefProjetRepository.save(chefProjet);
    }
    @Override
    public void deleteById(int id) {
        chefProjetRepository.deleteById(id);
    }
    @Override
    public ChefProjet validateLogin(String login, String password) {
        ChefProjet chefProjet = ChefProjetRepository.findByLogin(login);
        if (chefProjet != null && chefProjet.getPassword().equals(password)) {
            return chefProjet; // Credentials are correct
        }
        return null; // Credentials are incorrect
    }


}
