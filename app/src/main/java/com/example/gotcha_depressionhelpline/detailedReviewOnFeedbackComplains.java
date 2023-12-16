package com.example.gotcha_depressionhelpline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class detailedReviewOnFeedbackComplains extends AppCompatActivity {

    LinearLayout feedback, complain,against;
    String type, username,statement,complainAbout,complainAgainstUsername;
    TextView nameTV,statementTV,cName,cAgainst,cStatement,cAbout;
    Button review;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_review_on_feedback_complains);

        feedback = findViewById(R.id.llForFeedback);
        complain = findViewById(R.id.llForComplain);
        review=findViewById(R.id.review);
        against=findViewById(R.id.llAgainst);
        nameTV=findViewById(R.id.username);
        cName=findViewById(R.id.cUsername);
        cAbout=findViewById(R.id.cAbout);
        cStatement=findViewById(R.id.cStatement);
        cAgainst=findViewById(R.id.cAgainst);
        statementTV=findViewById(R.id.statement);
        Intent i = getIntent();
        username = i.getStringExtra("Username");
        type = i.getStringExtra("Type");
        if (type.equals("feedback")) {
            feedback.setVisibility(View.VISIBLE);
            complain.setVisibility(View.GONE);
            nameTV.setText(username);
            DatabaseReference feedbackRef = FirebaseDatabase.getInstance()
                    .getReference("FeedbackComplains")
                    .child("Feedbacks");
            Query usernameQuery = feedbackRef.orderByChild("feedbackGiverName").equalTo(username);
            usernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds: dataSnapshot.getChildren()) {
                        statement = ds.child("statement").getValue(String.class);
                    }
                    statementTV.setText(statement);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(detailedReviewOnFeedbackComplains.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        if(type.equals("complain")){
            complain.setVisibility(View.VISIBLE);
            feedback.setVisibility(View.GONE);
            cName.setText(username);
            DatabaseReference feedbackRef = FirebaseDatabase.getInstance()
                    .getReference("FeedbackComplains")
                    .child("Complains");
            Query usernameQuery = feedbackRef.orderByChild("complainMakerName").equalTo(username);
            usernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds: dataSnapshot.getChildren()) {
                        statement = ds.child("statement").getValue(String.class);
                        complainAbout=ds.child("complainAbout").getValue(String.class);
                        complainAgainstUsername=ds.child("complainAgainstUsername").getValue(String.class);
                    }
                    cStatement.setText(statement);
                    cAbout.setText(complainAbout);
                    if(complainAbout.equals("Services")){
                        against.setVisibility(View.VISIBLE);
                        cAgainst.setText("Services");
                    }
                    else{
                        against.setVisibility(View.VISIBLE);
                        cAgainst.setText(complainAgainstUsername);
                        review.setVisibility(View.VISIBLE);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(detailedReviewOnFeedbackComplains.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    public void review_Clicked(View view) {
        Intent i= new Intent(this,actionsAgainstComplainsActivity.class);
        i.putExtra("username",complainAgainstUsername);
        i.putExtra("complainMakerName",username);
        startActivity(i);
    }
}
