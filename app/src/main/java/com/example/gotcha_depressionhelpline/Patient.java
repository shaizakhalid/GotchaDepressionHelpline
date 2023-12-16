package com.example.gotcha_depressionhelpline;

public class Patient {
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

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
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

    public String username, email, password, userid, title, fname, lname, gender, previousDiagnois, experience, cnic;

    public Patient() {

    }

    public Patient(String username, String email, String password, String userid,
                String title, String fname, String lname, String gender, String previousDiagnois,
                String experience, String cnic) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.userid = userid;
        this.title = title;
        this.fname = fname;
        this.lname = lname;
        this.gender = gender;
        this.previousDiagnois = previousDiagnois;
        this.experience = experience;
        this.cnic = cnic;
    }
}
