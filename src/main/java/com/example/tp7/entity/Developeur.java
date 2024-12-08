package com.example.tp7.entity;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "Developeur")
public class Developeur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @ElementCollection
    @CollectionTable(
            name = "Developeur_Competences",
            joinColumns = @JoinColumn(name = "id_dev")
    )
    @Column(name = "competence")
    private Set<String> competences;

    public Developeur() {
    }

    public Developeur(String name, String login, String password, Set<String> competences) {
        this.name = name;
        this.login = login;
        this.password = password;
        this.competences = competences;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getCompetences() {
        return competences;
    }

    public void setCompetences(Set<String> competences) {
        this.competences = competences;
    }

}
