package com.example.tp7.DAO;

import com.example.tp7.entity.Developeur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DevelopeurRepository extends JpaRepository<Developeur, Integer> {
    Developeur findByLoginAndPassword(String login, String password);
    Developeur findByLogin(String login);
    List<Developeur> findByNameContainingIgnoreCase(String name);

}