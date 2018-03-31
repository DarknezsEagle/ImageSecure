package com.example.vincent.imagesecurity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LockImages extends AppCompatActivity {
    public static String FILE_PATH = "FILE_PATH";
    public static String ADDING_IMAGE = "ADDING_IMAGE";
    public static String FILE_NAME = "FILE_NAME";
    public static String FILE_FORMAT = "FILE_FORMAT";
    private static final int REQUEST_CAMERA = 0;
    private static final int REQUEST_GALLERY = 1;
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
        Log.i("LockImages","In REQUEST_CAMERA");

        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
        Log.i("LockImages","data.getdata"+data.getData());
            Uri uri = data.getData();
            String filePath = getRealPathFromURI(uri);
            String uriSplit[] = filePath.split("/");
            String nameTemp = uriSplit[uriSplit.length-1];
            String nameFormat[] = nameTemp.split("\\.");
            Intent intent = new Intent(LockImages.this,EncryptImage.class);
            intent.putExtra(ADDING_IMAGE,uri);
            intent.putExtra(FILE_NAME,nameFormat[0]);
            intent.putExtra(FILE_FORMAT,nameFormat[1]);
            Log.d("LockImages","FilePath: "+filePath);
            Log.d("LockImages","FilePath: "+ uriSplit[uriSplit.length-1]);
            Log.d("LockImages","Name Temp: "+ nameTemp);
            Log.d("LockImages","Name Format: "+ TextUtils.join(",",nameFormat));
            startActivity(intent);
        }

        if (resultCode == RESULT_OK && requestCode == REQUEST_GALLERY) {
            Log.i("LockImages","In REQUEST_GALLERY");
            Log.d("LockImages","data.getClipData: "+data.getClipData());
            if(data.getClipData() != null) {
                Log.d("LockImages", "If case-Gallery");
                int count = data.getClipData().getItemCount();
                int currentItem = 0;
                while(currentItem < count) {
//                    Uri imageUri = data.getClipData().getItemAt(currentItem).getUri();
//                    allPathList.add(getAbsolutePath(imageUri));
//                    do something with the image (save it to some directory or whatever you need to do with it here)
//                    currentItem = currentItem + 1;
                }
            } else if(data.getData() != null) {
                Log.d("LockImages", "Else case-Gallery");
                Uri imageUri = data.getData();
                String filePath = getAbsolutePath(imageUri);
                String uriSplit[] = filePath.split("/");
                String nameTemp = uriSplit[uriSplit.length-1];
                String nameFormat[] = nameTemp.split("\\.");
                Intent intent = new Intent(LockImages.this,EncryptImage.class);
                intent.putExtra(FILE_PATH,filePath);
                intent.putExtra(ADDING_IMAGE,imageUri);
                intent.putExtra(FILE_NAME,nameFormat[0]);
                intent.putExtra(FILE_FORMAT,nameFormat[1]);
                startActivity(intent);
                Log.d("LockImages","--"+ getAbsolutePath(imageUri));
                Log.d("LockImages","FilePath: "+filePath);
                Log.d("LockImages","FilePath: "+ uriSplit[uriSplit.length-1]);
                Log.d("LockImages","Name Temp: "+ nameTemp);
                Log.d("LockImages","Name Format: "+ TextUtils.join(",",nameFormat));
//                allPathList.add(getAbsolutePath(imageUri));
                //do something with the image (save it to some directory or whatever you need to do with it here)
            }

        }
    }

    @RequiresApi(19)
    public String getAbsolutePath(Uri uri) {
        String wholeID = DocumentsContract.getDocumentId(uri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = { MediaStore.Images.Media.DATA };

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = getContentResolver().
                query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        column, sel, new String[]{ id }, null);

        String filePath = "";

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }

        cursor.close();
        return  filePath;
    }

    public String getRealPathFromURI(Uri contentUri) {

        // can post image
        String [] proj={MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery( contentUri,
                proj, // Which columns to return
                null, // WHERE clause; which rows to return (all rows)
                null, // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
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
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent
                        , "Select photo from"), REQUEST_GALLERY);

            }
        };
}
