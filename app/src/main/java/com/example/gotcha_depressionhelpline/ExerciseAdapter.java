package com.example.gotcha_depressionhelpline;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;


public class ExerciseAdapter extends ArrayAdapter<Exercise> {

        private Context mContext;
        private List<Exercise> exerciseList;

        public ExerciseAdapter(Context context, ArrayList<Exercise> list) {
            super(context, 0 , list);
            mContext = context;
            exerciseList = list;
        }

    public ExerciseAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }


    @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View listItem = convertView;

            if(listItem == null) {
                listItem = LayoutInflater.from(mContext).inflate(R.layout.list_item,parent,false);
            }

            Exercise currentContact = exerciseList.get(position);

            TextView name = listItem.findViewById(R.id.textView_name);
            name.setText(currentContact.getexerciseName());

            TextView description = listItem.findViewById(R.id.textView_Phone);
            description.setText(currentContact.getexerciseDescription());

        ImageView img = listItem.findViewById(R.id.imageView_userimg);
        img.setImageResource(currentContact.geteImage());
            return listItem;
        }
    }