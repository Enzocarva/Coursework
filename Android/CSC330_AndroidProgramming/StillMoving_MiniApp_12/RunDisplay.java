package edu.miami.cs.enzo_carvalho.stillmovingminiapp12;

import androidx.appcompat.app.AppCompatActivity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

//==============================================================================================================================================================
public class RunDisplay extends AppCompatActivity implements MediaPlayer.OnCompletionListener {
    private ImageView displayPhoto;
    private VideoView displayVideo;
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_display);

        // Get URIs from intent
        Uri photo = this.getIntent().getParcelableExtra("photoURI");
        Uri video = this.getIntent().getParcelableExtra("videoURI");

        // Get relative resource xml code and display video and photo
        displayPhoto = findViewById(R.id.photo);
        displayVideo = findViewById(R.id.video);
        displayVideo.setVisibility(View.VISIBLE);
        displayPhoto.setImageURI(photo);
        displayVideo.setVideoURI(video);

        // Set onCompletionListener and start video
        displayVideo.setOnCompletionListener(this);
        displayVideo.start();
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void onCompletion(MediaPlayer mediaPlayer) {
//        Log.i("CHECK_MY_APP", "Got to onCompletion");
        Toast.makeText(RunDisplay.this, "Video finished", Toast.LENGTH_LONG).show();
        finish();
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        finish();
//    }
}
//==============================================================================================================================================================