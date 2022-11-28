package com.example.springsecurityapplication.models;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Table(name = "Person")
public class Person {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Поле не может быть пустым")
    @Size(min = 5, max = 100, message = "Поле должно содержать от 5 до 20 символов")
    @Column(name = "login")
    private String login;

    @NotEmpty(message = "Поле не может быть пустым")
    @Size(min = 5, max = 100, message = "Поле должно содержать от 5 до 20 символов")
    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role;

    public Person() {
    }

    public Person(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
}
