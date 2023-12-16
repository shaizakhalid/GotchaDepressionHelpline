package com.example.gotcha_depressionhelpline;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class therapistHomeFragment extends Fragment {
    therapistOnclicks delegate;
    CardView sessionReports,schedule,tasks,preferences;
    TextView therapistUsername;
    public therapistHomeFragment() {
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
        View view= inflater.inflate(R.layout.fragment_therapist_home, container, false);
        sessionReports =view.findViewById(R.id.sessionReports);
        therapistUsername =view.findViewById(R.id.therapistName);
        schedule =view.findViewById(R.id.therapistSchedule);
        tasks =view.findViewById(R.id.therapistTasks);
        preferences =view.findViewById(R.id.therapistPreferences);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("Login_Session", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        if(!(username.isEmpty())){
            therapistUsername.setText(username);
        }
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
        tasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delegate.callTherapistTasks();
            }
        });
        preferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delegate.viewTherapistPreference();
            }
        });

        return view;
    }

}
