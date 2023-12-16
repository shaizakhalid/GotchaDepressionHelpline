package com.example.gotcha_depressionhelpline;


public class User {
    public String username,email, password, userid, title, name, gender, previousDiagnois, experience, cnic;

    public User() {

    }





    public User(String username, String email, String password, String userid, String title, String name,
                String gender, String previousDiagnois, String experience, String cnic) {
        this.email = email;
        this.username=username;
        this.password = password;
        this.userid = userid;
        this.title = title;
        this.name = name;
        this.gender = gender;
        this.previousDiagnois = previousDiagnois;
        this.experience = experience;
        this.cnic = cnic;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPreviousDiagnois() {
        return previousDiagnois;
    }

    public void setPreviousDiagnois(String previousDiagnois) {
        this.previousDiagnois = previousDiagnois;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

}