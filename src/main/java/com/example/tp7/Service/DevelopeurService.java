package com.example.tp7.Service;

import com.example.tp7.entity.Developeur;

import java.util.List;

public interface DevelopeurService {

    List<Developeur> findAll();

    Developeur findById(int id);

    Developeur save(Developeur developeur);

    void deleteById(int id);




}
