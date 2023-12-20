package edu.miami.cs.enzo_carvalho.videwithmusicminiapp11;

import androidx.appcompat.app.AppCompatActivity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.VideoView;

//==============================================================================================================================================================
public class MainActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {
    private MediaPlayer mediaPlayer;
    private VideoView videoView;
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set the completion listener for video
        videoView = findViewById(R.id.videScreen);
        videoView.setOnCompletionListener(this);

        // Set the URL for the video and video screen
        videoView.setVideoURI(Uri.parse("https://www.cs.miami.edu/home/geoff/Courses/CSC330-23F/Content/Media/mfog.mp4"));

        // Set the URL for the music
        mediaPlayer = MediaPlayer.create(this, Uri.parse("https://www.cs.miami.edu/home/geoff/Courses/CSC330-23F/Content/Media/chin_chin_choo.mp3"));
        mediaPlayer.setOnErrorListener(this);
        videoView.setVisibility(View.INVISIBLE);

        // Make the music loop, and start it as soon as the app opens
        mediaPlayer.isLooping();
        mediaPlayer.start();
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void clickListener(View view) {
        switch (view.getId()) {
            case R.id.playButton:
                mediaPlayer.pause();
                videoView.setVisibility(View.VISIBLE);
                videoView.start();
                break;
            case R.id.pauseButton:
                videoView.pause();
                mediaPlayer.start();
                break;
            case R.id.resumeButton:
                mediaPlayer.pause();
                videoView.start();
                break;
            case R.id.stopButton:
                videoView.stopPlayback();
                finish();
                break;
            default:
                break;
        }
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        finish();
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    public boolean onError(MediaPlayer mediaPlayer, int whatHappened, int extra) {
        finish();
        return true;
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void onDestroy() {
        super.onDestroy();

        if (videoView.isPlaying())
            videoView.stopPlayback();

        mediaPlayer.stop();
        mediaPlayer.release();
    }
}
//==============================================================================================================================================================