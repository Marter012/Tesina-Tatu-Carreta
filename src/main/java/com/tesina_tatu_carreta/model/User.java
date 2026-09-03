package com.tesina_tatu_carreta.model;

public class User {

    private int userId;
    private String fullName;
    private String username;
    private String password;
    private String role;
    private String status;


    // =========================
    // EMPTY CONSTRUCTOR
    // =========================

    public User() {
    }


    // =========================
    // FULL CONSTRUCTOR
    // =========================

    public User(
            int userId,
            String fullName,
            String username,
            String password,
            String role,
            String status) {

        this.userId = userId;
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.role = role;
        this.status = status;
    }


    // =========================
    // USER ID
    // =========================

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    // =========================
    // FULL NAME
    // =========================

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


    // =========================
    // USERNAME
    // =========================

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    // =========================
    // PASSWORD
    // =========================

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    // =========================
    // ROLE
    // =========================

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    // =========================
    // STATUS
    // =========================

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}