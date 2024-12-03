package com.example.tp7.entity;

import jakarta.persistence.*;

@Entity
@Table(name="Tbl_dev")
public class Developeur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_dev")
    private int id;

    @Column(name = "nom_dev")
    private String name;

    @Column(name="login_dev")
    private String login;

    @Column(name="password_dev")
    private String password;

    @Column(name="competences_id")
    private int competences;


}
