package com.example.gotcha_depressionhelpline;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.onesignal.OSNotification;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;
import com.sinch.android.rtc.SinchError;

public class patientDashboardActivity extends BaseActivity
        implements  patientOnclicks,SinchService.StartFailedListener, ServiceConnection{
    String username;
    String key;
    ChipNavigationBar navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_dashboard);

        if (ContextCompat.checkSelfPermission(patientDashboardActivity.this,
                android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(patientDashboardActivity.this,
                        android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(patientDashboardActivity.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(patientDashboardActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(patientDashboardActivity.this,
                    new String[]{android.Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_PHONE_STATE,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }

        Firebase.setAndroidContext(this);

        SharedPreferences sharedPreferences = getSharedPreferences("Login_Session", Context.MODE_PRIVATE);
         username = sharedPreferences.getString("username", "");

        OneSignal.startInit(this)
                .setNotificationReceivedHandler(new ExampleNotificationReceivedHandler())
                .setNotificationOpenedHandler(new ExampleNotificationOpenedHandler())
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
        OneSignal.sendTag("User_ID", username);

        getSupportActionBar().setTitle("Gotcha'- Depression Helpline");
        loadFragment(new patientHomeFragment());


        navView = findViewById(R.id.nav_view);
        navView.setItemSelected(R.id.navigation_home,true);
        navView.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i){
                    case R.id.navigation_home:
                        fragment = new patientHomeFragment();
                        break;
                    case R.id.navigation_excercise:
                        fragment = new patientTasksFragment();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.logout) {
            SharedPreferences sharedPreferences = getSharedPreferences("Login_Session", Context.MODE_PRIVATE);
            SharedPreferences sharedPreferences1 = getSharedPreferences("Depression_Result", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            SharedPreferences.Editor editor2 = sharedPreferences1.edit();

            editor.remove("Token");
            editor.remove("patientType");
            editor2.remove("Depression_Level");

            editor.commit();
            editor2.commit();

            FirebaseAuth.getInstance().signOut();
            finish();
            Intent intent = new Intent(patientDashboardActivity.this, frontPage.class);
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

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void callExercises() {
        try {
            Intent nav = new Intent(patientDashboardActivity.this, ExerciseMainPage.class);
            startActivity(nav);
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void callTherapySessions() {
        Intent i = new Intent(patientDashboardActivity.this, sessionDetailsActivity.class);
        startActivity(i);
    }

    @Override
    public void callMytasks() {
        navView.setItemSelected(R.id.navigation_excercise,true);
        Fragment fragment = new patientTasksFragment();
        loadFragment(fragment);
    }

    @Override
    public void callAssessmentDetails() {
        Intent i = new Intent(patientDashboardActivity.this, assessmentDetailsActivity.class);
        startActivity(i);
    }


    @Override
    protected void onServiceConnected() {
        getSinchServiceInterface().setStartListener(this);
        getSinchServiceInterface().startClient(username);
    }

    @Override
    public void onStartFailed(SinchError error) {

    }

    @Override
    public void onStarted() {

    }

    private class ExampleNotificationReceivedHandler implements OneSignal.NotificationReceivedHandler {
        @Override
        public void notificationReceived(OSNotification notification) {
//            JSONObject data = notification.payload.additionalData;
            String notificationID = notification.payload.notificationID;
//            String title = notification.payload.title;
            String body = notification.payload.body;
            if(body.equals("Your therapist recommended you to take another session")){
                key="Session";
                SharedPreferences sharedPreferences = getSharedPreferences("Login_Session", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("AnotherSession", "Yes");
                editor.apply();
            }
            else if(body.equals("Your Account has been Blocked")) {
                key="Blocked";
                SharedPreferences sharedPreferences = getSharedPreferences("Login_Session", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Token", "disabled");
                editor.apply();

            }
            else{
                key="Warning";
            }

            getApplicationContext().bindService(new Intent(getApplicationContext(), SinchService.class), (ServiceConnection) this, Context.BIND_AUTO_CREATE);
            Log.i("OneSignalExample", "NotificationID received: " + notificationID);



        }
    }


    private class ExampleNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
        // This fires when a notification is opened by tapping on it.
        @Override
        public void notificationOpened(OSNotificationOpenResult result) {

            Object activityToLaunch = blockActivity.class;

            if(key.equals("Blocked")) {
                Intent intent = new Intent(getApplicationContext(), (Class<?>) activityToLaunch);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            else if(key.equals("Session")){
                Intent intent = new Intent(getApplicationContext(), BookingForClinicalTreatment.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            else{
                Intent intent = new Intent(getApplicationContext(), patientDashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }


            // The following can be used to open an Activity of your choice.
            // Replace - getApplicationContext() - with any Android Context.
            // Intent intent = new Intent(getApplicationContext(), YourActivity.class);
//            Intent intent = new Intent(getApplicationContext(), (Class<?>) activityToLaunch);
//
            // intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            // startActivity(intent);
//            startActivity(intent);

            // Add the following to your AndroidManifest.xml to prevent the launching of your main Activity
            //   if you are calling startActivity above.
        /*
           <application ...>
             <meta-data android:name="com.onesignal.NotificationOpened.DEFAULT" android:value="DISABLE" />
           </application>
        */
        }
    }

}
