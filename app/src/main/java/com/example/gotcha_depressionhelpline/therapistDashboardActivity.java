package com.example.gotcha_depressionhelpline;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.onesignal.OSNotification;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;

public class therapistDashboardActivity extends AppCompatActivity  implements therapistOnclicks {

    String key;
    ChipNavigationBar navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_therapist_dashboard);
        Firebase.setAndroidContext(this);
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .setNotificationReceivedHandler(new therapistDashboardActivity.ExampleNotificationReceivedHandler())
                .setNotificationOpenedHandler(new therapistDashboardActivity.ExampleNotificationOpenedHandler())
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        SharedPreferences sharedPreferences = getSharedPreferences("Login_Session", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        OneSignal.sendTag("User_ID",username );
        getSupportActionBar().setTitle("Gotcha'- Depression Helpline");

        loadFragment(new therapistHomeFragment());

        navView = findViewById(R.id.nav_view);
        navView.setItemSelected(R.id.navigation_home,true);
        navView.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i){
                    case R.id.navigation_home:
                        fragment = new therapistHomeFragment();
                        break;
                    case R.id.navigation_excercise:
                        fragment = new therapistTasksFragment();
                        break;
                    case R.id.navigation_profile:
                        fragment = new profileFragment();
                        break;
                }


                loadFragment(fragment);
            }
        });

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
            editor.remove("username");
            editor.remove("TherapistPreferences");
            editor.commit();

            FirebaseAuth.getInstance().signOut();
            finish();
            Intent intent=new Intent(therapistDashboardActivity.this, frontPage.class);
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

    @Override
    public void callSchedule() {
        Intent intent=new Intent(therapistDashboardActivity.this,therapistScheduleActivity.class);
        startActivity(intent);
    }

    @Override
    public void viewSessionReports() {

        Intent intent=new Intent(therapistDashboardActivity.this,viewReportsActivity.class);
        startActivity(intent);
    }

    @Override
    public void callTherapistTasks() {
        navView.setItemSelected(R.id.navigation_excercise,true);
        Fragment fragment = new therapistTasksFragment();
        loadFragment(fragment);
    }

    @Override
    public void viewTherapistPreference() {

        Intent intent=new Intent(therapistDashboardActivity.this,therapistPreferences.class);
        startActivity(intent);
    }

    private class ExampleNotificationReceivedHandler implements OneSignal.NotificationReceivedHandler {
        @Override
        public void notificationReceived(OSNotification notification) {
            JSONObject data = notification.payload.additionalData;
            String notificationID = notification.payload.notificationID;
            String title = notification.payload.title;
            String body = notification.payload.body;


            Log.i("OneSignalExample", "NotificationID received: " + notificationID);
            if(body.equals("We have recieved complain against your behaviour. You are warn to rectify your behaviour, if you want to continue our service")){
                key="Warning";

            }
            else if(body.equals("Your Account has been authenticated")){
                key="preference";
            }
            else{
                SharedPreferences sharedPreferences = getSharedPreferences("Login_Session", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Token", "disabled");
                editor.apply();
                key="Blocked";
            }

        }
    }

    private class ExampleNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
        // This fires when a notification is opened by tapping on it.
        @Override
        public void notificationOpened(OSNotificationOpenResult result) {

            Object activityToLaunch = blockActivity.class;

            if (key.equals("Blocked")) {
                Intent intent = new Intent(getApplicationContext(), (Class<?>) activityToLaunch);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            else if(key.equals("Warning") ){
                Intent intent = new Intent(getApplicationContext(), therapistDashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            else {
                Intent intent = new Intent(getApplicationContext(), therapistPreferences.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }
}

