package com.ashunevich.finobserver.transactions;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

abstract class TransactionsUtils {
     protected static void hideProgressBar(ProgressBar bar, TextView textView){
         bar.setVisibility(View.GONE);
         textView.setVisibility(View.GONE);
     }

    protected static String returnChipText(ChipGroup chipGroup){
        String text = null;
        List<Integer> ids = chipGroup.getCheckedChipIds();
        for (Integer id:ids){
            Chip chip = chipGroup.findViewById(id);
            text = chip.getText().toString();
        }
        return text;
    }

    protected static int returnActiveChipId(ChipGroup chipGroup){
        int idChip = 0;
        List<Integer> ids = chipGroup.getCheckedChipIds();
        for (Integer id:ids){
            Chip chip = chipGroup.findViewById(id);
            idChip = chip.getId();
        }
        return idChip;
    }

    protected static double stringToDouble(String string){
         return Double.parseDouble(string);
    }

    protected static int stringToInteger(String string){
        return Integer.parseInt(string);
    }

    protected static String getSelectedItemFromSpinner(Spinner spinner){
         return  spinner.getSelectedItem().toString();
    }

    protected static void setChipGroupUncheck(ChipGroup chipGroup){
        chipGroup.clearCheck();
    }


}
