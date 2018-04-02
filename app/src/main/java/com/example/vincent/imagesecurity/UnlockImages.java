package com.example.vincent.imagesecurity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import utils.RealPathUtil;

public class UnlockImages extends AppCompatActivity {
    private Button photoSelect;
    private static final int REQUEST_FILE_SELECT = 3;
    public static String FILE_PATH = "FILE_PATH";
    public static String FILE_NAME = "FILE_NAME";
    public static String FILE_FORMAT = "FILE_FORMAT";
    public static String DECRYPTED_IMAGE = "DECRYPTED_IMAGE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlock_images);

        photoSelect = (Button) findViewById(R.id.photoSelect);

        photoSelect.setOnClickListener(photoSelectOnClickListener);
    }

    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_FILE_SELECT && resultCode == RESULT_OK){
            Uri uri = data.getData();
            String filePath = RealPathUtil.getRealPath(UnlockImages.this, uri);
            String uriSplit[] = filePath.split("/");
            String nameTemp = uriSplit[uriSplit.length-1];
            String nameFormat[] = nameTemp.split("\\.");
            Intent intent = new Intent(UnlockImages.this,DecryptImage.class);
            intent.putExtra(DECRYPTED_IMAGE,uri);
            intent.putExtra(FILE_PATH,filePath);
            intent.putExtra(FILE_NAME,nameFormat[0]);
            intent.putExtra(FILE_FORMAT,nameFormat[1]);
            startActivity(intent);
            finish();

        }
    }

        View.OnClickListener photoSelectOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(Intent.createChooser(intent
                        , "Select file from"), REQUEST_FILE_SELECT);
            }
        };
}
