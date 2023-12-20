package edu.miami.cs.enzo_carvalho.facinghomeminiapp19;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.Manifest;
import android.os.Looper;
import android.os.Vibrator;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import java.util.Map;

//==============================================================================================================================================================
public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private Vibrator buzzer;
    private ToneGenerator beeper;
    private ProgressBar progressBar;
    private SensorManager sensorManager;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private boolean alreadyBeeping = false;
    private float[] magneticField = new float[3];
    private boolean magneticFieldAvailable;
    private float[] gravity = new float[3];
    private boolean gravityAvailable;
    private float[] orientation = new float[3];
    private boolean orientationAvailable;
    private int previousDegreesOffHome;
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPermissions.launch(new String[] {Manifest.permission.VIBRATE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION});
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void goOnCreating(boolean havePermission) {

        if (havePermission) {
            setContentView(R.layout.activity_main);

            sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            buzzer = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            beeper = new ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.MAX_VOLUME);


            progressBar = findViewById(R.id.progressBar);
            progressBar.setProgress(progressBar.getMin());

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            locationRequest = new LocationRequest();
            locationRequest.setInterval(2000);
            locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);
            try {
                fusedLocationClient.requestLocationUpdates(locationRequest, myLocationCallBack, Looper.myLooper());
            } catch (SecurityException e) {
                Toast.makeText(this, "Something went wrong with start locating", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "Not enough permissions to run the app", Toast.LENGTH_SHORT).show();
        }
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    LocationCallback myLocationCallBack = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {

            super.onLocationResult(locationResult);
            fusedLocationClient.removeLocationUpdates(myLocationCallBack);

            Location birthPlace = new Location("HOME");
            birthPlace.setLatitude(-25.441105d);
            birthPlace.setLongitude(-49.276855d);

            if (!MainActivity.this.startSensor(2) || !MainActivity.this.startSensor(1)) {
                Toast.makeText(MainActivity.this, "Can't get orientation", Toast.LENGTH_SHORT).show();
                MainActivity.this.finish();
            }

            Location currentLocation = locationResult.getLastLocation();
            if (currentLocation == null) {
                Toast.makeText(MainActivity.this, "Location is null", Toast.LENGTH_SHORT).show();
                return;
            }
            birthPlace.bearingTo(currentLocation);

            magneticFieldAvailable = startSensor(Sensor.TYPE_MAGNETIC_FIELD);
            gravityAvailable = startSensor(Sensor.TYPE_ACCELEROMETER);
            orientationAvailable = magneticFieldAvailable && gravityAvailable;
        }
    };
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    private boolean startSensor(int sensorType) {

        if (sensorManager.getSensorList(sensorType).isEmpty()) {
            return(false);
        } else {
            sensorManager.registerListener(this,sensorManager.getDefaultSensor(sensorType),
                    SensorManager.SENSOR_DELAY_GAME);
            return(true);
        }
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void onSensorChanged(SensorEvent event) {

        boolean gravityChanged,magneticFieldChanged,orientationChanged;
        float[] rotation = new float[9];
        float[] inclination = new float[9];
        float[] newOrientation = new float[3];
        long[] gettingCloseBuzz = {0, 150, 50, 150};
        previousDegreesOffHome = 180;

        gravityChanged = magneticFieldChanged = orientationChanged = false;
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                gravityChanged = arrayCopyChangeTest(event.values,gravity,
                        Float.parseFloat(getResources().getString(edu.miami.cs.enzo_carvalho.facinghomeminiapp19.R.string.minimum_gravity_change)));
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                magneticFieldChanged = arrayCopyChangeTest(event.values,magneticField,
                        Float.parseFloat(getResources().getString(edu.miami.cs.enzo_carvalho.facinghomeminiapp19.R.string.minimum_magnetic_change)));
                break;
            default:
                break;
        }

        if ((gravityChanged || magneticFieldChanged) &&
    //----Transform from the device coordinate system to the world's coordinate system
                SensorManager.getRotationMatrix(rotation,inclination,gravity,magneticField)) {
            SensorManager.getOrientation(rotation,newOrientation);
            newOrientation[0] = (float)Math.toDegrees(newOrientation[0]);
            newOrientation[1] = (float)Math.toDegrees(newOrientation[1]);
            newOrientation[2] = (float)Math.toDegrees(newOrientation[2]);
            orientationChanged = arrayCopyChangeTest(newOrientation,orientation,Float.parseFloat(
                    getResources().getString(edu.miami.cs.enzo_carvalho.facinghomeminiapp19.R.string.minimum_orientation_change)));
        }

        if (!orientationChanged)
                return;

        // Check if phone is flat
        if (Math.abs(orientation[1]) > Float.parseFloat(getResources().getString(R.string.minimum_orientation_change)) ||
                Math.abs(orientation[2]) > Float.parseFloat(getResources().getString(R.string.minimum_orientation_change))) {
            Toast.makeText(this, "Phone is not flat - no meaningful compass bearing", Toast.LENGTH_SHORT).show();
        } else {
            float[] fArr = this.orientation;
            float f = fArr[0];
            float f2 = newOrientation[0];
            int degreesOffHome = (int) Math.min(((f - f2) + 360.0f) % 360.0f, ((f2 - fArr[0]) + 360.0f) % 360.0f);
            if (degreesOffHome != previousDegreesOffHome) {
                progressBar.setProgress(degreesOffHome);
                if (degreesOffHome < 2 && previousDegreesOffHome >= 2) {
                    buzzer.vibrate(gettingCloseBuzz, -1);
                    Toast.makeText(this, "Bzzzzzzzz", Toast.LENGTH_LONG).show();
                }
                previousDegreesOffHome = degreesOffHome;
            }
        }
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        String sensorStatus;

        switch (accuracy) {
            case SensorManager.SENSOR_STATUS_NO_CONTACT:
                sensorStatus = "NO CONTACT";
                break;
            case SensorManager.SENSOR_STATUS_UNRELIABLE:
                sensorStatus = "UNRELIABLE";
                break;
            case SensorManager.SENSOR_STATUS_ACCURACY_LOW:
                sensorStatus = "LOW";
                break;
            case SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM:
                sensorStatus = "MEDUIM";
                break;
            case SensorManager.SENSOR_STATUS_ACCURACY_HIGH:
                sensorStatus = "HIGH";
                break;
            default:
                sensorStatus = "UNKNOWN";
                break;
        }
        Toast.makeText(this,"The accuracy has changed for " + sensor.getName() + " to " +
                sensorStatus,Toast.LENGTH_SHORT).show();
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    private boolean arrayCopyChangeTest(float[] from,float[] to,float amountForChange) {

        int copyIndex;
        boolean changed = false;

        for (copyIndex=0;copyIndex < to.length;copyIndex++) {
            if (Math.abs(from[copyIndex] - to[copyIndex]) > amountForChange) {
                changed = true;
            }
        }
        if (changed) {
            for (copyIndex = 0; copyIndex < to.length; copyIndex++) {
                to[copyIndex] = from[copyIndex];
            }
        }
        return(changed);
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void onDestroy() {
        super.onDestroy();
        fusedLocationClient.removeLocationUpdates(myLocationCallBack);
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