package edu.miami.cs.enzo_carvalho.listphotosminiapp9;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//==============================================================================================================================================================
public class MyListAdapter extends SimpleAdapter {
    private int[] faceIDs;
    private Context viewContext;
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    public MyListAdapter(Context context, ArrayList<HashMap<String, Object>> rows, int list_rowFileId, String[] fromHashMapFieldNames, int[] toListFieldIds) {
        super(context, rows, list_rowFileId, fromHashMapFieldNames, toListFieldIds);

        viewContext = context;
        faceIDs = new int[getCount()];
        Arrays.fill(faceIDs, R.mipmap.thumbs_up);
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    public int swapFace(int position) {

        if (faceIDs[position] == R.mipmap.thumbs_up)
            return (faceIDs[position] = R.mipmap.thumbs_down);

        return (faceIDs[position] = R.mipmap.thumbs_up);
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView image;
        View view;

        view = super.getView(position, convertView, parent);
        image = (ImageView)view.findViewById(R.id.country_image);
        image.setImageDrawable(ResourcesCompat.getDrawable(viewContext.getResources(),faceIDs[position],null));

        return view;
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
}
//==============================================================================================================================================================