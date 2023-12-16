package com.example.gotcha_depressionhelpline;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

interface therapistOnclicks{
    void callSchedule();
    void viewSessionReports();
    void callTherapistTasks();
    void viewTherapistPreference();
}

public class therapistTasksFragment extends Fragment {

    CardView sessionReports,schedule;
    therapistOnclicks delegate;


    public therapistTasksFragment() {
        // Required empty public constructor
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        delegate = (therapistOnclicks) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_therapist_tasks, container, false);
        sessionReports =view.findViewById(R.id.sessionReports);
        schedule =view.findViewById(R.id.schedule);

        sessionReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delegate.viewSessionReports();
            }
        });
        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delegate.callSchedule();
            }
        });

        return view;
    }

}
