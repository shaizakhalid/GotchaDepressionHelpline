package com.example.gotcha_depressionhelpline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;

public class frontPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_page);
    }



    public void linkTherapistRegister_Clicked(View view){
        Intent i=new Intent(this, therapistRegisterActivity.class);
        startActivity(i);
    }

    public void linkPatientRegister_Clicked(View aview){

        Intent i=new Intent(this, patientRegisterActivity.class);
        startActivity(i);
    }


    public void linkLogin_clicked(View view) {
        Intent i=new Intent(this, loginActivity.class);
        startActivity(i);
    }
}
