package com.ashunevich.finobserver.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.appcompat.app.AlertDialog;


public class Utils {

    public static String getTextFrom(EditText text) {
        return text.getText().toString().trim();
    }

    public static int getSelectedItemOnSpinnerPosition(Spinner spinner){
        return spinner.getSelectedItemPosition();
    }

    public static void initAfterDelay(Runnable runnable,int delay){
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(runnable, delay);
    }

    public static void showSnackBar (View view, String text){
         Snackbar.make (view,text, BaseTransientBottomBar.LENGTH_SHORT).show ();
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

    public static AlertDialog.Builder genericDialogOptions(
    Context context, String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title).setMessage(message);
        return builder;
    }

    public static void uiHideView(View v){
        v.setVisibility (View.GONE);
    }

    public static void uiShowView(View v){
        v.setVisibility (View.VISIBLE);
    }

    public static void uiUncheckChipGroup(ChipGroup chipGroup){
        chipGroup.clearCheck();
    }

    public static void uiHideHideShow(View x, View y, View z) {
        uiHideView(x);
        uiHideView(y);
        uiShowView(z);
    }

}
