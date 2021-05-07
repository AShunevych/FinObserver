package com.ashunevich.finobserver.utils;

import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Utils {
    public static String getTextFrom(EditText text) {
        return text.getText().toString().trim();
    }

    public static int getSelectedItemOnSpinnerPosition(Spinner spinner){
        return spinner.getSelectedItemPosition();
    }

    public static final ExecutorService singleThreadExecutor =
            Executors.newSingleThreadExecutor();

    public static int returnActiveChipId(ChipGroup chipGroup){
        int idChip = 0;
        List<Integer> ids = chipGroup.getCheckedChipIds();
        for (Integer id:ids){
            Chip chip = chipGroup.findViewById(id);
            idChip = chip.getId();
        }
        return idChip;
    }
}
