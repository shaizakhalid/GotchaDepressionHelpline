package com.example.gotcha_depressionhelpline;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class adminTasksFragment extends Fragment {
    CardView feedbackComplains,sessionReports;
    onclicks delegate;

    public adminTasksFragment() {
        // Required empty public constructor
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        delegate = (onclicks) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_admin_tasks, container, false);
        feedbackComplains =view.findViewById(R.id.feedbackComplain);
        sessionReports =view.findViewById(R.id.sessionReports);


        feedbackComplains.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delegate.viewFeedbackComplains();
            }
        });
        sessionReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delegate.viewSessionReports();
            }
        });

        return view;
    }

}
