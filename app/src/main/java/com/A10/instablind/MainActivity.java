package com.A10.instablind;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    static int REQUESTCODE = 1;
    ImageView selectedImage;
    EditText DetectedDescription;

    static int PreqCode = 1;
    Uri pickedImgUri;
    private boolean BlindIsTrue = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DetectedDescription = findViewById(R.id.DetectedDescription);
        selectedImage=findViewById(R.id.image_added);
        if(BlindIsTrue){
            speak();
        }

        selectedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= 22){

                checkAndRequestForPermission();
                }
                else {
                    openGallery();
                }
            }
        });

    }

    private void speak() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        try{
            startActivityForResult(intent,REQUEST_CODE_SPEECH_INPUT);

        }catch (Exception e){
            Toast.makeText(this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESTCODE);
    }

    private void checkAndRequestForPermission() {
         if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
             if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                 Toast.makeText(MainActivity.this,"If you want to add image you should give us permission to access your gallery",Toast.LENGTH_SHORT
                 ).show();
             }
             else{
                 ActivityCompat.requestPermissions(MainActivity.this,
                         new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                 PreqCode);

             }
         }
         else openGallery();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == REQUESTCODE && data !=null){
            pickedImgUri = data.getData();
            selectedImage.setImageURI(pickedImgUri);

        }
        switch (requestCode){
            case REQUEST_CODE_SPEECH_INPUT:{
                if (resultCode == RESULT_OK && null !=data){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    DetectedDescription.setText(result.get(0));
                }
            }
        }
    }
}