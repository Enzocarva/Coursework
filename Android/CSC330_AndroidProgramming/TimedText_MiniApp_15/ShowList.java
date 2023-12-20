package edu.miami.cs.enzo_carvalho.timedtextminiapp15;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

//==============================================================================================================================================================
public class ShowList extends AppCompatActivity {
    private static DataSQLiteDB dataBase;
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);
        dataBase = new DataSQLiteDB(this);
        fillTheList();
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    private void fillTheList() {
        int[] displayViews = {R.id.thoughtText, R.id.dateText};
        String[] displayFields = {"thought_name", "date"};

        SimpleCursorAdapter listAdapter;
        ListView theList;

        theList = (ListView) findViewById(R.id.the_list);
        listAdapter = new SimpleCursorAdapter(this, R.layout.list_item, dataBase.fetchAllData(), displayFields, displayViews, 0);
        theList.setAdapter(listAdapter);
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
}
//==============================================================================================================================================================