package com.example.tp7.DAO;

import com.example.tp7.entity.ProjDev;
import com.example.tp7.entity.Projet;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjetRepository extends JpaRepository<Projet, Integer> {
    List<Projet> findByStatut(int statut);

    List<Projet> findBystatut(int i);
}
