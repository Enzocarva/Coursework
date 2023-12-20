package edu.miami.cs.enzo_carvalho.timedtextminiapp15;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

//==============================================================================================================================================================
public class MainActivity extends AppCompatActivity {
    private static DataSQLiteDB dataBase;
    private EditText thought;
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataBase = new DataSQLiteDB(this);
        thought = findViewById(R.id.inputText);
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
//    private void goOnCreating(boolean havePermission) {
//        if (havePermission) {
//            setContentView(R.layout.activity_main);
//            dataBase = new DataSQLiteDB(this);
//            thought = findViewById(R.id.inputText);
//        }
//    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void clickHandler(View view) {

        switch (view.getId()) {
            case R.id.saveButton:
                String currentThought = thought.getText().toString();
                long milliSeconds = System.currentTimeMillis();
                SimpleDateFormat formattedDate = new SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault());
                String dateForCurrentThought = milliSeconds + " " + formattedDate.format(milliSeconds);
                ContentValues thoughtData = new ContentValues();
                thoughtData.put("thought_name", currentThought);
                thoughtData.put("date", dateForCurrentThought);
                if (!dataBase.addThought(thoughtData))
                    Toast.makeText(this, "Error adding thought to database", Toast.LENGTH_SHORT).show();
                thought.setText("");
                break;
            case R.id.viewListButton:
                Intent nextActivity = new Intent(MainActivity.this, ShowList.class);
                startActivity(nextActivity);
                break;
            default:
                break;
        }
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void onDestroy() {
        super.onDestroy();
        dataBase.close();
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
//    private ActivityResultLauncher<String[]> getPermissions = registerForActivityResult(
//            new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
//                public void onActivityResult(Map<String, Boolean> results) {
//                    for (String key : results.keySet()) {
//                        if (!results.get(key)) {
//                            goOnCreating(false);
//                        }
//                    }
//                    goOnCreating(true);
//                }
//            });
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
}
//==============================================================================================================================================================