package com.example.gotcha_depressionhelpline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.gotcha_depressionhelpline.Helper.AppPermission;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {

    FirebaseUser user;
    View whiteBlock_View;
    String token;
    String therapistType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);

        whiteBlock_View= findViewById(R.id.whiteblock);
        /*
         *  If the user is running Android 6.0 (API level 23) or later,
         *  the user has to grant your app its permissions while they are running the app
         */

        // Condition to check if you current SDK is equal or greater than 23 (VERSION_CODES.M)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            runtimePermissions();
        }
        user= FirebaseAuth.getInstance().getCurrentUser();


        Thread myThread= new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    sleep(5000);

                }
                catch(Exception e){
                    e.printStackTrace();
                }

                finally{
                    SharedPreferences sharedPreferences=getSharedPreferences("Login_Session", Context.MODE_PRIVATE);
                    token=sharedPreferences.getString("Token","");
                    therapistType=sharedPreferences.getString("Request","-1");
                    if(token.isEmpty()) {
                            Intent i = new Intent(MainActivity.this, frontPage.class);
                            startActivity(i);
                    }
                    else{
                        if(token.equals("Admin")) {
                            Intent i = new Intent(MainActivity.this, AdminDashboard.class);
                            startActivity(i);
                        }
                        else if(token.equals("Patient")){

                            Intent intent=new Intent(MainActivity.this,patientDashboardActivity.class);
                            startActivity(intent);
                        }
                        else if (token.equals("Therapist")){
                            SharedPreferences prefSharedPreferences=getSharedPreferences("Login_Session", Context.MODE_PRIVATE);
                            String prefToken=prefSharedPreferences.getString("TherapistPreferences","");
                            if(prefToken.equals("done")){
                                Intent intent = new Intent(getApplicationContext(), therapistDashboardActivity.class);
                                startActivity(intent);
                            }
                            else{
                                Intent intent=new Intent(MainActivity.this,therapistPreferences.class);
                                startActivity(intent);
                            }

                        }
                        else if(token.equals("invalidTherapist")){
                            Intent i=new Intent(MainActivity.this,pendingTherapistRequestActivity.class);
                            startActivity(i);
                        }
                        else {
                            Intent intent=new Intent(MainActivity.this,blockActivity.class);
                            startActivity(intent);
                        }

                    }
                    finish();
                }

            }
        });
        myThread.start();

        YoYo.with(Techniques.FadeInLeft)
                .duration(5000)
                .repeat(1)
                .playOn(findViewById(R.id.appWelcomeText));

        YoYo.with(Techniques.FadeInDown)
                .duration(5000)
                .repeat(1)
                .playOn(findViewById(R.id.whiteblock));

        Animation aniSlide = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.myanimation);
        whiteBlock_View.startAnimation(aniSlide);

    }

//    public void callother(){
//        if(token.equals("Patient")){
//
//            Intent intent=new Intent(MainActivity.this,patientDashboardActivity.class);
//            startActivity(intent);
//        }
//        else if(token.equals("Therapist")){
//            if(therapistType.equals("invalidTherapist")){
//                final String userid=user.getUid();
//                titleRef= new Firebase("https://gotcha-depression-helpline.firebaseio.com/Users/"
//                        +userid+"/title");
//                titleRef.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        String title = dataSnapshot.getValue(String.class);
//                        if(title.equals("therapist")){
//                            SharedPreferences sharedPreferences= getSharedPreferences("Login_Session",MODE_PRIVATE);
//                            SharedPreferences.Editor editor=sharedPreferences.edit();
//                            editor.putString("Token","Therapist");
//                            editor.apply();
//
//                            SharedPreferences sharedPreferences1=getSharedPreferences("Login_Session", Context.MODE_PRIVATE);
//                            String pref=sharedPreferences1.getString("TherapistPreferences","");
//                            if(pref.equals("done")){
//                                Intent i = new Intent(MainActivity.this, therapistDashboardActivity.class);
//                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
//                                        | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                                startActivity(i);
//                            }
//                            else {
//                                final String userid=user.getUid();
//                                titleRef= new Firebase("https://gotcha-depression-helpline.firebaseio.com/Users/"
//                                        +userid+"/username");
//                                titleRef.addValueEventListener(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(DataSnapshot dataSnapshot) {
//                                        String username = dataSnapshot.getValue(String.class);
//                                        Query usernameQuery = FirebaseDatabase.getInstance().getReference().child("TherapistPreferences").orderByChild("username").equalTo(username);
//                                        usernameQuery.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
//                                            @Override
//                                            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
//                                                if (dataSnapshot.getChildrenCount() > 0) {
//                                                    Intent i = new Intent(MainActivity.this, therapistDashboardActivity.class);
//                                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
//                                                            | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                                                    startActivity(i);
//                                                } else {
//                                                    Intent i = new Intent(MainActivity.this, therapistPreferences.class);
//                                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
//                                                            | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                                                    startActivity(i);
//                                                }
//                                            }
//
//                                            @Override
//                                            public void onCancelled(@NonNull DatabaseError databaseError) {
//                                                Toast.makeText(MainActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//                                            }
//                                        });
//                                    }
//                                    @Override
//                                    public void onCancelled(FirebaseError firebaseError) {
//                                        Toast.makeText(MainActivity.this, firebaseError.getMessage(), Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//                            }
//                        }
//                        else {
//                            Intent i = new Intent(MainActivity.this, pendingTherapistRequestActivity.class);
//                            startActivity(i);
//
//                        }
//                    }
//                    @Override
//                    public void onCancelled(FirebaseError firebaseError) {
//                        Toast.makeText(MainActivity.this, firebaseError.getMessage(), Toast.LENGTH_SHORT).show();
//
//                    }});
//            }
//            else if(therapistType.equals("requesting")){
//                Intent i = new Intent(MainActivity.this, therapistRequestActivity.class);
//                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
//                        | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                startActivity(i);
//            }
//            else{
//                Intent i = new Intent(MainActivity.this, therapistDashboardActivity.class);
//                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
//                        | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                startActivity(i);
//            }
//        }
//        else
//        {
//            Intent intent=new Intent(MainActivity.this,AdminDashboard.class);
//            startActivity(intent);
//        }
//    }
    /*
     * Runtime permission asking mechanism.
     */
    private void runtimePermissions() {

        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.RECORD_AUDIO)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[] { Manifest.permission.RECORD_AUDIO },
                        AppPermission.RECORD_AUDIO_PERMISSION);

                /* AppPermission.RECORD_AUDIO_PERMISSION is an
                 * app-defined int constant. The callback method gets the
                 * result of the request.
                 */
            }
        } else {
            // Permission has already been granted
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case AppPermission.RECORD_AUDIO_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // RECORD_AUDIO related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
}
