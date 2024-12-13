package com.example.tp7.DAO;

import com.example.tp7.entity.ChefProjet;
import com.example.tp7.entity.Developeur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChefProjetRepository extends JpaRepository<ChefProjet, Integer> {

    ChefProjet findByLogin(String login);
}
