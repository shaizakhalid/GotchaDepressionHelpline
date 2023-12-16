package com.example.gotcha_depressionhelpline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sinch.android.rtc.SinchError;

import java.util.ArrayList;

public class therapistScheduleActivity extends BaseActivity implements SinchService.StartFailedListener  {

    private ListView listView;
    private CustomListAdapter mAdapter;
    String username,startTime,endTime,depLevel,sessionDate;
    String usernameKey,endTimeKey,startTimeKey,sessionDateKey;
    ArrayList<Preferences> preferencesList = new ArrayList<Preferences>();
    private ProgressBar mProgressCircle;
    TextView noItemTextView;
    String therapistUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_therapist_schedule);
        listView = findViewById(R.id.listview);
        mProgressCircle = findViewById(R.id.progress_circle);
        noItemTextView= findViewById(R.id.noItemTextView);

        mAdapter = new CustomListAdapter(this,preferencesList);
        listView.setAdapter(mAdapter);
        listView.setEnabled(false);
        SharedPreferences sharedPreferences=getSharedPreferences("Login_Session", Context.MODE_PRIVATE);
        therapistUsername=sharedPreferences.getString("username","-1");
        Query usernameQuery = FirebaseDatabase.getInstance().getReference()
                .child("Sessions").orderByChild("therapistUserame").equalTo(therapistUsername);
        usernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() > 0) {
                            for (DataSnapshot ds: dataSnapshot.getChildren()) {
                                mProgressCircle.setVisibility(View.GONE);
                                username = ds.child("patientUsername").getValue(String.class);
                                startTime = ds.child("startTime").getValue(String.class);
                                endTime = ds.child("endTime").getValue(String.class);
                                depLevel=ds.child("deplevel").getValue(String.class);
                                sessionDate=ds.child("SessionDate").getValue(String.class);

                                preferencesList.add(new Preferences(username, startTime, endTime, sessionDate));
                                mAdapter.notifyDataSetChanged();
                           }
                        }
                        else{
                            mProgressCircle.setVisibility(View.GONE);
                            listView.setVisibility(View.GONE);
                            noItemTextView.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                        Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                TextView tv = view.findViewById(R.id.usernameTextView);
                TextView tv2=view.findViewById(R.id.startTime);
                TextView tv3=view.findViewById(R.id.endTime);
                TextView tv4=view.findViewById(R.id.sessionDate);
                usernameKey=tv.getText().toString();
                startTimeKey=tv2.getText().toString();
                endTimeKey=tv3.getText().toString();
                sessionDateKey=tv4.getText().toString();
                listClicked();

            }
        });

    }

    @Override
    protected void onServiceConnected() {
        listView.setEnabled(true);
        getSinchServiceInterface().setStartListener(this);

//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                listClicked();
//            }
//        }, 4000);

    }

    @Override
    public void onStartFailed(SinchError error) {

    }

    @Override
    public void onStarted() {

    }

    public void listClicked(){

//        if ( !getSinchServiceInterface().isStarted() ) {
            getSinchServiceInterface().startClient(therapistUsername);

//        } else {
            Intent i=new Intent(therapistScheduleActivity.this,therapySessionActivity.class);
            i.putExtra("patientUsername",usernameKey);
            i.putExtra("startTime",startTimeKey);
            i.putExtra("endTime",endTimeKey);
            i.putExtra("depLevel",depLevel);
            i.putExtra("sessionDate",sessionDateKey);
            startActivity(i);
//        }

    }
}
