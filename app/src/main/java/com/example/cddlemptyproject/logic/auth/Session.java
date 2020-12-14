package com.example.cddlemptyproject.logic.auth;

public class Session {


    private String email;

    public Session(String email){
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
