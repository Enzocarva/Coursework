package edu.miami.cs.enzo_carvalho.talking_picture_listproject2;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//==============================================================================================================================================================
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, CustomDialog.StopTalking {
    private Cursor audioCursor;
    private Cursor imageCursor;
    private MediaPlayer mediaPlayer;
    private DataRoomDB imageDB;
    private static final String DATABASE_NAME = "DescribedImagesRoom.db";
    public static TextToSpeech speaker;
    public static String voiceRecordingFileName; // FIXME can this be global and static?
    private DataRoomEntity currentDbEntity;
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPermissions.launch(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE});
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    private void goOnCreating(boolean havePermission) {
        String[] queryFieldsAudio = {MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA};
        int audioDataIndex, audioCursorRows;

        if (havePermission) {
            setContentView(R.layout.activity_main);

            // Get random song and start looping it
            audioCursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, queryFieldsAudio, null, null,
                    MediaStore.Audio.Media.TITLE + " ASC");
            assert audioCursor != null;

            audioDataIndex = audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
            audioCursor.moveToFirst();
            audioCursorRows = audioCursor.getCount();

            int randomNumAudio = (int)(Math.random() * audioCursorRows);
            audioCursor.moveToPosition(randomNumAudio);

            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            String fileData = audioCursor.getString(audioDataIndex);
            try {
                mediaPlayer.setDataSource(fileData);
                mediaPlayer.prepare();
                mediaPlayer.start();
                mediaPlayer.setLooping(true);
            } catch (IOException e) {
                Log.i("AUDIO ERROR","Couldn't prepare audio");
            }

            // Build database, fill list view, create filename for recording, and crete TTS
            imageDB = Room.databaseBuilder(getApplicationContext(), DataRoomDB.class, DATABASE_NAME).allowMainThreadQueries().build();
            fillListView();
            voiceRecordingFileName = getExternalCacheDir().getAbsolutePath() + "/" + getString(R.string.voiceRecordingFileName);
            speaker = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status == TextToSpeech.SUCCESS) {
                        Toast.makeText(MainActivity.this, R.string.availableToSpeak, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Something went wrong trying to load TTS in goOnCreating()", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            });

        } else {
            Toast.makeText(this, "Need more permissions", Toast.LENGTH_LONG).show();
            finish();
        }
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    private void fillListView() {
        String[] queryFieldsImage = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA};
        int imageDataColumnIndex, numImageCursorRows;
        Uri imageUri;

        // Fetch images to image Cursor
        imageCursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, queryFieldsImage, null, null,
                MediaStore.Images.Media.DEFAULT_SORT_ORDER);
        assert imageCursor != null;
        imageDataColumnIndex = imageCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        imageCursor.moveToFirst();
//        numImageCursorRows = imageCursor.getCount();
        long imageId;

        // Convert to ArrayList of HashMap, put thumbnail, if image is in DB then put description and recording
        ArrayList<HashMap<String, Object>> listRows = new ArrayList<>();
        HashMap<String, Object> oneListRow;

        do {
            oneListRow = new HashMap<>();
            imageUri = Uri.parse(imageCursor.getString(imageDataColumnIndex));
            if (imageUri != null) oneListRow.put("thumbnail", imageUri);
            else Toast.makeText(this, "Couldn't load image in fillListView()", Toast.LENGTH_SHORT).show();

            // One row from the DataBase based on the value of image _ID
            imageId = imageCursor.getInt(imageCursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
            if (imageDB.daoAccess().getImageByImageId(imageId) != null) {
                currentDbEntity = imageDB.daoAccess().getImageByImageId(imageId);
                oneListRow.put("description", currentDbEntity.getDescription());
                oneListRow.put("recording", currentDbEntity.getRecording());
            }
            listRows.add(oneListRow);
        } while (imageCursor.moveToNext());

        // Create and set SimpleAdapter, and assign adapter to ListView, set onItemClickListener
        String[] from = {"thumbnail", "description", "recording"};
        int[] to = {R.id.thumbnail, R.id.description};
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, listRows, R.layout.list_row, from, to);
        ListView theList = findViewById(R.id.theList);
        theList.setAdapter(simpleAdapter);
        theList.setOnItemClickListener(this);

        // Set ViewBinder
        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                switch (view.getId()) {
                    case R.id.thumbnail:
                        Uri thumbnail = (Uri) data;
                        ((ImageView)view).setImageURI(thumbnail);
                        break;
                    case R.id.description:
                        String description = (String) data;
                        ((TextView)view).setText(description);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Get image id and Uri from image cursor
        imageCursor.moveToPosition(position);
        int imageIdColumnIndex = imageCursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
        int imageDataColumnIndex = imageCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        long imageId = imageCursor.getLong(imageIdColumnIndex);
        Uri imageUri = Uri.parse(imageCursor.getString(imageDataColumnIndex));

        // Pause the song
        mediaPlayer.pause();

        // If image is in the DB look, Put Uri in Bundle, Show dialog, Speak description, Speak any recording.
        currentDbEntity = imageDB.daoAccess().getImageByImageId(imageCursor.getInt(imageIdColumnIndex));
        if (currentDbEntity != null) {
            Bundle bundle = new Bundle();
            CustomDialog dialogFragment;
            bundle.putParcelable("imageUri", imageUri);
            dialogFragment = new CustomDialog();
            dialogFragment.setArguments(bundle);
            dialogFragment.show(getSupportFragmentManager(), "myFragment");

            speaker.speak(currentDbEntity.getDescription(), TextToSpeech.QUEUE_ADD, null, null);
            // FIXME Speak any recording
        } else {
            Intent editActivityIntent = new Intent(MainActivity.this, EditActivity.class);
            editActivityIntent.putExtra("imageId", imageId);
            editActivityIntent.putExtra("imageUri", imageUri);
            editActivity.launch(editActivityIntent);
        }
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    ActivityResultLauncher<Intent> editActivity = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Intent checkIntent;
                checkIntent = result.getData();

                // if result ok (received a new description and or recording) put them in a new DB entity
                if (result.getResultCode() == Activity.RESULT_OK) {
                    long imageId = checkIntent.getLongExtra("imageId", 0);
                    String description = checkIntent.getStringExtra("description");
//                    byte[] recording = checkIntent.getByteArrayExtra("recording"); // FIXME gets nothing until I finish implementing

                    DataRoomEntity newDbEntity = new DataRoomEntity();
                    newDbEntity.setImageID(imageId);
                    newDbEntity.setDescription(description);
//                    newDbEntity.setRecording(recording);
                    imageDB.daoAccess().addImage(newDbEntity);

                    fillListView();
                }
                // Resume song
                mediaPlayer.start();
            }
        });
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void onDestroy() {
        super.onDestroy();
        imageDB.close();
        speaker.shutdown();
        mediaPlayer.stop();
        mediaPlayer.release();
        audioCursor.close();
        imageCursor.close();
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void stopTalking() {
        speaker.stop();
        // FIXME stop recording
        mediaPlayer.start();
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