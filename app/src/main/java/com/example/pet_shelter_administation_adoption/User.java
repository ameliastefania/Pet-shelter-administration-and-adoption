package com.example.pet_shelter_administation_adoption;

public class User {
    public String uuid;
    public String emailAddress;
    private String password;
    public String role;

    public User() {
        uuid = "";
        emailAddress = "anonymous";
        password = "password";
        role = "none";
    }

    public User(String uuid, String emailAddress, String password, String role) {
        this.uuid = uuid;
        this.emailAddress = emailAddress;
        this.password = password;
        this.role = role;
    }

    public User(String uuid, String emailAddress) {
        this.uuid = uuid;
        this.emailAddress = emailAddress;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "User{" +
                "uuid='" + uuid + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
