package com.example.gotcha_depressionhelpline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class therapistPreferences extends AppCompatActivity {

    Button addPref,select ,endSelect;
    private int sHour, sMinute;
    TextView startTime,endTime;
    LocalTime time1,time2;
    String changeToken;
    String username;
    Button changePref;
    LinearLayout addTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_therapist_preferences);

        changePref=findViewById(R.id.changepref);
        addTime=findViewById(R.id.addTime);

        SharedPreferences sharedPreferences=getSharedPreferences("Login_Session", Context.MODE_PRIVATE);
        username=sharedPreferences.getString("username","");
        changeToken=sharedPreferences.getString("TherapistPreferences","");
        if(changeToken.equals("done")){
            addPreferences();
            changePref.setVisibility(View.VISIBLE);
            changePref.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changePref.setVisibility(View.GONE);
                    addTime.setVisibility(View.VISIBLE);

                    if(changeToken.equals("done")){
                        Query usernameQuery = FirebaseDatabase.getInstance().getReference("TherapistPreferences")
                                .orderByChild("therapistUsername")
                                .equalTo(username);
                        usernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    String key = ds.getKey();
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("TherapistPreferences")
                                            .child(key)
                                            .removeValue();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                        Query usernameQuery2 = FirebaseDatabase.getInstance().getReference("TherapistAvailability")
                                .orderByChild("therapistUsername")
                                .equalTo(username);
                        usernameQuery2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    String key = ds.getKey();
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("TherapistAvailability")
                                            .child(key)
                                            .removeValue();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();

                            }
                        });
                    }

                }
            });

        }
        else{
            addPreferences();
            addTime.setVisibility(View.VISIBLE);
            }
    }


    public void addPreferences(){

        select =findViewById(R.id.select);
        startTime=findViewById(R.id.startTime);
        endTime=findViewById(R.id.endTime);
        addPref=findViewById(R.id.addPreferences);
        endSelect=findViewById(R.id.endSelect);

        select.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                sHour = c.get(Calendar.HOUR_OF_DAY);
                sMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(therapistPreferences.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                time1=LocalTime.of(hourOfDay,minute);
                                startTime.setText(hourOfDay + ":" + minute);

                            }
                        }, sHour, sMinute, false);
                timePickerDialog.show();

            }
        });
        endSelect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                sHour = c.get(Calendar.HOUR_OF_DAY);
                sMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(therapistPreferences.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                time2=LocalTime.of(hourOfDay,minute);

                                if(time1.isBefore(time2))
                                {   //If start time is before end date
//                                    startTime.plusMinutes(40);
                                    Duration difference = Duration.between(time1, time2);
                                    long hours = difference.toHours();
                                    difference = difference.minusHours(hours);
                                    long minutes = difference.toMinutes();
                                    difference = difference.minusMinutes(minutes);
                                    if( hours>0 ||minutes > 40 ) {
//                                        makeSlots(time1,time2);
                                        endTime.setText(hourOfDay + ":" + minute);
                                        addPref.setVisibility(View.VISIBLE);
                                    }
                                    else {
                                        endTime.setText("");
                                        endTime.setError(" ");
                                        Toast.makeText(getApplicationContext(), "Enter 40 minutes minimum", Toast.LENGTH_LONG).show();
                                    }
                                }
                                else if(time1.equals(time2))
                                {
                                    //If two time are equal
                                    endTime.setText("");
                                    endTime.setError(" ");
                                    Toast.makeText(getApplicationContext(), "EndTime must not Equal to StartTime", Toast.LENGTH_LONG).show();

                                }
                                else
                                { //If start time is after the end date
                                    endTime.setText("");
                                    endTime.setError(" ");
                                    Toast.makeText(getApplicationContext(), "End time must after Start time", Toast.LENGTH_LONG).show();
                                }


                            }
                        }, sHour, sMinute, false);
                timePickerDialog.show();

            }
        });



    }
    public void addPreferences_Clicked(View view) {
        addIntoDB();
        //adding in shared preferences for latter use
            SharedPreferences sharedPreferences= getSharedPreferences("Login_Session",MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("TherapistPreferences","done");
            editor.putString("Token","Therapist");
            editor.apply();
            Intent i = new Intent(therapistPreferences.this, therapistDashboardActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i);
    }
    public void addIntoDB(){


        final HashMap<String, String> pref =
            new HashMap<String, String>();
            pref.put("therapistUsername",username);
            pref.put("startTime", startTime.getText().toString());
            pref.put("endTime",endTime.getText().toString());
            FirebaseDatabase.getInstance().getReference("TherapistPreferences")
                    .child(FirebaseAuth.getInstance().getUid())
                    .setValue(pref).addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "preferences addded", Toast.LENGTH_SHORT).show();
                        FirebaseDatabase.getInstance().getReference("TherapistAvailability")
                                .push()
                                .setValue(pref)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                      if (task.isSuccessful()) {
                                          Toast.makeText(getApplicationContext(), "Availability Added", Toast.LENGTH_LONG).show();
                                      }
                                    }
                                });
                        }
                }
        });
    }
}
