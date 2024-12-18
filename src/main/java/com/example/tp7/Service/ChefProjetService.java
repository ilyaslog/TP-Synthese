package com.example.tp7.Service;

import com.example.tp7.entity.ChefProjet;
import com.example.tp7.entity.Developeur;

import java.util.List;

public interface ChefProjetService  {
    List<ChefProjet>findall();

    ChefProjet findById(int id);

    ChefProjet save(ChefProjet chefProjet);

    void deleteById(int id);

    public ChefProjet validateLogin(String login, String password);

    ChefProjet findByLogin(String login);  // To find ChefProjet by login
    ChefProjet updatePassword(String login, String newPassword); // Update password
    ChefProjet updateByid(int id, ChefProjet chefProjet);

}
