package edu.miami.cs.enzo_carvalho.whereamiminiapp18;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import java.util.Map;

//==============================================================================================================================================================
public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    Activity mainActivity;
    public static TextToSpeech speaker;
    private FusedLocationProviderClient fusedLocationClient;
    private Location currentLocation = null;
    private LocationRequest locationRequest;
    private boolean speaking;
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPermissions.launch(new String [] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION});
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    private void goOnCreating(boolean havePermissions) {
        if (havePermissions) {
            setContentView(R.layout.activity_main);
            mainActivity = MainActivity.this;
            speaker = new TextToSpeech(this, this);
        }
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            Toast.makeText(this, "I am ready to talk", Toast.LENGTH_SHORT).show();
            speaker.setOnUtteranceProgressListener(listener);
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            locationRequest = new LocationRequest();
            locationRequest.setInterval(5000); // Milliseconds
            locationRequest.setFastestInterval(5000);

            // Start Locating
            locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);
            try {
                fusedLocationClient.requestLocationUpdates(locationRequest, myLocationCallBack, Looper.myLooper());
            } catch (SecurityException e) {
                Toast.makeText(this, "Something went wrong with start locating", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "Could not build text to speech", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    LocationCallback myLocationCallBack = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);

            speaking = false;
            Location newLocation = locationResult.getLastLocation();
            if (newLocation == null) {
                Toast.makeText(MainActivity.this, "Location is null", Toast.LENGTH_SHORT).show();
                return;
            }

            if (currentLocation == null || currentLocation.distanceTo(newLocation) > 150) {
                currentLocation = newLocation;
                speaking = true;
                new SensorLocatorDecode(mainActivity).execute(currentLocation);
            } else {
                if (!speaking)
                    speaker.speak("You haven't moved", TextToSpeech.QUEUE_ADD, null, "NOT_MOVED");
            }

        }
    };
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    private UtteranceProgressListener listener = new UtteranceProgressListener() {
        @Override
        public void onStart(String s) {}

        @Override
        public void onDone(String utteranceID) {
            if (utteranceID.equals("MOVED"))
                speaking = false;
        }

        @Override
        public void onError(String s) {}
    };
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void onDestroy() {
        super.onDestroy();
        fusedLocationClient.removeLocationUpdates(myLocationCallBack);
        speaker.shutdown();
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    private ActivityResultLauncher<String[]> getPermissions = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
                @Override
                public void onActivityResult(Map<String, Boolean> results) {

                    for (String key:results.keySet()) {
                        if (!results.get(key)) {
                            goOnCreating(false);
                        }
                    }
                    goOnCreating(true);
                }
            });
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
}
//==============================================================================================================================================================