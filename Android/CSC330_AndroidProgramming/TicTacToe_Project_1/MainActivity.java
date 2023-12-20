package edu.miami.cs.enzo_carvalho.tic_tac_toeproject1;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileUriExposedException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import java.io.FileNotFoundException;
import java.util.Random;

//==============================================================================================================================================================
public class MainActivity extends AppCompatActivity {

    private float split;                    // Global variables
    private EditText player1;
    private EditText player2;
    private int uriID;
    private int playTime;
    private Uri player1URI;
    private Uri player2URI;
    private RatingBar ratingBar1;
    private RatingBar ratingBar2;
    private Button playButton;
    private int winner;
    private final int WIN_PLAYER1 = 1;
    private final int WIN_PLAYER2 = 2;

//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playTime = getResources().getInteger(R.integer.progressBar_time_10s);
        playButton = findViewById(R.id.play_button);
        split = 0.5F;                                       // Initialize split to 0.5
        player1 = findViewById(R.id.player1Name);           // Set the player names equal to the EditText field
        player2 = findViewById(R.id.player2Name);

        ratingBar1 = findViewById(R.id.ratingBar1);         // Create both rating bars
        ratingBar2 = findViewById(R.id.ratingBar2);
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void clickHandler(View view) {

        String player1Name = player1.getText().toString();      // Get the players' names
        String player2Name = player2.getText().toString();

        double randomPlayerPicker;
        int whoStarts;

        switch(view.getId()) {
            case R.id.play_button:
                randomPlayerPicker = Math.random();             // Generate a random number to pick who starts
               if (randomPlayerPicker < split) {                // Choose who starts and adjust split bias
                   whoStarts = 1;
                   split -= 0.1;
               } else {
                   whoStarts = 2;
                   split += 0.1;
               }

                Intent runGameActivity;                     // Enter all of the information that the next activity needs in the intent
                runGameActivity = new Intent(MainActivity.this, RunGame.class);
                runGameActivity.putExtra("Who_Starts", whoStarts);
                runGameActivity.putExtra("P1Name", player1Name);
                runGameActivity.putExtra("P2Name", player2Name);
                runGameActivity.putExtra("timeSelected", playTime);
                runGameActivity.putExtra("P1Image", player1URI);
                runGameActivity.putExtra("P2Image", player2URI);

                startPlay.launch(runGameActivity);      // Launch next activity
                break;
            case R.id.player1_button:
                startGallery.launch("image/*");    // Launch gallery to get picture for player icon
                uriID = R.id.player1_button;            // Set the URI as either player 1 or 2, to distinguish between them
                break;
            case R.id.player2_button:
                startGallery.launch("image/*");
                uriID = R.id.player2_button;
                break;
            default:
                break;
        }
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    ActivityResultLauncher<Intent> startPlay = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                // Intent to get the result sent from the intent in RunGame with the winner information
                Intent checkIntent;
                checkIntent = result.getData();

                // Get the result from the game activity, check who won and add a star accordingly, hide play button if anyone reaches 5 stars
                if (result.getResultCode() == Activity.RESULT_OK) {
                    winner = checkIntent.getIntExtra("Winner", -1);
                    if (winner == WIN_PLAYER1) {
                        ratingBar1.setRating(ratingBar1.getRating() + 1);
                    } else if (winner == WIN_PLAYER2) {
                        ratingBar2.setRating(ratingBar2.getRating() + 1);
                    }
                    if (ratingBar1.getRating() == 5 || ratingBar2.getRating() == 5)
                        playButton.setVisibility(View.INVISIBLE);
                }

            }
        });
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    ActivityResultLauncher<String> startGallery = registerForActivityResult(
            new ActivityResultContracts.GetContent(),new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri resultUri) {

                    if (resultUri != null) {                        // After the picture has been selected from gallery make sure it is not null
                        Context context = MainActivity.this;        // try to convert the uri to an image, then set it as the background of button/player icon
                        Drawable image = null;
                        try {
                            image = Drawable.createFromStream(context.getContentResolver().openInputStream(resultUri), resultUri.toString());
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        findViewById(uriID).setForeground(image);

                       if (uriID == R.id.player1_button) {        // Set the uri according to which player was selected
                           player1URI = resultUri;
                       } else {
                           player2URI = resultUri;
                       }

                    } else {
                        Toast.makeText(MainActivity.this, "Couldn't select photos", Toast.LENGTH_LONG).show();
                    }
                }
            });
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.reset_button:
                ratingBar1.setRating(0); // Reset rating bars
                ratingBar2.setRating(0);
                split = 0.5F;            // Reset split
                playButton.setVisibility(View.VISIBLE); // Make play button to appear
                return true;
            case R.id.button_1s:
                playTime = getResources().getInteger(R.integer.progressBar_time_1s);
                return true;
            case R.id.button_2s:
                playTime = getResources().getInteger(R.integer.progressBar_time_2s);
                return true;
            case R.id.button_5s:
                playTime = getResources().getInteger(R.integer.progressBar_time_5s);
                return true;
            case R.id.button_10s:
                playTime = getResources().getInteger(R.integer.progressBar_time_10s);
                return true;
            default:
                return true;
        }
    }
}
//==============================================================================================================================================================