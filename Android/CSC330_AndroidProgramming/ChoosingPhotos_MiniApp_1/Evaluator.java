package edu.miami.cs.enzo_carvalho.choosingphotos_miniapp2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ContentInfoCompat;

//=================================================================================================
public class Evaluator extends AppCompatActivity {
//-------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluator);

        Uri picUri = this.getIntent().getParcelableExtra("MyUri");
        ((ImageView)findViewById(R.id.selected_picture)).setImageURI(picUri);
    }
//-------------------------------------------------------------------------------------------------
    public void myClickHandler(View view) {
        Intent booleanIntent;
        booleanIntent = new Intent (Evaluator.this,MainActivity.class);

        switch (view.getId()) {
            case R.id.like_picture:
                // something
                booleanIntent.putExtra("booleanVal",true);
                setResult(RESULT_OK, booleanIntent);
                finish();
                break;
            case R.id.dislike_picture:
                // other
                booleanIntent.putExtra("booleanVal", false);
                setResult(RESULT_OK, booleanIntent);
                finish();
            default:
                break;
        }
    }
//-------------------------------------------------------------------------------------------------
}
//=================================================================================================
