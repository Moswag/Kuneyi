package com.android.cytex.model;

public class AndroidUser {

    private String name;
    private String surname;
    private String phonenumber;
    private String username;
    private String password;
    private String security;
    private int Sync_status;


    public AndroidUser() {
    }

    public AndroidUser(String name, String surname, String phonenumber, String username, String password, String security, int sync_status) {
        this.name = name;
        this.surname = surname;
        this.phonenumber = phonenumber;
        this.username = username;
        this.password = password;
        this.security = security;
        Sync_status = sync_status;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
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

    public String getSecurity() {
        return security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }

    public int getSync_status() {
        return Sync_status;
    }

    public void setSync_status(int sync_status) {
        Sync_status = sync_status;
    }
}
