package com.example.gotcha_depressionhelpline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;

public class loginActivity extends AppCompatActivity {
    EditText editTextUsername, editTextPassword;
    Button loginButton;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    private Firebase titleRef,usernameRef;
    String username,titleKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setTitle("Login");
        Firebase.setAndroidContext(this);

        editTextUsername=findViewById(R.id.editTextUsername);
        editTextPassword=findViewById(R.id.editTextPassword);
        progressBar=findViewById(R.id.simpleProgressBar);
        fAuth=FirebaseAuth.getInstance();
        loginButton=findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtil.hideKeyboard(loginActivity.this);//to hide keyboard
                String email=editTextUsername.getText().toString().trim();
                String password=editTextPassword.getText().toString().trim();

                if(email.isEmpty()){
                    editTextUsername.setError("Please Enter valid email");
                    editTextUsername.requestFocus();
                    return; // stopping the functions excecution
                }
                if(TextUtils.isEmpty(password) || password.length()<8){
                    editTextPassword.setError("Please Enter valid email");
                    editTextPassword.requestFocus();
                    return; // stopping the functions excecution
                }
                progressBar.setVisibility(View.VISIBLE);

                fAuth.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                               if(task.isSuccessful()){
                                   DatabaseReference feedbackRef1 = FirebaseDatabase.getInstance()
                                           .getReference("Users");
                                   final String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                   Query usernameQuery1 = feedbackRef1.orderByChild("userid").equalTo(userid);
                                   usernameQuery1.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                                       @Override
                                       public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                                           for (com.google.firebase.database.DataSnapshot ds: dataSnapshot.getChildren()) {
                                               titleKey = ds.child("title").getValue(String.class);
//                                               if(titleKey.equals("disabled")){
//                                                   //check if user is blocked
//                                                   Intent intent=new Intent(loginActivity.this,blockActivity.class);
//                                                   startActivity(intent);
//                                               }
//                                               else{
                                               callLogin();
//                                           }
                                           }
                                       }
                                       @Override
                                       public void onCancelled(@NonNull DatabaseError databaseError) {
                                           progressBar.setVisibility(View.GONE);
                                           Toast.makeText(loginActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                       }
                                   });

                               }
                               else{
                                   progressBar.setVisibility(View.GONE);
                                   Toast.makeText(getApplicationContext(),"Error: "+task.getException().getMessage(),Toast.LENGTH_LONG).show();

                               }
                            }
                        });
            }
        });
    }

public void callLogin(){
    SharedPreferences sharedPreferences= getSharedPreferences("Login_Session",MODE_PRIVATE);
    final SharedPreferences.Editor editor=sharedPreferences.edit();

    String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    usernameRef= new Firebase("https://gotcha-depression-helpline.firebaseio.com/Users/"
            +userid+"/username");
    usernameRef.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            username=dataSnapshot.getValue(String.class);
            editor.putString("username",username);
            editor.apply();
        }
        @Override
        public void onCancelled(FirebaseError firebaseError) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(loginActivity.this,"Error: "+firebaseError,Toast.LENGTH_LONG).show();
        }
    });
    titleRef= new Firebase("https://gotcha-depression-helpline.firebaseio.com/Users/"
            +userid+"/title");
    titleRef.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            String title=dataSnapshot.getValue(String.class);
            if(title.equals("patient")){
                editor.putString("Token","Patient");
                editor.apply();
                Query usernameQuery = FirebaseDatabase.getInstance().getReference().child("PatientHistory")
                        .orderByChild("username").equalTo(username);
                usernameQuery.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() > 0) {
                            progressBar.setVisibility(View.GONE);
                            SharedPreferences sharedPreferences = getSharedPreferences("Login_Session", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("patientType", "Old");
                            editor.apply();
                            Intent i = new Intent(loginActivity.this, patientDashboardActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(i);
                        }
                        else{
                            progressBar.setVisibility(View.GONE);
                            Intent i=new Intent(loginActivity.this,patientDashboardActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(i);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(loginActivity.this,"Error: "+databaseError,Toast.LENGTH_LONG).show();

                    }

                });

            }
            if(title.equals("therapist")){
                SharedPreferences sharedPreferences1= getSharedPreferences("Login_Session",MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences1.edit();
                editor.putString("TherapistPreferences","done");
                editor.apply();
                editor.putString("Token","Therapist");
                editor.apply();
                Toast.makeText(getApplicationContext(),"Therapist login successfull",Toast.LENGTH_LONG).show();
                Intent i=new Intent(loginActivity.this,therapistDashboardActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }
            if(title.equals("invalidTherapist")){
                editor.putString("Token","invalidTherapist");
                editor.apply();
                Toast.makeText(getApplicationContext(),"Therapist login successfull",Toast.LENGTH_LONG).show();
                Intent i=new Intent(loginActivity.this,pendingTherapistRequestActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }
            if(title.equals("admin")) {
                editor.putString("Token","Admin");
                editor.apply();
                Toast.makeText(getApplicationContext(),"Admin login successfull",Toast.LENGTH_LONG).show();
                Intent i=new Intent(loginActivity.this,AdminDashboard.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }
            if(title.equals("disabled")) {
                editor.putString("Token","disabled");
                editor.apply();
                Toast.makeText(getApplicationContext(),"Account Blocked",Toast.LENGTH_LONG).show();
                Intent i=new Intent(loginActivity.this,blockActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }


        }
        @Override
        public void onCancelled(FirebaseError firebaseError) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(loginActivity.this,"Error: "+firebaseError,Toast.LENGTH_LONG).show();
        }
    });

}
    public void call_RegisterPage(View aview){
        Intent i =new Intent(this, frontPage.class);
        i.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

}
