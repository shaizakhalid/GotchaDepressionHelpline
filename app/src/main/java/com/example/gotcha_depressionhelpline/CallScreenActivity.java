package com.example.gotcha_depressionhelpline;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallEndCause;
import com.sinch.android.rtc.calling.CallListener;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class CallScreenActivity extends BaseActivity {

    private AudioPlayer mAudioPlayer;
    private Timer mTimer;
    private UpdateCallDurationTask mDurationTask;
    String deplevel;

    private String mCallId,start,end;
    private long mCallStart = 0;

    public String receiver;
    public String caller,key;

    private final String TAG = "JSA-FCM";
    private TextView mCallDuration;
    private TextView mCallState;
    private TextView mCallerName;
    boolean callEstablished=false;

    private class UpdateCallDurationTask extends TimerTask {

        @Override
        public void run() {
            CallScreenActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateCallDuration();
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.callscreen);

        mAudioPlayer = new AudioPlayer(this);
        mCallDuration =  findViewById(R.id.callDuration);
        mCallerName = findViewById(R.id.remoteUser);
        mCallState = findViewById(R.id.callState);
        Button endCallButton =  findViewById(R.id.hangupButton);

        endCallButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                endCall();
            }
        });
        mCallStart = System.currentTimeMillis();
        mCallId = getIntent().getStringExtra(SinchService.CALL_ID);

        Intent intent = getIntent();
        mCallId = intent.getStringExtra("CALL_ID");
        deplevel=intent.getStringExtra("DepLevel");
        receiver = intent.getStringExtra("RECEIVER_NAME");
        start=intent.getStringExtra("StartTime");
        end=intent.getStringExtra("EndTime");

        SharedPreferences sharedPreferences = getSharedPreferences("Login_Session", Context.MODE_PRIVATE);
        caller = sharedPreferences.getString("username", "");

    }

    @Override
    public void onServiceConnected() {
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            call.addCallListener(new SinchCallListener());
            mCallerName.setText(receiver);
            mCallState.setText(call.getState().toString());
        } else {
            Log.e(TAG, "Started with invalid callId, aborting.");
            finish();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mDurationTask.cancel();
        mTimer.cancel();
    }

    @Override
    public void onResume() {
        super.onResume();
        mTimer = new Timer();
        mDurationTask = new UpdateCallDurationTask();
        mTimer.schedule(mDurationTask, 0, 500);
    }

    @Override
    public void onBackPressed() {
        // User should exit activity by ending call, not by going back.
        mAudioPlayer.stopProgressTone();
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            call.hangup();
        }
        finish();
    }

    private void endCall() {
        mAudioPlayer.stopProgressTone();
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            call.hangup();
        }
        finish();
    }

    private String formatTimespan(long timespan) {
        long totalSeconds = timespan / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        return String.format(Locale.US, "%02d:%02d", minutes, seconds);
    }

    private void updateCallDuration() {
        if (mCallStart > 0) {
            mCallDuration.setText(formatTimespan(System.currentTimeMillis() - mCallStart));
        }
    }

    private class SinchCallListener implements CallListener {

        @Override
        public void onCallEnded(Call call) {
            CallEndCause cause = call.getDetails().getEndCause();
            Log.d(TAG, "Call ended. Reason: " + cause.toString());
            mAudioPlayer.stopProgressTone();
            setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
            String endMsg = "Call ended: " + call.getDetails().toString();
            Toast.makeText(CallScreenActivity.this, endMsg, Toast.LENGTH_LONG).show();
            endCall();
            SharedPreferences sharedPreferences=getSharedPreferences("Login_Session", Context.MODE_PRIVATE);
            String token=sharedPreferences.getString("Token","");
            if(token.equals("Therapist")){
                if(callEstablished){
                    Intent i = new Intent(CallScreenActivity.this, reportWrittingActivity.class);
                    i.putExtra("patientUsername",receiver);
                    i.putExtra("startTime",start);
                    i.putExtra("endTime",end);
                    i.putExtra("deplevel",deplevel);
                    startActivity(i);

                }
            }
            else{
                SharedPreferences sharedPreferences2 = getSharedPreferences("Login_Session", MODE_PRIVATE);
                SharedPreferences.Editor editor2 = sharedPreferences2.edit();
                editor2.putString("AnotherSession", "No");
                editor2.apply();
            }
        }

        @Override
        public void onCallEstablished(Call call) {
            Log.d(TAG, "Call established");
            callEstablished=true;
            mAudioPlayer.stopProgressTone();
            mCallState.setText(call.getState().toString());
            mCallDuration.setVisibility(View.VISIBLE);
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
            mCallStart = System.currentTimeMillis();
        }

        @Override
        public void onCallProgressing(Call call) {
            Log.d(TAG, "Call progressing");
            mAudioPlayer.playProgressTone();
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {

        }
    }

}
