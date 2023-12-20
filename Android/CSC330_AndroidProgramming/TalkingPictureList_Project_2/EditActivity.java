package edu.miami.cs.enzo_carvalho.talking_picture_listproject2;

import androidx.activity.result.ActivityResult;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

//==============================================================================================================================================================
public class EditActivity extends AppCompatActivity {
    private MediaRecorder recorder;
    private long imageId;
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        imageId = this.getIntent().getLongExtra("imageId", 0);
        Uri imageUri = this.getIntent().getParcelableExtra("imageUri");
        ImageView theImage = findViewById(R.id.editImage);
        theImage.setImageURI(imageUri);
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void clickHandler(View view) {
        Intent backToMainActivityIntent = new Intent(EditActivity.this, MainActivity.class);
        File recordingFile;
        byte[] recordingBytes;
        FileInputStream recordingStream;

        switch (view.getId()) {
            case R.id.recordButton:
                // Start recording
                recorder = new android.media.MediaRecorder();
                recorder.setAudioSource(android.media.MediaRecorder.AudioSource.MIC);
                recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                recorder.setOutputFile(MainActivity.voiceRecordingFileName);
                try {
                    recorder.prepare();
                } catch (IOException e) {
                    Log.i("AUDIO ERROR", "PREPARE RECORDER");
                }
                recorder.start();
                break;
            case R.id.stopButton:
                recorder.stop();
                recorder.release();
                // FIXME read bytes from file
                recordingFile = new File(MainActivity.voiceRecordingFileName);
                recordingBytes = new byte[(int)recordingFile.length()];
                try {
                    recordingStream = new FileInputStream(recordingFile);
                    recordingStream.read(recordingBytes);
                    recordingStream.close();
                } catch (IOException e) {
                    Toast.makeText(this, "Could not save recording", Toast.LENGTH_SHORT).show();
                    recordingBytes = null;
                }
                recordingFile.delete();
                break;
            case R.id.saveButton:
                EditText descriptionBox = findViewById(R.id.editTextDescribe);
                String input = descriptionBox.getText().toString();
                if (input != null) {
                    backToMainActivityIntent = new Intent(EditActivity.this, MainActivity.class);
                    backToMainActivityIntent.putExtra("imageId", imageId);
                    backToMainActivityIntent.putExtra("description", input);
                    // FIXME backToMainActivityIntent.putExtra("recording", recordingFile)
                    setResult(RESULT_OK, backToMainActivityIntent);
                } else {
                    setResult(RESULT_CANCELED, backToMainActivityIntent);
                }
                break;
            default:
                Toast.makeText(this, "Something went wrong in clickHandler() in EditActivity", Toast.LENGTH_SHORT).show();
                break;
        }
        finish();
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
}
//==============================================================================================================================================================