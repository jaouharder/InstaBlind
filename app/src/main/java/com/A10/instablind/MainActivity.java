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
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    static int REQUESTCODE = 1;
    ImageView selectedImage;
    static int PreqCode = 1;
    Uri pickedImgUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selectedImage=findViewById(R.id.image_added);
        selectedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >=22){

                checkAndRequestForPermission();
                }
                else {
                    openGallery();
                }
            }
        });

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
    }
}