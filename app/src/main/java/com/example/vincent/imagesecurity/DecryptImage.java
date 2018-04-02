package com.example.vincent.imagesecurity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import algorithm.AESAlgorithm;

public class DecryptImage extends AppCompatActivity {
    private Button btnDecrypted;
    private EditText edtPassword;
    private ImageView ivInvalid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decrypt_image);

        initInstance();

    }

    private void initInstance() {
        btnDecrypted = findViewById(R.id.btnDecrypted);
        btnDecrypted.setOnClickListener(btnDecryptedOnClickListener);
        edtPassword = findViewById(R.id.edtPassword);
        ivInvalid = findViewById(R.id.ivInvalid);
    }


    View.OnClickListener btnDecryptedOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            String filePath = getIntent().getExtras().getString(UnlockImages.FILE_PATH);
            String fileName = getIntent().getExtras().get(UnlockImages.FILE_NAME) + "." + getIntent().getExtras().getString(UnlockImages.FILE_FORMAT);
            String decryptPwd = edtPassword.getText().toString();

            try {
                Bitmap bitmap = AESAlgorithm.decryptWithImage(decryptPwd, filePath, fileName);
                ivInvalid.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(DecryptImage.this, "Wrong Password", Toast.LENGTH_SHORT).show();
            }
        }

    };
}




