package com.example.gotcha_depressionhelpline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;

public class reportWrittingActivity extends AppCompatActivity {

    String patientName,patientUsername;
    TextView name,sdate,statement;
    String key,start,end;
    String deplevel;
    ProgressBar progressBar;
    String prefStart,prefEnd;
    String therapistUsername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_writting);

        name=findViewById(R.id.name);
        sdate=findViewById(R.id.sdate);
        statement=findViewById(R.id.statement);
        progressBar=findViewById(R.id.simpleProgressBar);

        Intent i=getIntent();
        patientUsername=i.getStringExtra("patientUsername");
        start =i.getStringExtra("startTime");
        end=i.getStringExtra("endTime");
        deplevel=i.getStringExtra("deplevel");

        name.setText(patientUsername);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date = LocalDate.now();
        String t = date.format(timeFormatter);
        LocalDate currentDate=LocalDate.parse(t,timeFormatter);
        sdate.setText(currentDate.format(timeFormatter));
    }

    public void reportSubmitOnclick(View view) {
        String textStatement= statement.getText().toString();
        if (textStatement.isEmpty()) {
            progressBar.setVisibility(View.GONE);
            statement.setError("Enter Report Statement");
            statement.requestFocus();
            return; // stopping the functions excecution
        }

        SharedPreferences sharedPreferences=getSharedPreferences("Login_Session", Context.MODE_PRIVATE);
        String therapistUsername=sharedPreferences.getString("username","-1");
        HashMap<String, String> report =
                new HashMap<String, String>();
        report.put("therapistUsername",therapistUsername);
        report.put("patientUsername",patientUsername);
        report.put("patientName",patientName);
        report.put("sessionDate",sdate.getText().toString());
        report.put("reportStatement",statement.getText().toString());

        FirebaseDatabase.getInstance().getReference("Reports")
                .push()
                .setValue(report).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    Toast.makeText(getApplicationContext(),"Report added successfully" , Toast.LENGTH_LONG).show();

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(reportWrittingActivity.this);
                    alertDialogBuilder.setTitle("Want Another Session?");
                    alertDialogBuilder.setMessage("Do You want this patient to conduct another Therapy Session?");
                    alertDialogBuilder.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    addTherapistRecommendation();
                                    deleteTherapySession();
                                    updateAvailability();

                                    sendNotification();
                                    Intent i = new Intent(reportWrittingActivity.this, therapistDashboardActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    startActivity(i);

                                }
                            });

                    alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //delete Session
                            deleteTherapySession();
                            updateAvailability();

                            Intent i = new Intent(reportWrittingActivity.this, therapistDashboardActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(i);
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }
        });

    }


    private void updateAvailability(){
        //add therapist availablibity in available duration slot

        SharedPreferences sharedPreferences=getSharedPreferences("Login_Session", Context.MODE_PRIVATE);
        therapistUsername=sharedPreferences.getString("username","");

        Query usernameQuery = FirebaseDatabase.getInstance().getReference()
                .child("TherapistPreferences").orderByChild("therapistUsername").equalTo(therapistUsername);
        usernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    prefStart= ds.child("startTime").getValue(String.class);
                    prefEnd= ds.child("endTime").getValue(String.class);


                    final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

                    final LocalTime startTime=  LocalTime.parse(start, timeFormatter);
                    final LocalTime stopTime=  LocalTime.parse(end, timeFormatter);
                    final LocalTime prefstart= LocalTime.parse(prefStart, timeFormatter);
                    final LocalTime prefend= LocalTime.parse(prefEnd, timeFormatter);

                    if((prefstart.isBefore(startTime) || prefstart.equals(startTime)) &&
                            (prefend.equals(stopTime) || prefend.isAfter(stopTime))) {

                        final HashMap<String, String> pref =
                                new HashMap<String, String>();
                        pref.put("therapistUsername",therapistUsername);
                        pref.put("startTime", start);
                        pref.put("endTime",end);
                        FirebaseDatabase.getInstance().getReference("TherapistAvailability")
                                .push()
                                .setValue(pref).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Availability Added", Toast.LENGTH_LONG).show();

                                }
                            }

                        });
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

    }

    private void deleteTherapySession() {

        Query usernameQuery = FirebaseDatabase.getInstance().getReference()
                .child("Sessions").orderByChild("patientUsername").equalTo(patientUsername);
        usernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    key = ds.getKey();
                    FirebaseDatabase.getInstance().getReference()
                            .child("Sessions")
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

    public void addTherapistRecommendation(){
        final HashMap<String, String> rec =
                new HashMap<String, String>();
        rec.put("patientUsername",patientUsername);
        rec.put("Recommendation", "AnotherSession");
        rec.put("Deplevel", deplevel);
        FirebaseDatabase.getInstance().getReference("TherapistRecommendations")
                .push()
                .setValue(rec).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "therapistRecommendation Updated sucessfully", Toast.LENGTH_LONG).show();

                }
            }

        });

    }
    private void sendNotification()
    {
        final String headerMessage="Session Report";
        final String message="Your therapist recommended you to take another session";
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT > 8) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    try {
                        String jsonResponse;

                        URL url = new URL("https://onesignal.com/api/v1/notifications");
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setUseCaches(false);
                        con.setDoOutput(true);
                        con.setDoInput(true);

                        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                        con.setRequestProperty("Authorization", "Basic YjliMmFjMzUtZGQ5MS00ZmYzLThlMTAtMjNkMjA3YWE0N2Vm");
                        con.setRequestMethod("POST");

                        String strJsonBody = "{"
                                + "\"app_id\": \"d7f136c6-79fc-454e-80a3-a7a75117db99\","

                                + "\"filters\": [{\"field\": \"tag\", \"key\": \"User_ID\", \"relation\": \"=\", \"value\": \"" + patientUsername + "\"}],"

                                + "\"data\": {\"foo\": \"bar\"},"
                                + "\"headings\": {\"en\":\""+ headerMessage+"\"},"
//                                + "\"smallIcon\": {\"en\":\""+ R.drawable.gotcha+"\"},"
                                + "\"contents\": {\"en\": \""+ message+"\"}"
                                + "}";


                        System.out.println("strJsonBody:\n" + strJsonBody);

                        byte[] sendBytes = strJsonBody.getBytes("UTF-8");
                        con.setFixedLengthStreamingMode(sendBytes.length);

                        OutputStream outputStream = con.getOutputStream();
                        outputStream.write(sendBytes);

                        int httpResponse = con.getResponseCode();
                        System.out.println("httpResponse: " + httpResponse);

                        if (httpResponse >= HttpURLConnection.HTTP_OK
                                && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                            Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        } else {
                            Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        }
                        System.out.println("jsonResponse:\n" + jsonResponse);

                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        });
    }

}

