package com.example.gotcha_depressionhelpline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.onesignal.OSNotification;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;

public class pendingTherapistRequestActivity extends AppCompatActivity {

    private Firebase titleRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_therapist_request);

        Firebase.setAndroidContext(this);
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .setNotificationReceivedHandler(new pendingTherapistRequestActivity.ExampleNotificationReceivedHandler())
                .setNotificationOpenedHandler(new pendingTherapistRequestActivity.ExampleNotificationOpenedHandler())
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
        Intent i=getIntent();
        String username=i.getStringExtra("username");
        OneSignal.sendTag("User_ID",username);

    }
    private class ExampleNotificationReceivedHandler implements OneSignal.NotificationReceivedHandler {
        @Override
        public void notificationReceived(OSNotification notification) {
            JSONObject data = notification.payload.additionalData;
            String notificationID = notification.payload.notificationID;
            String body = notification.payload.body;

            Log.i("OneSignalExample", "NotificationID received: " + notificationID);

            SharedPreferences sharedPreferences = getSharedPreferences("Login_Session", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("Token", "Therapist");
            editor.apply();

        }
    }


    private class ExampleNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
        // This fires when a notification is opened by tapping on it.
        @Override
        public void notificationOpened(OSNotificationOpenResult result) {
            Intent intent = new Intent(getApplicationContext(), therapistPreferences.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }
    }
}

