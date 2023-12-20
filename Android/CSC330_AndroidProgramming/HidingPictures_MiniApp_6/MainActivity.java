package edu.miami.cs.enzo_carvalho.hidingpicturesminiapp7;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

//==============================================================================================================================================================
public class MainActivity extends AppCompatActivity {
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerForContextMenu(findViewById(R.id.number_2));
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    public boolean onOptionsItemSelected(MenuItem item) {
        ImageView middleImage = findViewById(R.id.number_2);
        TextView theText = findViewById(R.id.the_text);

        switch (item.getItemId()) {
            case R.id.reset_button:
                middleImage.setVisibility(View.VISIBLE);
                theText.setText(R.string.indifferent_text);
                return true;
            case R.id.like_button:
                theText.setText(R.string.like_text);
                return true;
            case R.id.indifferent_button:
                theText.setText(R.string.indifferent_text);
                return true;
            case R.id.dislike_button:
                theText.setText(R.string.dislike_text);
                return true;
            case R.id.quit_button:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    public boolean onContextItemSelected(MenuItem item) {
        ImageView middleImage = findViewById(R.id.number_2);

        switch (item.getItemId()) {
            case R.id.invisible_button:
                middleImage.setVisibility(View.INVISIBLE);
                return true;
            case R.id.gone_button:
                middleImage.setVisibility(View.GONE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
}
//==============================================================================================================================================================