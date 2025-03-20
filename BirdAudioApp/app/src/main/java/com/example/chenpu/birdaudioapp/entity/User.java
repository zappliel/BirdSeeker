package com.example.chenpu.birdaudioapp.entity;

public class User {
    private int user_id;
    private String username;
    private String password;
    private String email;

    public static User tmpUserInfo;

    public static User getTmpUserInfo() {
        return tmpUserInfo;
    }

    public static void setTmpUserInfo(User userInfo){
        tmpUserInfo = userInfo;
    }

    public User(int user_id, String user_name, String password, String email) {
        this.user_id = user_id;
        this.username = user_name;
        this.password = password;
        this.email = email;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String user_name) {
        this.username = user_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
