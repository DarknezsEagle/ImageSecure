package com.example.vincent.imagesecurity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import utils.RealPathUtil;

public class LockImages extends AppCompatActivity {
    public static String FILE_PATH = "FILE_PATH";
    public static String ADDING_IMAGE = "ADDING_IMAGE";
    public static String FILE_NAME = "FILE_NAME";
    public static String FILE_FORMAT = "FILE_FORMAT";
    private static final int REQUEST_CAMERA = 0 ;
    private static final int REQUEST_GALLERY = 1;
    private static final int REQUEST_READ = 2;
    private Button addPhotos;
    private Button useCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_images);

        addPhotos = (Button) findViewById(R.id.addImage);
        useCamera = (Button) findViewById(R.id.useCamera);

        addPhotos.setOnClickListener(addPhotosOnClickListener);
        useCamera.setOnClickListener(useCameraOnClickListener);

    }

    @RequiresApi(19)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
        Log.i("LockImages","In REQUEST_CAMERA");
            Uri uri = data.getData();
            String filePath = RealPathUtil.getRealPath(LockImages.this, uri);
            String uriSplit[] = filePath.split("/");
            String nameTemp = uriSplit[uriSplit.length-1];
            String nameFormat[] = nameTemp.split("\\.");
            Intent intent = new Intent(LockImages.this,EncryptImage.class);
            intent.putExtra(ADDING_IMAGE,uri);
            intent.putExtra(FILE_NAME,nameFormat[0]);
            intent.putExtra(FILE_FORMAT,nameFormat[1]);
            startActivity(intent);
        }

        if (resultCode == RESULT_OK && requestCode == REQUEST_GALLERY) {
            Log.i("LockImages","In REQUEST_GALLERY");
            if(data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                int currentItem = 0;
                while(currentItem < count) {
//                    Uri imageUri = data.getClipData().getItemAt(currentItem).getUri();
//                    allPathList.add(getAbsolutePath(imageUri));
//                    do something with the image (save it to some directory or whatever you need to do with it here)
//                    currentItem = currentItem + 1;
                }
            } else if(data.getData() != null) {
                Uri imageUri = data.getData();
                String filePath = RealPathUtil.getRealPath(LockImages.this, imageUri);
                String uriSplit[] = filePath.split("/");
                String nameTemp = uriSplit[uriSplit.length-1];
                String nameFormat[] = nameTemp.split("\\.");
                Intent intent = new Intent(LockImages.this,EncryptImage.class);
                intent.putExtra(FILE_PATH,filePath);
                intent.putExtra(ADDING_IMAGE,imageUri);
                intent.putExtra(FILE_NAME,nameFormat[0]);
                intent.putExtra(FILE_FORMAT,nameFormat[1]);
                startActivity(intent);
                finish();
//                allPathList.add(getAbsolutePath(imageUri));
                //do something with the image (save it to some directory or whatever you need to do with it here)
            }
        }
    }


    View.OnClickListener useCameraOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                String timeStamp =
                        new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String SercurityPhotos = "IMG_" + timeStamp + ".jpg";
                File SecurityPhotos = new File(Environment.getExternalStorageDirectory()
                        , "DCIM/Camera/" + SercurityPhotos);
                Uri uri = Uri.fromFile(SecurityPhotos);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(Intent.createChooser(intent
                        , "Take a picture with"),REQUEST_CAMERA );



            }
        };

        View.OnClickListener addPhotosOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] permissoss = {"android.permission.READ_EXTERNAL_STORAGE"};
                if (ContextCompat.checkSelfPermission(LockImages.this.getBaseContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(LockImages.this, permissoss, REQUEST_READ);
                            /* Open Camera */
                } else {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent
                            , "Select photo from"), REQUEST_GALLERY);
                }
            }
        };
}
