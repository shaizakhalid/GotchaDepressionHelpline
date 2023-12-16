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

public class ShowRecordCustomListAdapter extends ArrayAdapter<User> {

    private Context mContext;
    private List<User> preferenceList = new ArrayList<>();
    public ShowRecordCustomListAdapter(@NonNull Context context, ArrayList<User> list) {
        super(context, 0 , list);
        mContext = context;
        preferenceList = list;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.show_record_list, parent, false);
        }
        User current = preferenceList.get(position);
        TextView username = listItem.findViewById(R.id.usernameTextView);
        username.setText(current.getUsername());
        TextView name = listItem.findViewById(R.id.nameTextView);
        name.setText(current.getname());
        TextView gender = listItem.findViewById(R.id.genderTextView);
        gender.setText(current.getGender());
        TextView email = listItem.findViewById(R.id.emailTextView);
        email.setText(current.getEmail());
        return listItem;
    }
}