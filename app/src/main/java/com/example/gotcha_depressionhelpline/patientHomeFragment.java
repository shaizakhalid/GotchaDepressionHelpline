package com.example.gotcha_depressionhelpline;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;



public class patientHomeFragment extends Fragment {
    Button takeQuiz;
    RelativeLayout afterQuiz;
    patientOnclicks delegate;
    TextView beforeQuizText;
    CardView tasks,exercises,therapySessions,quiz;
    TextView patientUsername;
    public patientHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        delegate = (patientOnclicks) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_patient_home, container, false);
        Firebase.setAndroidContext(getContext());

        takeQuiz = view.findViewById(R.id.takeQuiz);
        tasks =view.findViewById(R.id.patientTasks);
        therapySessions =view.findViewById(R.id.therapySessions);
        afterQuiz=view.findViewById(R.id.afterQuiz);
        quiz=view.findViewById(R.id.depQuiz);
        exercises=view.findViewById(R.id.exercises);
        beforeQuizText=view.findViewById(R.id.beforeQuizText);
        patientUsername=view.findViewById(R.id.patientName);


        SharedPreferences sharedPreferences= getContext().getSharedPreferences("Login_Session",getContext().MODE_PRIVATE);
        String title=sharedPreferences.getString("patientType","-1");
        String username = sharedPreferences.getString("username", "");

        if(title.equals("Old")){
            takeQuiz.setVisibility(View.GONE);
            beforeQuizText.setVisibility(View.GONE);
            afterQuiz.setVisibility(View.VISIBLE);
            patientUsername.setText(username);

        }
        else {

            afterQuiz.setVisibility(View.GONE);
            beforeQuizText.setVisibility(View.VISIBLE);
            takeQuiz.setVisibility(View.VISIBLE);
            }

        takeQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getContext(),QuizInstructionsActivity.class);
                startActivity(i);
                takeQuiz.setVisibility(View.GONE);
            }
        });

        tasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delegate.callMytasks();
            }
        });
        therapySessions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delegate.callTherapySessions();
            }
        });
        exercises.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delegate.callExercises();
            }
        });
        quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delegate.callAssessmentDetails();
            }
        });
        return view;
    }

}