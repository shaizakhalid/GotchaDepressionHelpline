package com.example.gotcha_depressionhelpline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;


public class assessmentDetailsActivity extends AppCompatActivity {

    TextView quizTaken,deplevel,depSeverity,nextEvaluation,remainingDays;
    String evaluationDateKey, nextEvaluationDateKey;
    Button takeQuiz;
    ProgressBar progressBar;
    LinearLayout assessmentDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_assessment_details);
        quizTaken=findViewById(R.id.quizTaken);
        deplevel=findViewById(R.id.deplevel);
        depSeverity=findViewById(R.id.depSeverity);
        nextEvaluation=findViewById(R.id.nextEvaluation);
        remainingDays=findViewById(R.id.remainingDays);
        takeQuiz=findViewById(R.id.takeQuiz);
        progressBar=findViewById(R.id.simpleProgressBar);
        assessmentDetails=findViewById(R.id.assessmentDetails);

        progressBar.setVisibility(View.VISIBLE);





        SharedPreferences sharedPreferences = getSharedPreferences("Login_Session", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        Query usernameQuery = FirebaseDatabase.getInstance().getReference().child("PatientHistory").orderByChild("username").equalTo(username);
        usernameQuery.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                FirebaseDatabase.getInstance().getReference().child("PatientHistory")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .addValueEventListener(new ValueEventListener(){
                                @SuppressLint("ResourceAsColor")
                                @Override
                          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    assessmentDetails.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                    evaluationDateKey=dataSnapshot.child("evaluationDate").getValue(String.class);
                                    quizTaken.setText(evaluationDateKey);
                                    deplevel.setText(dataSnapshot.child("level").getValue(String.class));
                                    depSeverity.setText(dataSnapshot.child("severity").getValue(String.class));
                                    nextEvaluationDateKey=dataSnapshot.child("nextEvaluation").getValue(String.class);
                                    nextEvaluation.setText(nextEvaluationDateKey);
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                    //use to convert from string to date format
                                    try {
//                                        Date evaluationDate = format.parse(evaluationDateKey);
                                        Date nextEvaluationDate = format.parse(nextEvaluationDateKey);
                                        Calendar c = Calendar.getInstance();
                                        Date currentDate = c.getTime();
                                        if(nextEvaluationDate.equals(currentDate) || currentDate.after(nextEvaluationDate)){
                                            takeQuiz.setVisibility(View.VISIBLE);
                                            takeQuiz.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent i= new Intent(assessmentDetailsActivity.this,QuizInstructionsActivity.class);
                                                    startActivity(i);
                                                    takeQuiz.setVisibility(View.GONE);
                                                }
                                            });
                                        }
                                        getDuration(currentDate,nextEvaluationDate);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.getMessage() , Toast.LENGTH_LONG).show();
            }
                            });
                }
            }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(getApplicationContext(),databaseError.getMessage() , Toast.LENGTH_LONG).show();
        }
        });


    }

    private void getDuration(Date d1, Date d2) {
        Duration diff = Duration.between(d1.toInstant(), d2.toInstant());

        long days = diff.toDays();
        diff = diff.minusDays(days);
        long hours = diff.toHours();
        diff = diff.minusHours(hours);
        long minutes = diff.toMinutes();
        diff = diff.minusMinutes(minutes);
        long seconds = diff.toMillis();

        StringBuilder formattedDiff = new StringBuilder();
        if(days!=0){
            if(days==1){
                formattedDiff.append(days + " Day ");

            }else {
                formattedDiff.append(days + " Days ");
            }
        }if(hours!=0){
            if(hours==1){
                formattedDiff.append(hours + " hour ");
            }else{
                formattedDiff.append(hours + " hours ");
            }
        }if(minutes!=0){
            if(minutes==1){
                formattedDiff.append(minutes + " minute ");
            }else{
                formattedDiff.append(minutes + " minutes ");
            }
        }if(seconds!=0){
            if(seconds==1){
                formattedDiff.append(seconds + " second ");
            }else{
                formattedDiff.append(seconds + " seconds ");
            }
        }
        remainingDays.setText( days+" ");
    }
}