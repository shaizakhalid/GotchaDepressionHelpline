package com.example.gotcha_depressionhelpline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.onesignal.OneSignal;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class authenticationActivity extends AppCompatActivity implements ImageAdapter.OnItemClickListener{

    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;
    String sendToUsername;
    private ProgressBar mProgressCircle;
    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    DatabaseReference firebaseUser;
    private Firebase userRef;
    private List<Upload> mUploads;
    User user;
    TextView message;
    String header, content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        Firebase.setAndroidContext(this);
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        mRecyclerView = findViewById(R.id.recycler_view);
        message=findViewById(R.id.noRequestMsg);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mProgressCircle = findViewById(R.id.progress_circle);

        mUploads = new ArrayList<>();
        mAdapter = new ImageAdapter(authenticationActivity.this, mUploads);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(authenticationActivity.this);

        mStorage = FirebaseStorage.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.getChildrenCount()>0){
                mUploads.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    Upload upload = postSnapshot.getValue(Upload.class);
                    upload.setKey(postSnapshot.getKey());
                    //will save the unique key so that we can identify correct entry
                    mUploads.add(upload);

                }
                mProgressCircle.setVisibility(View.GONE);
                mAdapter.notifyDataSetChanged();
                }
                else{
                    mProgressCircle.setVisibility(View.GONE);
                    message.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(authenticationActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });

    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(this,"Normal Click at position "+position,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAcceptClick(int position) {

        Upload selectedItem = mUploads.get(position);
        final String selectedKey = selectedItem.getKey();
        sendToUsername=selectedItem.getName();
        userRef= new Firebase("https://gotcha-depression-helpline.firebaseio.com/Users/"+selectedKey);
        try {
            userRef.child("title").setValue("therapist");
        } catch (Exception e) {
            e.printStackTrace();
        }
        userRef.addValueEventListener(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                    user = dataSnapshot.getValue(User.class);
                    firebaseUser = FirebaseDatabase.getInstance().getReference("Therapists").child(selectedKey);
                    Therapist therapist = new Therapist(user.getUsername(),user.getEmail(), user.getPassword(), selectedKey, "therapist",
                            user.getname(), user.getGender(), user.getExperience(), user.getCnic());
                    firebaseUser.setValue(therapist)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Account Authentication Successful", Toast.LENGTH_LONG).show();

                                    } else {
                                        Toast.makeText(getApplicationContext(), "Error" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(authenticationActivity.this, firebaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        final DatabaseReference uploadNode = FirebaseDatabase.getInstance().getReference().child("uploads");
        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getImageUrl());
        uploadNode.child(selectedKey).removeValue();
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(authenticationActivity.this, "Item removed", Toast.LENGTH_SHORT).show();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(authenticationActivity.this, "Item not removed", Toast.LENGTH_SHORT).show();
                    }
                });
        mUploads.remove(position);
        mAdapter.notifyItemRemoved(position);
        if(mUploads.size()==0){
            mRecyclerView.setVisibility(View.GONE);
            message.setVisibility(View.VISIBLE);
        }
        mAdapter.notifyDataSetChanged();
        header="Account Authentication";
        content="Your Account has been authenticated";
        sendAuthenticatonNotification();
    }

    @Override
    public void onRejectClick(int position) {

        Upload selectedItem = mUploads.get(position);
        final String selectedKey = selectedItem.getKey();
        final DatabaseReference userNode = FirebaseDatabase.getInstance().getReference().child("Users");
        final DatabaseReference uploadNode = FirebaseDatabase.getInstance().getReference().child("uploads");
        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getImageUrl());
        userNode.child(selectedKey).removeValue();
        uploadNode.child(selectedKey).removeValue();
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(authenticationActivity.this, "Request Rejected ", Toast.LENGTH_SHORT).show();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(authenticationActivity.this, "Request can't be rejected", Toast.LENGTH_SHORT).show();
            }
        });
        mUploads.remove(position);
        mAdapter.notifyItemRemoved(position);
        mAdapter.notifyDataSetChanged();
        if(mUploads.size()==0){
            mRecyclerView.setVisibility(View.GONE);
            message.setVisibility(View.VISIBLE);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }

    private void sendAuthenticatonNotification()
    {

        Toast.makeText(this, "Notification sent", Toast.LENGTH_SHORT).show();

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
                                + "\"headings\": {\"en\":\""+ header+"\"},"
                                + "\"contents\": {\"en\": \""+ content+"\"}"
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

