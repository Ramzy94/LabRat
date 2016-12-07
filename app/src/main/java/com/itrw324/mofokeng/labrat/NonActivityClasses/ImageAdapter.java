package com.itrw324.mofokeng.labrat.NonActivityClasses;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import com.itrw324.mofokeng.labrat.R;
import java.util.Random;

/**
 * Created by Mofokeng on 02-Dec-16.
 */

public class ImageAdapter extends BaseAdapter {
    private Context theContext;
    public static final boolean VEUNUE_IS_OCCUPIED = true;
    private Integer images[];

    public ImageAdapter(Context c) {
        theContext = c;
        images = new Integer[50];
        assignPC();
    }

    public ImageAdapter(Context c, boolean hasClass) {
        theContext = c;
        images = new Integer[50];
        assignPCsClass();
    }

    private void assignPC(){
        Random rndm = new Random();
        for(int o = 0;o<images.length;o++){
            if(rndm.nextInt(2)==0)
                images[o] = R.drawable.workstation_green;
            else
                images[o] = R.drawable.workstation_red2;
        }
    }

    private void assignPCsClass() {
        for (int o = 0; o < images.length; o++)
            images[o] = R.drawable.workstation_red2;
    }



    public int getCount() {
        return images.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(theContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(images[position]);
        return imageView;
    }
}