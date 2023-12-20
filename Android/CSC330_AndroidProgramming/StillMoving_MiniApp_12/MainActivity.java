package edu.miami.cs.enzo_carvalho.stillmovingminiapp12;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;
import java.io.File;
import java.util.Map;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
//==============================================================================================================================================================
public class MainActivity extends AppCompatActivity {
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    private Uri photoUri;
    private Uri videoUri;
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getPermissions.launch(new String[] {Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE});
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    private void goOnCreating(boolean havePermission) {
        if (havePermission) {
            setContentView(R.layout.activity_main);
            photoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider",
                    new File (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/"
                    + getResources().getString(R.string.cameraPhotoName)));
            videoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider",
                    new File (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).toString() + "/"
                    + getResources().getString(R.string.cameraVideoName)));
        } else {
            Toast.makeText(this, "Need more permissions", Toast.LENGTH_LONG).show();
            finish();
        }
        runCamera.launch(photoUri);
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    private ActivityResultLauncher<Uri> runCamera = registerForActivityResult(
        new ActivityResultContracts.TakePicture(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {

                if (result)
                    runVideo.launch(videoUri);
                else {
                    Toast.makeText(MainActivity.this, "Error in runCamera", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    private ActivityResultLauncher<Uri> runVideo = registerForActivityResult(
        new ActivityResultContracts.TakeVideo(), new ActivityResultCallback<Bitmap>() {
            @Override
            public void onActivityResult(Bitmap result) {
//                if (result != null) {
                    Intent sendPhotoAndVideo;
                    sendPhotoAndVideo = new Intent(MainActivity.this, RunDisplay.class);
                    sendPhotoAndVideo.putExtra("photoURI", photoUri);
                    sendPhotoAndVideo.putExtra("videoURI", videoUri);
//                    MainActivity.this.startActivity(sendPhotoAndVideo);
                    runDisplay.launch(sendPhotoAndVideo);
//                } else {
//                    Toast.makeText(MainActivity.this, "Error in runVideo", Toast.LENGTH_LONG).show();
//                    finish();
//                }
            }
        });
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    private ActivityResultLauncher<Intent> runDisplay = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
//                Log.i("CHECK_MY_APP", "Got to runDisplay ARL");
                finish();
            }
        });
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    private ActivityResultLauncher<String[]> getPermissions = registerForActivityResult(
        new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
            @Override
            public void onActivityResult(Map<String, Boolean> results) {

                for (String key:results.keySet()) {
                    if (!results.get(key)) {
                        goOnCreating(false);
                    }
                }
                goOnCreating(true);
            }
        });
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
}
//==============================================================================================================================================================