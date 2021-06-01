package com.A10.instablind;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MotionEvent;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class ToggleModeActivity extends AppCompatActivity {

    private int count = 0;
    private long startMillis=0;
    private TextToSpeech textToSpeech;
    private static boolean blindMode = true;
    private boolean ready = false;

    public static boolean isBlindMode() {
        return blindMode;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toggle_mode);
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.US);
                }
                //Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
                Log.println(Log.WARN, "TextToSpeech", "failed to init tts");
            }
        });
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toggleDialogue();  //speak after 2000ms
            }
        }, 2000);

    }

    private void toggleDialogue() {
        String toSpeak = "Tap the screen five times in four seconds to toggle voice commands on, " +
                "or tap once and wait for four seconds to toggle commands off";
        textToSpeech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, "toggle");
        ready = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean intentReady = false;
        if (!ready) return false;
        int eventaction = event.getAction();
        if (eventaction == MotionEvent.ACTION_UP) {

            //get system current milliseconds
            long time= System.currentTimeMillis();


            //if it is the first time, or if it has been more than 3 seconds since the first tap ( so it is like a new try), we reset everything
            if (startMillis==0){
                count = 1;
                startMillis = time;
            }
            else if ((time-startMillis> 4000) ) {
                blindMode = false;
                intentReady = true;
            }
            //it is not the first, and it has been  less than 3 seconds since the first
            else{ //  time-startMillis< 4000
                count++;
            }

            if (count==5) {
                blindMode = true;
                intentReady = true;
            }
            if (intentReady) {
                Intent startIntent = new Intent(ToggleModeActivity.this , StartActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("blindMode", blindMode);
                startIntent.putExtras(bundle);
                startActivity(startIntent);
            }
            return true;
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (textToSpeech != null) {
            textToSpeech.stop();
        }
    }
}
