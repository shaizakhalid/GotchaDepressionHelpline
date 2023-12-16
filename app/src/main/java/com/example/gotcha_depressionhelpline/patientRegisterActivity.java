package com.example.gotcha_depressionhelpline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OneSignal;

import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;

import java.util.regex.Pattern;


public class patientRegisterActivity extends AppCompatActivity {

    String title="patient";
    EditText name;
    EditText cnic;
    EditText editTextemail;
    EditText username;
    EditText password;
    FirebaseDatabase database;
    FirebaseUser firebaseUser;
    DatabaseReference referenceUser,referencePatient;
    private FirebaseAuth auth;
    RadioGroup genderRadioGroup, previousDiagonosisRadioGroup;
    Button register;
    EditText editTextrepassword;
    String gender="empty string";
    String previousDiagnosis="empty string";
    String experience="null";
    String cnicStr;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_register);
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        auth= FirebaseAuth.getInstance();
        firebaseUser=auth.getCurrentUser();
        database= FirebaseDatabase.getInstance();

        getSupportActionBar().setTitle("Register As a Patient");


        name=findViewById(R.id.editTextname);
        genderRadioGroup =  findViewById(R.id.radioGroupGender);
        register=findViewById(R.id.registerAsPatient);
        editTextrepassword=findViewById(R.id.editTextRePassword);
        progressBar=findViewById(R.id.simpleProgressBar);


        genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.male:
                        gender = "male";
                        break;
                    case R.id.female:
                        gender = "female";
                        break;
                    default:
                        gender ="empty string";
                        break;
                }
            }
        });
        previousDiagonosisRadioGroup =  findViewById(R.id.radioGroupPreviousDiagnosis);
        previousDiagonosisRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.yes:
                        previousDiagnosis="Yes";
                        break;
                    case R.id.no:
                        previousDiagnosis="No";
                        break;
                    default:
                        previousDiagnosis ="empty string";
                        break;
                }
            }
        });
        cnic=findViewById(R.id.editTextCNIC);
        editTextemail=findViewById(R.id.editTextGmail);
        username=findViewById(R.id.editTextUsername);
        password=findViewById(R.id.editTextPassword);



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                cnicStr=cnic.getText().toString();
                UIUtil.hideKeyboard(patientRegisterActivity.this);
                final String textusername = username.getText().toString().trim();
                final String textRePassword=editTextrepassword.getText().toString().trim();
                final Long textCnic;
                final String textPassword = password.getText().toString().trim();
                final String textemail = editTextemail.getText().toString().trim();
                final String textname = name.getText().toString().trim();
                final String texttitle = title.trim();
                final String textExperience = experience.trim();
                final String textDiagonsis;
                final String stringtextCnic;
                final String textgender;
                if (textname.isEmpty()) {
                    progressBar.setVisibility(View.GONE);
                    name.setError("Enter Name!");
                    name.requestFocus();
                    return; // stopping the functions excecution
                }
                if (textname.contains("0")||textname.contains("1")||textname.contains("2")||textname.contains("3")
                        ||textname.contains("4") ||textname.contains("5")||textname.contains("6")
                        ||textname.contains("7") ||textname.contains("8") ||textname.contains("9")) {
                    progressBar.setVisibility(View.GONE);
                    name.setError("Name don't contains Numbers!");
                    name.requestFocus();
                    return;
                }
                if(gender.equals("empty string")){
                    progressBar.setVisibility(View.GONE);
                    genderRadioGroup.requestFocus();
                    Toast.makeText(getApplicationContext(), "Select gender!", Toast.LENGTH_LONG).show();
                    return;
                }else{
                    textgender = gender.trim();
                }
                if (cnicStr.isEmpty()) {
                    progressBar.setVisibility(View.GONE);
                    cnic.setError("Enter CNIC");
                    cnic.requestFocus();
                    return; // stopping the functions excecution
                }
                else if(!(cnicStr.length() == 13) ){
                    progressBar.setVisibility(View.GONE);
                    cnic.setError("Enter CNIC of 13 digits without '-' ");
                    cnic.requestFocus();
                    return;
                }
                else {
                    textCnic = Long.parseLong(cnic.getText().toString());
                    stringtextCnic = textCnic.toString().trim();
                }
                if(previousDiagnosis.equals("empty string")){
                    progressBar.setVisibility(View.GONE);
                    previousDiagonosisRadioGroup.requestFocus();
                    Toast.makeText(getApplicationContext(), "Select Previous Diagonosis!", Toast.LENGTH_LONG).show();
                    return;
                }else{
                    textDiagonsis = previousDiagnosis.trim();

                }
                if (textusername.isEmpty()) {
                    progressBar.setVisibility(View.GONE);
                    username.setError("Enter username");
                    username.requestFocus();
                    return; // stopping the functions excecution
                }

                if (textusername.contains(" ")) {
                    progressBar.setVisibility(View.GONE);
                    username.setError("Username badly formatted");
                    username.requestFocus();
                    return; // stopping the functions excecution
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(textemail).matches()) {
                    progressBar.setVisibility(View.GONE);
                    editTextemail.setError("Enter valid email");
                    editTextemail.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(textPassword) || textPassword.length() < 8) {
                    progressBar.setVisibility(View.GONE);
                    password.setError("Password Error");
                    password.requestFocus();
                    Toast.makeText(getApplicationContext(), "Password must be 8 character minimun", Toast.LENGTH_LONG).show();
                    return; // stopping the functions excecution
                }
                if (textRePassword.isEmpty()) {
                    progressBar.setVisibility(View.GONE);
                    editTextrepassword.setError("Re Enter Password!");
                    editTextrepassword.requestFocus();
                    return; // stopping the functions excecution
                }
                if(textPassword.equals(textRePassword)) {
                    Query usernameQuery = FirebaseDatabase.getInstance().getReference().child("Users")
                            .orderByChild("username").equalTo(textusername);
                    usernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getChildrenCount() > 0) {
                                progressBar.setVisibility(View.GONE);
                                username.setError("Username Already Exists!");
                                username.requestFocus();
                            }
                            else {
                                auth.createUserWithEmailAndPassword(textemail, textPassword)
                                        .addOnCompleteListener(patientRegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull final Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    final String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                    referenceUser = FirebaseDatabase.getInstance().getReference("Users").child(userid);
                                                    //now add model for the user
                                                    //need to create user class also
                                                    //to Insert the data need to create FirebaseReference so create DatabaseReference
                                                    final User user = new User(textusername, textemail, textPassword,
                                                            userid, texttitle, textname, textgender,
                                                            textDiagonsis, textExperience, stringtextCnic);
                                                    referenceUser.setValue(user)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        referencePatient = FirebaseDatabase.getInstance().getReference("Patients").child(userid);
                                                                        referencePatient.setValue(user)
                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                        OneSignal.sendTag("User_ID", textusername);
                                                                                        progressBar.setVisibility(View.GONE);
                                                                                        SharedPreferences sharedPreferences = getSharedPreferences("Login_Session", MODE_PRIVATE);
                                                                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                                                                        editor.putString("Token", "Patient");
                                                                                        editor.putString("username",textusername);
                                                                                        editor.apply();
                                                                                        Toast.makeText(getApplicationContext(), "Patient Sucessfully Registered", Toast.LENGTH_LONG).show();
                                                                                        Intent i = new Intent(patientRegisterActivity.this, patientDashboardActivity.class);
                                                                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                                                                | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                                                        startActivity(i);

                                                                                    }
                                                                                });
                                                                    } else {
                                                                        progressBar.setVisibility(View.GONE);
                                                                        Toast.makeText(getApplicationContext(), "Error: " + task.getException(), Toast.LENGTH_LONG).show();
                                                                    }
                                                                }
                                                            });
                                                } else {
                                                    progressBar.setVisibility(View.GONE);
                                                    Toast.makeText(getApplicationContext(), "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Error: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    });
                }
                else{
                    progressBar.setVisibility(View.GONE);
                    password.setError("Password doesn't match!");
                    password.requestFocus();
                    return;
                }
            }
        });
}
public void call_LoginPage(View aview){
        Intent i=new Intent(this, loginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}