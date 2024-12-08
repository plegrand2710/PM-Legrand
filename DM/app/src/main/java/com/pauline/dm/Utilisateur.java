package com.pauline.dm;

public class Utilisateur {
    private int id;
    private String username;
    private String role;

    public Utilisateur(int id, String username, String role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }
}