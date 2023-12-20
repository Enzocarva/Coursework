package edu.miami.cs.enzo_carvalho.showaphotominiapp8;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;
import androidx.fragment.app.DialogFragment;
import android.os.Bundle;

//=========================================================================================================================================
public class UIDialogsFragment extends DialogFragment {
//-----------------------------------------------------------------------------------------------------------------------------------------
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder;
        dialogBuilder = new AlertDialog.Builder(getActivity());

        if (this.getArguments().getInt("dialog_type") == MainActivity.LIST_DIALOG) {
            dialogBuilder.setTitle("Choose a photo");
            dialogBuilder.setItems(R.array.photos_list, listListener);
        }
        return dialogBuilder.create();
    }
//-----------------------------------------------------------------------------------------------------------------------------------------
    public interface SetThePicture {
        public void setThePicture(int resourceId);
    }
//-----------------------------------------------------------------------------------------------------------------------------------------
    private DialogInterface.OnClickListener listListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int indexClicked) {

            SetThePicture myActivity;
            myActivity = (SetThePicture)getActivity();

            switch (indexClicked) {
                case 0:
                    myActivity.setThePicture(R.mipmap.ic_launcher);
                    dismiss();
                    break;
                case 1:
                    myActivity.setThePicture(R.mipmap.athens);
                    dismiss();
                    break;
                case 2:
                    myActivity.setThePicture(R.mipmap.monet_beach);
                    dismiss();
                    break;
                case 3:
                    myActivity.setThePicture(R.mipmap.monet_sunset);
                    dismiss();
                    break;
                default:
                    dismiss();
                    break;
            }
            dismiss();
        }
    };
//-----------------------------------------------------------------------------------------------------------------------------------------
}
//=========================================================================================================================================