package com.example.gotcha_depressionhelpline;

public class Therapist {

    public String  username,email, password, userid, title, name, gender, experience, cnic;

    public Therapist() {

    }

    public Therapist( String username,String email, String password, String userid,
                String title, String name,  String gender,
                String experience, String cnic) {

        this.email = email;
        this.password = password;
        this.userid = userid;
        this.title = title;
        this.name = name;
        this.gender = gender;
        this.experience = experience;
        this.cnic = cnic;
        this.username=username;
    }
}
