package edu.miami.cs.enzo_carvalho.whereamiminiapp18;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.speech.tts.TextToSpeech;
import android.widget.TextView;
import java.util.List;

//==============================================================================================================================================================
public class SensorLocatorDecode extends AsyncTask<Location, Void, String> {
    private final Activity theActivity;
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    public SensorLocatorDecode(Activity activity) {
        theActivity = activity;
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    protected String doInBackground(Location... location) {
        return(androidGeodecode(location[0]));
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    protected void onPostExecute(String result) {
        ((TextView)theActivity.findViewById(R.id.location)).setText(result);
        MainActivity.speaker.speak(result, TextToSpeech.QUEUE_FLUSH, null, "MOVED");
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    private String androidGeodecode(Location thisLocation) {

        Geocoder androidGeocoder;
        List<Address> addresses;
        Address firstAddress;
        String addressLine;
        String locationName;
        int index;

        if (Geocoder.isPresent()) {
            androidGeocoder = new Geocoder(theActivity.getApplicationContext());
            try {
                addresses = androidGeocoder.getFromLocation(thisLocation.getLatitude(),
                        thisLocation.getLongitude(),1);
                if (addresses.isEmpty()) {
                    return("ERROR: Unkown location");
                } else {
                    firstAddress = addresses.get(0);
                    locationName = "";
                    index = 0;
                    while ((addressLine = firstAddress.getAddressLine(index)) != null) {
                        locationName += addressLine + ", ";
                        index++;
                    }
                    return (locationName);
                }
            } catch (Exception e) {
                return("ERROR: " + e.getMessage());
            }
        } else {
            return("ERROR: No Geocoder available");
        }
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
}
//==============================================================================================================================================================
