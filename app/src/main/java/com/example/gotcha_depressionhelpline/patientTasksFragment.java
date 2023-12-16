package com.example.gotcha_depressionhelpline;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


interface patientOnclicks{
    void callExercises();
    void callTherapySessions();
    void callMytasks();
    void callAssessmentDetails();

}

public class patientTasksFragment extends Fragment {

    CardView exercises,therapySessions;
    patientOnclicks delegate;
    public patientTasksFragment() {
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
        View view= inflater.inflate(R.layout.fragment_patient_tasks, container, false);
        exercises =view.findViewById(R.id.exercises);
        therapySessions =view.findViewById(R.id.therapySessions);


        exercises.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delegate.callExercises();
            }
        });
        therapySessions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delegate.callTherapySessions();
            }
        });

        return view;
    }

}
