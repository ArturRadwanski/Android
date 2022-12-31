package com.example.artur001;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private LinearLayout CameraButton;
    private LinearLayout AlbumsButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 100);

        this.CameraButton = findViewById(R.id.camera);
        CameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//jeśli jest dostępny aparat
                if (intent.resolveActivity(getPackageManager()) != null) {
                    Log.d("tewst", "onClick: ");
                    startActivityForResult(intent, 200); // 200 - stała wartość, która później posłuży do identyfikacji tej akcji
                }
            }
        });

        this.AlbumsButton = findViewById(R.id.albums);
        AlbumsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AlbumsActivity.class);
                intent.putExtra("directory", "main");
                startActivity(intent);
            }
        });



    }
    public void checkPermission(String permission, int requestCode) {
        // jeśli nie jest przyznane to zażądaj
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
        } else {
            Toast.makeText(MainActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //
        switch (requestCode) {
            case 100:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("xasdasd", "onRequestPermissionsResult: true");

                } else {
                    Log.d("xasdfas", "onRequestPermissionsResult: false");
                }
                break;
            case 101 :

                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data); Log.d("tewst", "onActivityResult: ");
        Log.d("tewst", "onActivityResult: ");
            Bundle extras = data.getExtras();
            Bitmap b = (Bitmap) extras.get("data");
            Log.d("tewst", String.valueOf(b.getHeight()));
            Log.d("tewst", String.valueOf(b.getWidth()));
        Log.d("tewst", "onActivityResult: ");
            ImageView iv = findViewById(R.id.testPhoto);
            iv.setImageBitmap(b);
    }

//    @Override
//    protected void onActivityResupt(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(resultCode == 200){
//            Log.d("tewst", "onActivityResult: ");
//            Bundle extras = data.getExtras();
//            Bitmap b = (Bitmap) extras.get("data");
//            Log.d("tewst", String.valueOf(b.getHeight()));
//            Log.d("tewst", String.valueOf(b.getWidth()));
//            ImageView iv = findViewById(R.id.testPhoto);
//            iv.setImageBitmap(b);
//        }
//    }
}