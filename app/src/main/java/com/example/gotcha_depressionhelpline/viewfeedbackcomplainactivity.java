package com.example.gotcha_depressionhelpline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

public class viewfeedbackcomplainactivity extends AppCompatActivity {

    ListView complain,feedback;
    RadioGroup choice;
    String selection ,data;
    ArrayList<String> listforComplain,listforFeedback;
    ArrayAdapter<String> adapterforcomplain,adapterforFeedback;
    private Firebase firebaseref;
    int feedbackCount=0, complainCount=0;
    private ProgressBar mProgressCircle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewfeedbackcomplainactivity);

        Firebase.setAndroidContext(this);

        mProgressCircle = findViewById(R.id.progress_circle);
        listforComplain=new ArrayList<>();
        listforFeedback=new ArrayList<>();
        adapterforcomplain=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listforComplain);
        adapterforFeedback=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listforFeedback);
        complain=findViewById(R.id.compainListView);
        feedback=findViewById(R.id.feedbackListView);
        setFeedbackDetails();
        choice=findViewById(R.id.feedbackComplainRadioGroup);
        choice.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                              public void onCheckedChanged(RadioGroup group, int checkedId) {
                                                  switch (checkedId) {
                                                      case R.id.feedback:
                                                          selection = "feedback";
                                                          feedback.setVisibility(View.VISIBLE);
                                                          if(feedbackCount==0){
                                                             feedbackCount++;
                                                          }
                                                          complain.setVisibility(View.GONE);
                                                          break;
                                                      case R.id.complain:
                                                          selection = "complain";
                                                          if(complainCount==0){
                                                              setComplainDetails();
                                                              complainCount++;
                                                          }
                                                          complain.setVisibility(View.VISIBLE);
                                                          feedback.setVisibility(View.GONE);
                                                          break;
                                                  }
                                              }
                                          });

    }
    void setFeedbackDetails(){

        firebaseref= new Firebase("https://gotcha-depression-helpline.firebaseio.com/FeedbackComplains/Feedbacks");
        firebaseref.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                mProgressCircle.setVisibility(View.GONE);
                for (com.firebase.client.DataSnapshot ds: dataSnapshot.getChildren()) {

                    data = ds.child("feedbackGiverName").getValue(String.class);
                    listforFeedback.add(data);
                }
                feedback.setAdapter(adapterforFeedback);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                mProgressCircle.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), firebaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        feedback.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView
                String selectedItem = (String) parent.getItemAtPosition(position);
                Intent i=new Intent(viewfeedbackcomplainactivity.this,detailedReviewOnFeedbackComplains.class);
                i.putExtra("Username",selectedItem);
                i.putExtra("Type","feedback");
                startActivity(i);

            }
        });
    }
    void setComplainDetails(){

        firebaseref= new Firebase("https://gotcha-depression-helpline.firebaseio.com/FeedbackComplains/Complains");
        firebaseref.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                mProgressCircle.setVisibility(View.GONE);
                for (com.firebase.client.DataSnapshot ds: dataSnapshot.getChildren()) {
                    data = ds.child("complainMakerName").getValue(String.class);
                    listforComplain.add(data);
                }
                complain.setAdapter(adapterforcomplain);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                mProgressCircle.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), firebaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        complain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView
                String selectedItem = (String) parent.getItemAtPosition(position);
                Intent i=new Intent(viewfeedbackcomplainactivity.this,detailedReviewOnFeedbackComplains.class);
                i.putExtra("Username",selectedItem);
                i.putExtra("Type","complain");
                startActivity(i);

            }
        });
    }
}
