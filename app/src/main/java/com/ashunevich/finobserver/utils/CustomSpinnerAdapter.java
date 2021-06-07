package com.ashunevich.finobserver.utils;

import android.content.res.TypedArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ashunevich.finobserver.dashboard.AccountDialog;
import com.ashunevich.finobserver.databinding.CustomAccountSpinneritemBinding;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CustomSpinnerAdapter extends BaseAdapter {
    AccountDialog activity;
    private final TypedArray spinnerImages;


    public CustomSpinnerAdapter(@NonNull AccountDialog dialog, TypedArray images ) {
        this.spinnerImages = images;
        this.activity = dialog;

    }

    @Override
    public int getCount() {
        return spinnerImages.length ();
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

        CustomAccountSpinneritemBinding binding;
        if (convertView == null) {
            binding = CustomAccountSpinneritemBinding.inflate(activity.getLayoutInflater (), parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        }
        else{
            binding = (CustomAccountSpinneritemBinding) convertView.getTag();
        }
        binding.imIcon.setImageResource (spinnerImages.getResourceId (position,0));

        return convertView;
    }

        @Override
        public View getDropDownView ( int position, @Nullable View convertView,
        @NonNull ViewGroup parent){
            return getView(position, convertView, parent);
        }

}


