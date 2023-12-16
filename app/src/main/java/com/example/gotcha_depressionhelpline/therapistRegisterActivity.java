package com.example.gotcha_depressionhelpline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
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

import java.util.HashMap;

public class therapistRegisterActivity extends AppCompatActivity {

    private EditText username;
    private EditText editTextName;
    private EditText editTextpassword;
    private EditText editTextrepassword;
    private DatabaseReference referenceUser;
    private FirebaseAuth auth;
    String title="invalidTherapist";
    EditText editTextCnic;
    EditText editTextemail;
    String editTextgender="empty string";
    String previousDiagnosiss="null";
    String cnicStr;
    String experience="empty string";
    RadioGroup genderRadioGroup,exprienceRadioGroup;
    Button register;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_therapist_register);
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        auth= FirebaseAuth.getInstance();
        getSupportActionBar().setTitle("Register As a Therapist");

        editTextName=findViewById(R.id.editTextName);
        username=findViewById(R.id.editTextUsername);
        genderRadioGroup=findViewById(R.id.radioGroupGender);
        genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.male:
                        editTextgender = "male";
                        break;
                    case R.id.female:
                        editTextgender = "female";
                        break;
                    default:
                        editTextgender ="empty string";
                        break;
                }
            }
        });
        editTextCnic=findViewById(R.id.editTextCNIC);
        exprienceRadioGroup=findViewById(R.id.previousExperience);
        exprienceRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.yes:
                        experience="Yes";
                        break;
                    case R.id.no:
                        experience="No";
                        break;
                    default:
                        experience ="empty string";
                        break;
                }
            }
        });
        editTextpassword=findViewById(R.id.editTextPassword);
        editTextemail =  findViewById(R.id.editTextGmail);
        editTextrepassword=findViewById(R.id.editTextRePassword);
        register=findViewById(R.id.buttonRegister);
        progressBar=findViewById(R.id.simpleProgressBar);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cnicStr=editTextCnic.getText().toString();
                UIUtil.hideKeyboard(therapistRegisterActivity.this);
                progressBar.setVisibility(View.VISIBLE);
                final String textusername = username.getText().toString().trim();
                final Long textCnic;
                final String textPassword=editTextpassword.getText().toString().trim();
                final String textRePassword=editTextrepassword.getText().toString().trim();
                final String textemail=editTextemail.getText().toString().trim();
                final String textname=editTextName.getText().toString().trim();
                final String texttitle=title.trim();
                final String textExperience;
                final String textDiagonsis=previousDiagnosiss.trim();
                final String stringtextCnic;
                final String textgender;

                if(textname.isEmpty()){
                    progressBar.setVisibility(View.GONE);
                    editTextName.setError("Enter valid email!");
                    editTextName.requestFocus();
                    return; // stopping the functions excecution
                }
                if (textname.contains("0")||textname.contains("1")||textname.contains("2")||textname.contains("3")
                        ||textname.contains("4") ||textname.contains("5")||textname.contains("6")
                        ||textname.contains("7") ||textname.contains("8") ||textname.contains("9")) {
                    progressBar.setVisibility(View.GONE);
                    editTextName.setError("Name don't contains Numbers!");
                    editTextName.requestFocus();
                    return;
                }
                if(editTextgender.equals("empty string")){
                    progressBar.setVisibility(View.GONE);
                    genderRadioGroup.requestFocus();
                    Toast.makeText(getApplicationContext(), "Select gender!", Toast.LENGTH_LONG).show();
                    return;
                }else{
                    textgender = editTextgender.trim();
                }
                if (cnicStr.isEmpty()) {
                    progressBar.setVisibility(View.GONE);
                    editTextCnic.setError("Enter CNIC");
                    editTextCnic.requestFocus();
                    return; // stopping the functions excecution
                }else if(!(cnicStr.length() == 13) ){
                    progressBar.setVisibility(View.GONE);
                    editTextCnic.setError("Enter CNIC of 13 digits without '-' ");
                    editTextCnic.requestFocus();
                    return;
                }
                else{
                    textCnic = Long.parseLong(editTextCnic.getText().toString());
                    stringtextCnic = textCnic.toString().trim();
                }

                if(experience.equals("empty string")){
                    progressBar.setVisibility(View.GONE);
                    exprienceRadioGroup.requestFocus();
                    Toast.makeText(getApplicationContext(), "Select Previous Experience!", Toast.LENGTH_LONG).show();
                    return;
                }else{
                    textExperience = experience.trim();
                }

                if (textusername.isEmpty()) {
                    progressBar.setVisibility(View.GONE);
                    username.setError("Enter username");
                    username.requestFocus();
                    return; // stopping the functions excecution
                }

                if (textusername.contains(" ")) {
                    progressBar.setVisibility(View.GONE);
                    username.setError("Username badly formatted!");
                    username.requestFocus();
                    return; // stopping the functions excecution
                }

                if(!Patterns.EMAIL_ADDRESS.matcher(textemail).matches()){
                    progressBar.setVisibility(View.GONE);
                    editTextemail.setError("Enter valid email!");
                    editTextemail.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(textPassword) || textPassword.length()<8){
                    progressBar.setVisibility(View.GONE);
                    editTextpassword.setError("Enter valid email!");
                    editTextpassword.requestFocus();
                    return;
                }
                if (textRePassword.isEmpty()) {
                    progressBar.setVisibility(View.GONE);
                    editTextrepassword.setError("Re Enter Password!");
                    editTextrepassword.requestFocus();
                    return; // stopping the functions excecution
                }
                if(textPassword.equals(textRePassword)){
                    Query usernameQuery = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("username").equalTo(textusername);
                    usernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getChildrenCount() > 0) {
                                progressBar.setVisibility(View.GONE);
                                username.setError("Username Already Exists!");
                                username.requestFocus();
                            } else {
                                auth.createUserWithEmailAndPassword(textemail, textPassword)
                                        .addOnCompleteListener(therapistRegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    progressBar.setVisibility(View.GONE);
                                                    String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                    referenceUser = FirebaseDatabase.getInstance().getReference("Users").child(userid);
                                                    User pending = new User(textusername, textemail, textPassword, userid, texttitle, textname, textgender,
                                                            textDiagonsis, textExperience, stringtextCnic);
                                                    referenceUser.setValue(pending)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        SharedPreferences sharedPreferences= getSharedPreferences("Login_Session",MODE_PRIVATE);
                                                                        final SharedPreferences.Editor editor=sharedPreferences.edit();
                                                                        Toast.makeText(getApplicationContext(), "Therapist account created", Toast.LENGTH_LONG).show();
                                                                        editor.putString("Token", "invalidTherapist");
                                                                        editor.putString("username",textusername);
                                                                        editor.putString("Request","requesting");
                                                                        editor.apply();
                                                                        OneSignal.sendTag("User_ID", textusername);

                                                                        Intent intent = new Intent(therapistRegisterActivity.this, therapistRequestActivity.class);
                                                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                                                | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                                        startActivity(intent);
                                                                    } else {
                                                                        progressBar.setVisibility(View.GONE);
                                                                        Toast.makeText(getApplicationContext(), "Error" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                                    }
                                                                }
                                                            });
                                                } else {
                                                    progressBar.setVisibility(View.GONE);
                                                    Toast.makeText(getApplicationContext(), "Error" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
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
                    editTextpassword.setError("Password doesn't match!");
                    editTextpassword.requestFocus();
                    return;
                }
            }
        });
    }
    public void call_LoginPage(View aView){
        Intent i=new Intent(this, loginActivity.class);
        i.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}