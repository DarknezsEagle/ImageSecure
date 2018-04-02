package com.example.vincent.imagesecurity;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import algorithm.AESAlgorithm;


public class EncryptImage extends AppCompatActivity {
    private ImageView ivPhoto;
    private EditText edtPwdPhoto;
    private EditText edtNamePhoto;
    private Button btnSave;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encrypt_image);

        initInstances();

        initImage();


    }

    private void initImage() {
        uri = getIntent().getExtras().getParcelable(LockImages.ADDING_IMAGE);

        ContentResolver cr = getContentResolver();


        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(cr, uri);
            ivPhoto.setImageBitmap(bitmap);
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
        btnSave.setOnClickListener(btnSaveOnClickListener);
    }

        View.OnClickListener btnSaveOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String filePath = getIntent().getExtras().getString(LockImages.FILE_PATH);
                String fileName = edtNamePhoto.getText().toString()+"."+getIntent().getExtras().getString(LockImages.FILE_FORMAT);
                String password = edtPwdPhoto.getText().toString();
                try {
                    AESAlgorithm.encryptWithImage(password,filePath, fileName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
            }
        };


}
