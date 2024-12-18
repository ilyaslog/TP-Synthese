package com.example.tp7.DAO;

import com.example.tp7.entity.ProjDev;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjDevRepository extends JpaRepository<ProjDev, Integer> {

    // Find a ProjDev by the project ID
    ProjDev findByProjet_IdProj(Integer idProj);

    // Find a ProjDev by both project ID and developer ID
    ProjDev findByProjet_IdProjAndDeveloppeur_Id(Integer projectId, Integer developerId);


}
