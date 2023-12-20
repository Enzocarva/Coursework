package edu.miami.cs.enzo_carvalho.listphotosminiapp9;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.HashMap;

//==============================================================================================================================================================
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private MyListAdapter listAdapter;
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listAdapter = makeMyListAdapter();
        ((ListView)findViewById(R.id.countries_list)).setAdapter(listAdapter);
        ((ListView)findViewById(R.id.countries_list)).setOnItemClickListener(this);
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    private MyListAdapter makeMyListAdapter() {
        ArrayList<HashMap<String, Object>> rows;
        HashMap<String, Object> oneRow;
        String[] countries;
        String[] fromHashMapFieldNames = {"name", "image"};
        int[] toListFieldIds = {R.id.country_name, R.id.country_image};

        countries = getResources().getStringArray(R.array.countries_list);
        rows = new ArrayList<>();
        for (int i = 0; i < countries.length; i++) {
            oneRow = new HashMap<>();
            oneRow.put("name", countries[i]);
            oneRow.put("image", R.mipmap.thumbs_up);
            rows.add(oneRow);
        }
        return new MyListAdapter(this, rows, R.layout.list_row, fromHashMapFieldNames, toListFieldIds);
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int newPictureID = listAdapter.swapFace(position);

        ((ImageView)findViewById(R.id.big_image)).setImageResource(newPictureID);
        ((ImageView)view.findViewById(R.id.country_image)).setImageResource(newPictureID);
    }

}
//==============================================================================================================================================================