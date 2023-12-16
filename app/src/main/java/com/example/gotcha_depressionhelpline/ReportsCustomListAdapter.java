package com.example.gotcha_depressionhelpline;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ReportsCustomListAdapter extends ArrayAdapter<Report> {

    private Context mContext;
    private List<Report> preferenceList;
    public ReportsCustomListAdapter(@NonNull Context context, ArrayList<Report> list) {
        super(context, 0 , list);
        mContext = context;
        preferenceList = list;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;

        SharedPreferences sharedPreferences1=getContext().getSharedPreferences("Login_Session", Context.MODE_PRIVATE);
        String token=sharedPreferences1.getString("Token","");
        if(listItem == null) {

            if(token.equals("Admin")){
                listItem = LayoutInflater.from(mContext).inflate(R.layout.view_report_listrow, parent, false);

            }
            else{
                listItem = LayoutInflater.from(mContext).inflate(R.layout.report_listrow, parent, false);

            }
        }



        if(token.equals("Admin")) {

            Report current = preferenceList.get(position);
            TextView username = listItem.findViewById(R.id.patientUsernameTextView);
            username.setText(current.getPatientName());
            TextView therapistusername = listItem.findViewById(R.id.therapistUsernameTextView);
            therapistusername.setText(current.getTherapistName());
            TextView date = listItem.findViewById(R.id.sessionDate);
            date.setText(current.getSessionDate());
            TextView statement = listItem.findViewById(R.id.reportStatement);
            statement.setText(current.getReportStatement());
        }
        else{
            Report current = preferenceList.get(position);
            TextView username = listItem.findViewById(R.id.patientUsernameTextView);
            username.setText(current.getPatientName());
            TextView date = listItem.findViewById(R.id.sessionDate);
            date.setText(current.getSessionDate());
            TextView statement = listItem.findViewById(R.id.reportStatement);
            statement.setText(current.getReportStatement());

        }
            return listItem;
    }
}