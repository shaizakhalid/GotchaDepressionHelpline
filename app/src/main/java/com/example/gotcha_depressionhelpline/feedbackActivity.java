package com.example.gotcha_depressionhelpline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class feedbackActivity extends AppCompatActivity {

    EditText name, statment;
    RadioGroup radioGroupCatagory, radioGroupabout;
    String catagory;
    String about,complainAgainst;
    LinearLayout fromTherapist, fromPatient, listofperson;
    DatabaseReference dbroot;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
         userid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        getSupportActionBar().setTitle("FeedBack and Complaints");

        name = findViewById(R.id.name);
        statment = findViewById(R.id.statement);
        fromTherapist = findViewById(R.id.fromTherapist);
        listofperson = findViewById(R.id.listll);
        fromPatient = findViewById(R.id.fromPatient);

        radioGroupCatagory = findViewById(R.id.feedbackComplain);
        radioGroupCatagory.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.feedback:
                        catagory = "feedback";
                        break;
                    case R.id.complain:
                        catagory = "complain";
                        SharedPreferences sharedPreferences = getSharedPreferences("Login_Session", Context.MODE_PRIVATE);
                        String token = sharedPreferences.getString("Token", "");
                        if (token.equals("Patient")) {
                            fromPatient.setVisibility(View.VISIBLE);
                            radioGroupabout = findViewById(R.id.about);
                            radioGroupabout.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                public void onCheckedChanged(RadioGroup group, int checkedId) {
                                    switch (checkedId) {
                                        case R.id.therapist:
                                            about = "Therapist";
                                            listofperson.setVisibility(View.VISIBLE);
                                            final Spinner areaSpinner = findViewById(R.id.spinner);
                                            dbroot = FirebaseDatabase.getInstance().getReference("Therapists");
                                            dbroot.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    final List<String> areas = new ArrayList();
                                                    for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                                                        String areaName = areaSnapshot.child("username").getValue(String.class);
                                                        areas.add(areaName);
                                                    }
                                                    ArrayAdapter<String> areasAdapter = new ArrayAdapter(feedbackActivity.this, android.R.layout.simple_spinner_item, areas);
                                                    areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                    areaSpinner.setAdapter(areasAdapter);
                                                    areaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                        @Override
                                                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                                                            complainAgainst=areaSpinner.getItemAtPosition(position).toString();
                                                        }

                                                        @Override
                                                        public void onNothingSelected(AdapterView<?> parentView) {

                                                        }

                                                    });
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                            break;
                                        case R.id.service:
                                            about = "Services";
                                            break;
                                        default:
                                            throw new IllegalStateException("Unexpected value: " + checkedId);
                                    }
                                }
                            });
                        }
                        else {
                            fromTherapist.setVisibility(View.VISIBLE);
                            radioGroupabout = findViewById(R.id.tAbout);
                            radioGroupabout.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                public void onCheckedChanged(RadioGroup group, int checkedId) {
                                    switch (checkedId) {
                                        case R.id.patient:
                                            about = "Patient";
                                            listofperson.setVisibility(View.VISIBLE);
                                            final Spinner areaSpinner = findViewById(R.id.spinner);
                                            dbroot = FirebaseDatabase.getInstance().getReference("Patients");
                                            dbroot.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    final List<String> areas = new ArrayList();
                                                    for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                                                        String areaName = areaSnapshot.child("username").getValue(String.class);
                                                        areas.add(areaName);
                                                    }
                                                    ArrayAdapter<String> areasAdapter = new ArrayAdapter(feedbackActivity.this, android.R.layout.simple_spinner_item, areas);
                                                    areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                    areaSpinner.setAdapter(areasAdapter);
                                                    areaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                        @Override
                                                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                                                            complainAgainst=areaSpinner.getItemAtPosition(position).toString();
                                                        }
                                                        @Override
                                                        public void onNothingSelected(AdapterView<?> parentView) {
                                                            Toast.makeText(getApplicationContext(), "Please select option from Complain against", Toast.LENGTH_LONG).show();

                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                    Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();

                                                }
                                            });
                                            break;
                                        case R.id.tService:
                                            about = "Services";
                                            break;
                                    }
                                }
                            });
                        }
                        break;
                }
            }
        });

    }

    public void submitOnclick(View view) {
        //if(checkUsernameValidity(name.getText().toString())){
            if(catagory.equals("complain"))
            {
                if(about.equals("Services"))
                {
                    HashMap<String, String> complain =
                            new HashMap<String, String>();
                    complain.put("complainMakerName", name.getText().toString());
                    complain.put("complainAbout",about);
                    complain.put("statement", statment.getText().toString());
                    FirebaseDatabase.getInstance().getReference("FeedbackComplains").child("Complains").child(userid)
                            .setValue(complain).addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(getApplicationContext(), "Complain Successfully Submitted", Toast.LENGTH_LONG).show();
                                fromTherapist.setVisibility(View.GONE);
                                fromPatient.setVisibility(View.GONE);
                                listofperson.setVisibility(View.GONE);
                                name.setText("");
                                statment.setText("");
                                finish();
                            }

                        }
                    });


                }
                else
                {
                    HashMap<String, String> complain =
                            new HashMap<String, String>();
                    complain.put("complainMakerName", name.getText().toString());
                    complain.put("complainAgainstUsername", complainAgainst);
                    complain.put("complainAbout",about);
                    complain.put("statement", statment.getText().toString());
                    FirebaseDatabase.getInstance().getReference("FeedbackComplains").child("Complains").child(userid)
                            .setValue(complain).addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(getApplicationContext(), "Complain Successfully Submitted", Toast.LENGTH_LONG).show();
                                fromTherapist.setVisibility(View.GONE);
                                fromPatient.setVisibility(View.GONE);
                                listofperson.setVisibility(View.GONE);
                                name.setText("");
                                statment.setText("");
                                finish();
                            }

                        }
                    });

                }

            }
            else if(catagory.equals("feedback"))
            {
                HashMap<String, String> hm =
                        new HashMap<String, String>();
                hm.put("feedbackGiverName", name.getText().toString());
                hm.put("statement", statment.getText().toString());
                FirebaseDatabase.getInstance().getReference("FeedbackComplains").child("Feedbacks").child(userid)
                        .setValue(hm).addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(), "Feedback Successfully Submitted", Toast.LENGTH_LONG).show();
                            name.setText("");
                            statment.setText("");
                            finish();
                        }

                    }
                });

            }
            else
            {
                Toast.makeText(getApplicationContext(), "Please select any catagory", Toast.LENGTH_LONG).show();

            }

    }
}