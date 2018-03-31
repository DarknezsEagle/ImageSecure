package com.example.vincent.imagesecurity;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.net.URI;
import java.util.logging.Logger;

import algorithm.AESAlgorithm;


public class EncryptImage extends AppCompatActivity {
    private ImageView ivPhoto;
    private EditText edtPwdPhoto;
    private EditText edtNamePhoto;
    private Button btnSave;
    private Button btnDecrypted;

    private String encryptPwd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encrypt_image);

        initInstances();

        initImage();


    }

    private void initImage() {
        Uri uri = getIntent().getExtras().getParcelable(LockImages.ADDING_IMAGE);

        ContentResolver cr = getContentResolver();


        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(cr, uri);
            ivPhoto.setImageBitmap(bitmap);
            Toast.makeText(getApplicationContext()
                    , uri.getPath(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initInstances() {
        ivPhoto = findViewById(R.id.ivPhoto);
        edtNamePhoto = findViewById(R.id.edtNamePhoto);
        edtNamePhoto.setText(getIntent().getExtras().get(LockImages.FILE_NAME).toString());
        edtPwdPhoto = findViewById(R.id.edtPasswordPhoto);
        btnSave = findViewById(R.id.btnSave);
        btnDecrypted = findViewById(R.id.btnDecrypted);

        btnSave.setOnClickListener(btnSaveOnClickListener);
        btnDecrypted.setOnClickListener(btnDecryptedOnClickListener);

    }

        View.OnClickListener btnSaveOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edtNamePhoto.getText().toString();
                String password = edtPwdPhoto.getText().toString();
                Log.wtf("Click Save","Name: " + name);
                Log.wtf("Click Save","DecryptImage: " + password);

                try {
                    encryptPwd = AESAlgorithm.encrypt(edtPwdPhoto.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.wtf("Check Algo","Encrypt Pwd: "+ encryptPwd);
                
            }
        };
        View.OnClickListener btnDecryptedOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*
                String decryptPwd = null;
                try {
                    decryptPwd = AESAlgorithm.decrypt(encryptPwd);
                }catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("Check Dalgo","Decrypt Pwd: " + decryptPwd);*/
                URI uri = (URI) getIntent().getExtras().get(LockImages.ADDING_IMAGE);
                try {
                    AESAlgorithm.encryptWithImage("KrajokKai",uri.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.d("TestRealPath","Environment.getExternalStorageDirectory().getAbsolutePath(): "+ Environment.getExternalStorageDirectory().getAbsolutePath());
            }
        };


}
