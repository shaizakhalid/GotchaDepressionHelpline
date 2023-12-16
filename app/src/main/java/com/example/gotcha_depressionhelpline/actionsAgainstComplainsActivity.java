package com.example.gotcha_depressionhelpline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.FirebaseError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class actionsAgainstComplainsActivity extends AppCompatActivity {

    String username,userid,complainMaker,key;
    User user;
    TextView uName,uTitle,uEmail;
    String sendToUsername, title;
    String message,headerMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actions_against_complains);

        uName=findViewById(R.id.uName);
        uEmail=findViewById(R.id.uEmail);
        uTitle=findViewById(R.id.uTitle);
        Intent i= getIntent();
        username=i.getStringExtra("username");
        complainMaker=i.getStringExtra("complainMakerName");
        sendToUsername=username;
        DatabaseReference feedbackRef = FirebaseDatabase.getInstance().getReference("Users");
        Query usernameQuery = feedbackRef.orderByChild("username").equalTo(username);
        usernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    uName.setText(ds.child("name").getValue(String.class));
                    title=ds.child("title").getValue(String.class);
                    uTitle.setText(title);
                    uEmail.setText(ds.child("email").getValue(String.class));
                    userid=ds.child("userid").getValue(String.class);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(actionsAgainstComplainsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void warn_Clicked(View view) {
        headerMessage="Warning Message";
        message="We have recieved complain against your behaviour. You are warn to rectify your behaviour, if you want to continue our service";
        sendNotification();
        updateComplain();
        Toast.makeText(actionsAgainstComplainsActivity.this, "User warned", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), AdminDashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void Blocked_Clicked(View view) {
        AlertDialog.Builder dialog= new AlertDialog.Builder(this);
        dialog.setTitle("Are you sure?");
        dialog.setMessage("Blocking user will result in completely " +
                "removing account from the system and user won't be able to access the app");
        dialog.setPositiveButton("Block", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(title.equals("Patient")){
                    updateUser();
                    updatePatient();
                }
                else if(title.equals("therapist") || title.equals("Therapist")){
                    updateTherapist();
                    updateUser();
                }
                updateComplain();
                headerMessage="Account Blocked";
                message="Your Account has been Blocked";
                sendNotification();
                Toast.makeText(actionsAgainstComplainsActivity.this, "User Blocked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), AdminDashboard.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog=dialog.create();
        alertDialog.show();
    }

    private void updateComplain() {
        DatabaseReference feedbackRef1 = FirebaseDatabase.getInstance()
                .getReference("Users");
        Query usernameQuery1 = feedbackRef1.orderByChild("username").equalTo(complainMaker);
        usernameQuery1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                key = ds.child("userid").getValue(String.class);
                }
                final DatabaseReference deleteComplain = FirebaseDatabase.getInstance()
                        .getReference("FeedbackComplains").child("Complains");
                deleteComplain.child(key).removeValue();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(actionsAgainstComplainsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateUser()
    {
        DatabaseReference dbRef=FirebaseDatabase.getInstance().getReference("Users");
        dbRef.child(userid)
                .child("title")
                .setValue("disabled");
    }
    private void updateTherapist()
    {
        DatabaseReference dbRef=FirebaseDatabase.getInstance().getReference("Therapists");
        dbRef.child(userid)
                .child("title")
                .setValue("disabled");

    }
    private void updatePatient()
    {
        DatabaseReference dbRef=FirebaseDatabase.getInstance().getReference("Patients");
        dbRef.child(userid)
                .child("title")
                .setValue("disabled");
    }
    private void sendNotification()
    {

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

                                + "\"filters\": [{\"field\": \"tag\", \"key\": \"User_ID\", \"relation\": \"=\", \"value\": \"" + sendToUsername + "\"}],"

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
