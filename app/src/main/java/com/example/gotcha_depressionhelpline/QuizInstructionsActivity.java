package com.example.gotcha_depressionhelpline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class QuizInstructionsActivity extends AppCompatActivity {

    Button startQuiz;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_instructions);
        startQuiz= findViewById(R.id.startQuiz);
    }
    public void startQuiz_Clicked(View view){
        Intent i= new Intent(this,depressionAssessmentActivity.class);
        startActivity(i);
    }
}
