package com.dyr.mycamera;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    public static final String location = Environment.getExternalStorageDirectory()+"/Crime";
    public static final String thumbnail = location+"/thumbnail";

    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    public static final int CROP_RESULT_CODE = 3;

    private ImageView picture;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button takePhoto = (Button) findViewById(R.id.take_photo);
        Button chooseAlbum = (Button) findViewById(R.id.choose_from_album);
        picture =(ImageView) findViewById(R.id.picture);
        String path;

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    int checkCameraPermission = ContextCompat.checkSelfPermission(
                            MainActivity.this, Manifest.permission.CAMERA);
                    int checkStoragePermission = ContextCompat.checkSelfPermission(
                            MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE
                    );

                    if ((checkCameraPermission != PackageManager.PERMISSION_GRANTED) &&
                            (checkStoragePermission != PackageManager.PERMISSION_GRANTED)) {
                        ActivityCompat.requestPermissions(
                                MainActivity.this,
                                new String[]{
                                        Manifest.permission.CAMERA,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                                }
                                , TAKE_PHOTO);

                    } else {
                        try {
                            openCamera();
                        } catch (Exception e) {
                            Log.e(TAG, "Error in open camera");
                        }
                    }
                }
            }
        });

        chooseAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.
                        PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, CHOOSE_PHOTO );
                } else {
                    openAlbum();
                }
            }
        });

        path = FileSave.getFile(thumbnail,"Crime_thumbnail");
        if(path != null){
            displayImage(path);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if(resultCode == RESULT_OK) {
                    getThumbnailByManual(imageUri);
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    getThumbnailFromAlbum(data);
                }
                break;
            case CROP_RESULT_CODE:
                  Bitmap bitmap = data.getParcelableExtra("data");
                  FileSave.saveThumbnail(bitmap, thumbnail,"Crime_thumbnail");
                  picture.setImageBitmap(bitmap);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                   try {
                       openCamera();
                   }catch (Exception e){
                       Log.e(TAG, "error",e);
                   }
                } else {
                    Toast.makeText(this, "ungranted", Toast.LENGTH_SHORT).show();
                }
                break;
            case CHOOSE_PHOTO:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "ungranted", Toast.LENGTH_SHORT).show();
                }
            default:break;
        }
    }

    private void getThumbnailFromAlbum(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri,String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images
                        .Media.DATA));

            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            picture.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }

    private void openAlbum() {
        Intent album = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        album.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(album, CHOOSE_PHOTO );
    }

    private void  openCamera() throws IOException{
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        //default storage format is jpg
        File outputImage = FileSave.doSaveFile(this, "Crime");
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            imageUri = FileProvider.getUriForFile(MainActivity.this,
                    "com.dyr.mycamera.fileprovider" ,outputImage );
        } else {
            imageUri = Uri.fromFile(outputImage);
        }

        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, TAKE_PHOTO);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    protected void getThumbnailByManual(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");

        intent.setDataAndType(uri, "image/*");

        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 50);
        intent.putExtra("outputY", 50);

        intent.putExtra("return-data", true);

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, CROP_RESULT_CODE);
    }
}
