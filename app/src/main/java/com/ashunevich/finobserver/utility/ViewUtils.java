package com.ashunevich.finobserver.utility;

import android.view.View;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class ViewUtils {
    public static void uiShowSnackBar(View view, String text) {
        Snackbar.make (view, text, BaseTransientBottomBar.LENGTH_SHORT).show ();
    }

    public static int uiReturnActiveChipId(ChipGroup chipGroup) {
        int idChip = 0;
        List<Integer> ids = chipGroup.getCheckedChipIds ();
        for (Integer id : ids) {
            Chip chip = chipGroup.findViewById (id);
            idChip = chip.getId ();
        }
        return idChip;
    }

    public static void uiHideView(View v) {
        v.setVisibility (View.GONE);
    }

    public static void uiShowView(View v) {
        v.setVisibility (View.VISIBLE);
    }

    public static void uiUncheckChipGroup(ChipGroup chipGroup) {
        chipGroup.clearCheck ();
    }

    public static void uiHideHideShow(View x, View y, View z) {
        uiHideView (x);
        uiHideView (y);
        uiShowView (z);
    }

    public static void uIDisableView(View x){
        x.setEnabled (false);
    }
}