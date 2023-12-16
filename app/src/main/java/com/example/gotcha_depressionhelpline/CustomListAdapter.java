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
import androidx.annotation.PluralsRes;

import java.util.ArrayList;
import java.util.List;

class CustomListAdapter extends ArrayAdapter<Preferences> {

    private Context mContext;
    private List<Preferences> preferenceList = new ArrayList<>();
    String token;
    public CustomListAdapter(@NonNull Context context, ArrayList<Preferences> list) {
        super(context, 0 , list);
        mContext = context;
        preferenceList = list;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null){
            SharedPreferences sharedPreferences=getContext().getSharedPreferences("Login_Session", Context.MODE_PRIVATE);
            token=sharedPreferences.getString("Token","");
            if(token.equals("Patient")){
                listItem = LayoutInflater.from(mContext).inflate(R.layout.listrow,parent,false);
            }
            if(token.equals("Therapist")){
                listItem = LayoutInflater.from(mContext).inflate(R.layout.pending_sessions_row,parent,false);
            }

        }
        Preferences current = preferenceList.get(position);
        TextView username = listItem.findViewById(R.id.usernameTextView);
        username.setText(current.getUsername());
        TextView starttime = listItem.findViewById(R.id.startTime);
        starttime.setText(current.getStartTime());
        TextView endtime = listItem.findViewById(R.id.endTime);
        endtime.setText(current.getEndTime());
        if(token.equals("Therapist")){
            TextView sessionDate = listItem.findViewById(R.id.sessionDate);
            sessionDate.setText(current.getSessionDate());
        }
        return listItem;
    }
}

