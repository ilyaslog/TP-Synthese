        package com.example.tp7.entity;

        import jakarta.persistence.*;
        import java.util.Date;
        import java.util.List;

        @Entity
        @Table(name = "Projet")
        public class Projet {

            @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
            @Column(name = "idProj")
            private Integer idProj;

            @Column(name = "titre")
            private String titre;

            @Column(name = "description")
            private String description;

            @Temporal(TemporalType.DATE)
            @Column(name = "Debut_Proj")
            private Date debutProj;

            @Temporal(TemporalType.DATE)
            @Column(name = "Fin_Proj")
            private Date finProj;

            @Column(name = "Duree")
            private Integer duree;

            public int getStatut() {
                return statut;
            }

            public void setStatut(int statut) {
                this.statut = statut;
            }

            @Column(name = "Statut")
            private int statut; // 0: en cours, 1: terminé
            @Column(name = "Competences_Requise")
            private String competencesRequise;

            @ManyToOne
            @JoinColumn(name = "ID_Chef", referencedColumnName = "id")
            private ChefProjet chefProjet;

            @OneToMany(mappedBy = "projet", cascade = CascadeType.ALL)
            private List<ProjDev> projDevs;

        public Projet() {
            }

            public Projet(String titre, String description, Date debutProj, Date finProj, int duree, String competencesRequise, ChefProjet chefProjet, Developeur developeur) {
                this.titre = titre;
                this.description = description;
                this.debutProj = debutProj;
                this.finProj = finProj;
                this.duree = duree;
                this.competencesRequise = competencesRequise;
                this.chefProjet = chefProjet;
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

            public String getCompetencesRequise() {
                return competencesRequise;
            }

            public void setCompetencesRequise(String competencesRequise) {
                this.competencesRequise = competencesRequise;
            }

            public ChefProjet getChefProjet() {
                return chefProjet;
            }

            public void setChefProjet(ChefProjet chefProjet) {
                this.chefProjet = chefProjet;
            }



        }
