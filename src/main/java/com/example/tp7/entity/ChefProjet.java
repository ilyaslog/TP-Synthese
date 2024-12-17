package com.example.tp7.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "Chef_Projet")
public class ChefProjet {

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

    @Column(name = "Bio")
    private String bio;

    @OneToMany(mappedBy = "chefProjet", cascade = CascadeType.ALL)
    private List<Projet> projets;

    // Constructeurs, getters et setters
public ChefProjet() {
    }

    public ChefProjet(String name, String login, String password) {
        this.name = name;
        this.login = login;
        this.password = password;
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
}
