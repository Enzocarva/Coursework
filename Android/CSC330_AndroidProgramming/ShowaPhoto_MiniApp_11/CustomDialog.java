package edu.miami.cs.enzo_carvalho.showaphotominiapp8;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import androidx.fragment.app.DialogFragment;

public class CustomDialog extends DialogFragment implements View.OnClickListener {

    ImageView thePhoto;
    View dialogView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle(R.string.choose_photo_button);
        dialogView = inflater.inflate(R.layout.dialog, container);
        ((Button) dialogView.findViewById(R.id.dismiss_button)).setOnClickListener(this);
        thePhoto = (ImageView) dialogView.findViewById(R.id.photo);
        int photoCode;
        photoCode = CustomDialog.this.getArguments().getInt("photo_code");
        thePhoto.setImageResource(photoCode);

        return (dialogView);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dismiss_button:
                dismiss();
                break;
        }
    }
}
