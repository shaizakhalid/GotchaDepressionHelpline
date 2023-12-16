package com.example.gotcha_depressionhelpline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class BookingForClinicalTreatment extends AppCompatActivity {
    private ListView listView;
    private CustomListAdapter mAdapter;
    String username,startTime,endTime;
    String usernameKey,endTimeKey,startTimeKey;
    HashMap<String, String> session;
    ArrayList<Preferences> preferencesList = new ArrayList<Preferences>();
    String sessiondate;
    String key;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_preferences);


        progressBar=findViewById(R.id.progress_bar);
        listView = findViewById(R.id.listview);
        mAdapter = new CustomListAdapter(this,preferencesList);
        listView.setAdapter(mAdapter);


        progressBar.setVisibility(View.VISIBLE);
        FirebaseDatabase.getInstance().getReference("TherapistAvailability")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds: dataSnapshot.getChildren()) {
                            progressBar.setVisibility(View.GONE);
                            key=ds.getKey();
                            username = ds.child("therapistUsername").getValue(String.class);
                            startTime = ds.child("startTime").getValue(String.class);
                            endTime = ds.child("endTime").getValue(String.class);

                            preferencesList.add(new Preferences(username, startTime, endTime));
                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "Error: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();

                    }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                final Dialog dialog=new Dialog(BookingForClinicalTreatment.this);
                dialog.setTitle("Session Confirmation");
                dialog.setContentView(R.layout.confirmation_box);
                final TextView dialoagUsername=dialog.findViewById(R.id.dialogUsername);
                final TextView startTime=dialog.findViewById(R.id.dialogStartTime);
                final TextView endTime=dialog.findViewById(R.id.dialogEndTime);
                TextView tv = view.findViewById(R.id.usernameTextView);
                TextView tv2=view.findViewById(R.id.startTime);
                TextView tv3=view.findViewById(R.id.endTime);

                usernameKey=tv.getText().toString();
                startTimeKey=tv2.getText().toString();
                endTimeKey=tv3.getText().toString();

                dialoagUsername.setText(tv.getText().toString());
                startTime.setText(tv2.getText().toString());
                endTime.setText(tv3.getText().toString());

                Button bt=dialog.findViewById(R.id.cancel);
                bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                Button bookSession=dialog.findViewById(R.id.book_Session);
                bookSession.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        SharedPreferences sharedPreferences = getSharedPreferences("Login_Session", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("AnotherSession", "No");
                        editor.apply();
                        book_session();
                        removeRecommendation();
                    }
                });
                dialog.show();
            }
        });
    }

    public void removeRecommendation(){
        SharedPreferences sharedPreferences = getSharedPreferences("Login_Session", Context.MODE_PRIVATE);
        String patientUsername = sharedPreferences.getString("username", "");
        Query usernameQuery = FirebaseDatabase.getInstance().getReference()
                .child("TherapistRecommendations").orderByChild("patientUsername").equalTo(patientUsername);
        usernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    key = ds.getKey();
                    FirebaseDatabase.getInstance().getReference()
                            .child("TherapistRecommendations")
                            .child(key)
                            .removeValue();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }
    public void book_session(){


        final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:m[m]");
        final DateTimeFormatter timeFormatter2 = DateTimeFormatter.ofPattern("HH:mm");

        final LocalTime startTime=  LocalTime.parse(startTimeKey, timeFormatter);
        final LocalTime stopTime=  LocalTime.parse(endTimeKey, timeFormatter);
        int interval=40;
        LocalTime time= startTime;

        if(time.isBefore(stopTime)) {
            time=time.plusMinutes(interval);
           final LocalTime finaltime=time;
            HashMap<String, String> pref =
                    new HashMap<String, String>();
            pref.put("username",username);

            Calendar c = Calendar.getInstance();
            Date currentDate = c.getTime();
            SimpleDateFormat dateOutput = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat timeOutput = new SimpleDateFormat("HH:mm");
            String formattedTime= timeOutput.format(currentDate);
            String formattedDate = dateOutput.format(currentDate);
            LocalTime timeNow=  LocalTime.parse(formattedTime, timeFormatter);
            // if session strt time is before current time then add 1day in date

            if(startTime.isBefore(timeNow)){

                c.add(Calendar.DATE, 1);  // number of days to add
                sessiondate = dateOutput.format(c.getTime());
            }
            else{
                sessiondate=formattedDate;
            }

//            FirebaseDatabase.getInstance().getReference("TherapistAvailability")
//                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                    .setValue(pref).addOnCompleteListener(new OnCompleteListener<Void>()
//            {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    if (task.isSuccessful()) {
                        SharedPreferences sharedPreferences=getSharedPreferences("Login_Session", Context.MODE_PRIVATE);
                        String patientUsername=sharedPreferences.getString("username","");
                        SharedPreferences depsharedPreferences=getSharedPreferences("Depression_Result", Context.MODE_PRIVATE);
                        String severity=depsharedPreferences.getString("DepressionSeverityLevel","-1");
                        session = new HashMap<String, String>();
                        session.put("therapistUserame", usernameKey);
                        session.put("patientUsername", patientUsername);
                        session.put("SessionDate", sessiondate);
                        session.put("startTime", startTime.format(timeFormatter2));
                        session.put("endTime", finaltime.format(timeFormatter2));
                        session.put("deplevel",severity);
                        FirebaseDatabase.getInstance().getReference("Sessions")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(session).addOnCompleteListener(new OnCompleteListener<Void>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Duration difference = Duration.between(finaltime, stopTime);
                                    long hours = difference.toHours();
                                    difference = difference.minusHours(hours);
                                    long minutes = difference.toMinutes();
                                    difference = difference.minusMinutes(minutes);
                                    if((hours > 0) || (minutes >= 40)) {

                                        FirebaseDatabase.getInstance().getReference("TherapistAvailability")
                                                .child(key).child("startTime")
                                                .setValue(finaltime.format(timeFormatter2));
                                        Toast.makeText(getApplicationContext(), "Session Booked Successfully", Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(BookingForClinicalTreatment.this, patientDashboardActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        startActivity(i);


                                    }
                                    else{
                                        FirebaseDatabase.getInstance().getReference("TherapistAvailability")
                                                .child(key).removeValue();
                                        Toast.makeText(getApplicationContext(), "Session Booked Successfully", Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(BookingForClinicalTreatment.this, patientDashboardActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        startActivity(i);
                                    }

                                }
                            }
                        });
//                    }
//                }
//            });
            }

    }


}
