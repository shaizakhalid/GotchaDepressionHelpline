package com.example.gotcha_depressionhelpline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class depressionAssessmentActivity extends AppCompatActivity {

    private TextView question,quesHeading;
    private Firebase questionRef;
    Button finish,opt1,opt2,opt3,opt4;
    TextView finishText;
    TextView number;
    LinearLayout showQuestion;
    ProgressBar progressBar;
    private int questionNum=1;
    private int score=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_depression_assessment);

        Firebase.setAndroidContext(this);
        question = findViewById(R.id.question);
        quesHeading=findViewById(R.id.quesText);
        opt1=findViewById(R.id.notAtAll);
        opt2=findViewById(R.id.severalDays);
        showQuestion=findViewById(R.id.showQuestion);
        progressBar=findViewById(R.id.simpleProgressBar);
        opt3=findViewById(R.id.moreThanHalfTheDays);
        opt4=findViewById(R.id.nearlyEveryDay);
        finish=findViewById(R.id.finish);
        finishText=findViewById(R.id.textfinish);
        number=findViewById(R.id.num);

        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateQuestion();
    }

    public void updateQuestion(){
        opt1.setBackgroundResource(R.drawable.quiz_button_back);
        opt2.setBackgroundResource(R.drawable.quiz_button_back);
        opt3.setBackgroundResource(R.drawable.quiz_button_back);
        opt4.setBackgroundResource(R.drawable.quiz_button_back);

        number.setText(questionNum+" ");
//      get question from firebase
        questionRef= new Firebase("https://gotcha-depression-helpline.firebaseio.com/questionnaire/questionnaire1/"+
                questionNum+"/question");

        questionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String ques= dataSnapshot.getValue(String.class);
                question.setText(ques);
                progressBar.setVisibility(View.GONE);
                showQuestion.setVisibility(View.VISIBLE);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(depressionAssessmentActivity.this,"Error in retriving question",Toast.LENGTH_LONG).show();
            }
    });
        questionNum++;

        if(questionNum==11){
            question.setVisibility(View.GONE);
            quesHeading.setVisibility(View.GONE);
            opt1.setVisibility(View.GONE);
            opt2.setVisibility(View.GONE);
            opt3.setVisibility(View.GONE);
            opt4.setVisibility(View.GONE);
            number.setVisibility(View.GONE);
            finishText.setVisibility(View.VISIBLE);
            finish.setVisibility(View.VISIBLE);
            finish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(depressionAssessmentActivity.this,assessmentResultActivity.class);
//                    String Score=score+" ";
                    i.putExtra("score",score);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(i);
                }
            });
        }
}
    public void notAtAll_Clicked(View aview){
        opt1.setBackgroundResource(R.drawable.quiz_button_pressed);
//        opt2.setBackgroundResource(R.drawable.quiz_button_back);
//        opt3.setBackgroundResource(R.drawable.quiz_button_back);
//        opt4.setBackgroundResource(R.drawable.quiz_button_back);

        updateQuestion();
    }
    public void severalDays_Clicked(View aview){
//        opt1.setBackgroundResource(R.drawable.quiz_button_back);
        opt2.setBackgroundResource(R.drawable.quiz_button_pressed);
//        opt3.setBackgroundResource(R.drawable.quiz_button_back);
//        opt4.setBackgroundResource(R.drawable.quiz_button_back);
        score= score+1;
        updateQuestion();
    }
    public void moreThanHalfTheDays_clicked(View aview){
//        opt1.setBackgroundResource(R.drawable.quiz_button_back);
//        opt2.setBackgroundResource(R.drawable.quiz_button_back);
        opt3.setBackgroundResource(R.drawable.quiz_button_pressed);
//        opt4.setBackgroundResource(R.drawable.quiz_button_back);
        score= score+2;
        updateQuestion();
    }
    public void nearlyEveryDay(View aview){
//        opt1.setBackgroundResource(R.drawable.quiz_button_back);
//        opt2.setBackgroundResource(R.drawable.quiz_button_back);
//        opt3.setBackgroundResource(R.drawable.quiz_button_back);
        opt4.setBackgroundResource(R.drawable.quiz_button_pressed);
        score=score+3;
        updateQuestion();
    }
}
