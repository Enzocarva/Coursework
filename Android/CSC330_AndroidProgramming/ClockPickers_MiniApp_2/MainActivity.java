package edu.miami.cs.enzo_carvalho.clockpickersminiapp5;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;
//==============================================================================================================================================================
public class MainActivity extends AppCompatActivity implements DatePicker.OnDateChangedListener, TimePicker.OnTimeChangedListener {
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    private DatePicker datePicker;
    private TimePicker timePicker;
    Handler myHandler = new Handler();
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        datePicker = findViewById(R.id.date_picker);
        datePicker.init(datePicker.getYear(), datePicker.getMonth(),datePicker.getDayOfMonth(),this);
        timePicker = findViewById(R.id.time_picker);
        timePicker.setOnTimeChangedListener(this);
        myProgresser.run();
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    private final Runnable myProgresser = new Runnable() {
        @Override
        public void run() {
            GregorianCalendar calendar;

            calendar = new GregorianCalendar(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), timePicker.getHour(), timePicker.getMinute());
            calendar.add(Calendar.SECOND, 60);
            datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            timePicker.setHour(calendar.get(Calendar.HOUR_OF_DAY));
            timePicker.setMinute(calendar.get(Calendar.MINUTE));

            if (!myHandler.postDelayed(myProgresser, 60000))
                Log.e("ERROR", "Cannot postDelayed");
        }
    };
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    protected void onDestroy() {

        super.onDestroy();
        myHandler.removeCallbacks(myProgresser);
    }
}
//==============================================================================================================================================================