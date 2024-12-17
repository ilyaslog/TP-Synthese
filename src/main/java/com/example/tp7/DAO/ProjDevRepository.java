package com.example.tp7.DAO;

import com.example.tp7.entity.ProjDev;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjDevRepository extends JpaRepository<ProjDev, Integer> {
}
