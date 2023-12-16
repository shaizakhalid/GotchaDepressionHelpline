package com.example.gotcha_depressionhelpline;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import static android.content.Context.MODE_PRIVATE;

public class profileFragment extends Fragment{


    public profileFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_profile, container, false);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("Login_Session", MODE_PRIVATE);
        String token=sharedPreferences.getString("Token","");


        LinearLayout linearLayoutAboutme= view.findViewById(R.id.aboutmell);
        linearLayoutAboutme.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent i=new Intent(getContext(),aboutMeActivity.class);
                startActivity(i);
            }
        });
        LinearLayout linearLayoutFeedback= view.findViewById(R.id.feedbackll);

        if(token.equals("Admin")){
        linearLayoutFeedback.setVisibility(View.GONE);
        }

        linearLayoutFeedback.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getContext(),feedbackActivity.class);
                startActivity(i);
            }
        });
        LinearLayout linearLayoutDisclaimer= view.findViewById(R.id.disclaimerll);
        linearLayoutDisclaimer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getContext(),disclaimerActivity.class);
                startActivity(i);
            }
        });
        LinearLayout linearLayoutprivacy= view.findViewById(R.id.privacyPolicyll);
        linearLayoutprivacy.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent i=new Intent(getContext(),PrivacyPolicyActivity.class);
                startActivity(i);
            }
        });
        LinearLayout linearLayoutTermsandConditions= view.findViewById(R.id.terms_and_conditionsll);
        linearLayoutTermsandConditions.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent i=new Intent(getContext(),TermsAndCondition.class);
                startActivity(i);
            }
        });
        return view;
    }
}
