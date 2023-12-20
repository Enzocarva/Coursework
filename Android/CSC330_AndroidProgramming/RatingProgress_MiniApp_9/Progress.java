package edu.miami.cs.enzo_carvalho.ratingprogressminiapp4;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;

public class Progress extends AppCompatActivity {

    private ProgressBar myProgressBar;
    private int barClickTime;
    Handler myHandler = new Handler();
    //-------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        myProgressBar = findViewById(R.id.time_left);
        myProgressBar.setProgress(myProgressBar.getMax());
        barClickTime = getResources().getInteger(R.integer.bar_click_time);
        myProgresser.run();
    }
//--------------------------------------------------------------------------------------------------------

    private final Runnable myProgresser = new Runnable() {

        public void run() {

//            Intent mainActivity = new Intent();
//            mainActivity.setClassName( "edu.miami.cs.enzo_carvalho.ratingprogressminiapp3","edu.miami.cs.enzo_carvalho.ratingprogressminiapp3.MainActivity");
            Intent mainActivity;
            mainActivity = new Intent(Progress.this, MainActivity.class);

            myProgressBar.setProgress(myProgressBar.getProgress()-barClickTime);
            if (myProgressBar.getProgress() <= 0) {
                setResult(RESULT_OK, mainActivity);
                finish();
            } else {
                setResult(RESULT_CANCELED, mainActivity);
            }

            if (!myHandler.postDelayed(myProgresser,barClickTime)) {
                Log.e("ERROR","Cannot postDelayed");
            }
        }
    };
    //-------------------------------------------------------------------------------------------------
    @Override
    protected void onDestroy() {

        super.onDestroy();
        myHandler.removeCallbacks(myProgresser);
    }
//-------------------------------------------------------------------------------------------------
}
//=================================================================================================