package com.example.weebturkishdep;

public class UserData {
    private static UserData instance;
    private String username;

    private UserData() {
        // Private constructor to prevent instantiation
    }

    public static UserData getInstance() {
        if (instance == null) {
            instance = new UserData();
        }
        return instance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}