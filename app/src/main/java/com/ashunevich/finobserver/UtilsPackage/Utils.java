package com.ashunevich.finobserver.UtilsPackage;

import android.widget.EditText;
import android.widget.Spinner;


public class Utils {
    public static String getTextFrom(EditText text) {
        return text.getText().toString().trim();
    }

    public static int getSelectedItemOnSpinnerPosition(Spinner spinner){
        return spinner.getSelectedItemPosition();
    }

}
