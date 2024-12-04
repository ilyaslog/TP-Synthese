package com.example.tp7.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ProjDev")
public class ProjDev {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_DevInProj")
    private int idDevInProj;

    @ManyToOne
    @JoinColumn(name = "idProj")
    private Projet projet;

    @ManyToOne
    @JoinColumn(name = "idDev")
    private Developeur developeur;

    @Column(name = "Commentaire")
    private String commentaire;

    @Column(name = "Stars")
    private int stars;


    public ProjDev() {
    }

    public ProjDev(Projet projet, Developeur developeur, String commentaire, int stars) {
        this.projet = projet;
        this.developeur = developeur;
        this.commentaire = commentaire;
        this.stars = stars;
    }

    public int getIdDevInProj() {
        return idDevInProj;
    }

    public void setIdDevInProj(int idDevInProj) {
        this.idDevInProj = idDevInProj;
    }

    public Projet getProjet() {
        return projet;
    }

    public void setProjet(Projet projet) {
        this.projet = projet;
    }

    public Developeur getDevelopeur() {
        return developeur;
    }

    public void setDevelopeur(Developeur developeur) {
        this.developeur = developeur;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

}
