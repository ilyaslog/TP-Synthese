package com.example.tp7.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "Developeur")
public class Developeur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "Competences")
    private String competences;

    @OneToMany(mappedBy = "developpeur", cascade = CascadeType.ALL)
    private List<ProjDev> projDevs;

    public Developeur() {
    }

    public Developeur(String name, String login, String password, String competences) {
        this.name = name;
        this.login = login;
        this.password = password;
        this.competences = competences;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getCompetences() {
        return competences;
    }

    public void setCompetences(String competences) {
        this.competences = competences;
    }

    public List<ProjDev> getProjDevs() {
        return projDevs;
    }

    public void setProjDevs(List<ProjDev> projDevs) {
        this.projDevs = projDevs;
    }

    @Override
    public String toString() {
        return "Developeur{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", competences='" + competences + '\'' +
                '}';
    }


}
