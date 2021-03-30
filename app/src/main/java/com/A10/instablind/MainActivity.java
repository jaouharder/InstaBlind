package com.A10.instablind;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Button login;
    private EditText username;
    private EditText password;
    private TextView signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }



    public  void LoginHandler(View v){

         username=findViewById(R.id.username);
         password=findViewById(R.id.password);
         String usernameInput=username.getText().toString();
         String passwordInput=password.getText().toString();

         //##################Login Logic Part#################################
         if(usernameInput.equals("azerty") && passwordInput.equals("23"))   Log.d("success","u have logged in");
         else Log.d("failure","u have not logged in");

    }
}