package com.example.gotcha_depressionhelpline;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class detailedRecordActivity extends AppCompatActivity {

    ListView listView;
    String userid,type;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ArrayList<Patient> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_record);

        firebaseDatabase=FirebaseDatabase.getInstance();



        listView= findViewById(R.id.list);
        final ArrayAdapter<Patient> arrayAdapter= new ArrayAdapter<Patient>(this ,android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);
        Intent i= getIntent();
        type=i.getStringExtra("Type");
        userid=i.getStringExtra("UserID");
        if(type.equals("Patient")){
            databaseReference= firebaseDatabase.getReference("Users").child(userid);
            databaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Patient value = dataSnapshot.getValue(Patient.class);
                    arrayList.add(value);
                    arrayAdapter.notifyDataSetChanged();

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }
}