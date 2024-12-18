package com.example.tp7.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ProjDev")
public class ProjDev {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_DevInProj")
    private Integer idDevInProj;

    @ManyToOne
    @JoinColumn(name = "idProj", referencedColumnName = "idProj")
    private Projet projet;

    @ManyToOne
    @JoinColumn(name = "idDev", referencedColumnName = "id")
    private Developeur developpeur;

    @Column(name = "Commentaire")
    private String commentaire;

    @Column(name = "Stars")
    private Integer stars;


public ProjDev() {
    }

    public ProjDev(Projet projet, Developeur developpeur, String commentaire, int stars) {
        this.projet = projet;
        this.developpeur = developpeur;
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

    public Developeur getDeveloppeur() {
        return developpeur;
    }

    public void setDeveloppeur(Developeur developpeur) {
        this.developpeur = developpeur;
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
