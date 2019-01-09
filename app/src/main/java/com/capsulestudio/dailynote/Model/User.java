package com.capsulestudio.dailynote.Model;

/**
 * Created by Juman on 1/23/2018.
 */

public class User {
    private int id;
    private String email;
    private String pass;

    public User() {
    }

    public User(String email, String password) {
        this.email = email;
        this.pass = password;
    }

    public User(int id, String email, String password) {
        this.id = id;
        this.email = email;
        this.pass = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return pass;
    }

    public void setPassword(String password) {
        this.pass = password;
    }
}
