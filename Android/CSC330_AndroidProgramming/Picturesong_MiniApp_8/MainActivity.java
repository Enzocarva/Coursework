package edu.miami.cs.enzo_carvalho.picturesongminiapp14;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import java.io.IOException;
import java.util.Map;
import android.Manifest;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

//==============================================================================================================================================================
public class MainActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {
    private Cursor imageCursor;
    private Cursor audioCursor;
    private MediaPlayer mediaPlayer;
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPermissions.launch(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE});
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    private void goOnCreating(boolean havePermission) {

        String[] queryFieldsImage = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA};
        int imageDataIndex, imageCursorRows;
        ImageView theImage;
        Uri imageUri;

        String[] queryFieldsAudio = {MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA};
        int audioDataIndex, audioCursorRows;

        if (havePermission) {
            setContentView(R.layout.activity_main);
            // Query the database for image, set the cursor to DATA column, move cursor to first, and get the number of rows in image cursor (size)
            imageCursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, queryFieldsImage, null, null,
                    MediaStore.Images.Media.DEFAULT_SORT_ORDER);
            assert imageCursor != null;
            imageDataIndex = imageCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            imageCursor.moveToFirst();
            imageCursorRows = imageCursor.getCount();

            // Choose random row (image) to display, + 1 to make the last row inclusive
            int randomNumImage = (int)(Math.random() * imageCursorRows);
            imageCursor.moveToPosition(randomNumImage);

            // Display the image
            theImage = findViewById(R.id.Image);
//            theImage.setImageURI(Uri.parse(imageCursor.getString(imageCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))));
            theImage.setImageResource(android.R.color.transparent);
            imageUri = Uri.parse(imageCursor.getString(imageDataIndex));
            if (imageUri != null)
                theImage.setImageURI(imageUri);
            else
                Toast.makeText(this, "Couldn't load the image", Toast.LENGTH_SHORT).show();


            // Query the database for audio, set the cursor to DATA column, move cursor to first, and get the number of rows in audio cursor
            audioCursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, queryFieldsAudio, null, null,
                    MediaStore.Audio.Media.TITLE + " ASC");
            assert audioCursor != null;
//            String logString = "";
//            for (int i = 0; i < audioCursorRows; i++) {
//                logString = audioCursor.getString(i);
//                Log.i("XYZ123", "" + logString);
//            }

            audioDataIndex = audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
            audioCursor.moveToFirst();
            audioCursorRows = audioCursor.getCount();

            // Choose random row (song) to play, + 1 to make the last row inclusive
            int randomNumAudio = (int)(Math.random() * audioCursorRows);
            audioCursor.moveToPosition(randomNumAudio);

            // Set up audio to be played
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            String fileName = audioCursor.getString(audioDataIndex);
            try {
                mediaPlayer.setDataSource(fileName);
                mediaPlayer.prepare();
                mediaPlayer.start();
                mediaPlayer.isLooping();
            } catch (IOException e) {
                Log.i("AUDIO ERROR","Couldn't prepare audio");
            }

        } else {
            Toast.makeText(this, "Need more permissions", Toast.LENGTH_LONG).show();
            finish();
        }
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        finish();
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void onDestroy() {
        super.onDestroy();
        imageCursor.close();
        audioCursor.close();
        mediaPlayer.release();
    }
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