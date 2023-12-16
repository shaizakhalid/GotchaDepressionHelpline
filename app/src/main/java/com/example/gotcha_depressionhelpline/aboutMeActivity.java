package com.example.gotcha_depressionhelpline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

public class aboutMeActivity extends AppCompatActivity {

    private Firebase nameRef,  emailRef, cnicRef, genderRef;
    TextView nametv, gendertv, cnictv, emailtv;
    String fname, lname, gender, cnic, email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        Firebase.setAndroidContext(this);

        nametv=findViewById(R.id.nameTextView);
        gendertv=findViewById(R.id.genderTextView);
        cnictv=findViewById(R.id.cnicTextView);
        emailtv=findViewById(R.id.emailTextView);
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        nameRef= new Firebase("https://gotcha-depression-helpline.firebaseio.com/Users/"
                +userid+"/name");
        nameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fname=dataSnapshot.getValue(String.class);
                nametv.setText(fname);
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        cnicRef= new Firebase("https://gotcha-depression-helpline.firebaseio.com/Users/"
                +userid+"/cnic");
        cnicRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cnic=dataSnapshot.getValue(String.class);
                cnictv.setText(cnic);
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        emailRef= new Firebase("https://gotcha-depression-helpline.firebaseio.com/Users/"
                +userid+"/email");
        emailRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                email=dataSnapshot.getValue(String.class);
                emailtv.setText(email);
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        genderRef= new Firebase("https://gotcha-depression-helpline.firebaseio.com/Users/"
                +userid+"/gender");
        genderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                gender=dataSnapshot.getValue(String.class);
                gendertv.setText(gender);
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }
}
