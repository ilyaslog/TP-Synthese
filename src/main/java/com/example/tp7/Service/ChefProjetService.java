package com.example.tp7.Service;

import com.example.tp7.entity.ChefProjet;

import java.util.List;

public interface ChefProjetService  {
    List<ChefProjet>findall();

    ChefProjet findById(int id);

    ChefProjet save(ChefProjet chefProjet);

    void deleteById(int id);

}
