package edu.miami.cs.enzo_carvalho.countmeinminiapp17;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

//==============================================================================================================================================================
public class MainActivity extends AppCompatActivity {
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get default preferences and set the preferences editor
        SharedPreferences currentPreferences;
        currentPreferences = getSharedPreferences(String.valueOf(this.getApplicationContext()), MODE_PRIVATE); // name = android.app.Application@7225491
        SharedPreferences.Editor editor;
        editor = currentPreferences.edit();

        // Extract the number of runs, increment it and put it in the UI
        TextView numRunsUI = findViewById(R.id.theNumber);
        int numRuns = currentPreferences.getInt("NumRuns", 0);
        numRuns++;
        numRunsUI.setText(Integer.toString(numRuns));

        // Make preferences editor, update runs and commit
        editor.putInt("NumRuns", numRuns);
        editor.commit();
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
}
//==============================================================================================================================================================