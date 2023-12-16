package com.example.gotcha_depressionhelpline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class viewReportsActivity extends AppCompatActivity {

    TextView textType,noItemTextView;
    ListView listView;
    private ReportsCustomListAdapter mAdapter;
    String patientName, sessiondate, statement;
    ArrayList<Report> usersList = new ArrayList<Report>();
    private ProgressBar mProgressCircle;
    String therapistName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reports);

        Firebase.setAndroidContext(this);

        textType=findViewById(R.id.type);
        listView=findViewById(R.id.recordListview);
        mProgressCircle = findViewById(R.id.progress_circle);
        noItemTextView= findViewById(R.id.noItemTextView);

        mProgressCircle.setVisibility(View.VISIBLE);
        listView = findViewById(R.id.listview);
        mAdapter = new ReportsCustomListAdapter(this,usersList);
        listView.setAdapter(mAdapter);
        SharedPreferences sharedPreferences1=getSharedPreferences("Login_Session", Context.MODE_PRIVATE);
        String token=sharedPreferences1.getString("Token","");

        if(token.equals("Therapist")) {

            String username = sharedPreferences1.getString("username", "");

            Query usernameQuery1 = FirebaseDatabase.getInstance().getReference("Reports")
                    .orderByChild("therapistUsername").equalTo(username);
            usernameQuery1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount() > 0) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            mProgressCircle.setVisibility(View.GONE);
                            patientName = ds.child("patientUsername").getValue(String.class);
                            sessiondate = ds.child("sessionDate").getValue(String.class);
                            statement = ds.child("reportStatement").getValue(String.class);

                            usersList.add(new Report(patientName, sessiondate, statement));
                            mAdapter.notifyDataSetChanged();
                        }
                    } else {
                        mProgressCircle.setVisibility(View.GONE);
                        listView.setVisibility(View.GONE);
                        noItemTextView.setVisibility(View.VISIBLE);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(viewReportsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
        else{

            Query usernameQuery1 = FirebaseDatabase.getInstance().getReference("Reports");
            usernameQuery1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount() > 0) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            mProgressCircle.setVisibility(View.GONE);
                            patientName = ds.child("patientUsername").getValue(String.class);
                            therapistName = ds.child("patientUsername").getValue(String.class);
                            sessiondate = ds.child("sessionDate").getValue(String.class);
                            statement = ds.child("reportStatement").getValue(String.class);

                            usersList.add(new Report(patientName,therapistName, sessiondate, statement));
                            mAdapter.notifyDataSetChanged();
                        }
                    } else {
                        mProgressCircle.setVisibility(View.GONE);
                        listView.setVisibility(View.GONE);
                        noItemTextView.setVisibility(View.VISIBLE);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(viewReportsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }

    }
}
