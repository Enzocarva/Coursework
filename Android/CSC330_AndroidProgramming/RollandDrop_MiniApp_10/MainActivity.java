package edu.miami.cs.enzo_carvalho.rollanddropminiapp21;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
//==============================================================================================================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void clickHandler(View view) {
        ImageView imageToAnimate;
        Animation imageAnimation;

        imageToAnimate = (ImageView) findViewById(R.id.image);
        imageToAnimate.setAnimation(null);
        if (view.getId() == R.id.RollButton) {
            Log.i("CHECKINGPOINTS", "got here");
            imageAnimation = AnimationUtils.loadAnimation(this, R.anim.image_roll_and_drop);
            imageToAnimate.startAnimation(imageAnimation);
        }
    }
}
//==============================================================================================================================================================