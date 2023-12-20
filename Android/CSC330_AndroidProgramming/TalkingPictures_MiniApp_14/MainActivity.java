package edu.miami.cs.enzo_carvalho.talkingpicturesminiapp13;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Map;

//==============================================================================================================================================================
public class MainActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {
    private MediaRecorder recorder;
    private String recordFileName;
    private MediaPlayer player;
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getPermissions.launch(new String[] {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE});
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    private void goOnCreating(boolean havePermission) {

        if (havePermission) {
            setContentView(R.layout.activity_main);

            // Get the file type name
            recordFileName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).toString() + "/" +
                    getResources().getString(R.string.audioFileName);

            // Start recording
            recorder = new android.media.MediaRecorder();
            recorder.setAudioSource(android.media.MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setOutputFile(recordFileName);
            try {
                recorder.prepare();
            } catch (IOException e) {
                Log.i("AUDIO ERROR", "PREPARE RECORDER");
            }
            Toast.makeText(this, "Start recording", Toast.LENGTH_SHORT).show();
            recorder.start();

            // Launch the gallery to choose a photo
            startGallery.launch("image/*");
        } else {
            Toast.makeText(this, "Need more permissions", Toast.LENGTH_LONG).show();
            finish();
        }
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    private ActivityResultLauncher<String> startGallery = registerForActivityResult(
        new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {

                // Stop recording
                Toast.makeText(MainActivity.this,"Stopping recording",Toast.LENGTH_SHORT).show();
                recorder.stop();
                recorder.release();

                // If successfully returned a URI from gallery:
                if (result != null) {
                    ((ImageView)findViewById(R.id.photo)).setImageURI(result); // Show picture
                    player = new MediaPlayer(); // Start playing recording
                    player.setOnCompletionListener(MainActivity.this);
                    try {
                        player.setDataSource(recordFileName);
                        player.prepare();
                    } catch (IOException e) {
                        Log.i("AUDIO ERROR","PREPARING TO PLAY");
                    }
                    Toast.makeText(MainActivity.this,"Starting to play",Toast.LENGTH_SHORT).show();
                    player.start();
                }
            }
        });
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void onCompletion(MediaPlayer mediaPlayer) {

        mediaPlayer.release();
        finish();
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    private ActivityResultLauncher<String[]> getPermissions = registerForActivityResult(
        new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
            @Override
            public void onActivityResult(Map<String, Boolean> results) {

                for (String key: results.keySet()) {
                    if (!results.get(key)) {
                        goOnCreating(false);
                    }
                }
                goOnCreating(true);
            }
        });
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void onDestroy() {

        super.onDestroy();
        recorder.release();
        player.release();
    }
}
//==============================================================================================================================================================