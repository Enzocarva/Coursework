package edu.miami.cs.enzo_carvalho.showaphotominiapp8;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

//=========================================================================================================================================
public class MainActivity extends AppCompatActivity implements UIDialogsFragment.SetThePicture {
//-----------------------------------------------------------------------------------------------------------------------------------------
    public static final int LIST_DIALOG = 1;
//-----------------------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
//-----------------------------------------------------------------------------------------------------------------------------------------
    public void myClickHandler(View view) {
        Bundle bundleToFragment;
        UIDialogsFragment theDialogsFragment;
        bundleToFragment = new Bundle();

        switch (view.getId()) {
            case R.id.choose_photo_button:
                bundleToFragment.putInt("dialog_type", LIST_DIALOG);
                break;
            case R.id.exit_button:
                finish();
                break;
            default:
                break;
        }
        theDialogsFragment = new UIDialogsFragment();
        theDialogsFragment.setArguments(bundleToFragment);
        theDialogsFragment.show(getSupportFragmentManager(), "my_fragment");
    }
//-----------------------------------------------------------------------------------------------------------------------------------------
    public void setThePicture(int resourceId) {

        Bundle bundleToFragment;
        bundleToFragment = new Bundle();
        bundleToFragment.putInt("photo_code", resourceId);

        CustomDialog customFrag;
        customFrag = new CustomDialog();
        customFrag.setArguments(bundleToFragment);
        customFrag.show(getSupportFragmentManager(), "custom_fragment");
    }
}
//=========================================================================================================================================
