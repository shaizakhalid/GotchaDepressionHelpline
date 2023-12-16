package com.example.gotcha_depressionhelpline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.onesignal.OneSignal;

public class AdminDashboard extends AppCompatActivity implements onclicks{


    String type;
    ChipNavigationBar navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        OneSignal.sendTag("User_ID", "admin1234");


        getSupportActionBar().setTitle("Gotcha'- Depression Helpline");
        loadFragment(new adminHomeFragment());


         navView = findViewById(R.id.nav_view);
        navView.setItemSelected(R.id.navigation_home,true);
        navView.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i){
                    case R.id.navigation_home:
                        fragment = new adminHomeFragment();
                        break;
                    case R.id.navigation_excercise:
                        fragment = new adminTasksFragment();
                        break;
                    case R.id.navigation_profile:
                        fragment = new profileFragment();
                        break;
                        }
                loadFragment(fragment);
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id=item.getItemId();
        if(id == R.id.logout)
        {
            SharedPreferences sharedPreferences=getSharedPreferences("Login_Session", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.remove("Token");
            editor.commit();

            FirebaseAuth.getInstance().signOut();
            finish();
            Intent intent=new Intent(AdminDashboard.this, frontPage.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        else if(id == R.id.updatePassword){
            Intent i=new Intent(this,updatePasswordActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
    private boolean loadFragment(Fragment fragment){
        if(fragment!=null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .commit();
            return true;
        }
        return false;
    }
    @Override
    public void callAdmintasks() {
        navView.setItemSelected(R.id.navigation_excercise,true);
        Fragment fragment = new adminTasksFragment();
        loadFragment(fragment);
    }

    @Override
    public void callTherapistRequests() {
        type="TherapistRequests";
        Intent i=new Intent(this,authenticationActivity.class);
        i.putExtra("type",type);
        startActivity(i);
    }

    @Override
    public void callPatientRecord() {
        type="Patient";
        Intent i=new Intent(this,showRecordActivity.class);
        i.putExtra("type",type);
        startActivity(i);
    }

    @Override
    public void viewSessionReports() {
        type="Patient";
        Intent i=new Intent(this,viewReportsActivity.class);
        i.putExtra("type",type);
        startActivity(i);
    }

    @Override
    public void callTherapistRecord() {
        type="Therapist";
        Intent i=new Intent(this,showRecordActivity.class);
        i.putExtra("type",type);
        startActivity(i);

    }

    @Override
    public void viewFeedbackComplains() {
        Intent i=new Intent(this,viewfeedbackcomplainactivity.class);
        i.putExtra("type",type);
        startActivity(i);
    }

}
