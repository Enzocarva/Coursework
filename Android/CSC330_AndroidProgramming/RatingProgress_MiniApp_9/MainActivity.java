package edu.miami.cs.enzo_carvalho.ratingprogressminiapp4;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.RatingBar;
//==============================================================================================================================================================
public class MainActivity extends AppCompatActivity {
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    private RatingBar ratingBar;
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ratingBar = findViewById(R.id.ratingBar);
        ratingBar.setNumStars(0);
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void myClickHandler(View view) {
        switch (view.getId()) {
            case R.id.start:
                Intent nextActivity;
                nextActivity = new Intent (MainActivity.this, Progress.class); // Send an Intent from MainActivity to Progress class/Activity
                startProgress.launch(nextActivity);
        }
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    ActivityResultLauncher<Intent> startProgress = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {

                if (result.getResultCode() == Activity.RESULT_OK) {
                    ratingBar.setRating(ratingBar.getRating() + 1);
                    if (ratingBar.getRating() == 5)
                        finish();
                } else {
                    Toast.makeText(MainActivity.this,"No Progress was made", Toast.LENGTH_LONG).show();
                }
            }
        });
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
}
//==============================================================================================================================================================