package edu.miami.cs.enzo_carvalho.tic_tac_toeproject1;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.FileNotFoundException;
import java.io.InputStream;

//==============================================================================================================================================================
public class RunGame extends AppCompatActivity {

    // Global Variables
    private ProgressBar progressBar;
    private int progressBarClickTime;
    Handler handler = new Handler();
    private int currentPlayer;
    private int numberOfMoves;
    private int currentID;
    private int winner;

    // Global resource data
    private TextView displayPlayerName;
    private int[][] playingBoard;
    private final int PLAYER_1 = 1;
    private final int PLAYER_2 = 2;

    // Global variables from intent
    private int playTime;
    private String player1Name;
    private String player2Name;
    private Uri player1Image;
    private Uri player2Image;

//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.run_game);

        // Get player name
        displayPlayerName = findViewById(R.id.player_name_game_screen);

        // Get info sent from the intent
        player1Name = this.getIntent().getStringExtra("P1Name");
        player2Name = this.getIntent().getStringExtra("P2Name");
        currentPlayer = this.getIntent().getIntExtra("Who_Starts", 0);
        playTime = this.getIntent().getIntExtra("timeSelected", getResources().getInteger(R.integer.progressBar_time_10s));
        player1Image = this.getIntent().getParcelableExtra("P1Image");
        player2Image = this.getIntent().getParcelableExtra("P2Image");

        // Initialize progress bar and bar click time
        progressBar = findViewById(R.id.progressBar);
        progressBarClickTime = getResources().getInteger(R.integer.progressBar_click_time);

        // Initialize matrix/grid of buttons
        playingBoard = new int[3][3];
        numberOfMoves = 0;

        // Start the game
        startPlayer();
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void startPlayer() {

        // Fill progress bar
        progressBar.setMax(playTime);
        progressBar.setProgress(progressBar.getMax());

        // Set player names based on who's turn it is
        if (currentPlayer == PLAYER_1) {
            displayPlayerName.setText(player1Name);
            findViewById(R.id.player1_icon).setVisibility(View.VISIBLE);
            findViewById(R.id.player2_icon).setVisibility(View.INVISIBLE);
            currentID = R.id.player1_icon;
        } else {
            displayPlayerName.setText(player2Name);
            findViewById(R.id.player2_icon).setVisibility(View.VISIBLE);
            findViewById(R.id.player1_icon).setVisibility(View.INVISIBLE);
            currentID = R.id.player2_icon;
        }

        // Set player icons
        setImageButtons(player1Image, R.id.player1_icon);
        setImageButtons(player2Image, R.id.player2_icon);

        // Run the game
        gameProgress.run();
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    private final Runnable gameProgress = new Runnable() {

        public void run() {

            // Start/Reduce progress bar and switch the players' turn
            handler.removeCallbacks(gameProgress);
            progressBar.setProgress(progressBar.getProgress()-progressBarClickTime);
            if (progressBar.getProgress() <= 0) {
                if (currentPlayer == PLAYER_1) currentPlayer = PLAYER_2;
                else currentPlayer = PLAYER_1;
                startPlayer();
            }

            if (!handler.postDelayed(gameProgress, progressBarClickTime))
                Log.e("ERROR", "Cannot PostDelayed");
        }
    };
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void gameClickHandler(View view) {
        switch (view.getId()) {
            case R.id.button_00:
                checkButton(0,0, R.id.button_00);
                break;
            case R.id.button_01:
                checkButton(0,1, R.id.button_01);
                break;
            case R.id.button_02:
                checkButton(0,2, R.id.button_02);
                break;
            case R.id.button_10:
                checkButton(1, 0, R.id.button_10);
                break;
            case R.id.button_11:
                checkButton(1,1, R.id.button_11);
                break;
            case R.id.button_12:
                checkButton(1,2, R.id.button_12);
                break;
            case R.id.button_20:
                checkButton(2,0, R.id.button_20);
                break;
            case R.id.button_21:
                checkButton(2,1, R.id.button_21);
                break;
            case R.id.button_22:
                checkButton(2,2, R.id.button_22);
                break;
            default:
                break;
        }
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Records what button out of the grid was selected
    public void checkButton(int x, int y, int chosenButton) {

        // Record the move based on x and y coordinates given and make that button false and set the foreground of the button based on the player icon
        if (playingBoard[x][y] == 0) {
            numberOfMoves++;
            playingBoard[x][y] = currentPlayer; // Record move
            findViewById(chosenButton).setClickable(false); // Make the button un-clickable
            findViewById(chosenButton).setForeground(findViewById(currentID).getForeground()); // Set button to player icon
        }

        // Only check for wins if even possible, so at least 3 buttons have been selected
        if (numberOfMoves >= 3) checkForWins(x, y);

        // Switch players and start next turn
        if (currentPlayer == PLAYER_1) currentPlayer = PLAYER_2;
        else currentPlayer = PLAYER_1;
        startPlayer();
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Checks if any of the players won based on the most recent recorded move
    public void checkForWins(int row, int col) {

        int rowWin, colWin, diagonalWin, antiDiagonalWin;
        rowWin = colWin = diagonalWin = antiDiagonalWin = 0;

        for (int index = 0; index < 3; index++) {
            // Row case win
            if (playingBoard[row][index] == currentPlayer) rowWin++;

            // Colum case win
            if (playingBoard[index][col] == currentPlayer) colWin++;

            // Diagonal case win
            if (playingBoard[index][index] == currentPlayer) diagonalWin++;

            // Anti-Diagonal case win
            if (playingBoard[index][2 - index] == currentPlayer) antiDiagonalWin++;
        }

        // Make intent to send winner info to MainActivity
        Intent main;
        main = new Intent(RunGame.this, MainActivity.class);

        // If someone won, send info through intent and finish the game
        if (rowWin == 3 || colWin == 3 || diagonalWin == 3 || antiDiagonalWin == 3) {
            setResult(RESULT_OK, main);
            winner = currentPlayer;
            main.putExtra("Winner", winner);
            finish();
        }

        // If the game was a draw
        if (numberOfMoves >= 9) {
            setResult(RESULT_CANCELED, main);
            finish();
        }
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void setImageButtons(Uri image, int resourceID) {

        // Transform a uri into a drawable with this try catch block
        if (image != null) {
            Context context = RunGame.this;
            Drawable drawable = null;
            try {
                drawable = Drawable.createFromStream(context.getContentResolver().openInputStream(image), image.toString());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // Set the player icons as the chosen image (or default if no image was chosen)
            findViewById(resourceID).setForeground(drawable);
        }
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(gameProgress);
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
}
//==============================================================================================================================================================