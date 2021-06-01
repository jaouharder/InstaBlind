package com.A10.instablind;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {

    private EditText username;
    private EditText name;
    private EditText email;
    private EditText password;
    private Button register;
    private TextView loginUser;

    private DatabaseReference mRootRef;
    private FirebaseAuth mAuth;

    private TextToSpeech textToSpeech;
    private int field = 0;
    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;
    private boolean blindMode = true;


    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.username);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);
        loginUser = findViewById(R.id.login_user);

        mRootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        pd = new ProgressDialog(this);

        loginUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this , LoginActivity.class));
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtUsername = username.getText().toString();
                String txtName = name.getText().toString();
                String txtEmail = email.getText().toString();
                String txtPassword = password.getText().toString();

                if (TextUtils.isEmpty(txtUsername) || TextUtils.isEmpty(txtName)
                        || TextUtils.isEmpty(txtEmail) || TextUtils.isEmpty(txtPassword)){
                    Toast.makeText(RegisterActivity.this, "Empty credentials!", Toast.LENGTH_SHORT).show();
                } else if (txtPassword.length() < 6){
                    Toast.makeText(RegisterActivity.this, "Password too short!", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(txtUsername , txtName , txtEmail , txtPassword);
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
                processRegisterResults(speechResult);
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
                registerDialogue();  //speak after 2000ms
            }
        }, 2000);

    }

    private void processRegisterResults(String speechResult) {
        if (field == 0) {
            username.setText(speechResult);
            field += 1;
            registerDialogue();
        }
        else if (field == 1) {
            name.setText(speechResult);
            field += 1;
            registerDialogue();
        }
        else if (field == 2) {
            email.setText(speechResult);
            field += 1;
            registerDialogue();
        }
        else if (field == 3) {
            password.setText(speechResult);
            String txtUsername = username.getText().toString();
            String txtName = name.getText().toString();
            String txtEmail = email.getText().toString();
            String txtPassword = password.getText().toString();

            if (TextUtils.isEmpty(txtUsername) || TextUtils.isEmpty(txtName)
                    || TextUtils.isEmpty(txtEmail) || TextUtils.isEmpty(txtPassword)){
                Toast.makeText(RegisterActivity.this, "Empty credentials!", Toast.LENGTH_SHORT).show();
            } else if (txtPassword.length() < 6){
                Toast.makeText(RegisterActivity.this, "Password too short!", Toast.LENGTH_SHORT).show();
            } else {
                registerUser(txtUsername , txtName , txtEmail , txtPassword);
            }
        }
    }


    private void registerDialogue() {
        String toSpeak = "";
        if (field == 0) {
            toSpeak = "What is your username?";
        }
        else if (field == 1) {
            toSpeak = "What is your name?";
        }
        else if (field == 2) {
            toSpeak = "What is your email?";
        }
        else if (field == 3) {
            toSpeak = "What is your password?";
        }
        textToSpeech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, "toggle");

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


    private void registerUser(final String username, final String name, final String email, String password) {

        pd.setMessage("Please Wail!");
        pd.show();

        mAuth.createUserWithEmailAndPassword(email , password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                HashMap<String , Object> map = new HashMap<>();
                map.put("name" , name);
                map.put("email", email);
                map.put("username" , username);
                map.put("id" , mAuth.getCurrentUser().getUid());
                map.put("bio" , "");
                map.put("imageurl" , "default");

                mRootRef.child("Users").child(mAuth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            pd.dismiss();
                            Toast.makeText(RegisterActivity.this, "Update the profile " +
                                    "for better expereince", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this , MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
