package com.example.weepdep.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String username;
    private String anon;
    private String password;

    public User() {
    	this.anon = "anonymus";
    }
    
    public User(String id) {
        this.id = id;
        this.anon = "anonymus";
    }
    
    // getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getAnon() {
        return anon;
    }
}
