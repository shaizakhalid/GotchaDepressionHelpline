package com.example.gotcha_depressionhelpline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class sessionDetailsActivity extends AppCompatActivity {

    TextView therapistName,sessionTime,sessionDate,noSession;
    String sessionTimeKey;
    LinearLayout bookSession;
    ProgressBar progressBar;
    LinearLayout anotherSession;
    String username,noBookedSession="NO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_details);

        therapistName=findViewById(R.id.therapistName);
        sessionTime=findViewById(R.id.sessionTime);
        sessionDate=findViewById(R.id.sessionDate);
        noSession=findViewById(R.id.noSession);
        bookSession=findViewById(R.id.BookedSession);
        progressBar=findViewById(R.id.simpleProgressBar);
        anotherSession=findViewById(R.id.anotherSession);

        progressBar.setVisibility(View.VISIBLE);
        SharedPreferences sharedPreferences = getSharedPreferences("Login_Session", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");

        Query usernameQuery = FirebaseDatabase.getInstance().getReference().child("Sessions")
                    .orderByChild("patientUsername").equalTo(username);
            usernameQuery.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                @Override
                public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getChildrenCount()>0){
                        noBookedSession="YES";
                        progressBar.setVisibility(View.GONE);
                        noSession.setVisibility(View.GONE);
                        bookSession.setVisibility(View.VISIBLE);
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String key = ds.getKey();
                            FirebaseDatabase.getInstance().getReference().child("Sessions")
                                    .child(key).addValueEventListener(new ValueEventListener(){
                                @SuppressLint("ResourceAsColor")
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    sessionTimeKey=dataSnapshot.child("startTime").getValue(String.class);
                                    sessionTime.setText(sessionTimeKey);
                                    therapistName.setText(dataSnapshot.child("therapistUserame").getValue(String.class));
                                    sessionDate.setText(dataSnapshot.child("SessionDate").getValue(String.class));

                                    SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                                    try {
                                        Date sessionTime = format.parse(sessionTimeKey);
                                        Calendar c = Calendar.getInstance();
                                        Date currentTimebefore = c.getTime();
                                        String currenttimeStr = format.format(currentTimebefore);
                                        Date currentTime = format.parse(currenttimeStr);

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(getApplicationContext(),databaseError.getMessage() , Toast.LENGTH_LONG).show();

                                }
                            });
                        }
                    }
                    else{
                        if(noBookedSession.equals("NO")){

                            progressBar.setVisibility(View.GONE);
                            noSession.setVisibility(View.VISIBLE);

                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(),databaseError.getMessage() , Toast.LENGTH_LONG).show();
                }
            });

        Query usernameQuery1 = FirebaseDatabase.getInstance().getReference()
                .child("TherapistRecommendations")
                .orderByChild("patientUsername").equalTo(username);
        usernameQuery1.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() > 0) {
                    noBookedSession="YES";
                    progressBar.setVisibility(View.GONE);
                    noSession.setVisibility(View.GONE);
                    bookSession.setVisibility(View.GONE);

                    anotherSession.setVisibility(View.VISIBLE);

                }
                else{
                    if(noBookedSession.equals("NO")){

                        progressBar.setVisibility(View.GONE);
                        noSession.setVisibility(View.VISIBLE);

                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.getMessage() , Toast.LENGTH_LONG).show();

            }
        });

        }




    public void ok_clicked(View view) {
        finish();

    }

    public void bookSessionClicked(View view) {
        Intent i=new Intent(this, BookingForClinicalTreatment.class);
        startActivity(i);
    }
}
