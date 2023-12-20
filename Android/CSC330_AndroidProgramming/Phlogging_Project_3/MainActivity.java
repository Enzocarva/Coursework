package edu.miami.cs.enzo_carvalho.phloggingproject3;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.Priority;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

//==============================================================================================================================================================
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    public static DataRoomDB phloggDB;
    public static final String DATABASE_NAME = "PhloggingRoom.db";
    private Location currentLocation = null;
    private LocationRequest locationRequest;
    private FusedLocationProviderClient fusedLocationClient;
    private DataRoomEntity currentDbEntity;
    private List<DataRoomEntity> dataRoomEntities;
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPermissions.launch(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CAMERA});
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    private void goOnCreating(boolean havePermissions) {

        // Build DataBase, Fill list view
        if (havePermissions) {
            setContentView(R.layout.activity_main);
            phloggDB = Room.databaseBuilder(getApplicationContext(), DataRoomDB.class, DATABASE_NAME).allowMainThreadQueries().build();

            fillListView();

            // Start locating
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            locationRequest = new LocationRequest();
            locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(5000); // Milliseconds
            locationRequest.setFastestInterval(5000);
            try {
                fusedLocationClient.requestLocationUpdates(locationRequest, myLocationCallBack, Looper.myLooper());
            } catch (SecurityException e) {
                Toast.makeText(this, "Something went wrong with start locating in goOnCreating()", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    private void fillListView() {

        // Get list of DB entries
        dataRoomEntities = phloggDB.daoAccess().fetchAllPhloggs();
        if (dataRoomEntities.isEmpty()) {
            Toast.makeText(this, "Database is empty", Toast.LENGTH_LONG).show();
        }
        assert dataRoomEntities != null; // to make sure the list updates (especially when it is null)

        // Convert list to array list of hash maps
        ArrayList<HashMap<String, Object>> listRows = new ArrayList<>();
        HashMap<String, Object> oneListRow;

        for (int i = 0; i < dataRoomEntities.size(); i++) {
            oneListRow = new HashMap<>();
            currentDbEntity = dataRoomEntities.get(i);
            oneListRow.put("title", currentDbEntity.getTitle());
            oneListRow.put("photoUri", currentDbEntity.getPhotoUri());
            oneListRow.put("date", currentDbEntity.getTime());
            listRows.add(oneListRow);
        }

        // Create and Set simple adapter and Set click listener
        String[] from = {"title", "photoUri", "date"};
        int[] to = {R.id.listTitle, R.id.thumbnail, R.id.listDateAndTime};
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, listRows, R.layout.list_item, from, to);
        ListView theList = findViewById(R.id.theList);
        theList.setAdapter(simpleAdapter);
        theList.setOnItemClickListener(this);

        // Set View Binder
        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                switch (view.getId()) {
                    case R.id.listTitle:
                        String title = (String) data;
                        ((TextView)view).setText(title);
                        break;
                    case R.id.thumbnail:
                        Uri thumbnail = Uri.parse(textRepresentation);
                        ((ImageView)view).setImageURI(thumbnail);
                        break;
                    case R.id.listDateAndTime:
                        String dateAndTime = (String) data;
                        ((TextView)view).setText(dateAndTime);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void clickHandler(View view) {

        // Get current date/time and location and put them in an intent, launch display activity
        if (view.getId() == R.id.phloggButton) {

            // Get current time and format it
            long milliSeconds = System.currentTimeMillis();
            SimpleDateFormat formattedDate = new SimpleDateFormat("H:mm:ss EEEE, MMMM d, yyyy", Locale.getDefault());
            String currentDateAndTime = formattedDate.format(milliSeconds);

            Intent displayActivityIntent = new Intent(MainActivity.this, DisplayActivity.class);
            displayActivityIntent.putExtra("date", currentDateAndTime);
            displayActivityIntent.putExtra("latitude", currentLocation.getLatitude());
            displayActivityIntent.putExtra("longitude", currentLocation.getLongitude());
            displayActivity.launch(displayActivityIntent);
        }
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        // put item time into Intent
        Intent displayActivityIntent = new Intent(MainActivity.this, DisplayActivity.class);
        displayActivityIntent.putExtra("date",dataRoomEntities.get(position).getTime());
        displayActivity.launch(displayActivityIntent);
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    ActivityResultLauncher<Intent> displayActivity = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {

                if (result.getResultCode() == RESULT_OK)
                    fillListView();
            }
        });
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    LocationCallback myLocationCallBack = new LocationCallback() {
    @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);

            // Save the location if it's not null
            Location newLocation = locationResult.getLastLocation();
            if (newLocation == null) {
                Toast.makeText(MainActivity.this, "Location is null in myLocationCallBack()", Toast.LENGTH_SHORT).show();
                return;
            }
            currentLocation = newLocation;
        }
    };
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void onDestroy() {
        super.onDestroy();
        phloggDB.close();
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