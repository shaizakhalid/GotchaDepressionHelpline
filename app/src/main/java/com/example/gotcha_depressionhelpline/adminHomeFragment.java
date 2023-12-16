package com.example.gotcha_depressionhelpline;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
interface onclicks{
     void callAdmintasks();
     void callTherapistRequests();
     void callPatientRecord();
     void callTherapistRecord();
     void viewFeedbackComplains();
     void viewSessionReports();
}

public class adminHomeFragment extends Fragment {

    CardView adminTasks,therapistRequest,patientRecord,therapistRecord;
    onclicks delegate;
    public adminHomeFragment() {
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
        View view= inflater.inflate(R.layout.fragment_admin_home, container, false);
        adminTasks =view.findViewById(R.id.adminTasks);
        patientRecord =view.findViewById(R.id.patientReord);
        therapistRecord =view.findViewById(R.id.therapistRecord);
        therapistRequest =view.findViewById(R.id.therapistRequest);

        adminTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               delegate.callAdmintasks();
            }
        });
        patientRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delegate.callPatientRecord();
            }
        });
        therapistRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delegate.callTherapistRecord();
            }
        });
        therapistRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delegate.callTherapistRequests();
            }
        });

        return view;
    }
}
