package com.example.gotcha_depressionhelpline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class assessmentResultActivity extends AppCompatActivity {

    TextView depressionlevel,DepStr,nodep,recommendation;
    int score;
    String severity;
    Button exercise,therapySessions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_result);

        depressionlevel=findViewById(R.id.depLevel);
        DepStr=findViewById(R.id.depLevelString);
        nodep=findViewById(R.id.noDep);
        recommendation=findViewById(R.id.recommendation);
        exercise=findViewById(R.id.linkExercises);
        therapySessions=findViewById(R.id.linkTherapySession);

        score= getIntent().getIntExtra("score",-1);
        depressionlevel.setText(score + "");


        if(score>=0 && score<5){
            severity="No Depression";
            DepStr.setText(severity);
            nodep.setVisibility(View.VISIBLE);
            Toast.makeText(this,"none",Toast.LENGTH_LONG).show();
        }
        else if(score>4 && score<10){
            //mild 5-9
            severity=" Mild";
            recommendation.setVisibility(View.VISIBLE);
            exercise.setVisibility(View.VISIBLE);
            DepStr.setText(severity);
            Toast.makeText(this,"mild",Toast.LENGTH_LONG).show();
        }
        else if(score>9 && score<15){
            //moderate 10-14
            severity=" Moderate";
            recommendation.setVisibility(View.VISIBLE);
            exercise.setVisibility(View.VISIBLE);
            DepStr.setText(severity);
            Toast.makeText(this,"moderate",Toast.LENGTH_LONG).show();
        }
        else if(score>14 && score<20){
            //moderately severe 15-19
            severity=" Moderately Severe";
            recommendation.setVisibility(View.VISIBLE);
            exercise.setVisibility(View.VISIBLE);
            DepStr.setText(severity);

            Toast.makeText(this,"moderately severe",Toast.LENGTH_LONG).show();
        }
        else if(score>19 && score<28){
            //severe 20-27
            severity="Severe";
            recommendation.setVisibility(View.VISIBLE);
            exercise.setVisibility(View.VISIBLE);
            therapySessions.setVisibility(View.VISIBLE);
            DepStr.setText(severity);
            Toast.makeText(this,"severe",Toast.LENGTH_LONG).show();
        }
        SharedPreferences sharedPreferences= getSharedPreferences("Login_Session",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("patientType","Old");
        editor.apply();
        SharedPreferences sharedPreferences2= getSharedPreferences("Depression_Result",MODE_PRIVATE);
        SharedPreferences.Editor editor2=sharedPreferences2.edit();
        editor2.putInt("Depression_Level",score);
        editor2.putString("DepressionSeverityLevel",severity);
        editor2.apply();
        addIntoDB();
    }

    public void linkExercise_Clicked(View view) {
        Intent i =new Intent(this,ExerciseMainPage.class);
        startActivity(i);
    }

    public void linkSessions_Clicked(View view) {
        Intent i =new Intent(this, BookingForClinicalTreatment.class);
        startActivity(i);
    }
    public void addIntoDB(){
        SharedPreferences sharedPreferences=getSharedPreferences("Login_Session", Context.MODE_PRIVATE);
        String username=sharedPreferences.getString("username","");
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss");
        String reg_date = df.format(c.getTime());

        c.add(Calendar.DATE, 15);  // number of days to add
        String end_date = df.format(c.getTime());

        HashMap<String, String> pref =
                new HashMap<String, String>();
        pref.put("level", String.valueOf(score));
        pref.put("severity",severity);
        pref.put("username",username);
        pref.put("evaluationDate",reg_date);
        pref.put("nextEvaluation",end_date);
        FirebaseDatabase.getInstance().getReference("PatientHistory")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(pref).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

            }
        }
    });
}
}
