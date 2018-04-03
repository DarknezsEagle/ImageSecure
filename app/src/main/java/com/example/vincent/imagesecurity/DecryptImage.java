package com.example.vincent.imagesecurity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import algorithm.EncryptAndDecryptAlgorithm;

public class DecryptImage extends AppCompatActivity {
    private Button btnDecrypted;
    private Button btnMainMenu;
    private TextView tvFileName;
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
        btnMainMenu = findViewById(R.id.btn_continue);
        btnMainMenu.setOnClickListener(btnContinueOnClickListener);
        tvFileName = findViewById(R.id.tvFileName);
        tvFileName.setText(getIntent().getExtras().getString(UnlockImages.FILE_NAME));
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
                Bitmap bitmap = EncryptAndDecryptAlgorithm.decryptWithImageAES(decryptPwd, filePath, fileName);
                ivInvalid.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(DecryptImage.this, "Wrong Password", Toast.LENGTH_SHORT).show();
            }
        }

    };

    View.OnClickListener btnContinueOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };
}




