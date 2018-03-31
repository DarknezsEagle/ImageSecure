package com.example.vincent.imagesecurity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class UnlockImages extends AppCompatActivity {
    private Button photoSelect;
    private static final int REQUEST_GALLERY = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlock_images);

        photoSelect = (Button) findViewById(R.id.photoSelect);

        photoSelect.setOnClickListener(photoSelectOnClickListener);
    }

    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GALLERY && resultCode == RESULT_OK){
            Intent intent = new Intent(UnlockImages.this,DecryptImage.class);
            startActivity(intent);

        }
    }

        View.OnClickListener photoSelectOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent
                        , "Select photo from"), REQUEST_GALLERY);
            }
        };
}
