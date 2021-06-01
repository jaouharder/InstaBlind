package com.A10.instablind;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button login;
    private TextView registerUser;

    private FirebaseAuth mAuth;

    private TextToSpeech textToSpeech;
    private int field = 0;
    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;
    private boolean blindMode = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        registerUser = findViewById(R.id.register_user);

        mAuth = FirebaseAuth.getInstance();

        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this , RegisterActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();

                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText(LoginActivity.this, "Empty Credentials!", Toast.LENGTH_SHORT).show();
                } else {
                    loginUser(txt_email , txt_password);
                }
            }
        });

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.US);
                }
            }
        });

        Bundle bundle = getIntent().getExtras();
        blindMode = true;
        if (bundle != null)
            blindMode = bundle.getBoolean("blindMode");
        Log.println(Log.ERROR, "BLIND", String.valueOf(blindMode));
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.US);

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {
                Log.println(Log.ERROR, "speechRecognizer", "Listening!");
            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                String speechResult = data.get(0);
                Log.println(Log.ERROR, "VOICE =", speechResult);
                processLoginResults(speechResult);
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loginDialogue();  //speak after 2000ms
            }
        }, 2000);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!blindMode) {
            return true;
        }
        int eventaction = event.getAction();
        if (eventaction == MotionEvent.ACTION_DOWN) {
            Log.println(Log.ERROR, "SPEAKING", "SPEAKING");
            speechRecognizer.startListening(speechRecognizerIntent);
            return true;
        }
        else if (eventaction == MotionEvent.ACTION_UP) {
            speechRecognizer.stopListening();
            return true;
        }
        return true;
    }


    private void processLoginResults(String speechResult) {
        switch (field) {
            case 1:
                password.setText(speechResult);
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();

                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText(LoginActivity.this, "Empty Credentials!", Toast.LENGTH_SHORT).show();
                    loginDialogue();
                    field = 0;
                } else {
                    loginUser(txt_email , txt_password);
                }

                return;
            case 0:
                email.setText(speechResult);
                field = 1;
                loginDialogue();
                return;
        }
    }

    private void loginDialogue() {
        String toSpeak = "";
        if (field == 0) {
            toSpeak = "What is your email?";
        }
        if (field == 1) {
            toSpeak = "What is your password?";
        }
        textToSpeech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, "toggle");
    }
//        textToSpeech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, "toggle");

    private void loginUser(String email, String password) {

        mAuth.signInWithEmailAndPassword(email , password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Update the profile " +
                            "for better expereince", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this , MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (textToSpeech != null) {
            textToSpeech.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        speechRecognizer.destroy();
    }

}
