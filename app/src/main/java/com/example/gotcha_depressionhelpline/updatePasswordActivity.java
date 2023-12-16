package com.example.gotcha_depressionhelpline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class updatePasswordActivity extends AppCompatActivity {

    EditText currPassword,newPassword, renewPassword;
    ProgressBar progressBar;
    Firebase firebaseref;
    String textCurrPassword,textNewPassword,textRePassword;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        Firebase.setAndroidContext(this);


        currPassword=findViewById(R.id.currentPasswordEditText);
        newPassword=findViewById(R.id.newPasswordEditText);
        renewPassword=findViewById(R.id.reNewPasswordEditText);
        progressBar=findViewById(R.id.simpleProgressBar);
    }

    public void submitClicked(View view) {
        progressBar.setVisibility(View.VISIBLE);
        textCurrPassword=currPassword.getText().toString().trim();
        textNewPassword=newPassword.getText().toString().trim();
        textRePassword=renewPassword.getText().toString().trim();
        if (textCurrPassword.isEmpty()) {
            currPassword.setError("Enter Current Password");
            currPassword.requestFocus();
            return; // stopping the functions excecution
        }

        if (textNewPassword.isEmpty()) {
            newPassword.setError("Enter New Password");
            newPassword.requestFocus();
            return; // stopping the functions excecution
        }

        if (textRePassword.isEmpty()) {
            renewPassword.setError("Re Enter Password");
            renewPassword.requestFocus();
            return; // stopping the functions excecution
        }

        final String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        firebaseref= new Firebase("https://gotcha-depression-helpline.firebaseio.com/Users/"+userid);
        firebaseref.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                    String password = dataSnapshot.child("password").getValue(String.class);
                    if(password.equals(textCurrPassword)) {
                        if (textNewPassword.equals(textRePassword)) {
                            firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
                            firebaseUser.updatePassword(textNewPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(getApplicationContext(), "Password changed successful", Toast.LENGTH_LONG).show();
                                        currPassword.setText("");
                                        newPassword.setText("");
                                        renewPassword.setText("");
                                        FirebaseDatabase.getInstance().getReference().child("Users").child(userid).child("password").setValue(textNewPassword);
                                        SharedPreferences sharedPreferences1 = getSharedPreferences("Login_Session", Context.MODE_PRIVATE);
                                        String type = sharedPreferences1.getString("Token", "");
                                        if(type.equals("Patient")){
                                            Intent i = new Intent(updatePasswordActivity.this, patientDashboardActivity.class);
                                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                                    Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                            startActivity(i);
                                            FirebaseDatabase.getInstance().getReference().child("Patients").child(userid).child("password").setValue(textNewPassword);
                                        }
                                        else if(type.equals("Therapist")){
                                            Intent i = new Intent(updatePasswordActivity.this, therapistDashboardActivity.class);
                                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                                    Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                            startActivity(i);
                                            FirebaseDatabase.getInstance().getReference().child("Therapists").child(userid).child("password").setValue(textNewPassword);
                                        }
                                        else{
                                            Intent i = new Intent(updatePasswordActivity.this, AdminDashboard.class);
                                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                                    Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                            startActivity(i);
                                        }
                                    }
                                }
                            });
                        } else {
                            newPassword.setError("Password Doesn't match");
                            currPassword.requestFocus();
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "New password doesn't match", Toast.LENGTH_LONG).show();

                        }
                    }
                    else{
                        currPassword.setError("Current Password Incorrect");
                        currPassword.requestFocus();
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Current password incorrect", Toast.LENGTH_LONG).show();
                    }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), firebaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}

