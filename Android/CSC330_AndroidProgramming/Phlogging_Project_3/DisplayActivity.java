package edu.miami.cs.enzo_carvalho.phloggingproject3;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.UUID;

//==============================================================================================================================================================
public class DisplayActivity extends AppCompatActivity {
    private boolean entryExists;
    private EditText title;
    private EditText description;
    private ImageView image;
    private TextView displayTime;
    private TextView location;
    private Uri newImageUri = null;
    private double currentLatitude;
    private double currentLongitude;
    private DataRoomEntity currentEntity;
    private long newId = 0;
    //--------------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        // Get time from intent and put it in UI
        Intent fromMainIntent = getIntent();
        String currentTime = fromMainIntent.getStringExtra("date");
        displayTime = findViewById(R.id.displayDateAndTime);
        displayTime.setText(currentTime);

        // Set UIs
        title = findViewById(R.id.displayTitle);
        description = findViewById(R.id.displayDescription);
        image = findViewById(R.id.displayImage);
        location = findViewById(R.id.displayLocation);

        // If there's a DB entry with this time, put rest of data in UI, else get rest of data from intent
        currentEntity = MainActivity.phloggDB.daoAccess().getPhloggByTime(currentTime);
        if (currentEntity != null) {
            entryExists = true;
            title.setText(currentEntity.getTitle());
            description.setText(currentEntity.getDescription());
            try {
                newImageUri = Uri.parse(currentEntity.getPhotoUri());
            } catch (NullPointerException e) {
            }
            image.setImageURI(newImageUri);
            location.setText("Lat : " + currentEntity.getLatitude() + " Lon: " + currentEntity.getLongitude());
        } else {
            entryExists = false;
            currentLatitude = fromMainIntent.getDoubleExtra("latitude", 0.0);
            currentLongitude = fromMainIntent.getDoubleExtra("longitude", 0.0);
            location.setText("Lat: " + currentLatitude + " Lon: " + currentLongitude);
        }
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cameraButton:
                // Generate a unique Uri
                // FIXME Want to figure out how to make this commented out code work
//                String rand = UUID.randomUUID().toString();
//                Log.i("CHECKING THINGS", "The random num = " + rand);
//                newImageUri = Uri.parse(rand);
//                Log.i("CHECKING THINGS", "The URI = " + newImageUri);

                Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                runCamera.launch(imageIntent);
                break;
            case R.id.saveButton:
                DataRoomEntity newEntity = new DataRoomEntity();
                newEntity.setPhloggID(newId);
                newId++;
                newEntity.setTitle(title.getText().toString());
                newEntity.setDescription(description.getText().toString());
                if (newImageUri != null) newEntity.setPhotoUri(newImageUri.toString());
                newEntity.setTime(displayTime.getText().toString());
                newEntity.setLatitude(currentLatitude);
                newEntity.setLongitude(currentLongitude);

                if (entryExists) {
                    newEntity.setPhloggID(currentEntity.getPhloggID());
                    MainActivity.phloggDB.daoAccess().updatePhlogg(newEntity);
                } else {
                    MainActivity.phloggDB.daoAccess().addPhlogg(newEntity);
                }

                setResult(Activity.RESULT_OK);
                finish();
                break;
            case R.id.deleteButton:
                if (entryExists)
                    MainActivity.phloggDB.daoAccess().deletePhlogg(currentEntity);
                setResult(Activity.RESULT_OK);
                finish();
                break;
        }
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    // FIXME Want to figure out how to make this commented out code work
//    private ActivityResultLauncher<Uri> runCamera = registerForActivityResult(
//        new ActivityResultContracts.TakePicture(), new ActivityResultCallback<Boolean>() {
//            @Override
//            public void onActivityResult(Boolean result) {
//                if (result) {
//                    image.setImageURI(newImageUri);
//                    Log.i("CHECKING THINGS", "The URI inside IF = " + newImageUri);
//                }
//                image.setImageURI(newImageUri);
//                Log.i("CHECKING THINGS", "The URI outside IF = " + newImageUri);
//            }
//        });

    private ActivityResultLauncher<Intent> runCamera = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    // Get bitmap and convert it to Uri
                    Bundle bundle = result.getData().getExtras();
                    Bitmap newImageBitMap = (Bitmap) bundle.get("data");

                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    newImageBitMap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    String path = MediaStore.Images.Media.insertImage(DisplayActivity.this.getContentResolver(), newImageBitMap, "Title", null);
                    newImageUri = Uri.parse(path);

                    if (result.getResultCode() == RESULT_OK) {
                        image.setImageURI(newImageUri);
                    }
                }
            });
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
}
//==============================================================================================================================================================