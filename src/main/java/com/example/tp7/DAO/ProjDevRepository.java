package com.example.tp7.DAO;

import com.example.tp7.entity.ProjDev;
import com.example.tp7.entity.Projet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjDevRepository extends JpaRepository<ProjDev, Integer> {

    @Query("SELECT pd FROM ProjDev pd WHERE pd.projet.idProj = :idProj")
    List<ProjDev> findByProjet_IdProj(@Param("idProj") Integer idProj);

    @Query("SELECT pd FROM ProjDev pd WHERE pd.projet.idProj = :projectId AND pd.developpeur.id = :developerId")
    Optional<ProjDev> findByProjet_IdProjAndDeveloppeur_Id(@Param("projectId") Integer projectId, @Param("developerId") Integer developerId);

   @Query("SELECT COUNT(p) FROM Projet p WHERE p.statut = 1 AND p.chefProjet.id = :idChef")
    long countFinishedProjectsByChef(@Param("idChef") Integer idChef);

    @Query("SELECT COUNT(p) FROM Projet p WHERE p.statut = 0 AND p.chefProjet.id = :idChef")
    long countUnfinishedProjectsByChef(@Param("idChef") Integer idChef);

  @Query("SELECT AVG(pd.stars) FROM ProjDev pd JOIN pd.projet p WHERE p.chefProjet.id = :idChef")
    Double findAverageRatingByChef(@Param("idChef") Integer idChef);

  @Query("SELECT COUNT(pd) FROM ProjDev pd WHERE pd.developpeur.id = :idDev AND pd.projet.statut = 1")
    long countFinishedProjectsByDeveloper(@Param("idDev") Integer idDev);

    @Query("SELECT COUNT(pd) FROM ProjDev pd WHERE pd.developpeur.id = :idDev AND pd.projet.statut = 0")
    long countUnfinishedProjectsByDeveloper(@Param("idDev") Integer idDev);
    @Query("SELECT AVG(pd.stars) FROM ProjDev pd WHERE pd.developpeur.id = :idDev AND pd.projet.statut = 1")
    Double findAverageRatingByDeveloper(@Param("idDev") Integer idDev);

    @Query("SELECT DISTINCT p FROM Projet p JOIN p.projDevs pd WHERE p.statut = :statut AND pd.developpeur.id = :idDev")
    List<Projet> findByStatutAndDeveloppeur_Id(@Param("statut") int statut, @Param("idDev") Integer idDev);
}
