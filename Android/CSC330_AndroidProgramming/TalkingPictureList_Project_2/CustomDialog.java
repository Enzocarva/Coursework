package edu.miami.cs.enzo_carvalho.talking_picture_listproject2;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.DialogFragment;
//==============================================================================================================================================================
public class CustomDialog extends DialogFragment implements View.OnClickListener {
    View dialogView;
    ImageView dialogImage;
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate dialog, get image URI and set in dialog, set onClickListener
        dialogView = inflater.inflate(R.layout.dialog, container);
        ((Button) dialogView.findViewById(R.id.dismissButton)).setOnClickListener(this);
        dialogImage = (ImageView) dialogView.findViewById(R.id.dialogImage);
        Uri imageUri = CustomDialog.this.getArguments().getParcelable("imageUri");
        dialogImage.setImageURI(imageUri);

        return dialogView;
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void onClick(View view) {
        StopTalking myActivity;
        myActivity = (StopTalking) getActivity();
        if (view.getId() == R.id.dismissButton) {
            myActivity.stopTalking();
            dismiss();
        }
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    public interface StopTalking {
        public void stopTalking();
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
}
//==============================================================================================================================================================