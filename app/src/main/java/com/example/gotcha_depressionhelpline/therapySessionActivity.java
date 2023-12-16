package com.example.gotcha_depressionhelpline;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sinch.android.rtc.calling.Call;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class therapySessionActivity extends BaseActivity {

    String username, starttime, endtime, gender, deplevel,name,date;
    TextView patientNameTv,patientGenderTv,deplevelTv,startTimeTv,stopTimeTv,sessionDate;
    Button makeCall;
    String ontime="No";
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_therapy_session);

        patientNameTv=findViewById(R.id.patientName);
        patientGenderTv=findViewById(R.id.gender);
        deplevelTv=findViewById(R.id.deplevel);
        startTimeTv=findViewById(R.id.startTime);
        stopTimeTv=findViewById(R.id.endTime);
        sessionDate=findViewById(R.id.sessionDate);
        //remainingTime=findViewById(R.id.remainingTime);
        makeCall=findViewById(R.id.makeCall);

        Intent i = getIntent();
        username = i.getStringExtra("patientUsername");
        starttime = i.getStringExtra("startTime");
        endtime = i.getStringExtra("endTime");
        deplevel = i.getStringExtra("depLevel");
        date= i.getStringExtra("sessionDate");

        patientNameTv.setText(username);
        deplevelTv.setText(deplevel);
        startTimeTv.setText(starttime);
        stopTimeTv.setText(endtime);
        sessionDate.setText(date);

        Query usernameQuery = FirebaseDatabase.getInstance().getReference()
                .child("Patients").orderByChild("username").equalTo(username);
        usernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    key = ds.getKey();
                    FirebaseDatabase.getInstance().getReference().child("Patients")
                            .child(key).addValueEventListener(new ValueEventListener(){

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                gender = dataSnapshot.child("gender").getValue(String.class);
                                name = dataSnapshot.child("name").getValue(String.class);
//                                patientNameTv.setText(name);
                                patientGenderTv.setText(gender);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(getApplicationContext(),databaseError.getMessage() , Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:m[m]");

        LocalTime start=  LocalTime.parse(starttime, timeFormatter);
        LocalTime stop=  LocalTime.parse(endtime, timeFormatter);
        LocalTime time = LocalTime.now();
        String t = time.format(DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime current=LocalTime.parse(t,timeFormatter);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String today = df.format(cal.getTime());
        if(current.equals(start) &&  date.equals(today) ){
            ontime="Yes";
        }
        else if((current.isAfter(start) && current.isBefore(stop)) && date.equals(today) ){
            ontime="Yes";
        }

//        if(ontime.equals("Yes")){
            makeCall.setVisibility(View.VISIBLE);
//        }
        makeCall.setEnabled(false);
        makeCall.setOnClickListener(buttonClickListener);

        }
    @Override
    protected void onServiceConnected() {
        makeCall.setEnabled(true);

    }


    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(therapySessionActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    therapySessionActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(therapySessionActivity.this,
                        new String[]{android.Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_PHONE_STATE,
                                Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},
                        1);
            }


            Call call = getSinchServiceInterface().callUser(username);
            String callId = call.getCallId();

            Intent callScreen = new Intent(therapySessionActivity.this, CallScreenActivity.class);
            callScreen.putExtra(SinchService.CALL_ID, callId);
            callScreen.putExtra("RECEIVER_NAME",username);
            callScreen.putExtra("StartTime",starttime);
            callScreen.putExtra("EndTime",endtime);
            callScreen.putExtra("DepLevel",deplevel);
            startActivity(callScreen);

        }
    };
}
