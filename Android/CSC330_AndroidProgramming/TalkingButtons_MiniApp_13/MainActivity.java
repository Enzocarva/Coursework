package edu.miami.cs.enzo_carvalho.talkingbuttonsminiapp10;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

//==============================================================================================================================================================
public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    private TextToSpeech speaker;
    private int numOnQueue;
    private Handler handler = new Handler();
    private final int fiveSeconds = 5000; // MilliSeconds
    //--------------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        speaker = new TextToSpeech(this, this);
        numOnQueue = 0;
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {
            Toast.makeText(this, R.string.available_to_speak, Toast.LENGTH_SHORT).show();
            speaker.setOnUtteranceProgressListener(listener);
            handler.postDelayed(progress, fiveSeconds);
        } else {
            Toast.makeText(this, "Something went wrong trying to load TTS", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    private final Runnable progress = new Runnable() {
    @Override
    public void run() {
        speaker.speak(getResources().getString(R.string.nothing_to_say), TextToSpeech.QUEUE_ADD, null, "NOTHING_SAID");
        handler.postDelayed(progress, fiveSeconds);
    }
    };
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void clickHandler(View view) {

        if (view.getId() == R.id.exit_button) {
            finish();
        } else {
            handler.removeCallbacks(progress);
            speaker.speak(((Button) view).getText().toString(), TextToSpeech.QUEUE_ADD, null, "SOMETHING_SAID");
            numOnQueue++;
        }
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    private UtteranceProgressListener listener = new UtteranceProgressListener() {
    @Override
    public void onStart(String utteranceID) {}

    @Override
    public void onDone(String utteranceID) {

//         if (utteranceId.equals("SOMETHING_SAID") && --numOnQueue == 0)
//               handler.postDelayed(progress, fiveSeconds);

        if (utteranceID.equals("SOMETHING_SAID")) {
            numOnQueue--;
            if (numOnQueue == 0)
                handler.postDelayed(progress, fiveSeconds);
        }
    }

    @Override
    public void onError(String utteranceID) {}
    };
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void onDestroy() {

        super.onDestroy();
        handler.removeCallbacks(progress);
        speaker.shutdown();
    }
}
//==============================================================================================================================================================