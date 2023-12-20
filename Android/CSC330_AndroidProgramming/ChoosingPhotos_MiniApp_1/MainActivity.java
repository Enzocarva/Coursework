package edu.miami.cs.enzo_carvalho.choosingphotos_miniapp2;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
//=================================================================================================
public class MainActivity extends AppCompatActivity {
//-------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startGallery.launch("image/*");
    }
//-------------------------------------------------------------------------------------------------
    ActivityResultLauncher<String> startGallery = registerForActivityResult(
            new ActivityResultContracts.GetContent(),new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri resultUri) {
                    if (resultUri != null) {
                        Intent nextActivity;
                        nextActivity = new Intent (MainActivity.this,Evaluator.class); // Send an Intent from MainActivity to Evaluator class/Activity
                        nextActivity.putExtra("MyUri", resultUri); // Send the Uri to Evaluator activity
                        startEvaluator.launch(nextActivity);
                    } else {
                        Toast.makeText(MainActivity.this, "Choose a photo you sissy", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            });
//-------------------------------------------------------------------------------------------------
    ActivityResultLauncher<Intent> startEvaluator = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Intent checkIntent;
                    boolean intentValue;

                    if (result.getResultCode() == Activity.RESULT_OK) {
                        checkIntent = result.getData();
                        intentValue = checkIntent.getBooleanExtra("booleanVal", false);
                    } else {
                        intentValue = false;
                    }

                    if (intentValue) {
                        Toast.makeText(MainActivity.this, "Great Choice", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        startGallery.launch("image/*");
                    }
                }
            });
//-------------------------------------------------------------------------------------------------
}
//=================================================================================================