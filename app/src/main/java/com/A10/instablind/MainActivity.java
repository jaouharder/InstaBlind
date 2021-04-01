package com.A10.instablind;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {
    private Button login;
    private Button next;
    private EditText username;
    private EditText password;
    private SignInButton signin;
    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signin=findViewById(R.id.sign_in_button);
        signin.setOnClickListener(v -> {
            switch (v.getId()) {
                case R.id.sign_in_button:
                    signIn();
                    break;
                // ...
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            UpdateUI();
            next =(Button)findViewById(R.id.next);
            next.setOnClickListener(v -> {
                if(v.getId()==R.id.next){
                    Log.d("success","button recognized");
                    //validation de password
                    EditText passwordtxt=findViewById(R.id.editTextTextPassword2);
                    Log.d("pass_saisie",passwordtxt.getText().toString());
                    if(passwordtxt.getText().toString().equals("fuckoff3")){
                        Log.d("success","password fine");
                        //go to home page
                        Intent intent=new Intent(this,HomePage.class);
                        intent.putExtra("password",passwordtxt.getText().toString());
                        startActivity(intent);
                    }
                    else Log.d("failure","password  not fine");




                }
            });




        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Error", "signInResult:failed code=" + e.getStatusCode());

        }
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
    private void UpdateUI(){
        LinearLayout layout=findViewById(R.id.NewPassword);
        LinearLayout loginform=findViewById(R.id.LoginForm);
        signin.setVisibility(View.INVISIBLE);
        loginform.setVisibility(View.INVISIBLE);
        layout.setVisibility(View.VISIBLE);
    }
}