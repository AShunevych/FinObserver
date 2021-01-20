package com.ashunevich.finobserver.UtilPackage;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.ashunevich.finobserver.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CustomSpinnerAdapter extends BaseAdapter {

    private final ArrayList<Drawable> spinnerImages;
    Context mContext;
    LayoutInflater mInflater;

    public CustomSpinnerAdapter(@NonNull Context context, ArrayList<Drawable> images) {
        this.spinnerImages = images;
        mInflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return spinnerImages.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ImageItem item = new ImageItem();
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.custom_account_spinneritem, parent, false);
            item.image = (ImageView) convertView.findViewById(R.id.imIcon);
            convertView.setTag(item);
        }
        else{
            item = (ImageItem) convertView.getTag();
        }
        item.image.setImageDrawable(spinnerImages.get(position));


        return convertView;

    }

        @Override
        public View getDropDownView ( int position, @Nullable View convertView,
        @NonNull ViewGroup parent){
            return getView(position, convertView, parent);
        }



    private static class ImageItem {

        public void setImage(ImageView image) {
            this.image = image;
        }

        ImageView image;
    }
}


