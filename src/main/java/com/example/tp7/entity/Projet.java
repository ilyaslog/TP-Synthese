package com.example.tp7.entity;

import jakarta.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "Projet")
public class Projet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idProj")
    private int idProj;

    @Column(name = "titre")
    private String titre;

    @Column(name = "description")
    private String description;

    @Column(name = "Debut_Proj")
    private Date debutProj;

    @Column(name = "Fin_Proj")
    private Date finProj;

    @Column(name = "Duree")
    private int duree;

    @ElementCollection
    @CollectionTable(
            name = "Projet_Competences_Requise",
            joinColumns = @JoinColumn(name = "id_proj")
    )
    @Column(name = "competence")
    private Set<String> competencesRequise;

    @ManyToOne
    @JoinColumn(name = "ID_Chef")
    private ChefProjet chefProjet;

    @ManyToOne
    @JoinColumn(name = "id_Dev")
    private Developeur developeur;

    public Projet() {
    }

    public Projet(String titre, String description, Date debutProj, Date finProj, int duree, Set<String> competencesRequise, ChefProjet chefProjet, Developeur developeur) {
        this.titre = titre;
        this.description = description;
        this.debutProj = debutProj;
        this.finProj = finProj;
        this.duree = duree;
        this.competencesRequise = competencesRequise;
        this.chefProjet = chefProjet;
        this.developeur = developeur;
    }

    public int getIdProj() {
        return idProj;
    }

    public void setIdProj(int idProj) {
        this.idProj = idProj;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDebutProj() {
        return debutProj;
    }

    public void setDebutProj(Date debutProj) {
        this.debutProj = debutProj;
    }

    public Date getFinProj() {
        return finProj;
    }

    public void setFinProj(Date finProj) {
        this.finProj = finProj;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public Set<String> getCompetencesRequise() {
        return competencesRequise;
    }

    public void setCompetencesRequise(Set<String> competencesRequise) {
        this.competencesRequise = competencesRequise;
    }

    public ChefProjet getChefProjet() {
        return chefProjet;
    }

    public void setChefProjet(ChefProjet chefProjet) {
        this.chefProjet = chefProjet;
    }

    public Developeur getDevelopeur() {
        return developeur;
    }

    public void setDevelopeur(Developeur developeur) {
        this.developeur = developeur;
    }


}
