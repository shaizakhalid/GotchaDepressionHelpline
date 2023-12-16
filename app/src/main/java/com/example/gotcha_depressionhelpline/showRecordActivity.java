package com.example.gotcha_depressionhelpline;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class showRecordActivity extends AppCompatActivity {

    TextView textType;
    ListView listView;
    private ShowRecordCustomListAdapter mAdapter;
    Firebase firebaseref;
    String type;
    ArrayList<User> usersList = new ArrayList<User>();
    private ProgressBar mProgressCircle;
//    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_record);

        Firebase.setAndroidContext(this);

        textType=findViewById(R.id.type);
        listView=findViewById(R.id.recordListview);
        mProgressCircle = findViewById(R.id.progress_circle);

        mAdapter = new ShowRecordCustomListAdapter(this,usersList);
        listView.setAdapter(mAdapter);

//        user = new User();
//        arrayList=new ArrayList<>();
//        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList);
        type= getIntent().getStringExtra("type");
         if(type.equals("Patient")){
             textType.setText("Patient Record");
             firebaseref= new Firebase("https://gotcha-depression-helpline.firebaseio.com/Patients");
             showRecord();
////             dbref= FirebaseDatabase.getInstance().getReference().child("Users");
         }
         else {
             textType.setText("Therapist Record");
             firebaseref= new Firebase("https://gotcha-depression-helpline.firebaseio.com/Therapists");
             showRecord();
////             firebaseref = FirebaseDatabase.getInstance().getReference("");
//
         }
//         listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//             @Override
//             public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                     Intent i=new Intent(showRecordActivity.this,detailedRecordActivity.class);
//                     i.putExtra("Type",type);
//                     i.putExtra("UserID",key);
//                     startActivity(i);
//
//             }
//
//             @Override
//             public void onNothingSelected(AdapterView<?> parent) {
//
//             }
//         });

    }

    private void showRecord() {

             firebaseref.addValueEventListener(new ValueEventListener()
             {
                 @Override
                 public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                     mProgressCircle.setVisibility(View.GONE);
                     for (DataSnapshot ds: dataSnapshot.getChildren()) {
                         String username = ds.child("username").getValue(String.class);
                         String name = ds.child("name").getValue(String.class);
                         String email = ds.child("email").getValue(String.class);
                         String gender=ds.child("gender").getValue(String.class);

                         usersList.add(new User(username, email, " ", " ", " ", name,gender," "," "," "));
                         mAdapter.notifyDataSetChanged();
                     }
                 }

                 @Override
                 public void onCancelled(FirebaseError firebaseError) {
                     mProgressCircle.setVisibility(View.GONE);
                     Toast.makeText(getApplicationContext(), firebaseError.getMessage(), Toast.LENGTH_LONG).show();
                 }
         });

    }
}
