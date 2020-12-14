package com.example.cddlemptyproject.logic.models;


public class User {

    private String email;
    private String nome;

    public User(String nome, String email){
        this.email = email;
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public String getNome() {
        return nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
