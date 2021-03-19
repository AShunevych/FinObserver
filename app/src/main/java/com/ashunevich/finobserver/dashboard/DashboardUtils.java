package com.ashunevich.finobserver.dashboard;


import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.ashunevich.finobserver.UtilsPackage.PostPOJO;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.ashunevich.finobserver.UtilsPackage.Utils.getTextFrom;


abstract class DashboardUtils {

    //shared prefs constants
     static String PREFERENCE_NAME ="APP_PREF";
     static final String BALANCE="BALANCE";
     static final String INCOME="INCOME";
     static final String EXPENDITURES="EXPENDITURES";
     static final String TOTAL = "TOTAL";

    //Operations constants
     static final String KEY_UPDATE = "UPDATE";
     static final String KEY_CREATE = "CREATE";
     static final String KEY_CANCEL = "CANCEL";
     static final String DIALOG_STATIC = "DIALOG";



    static int getImageInt(String type){
       if(type.matches("Income")){
           return 0;
       }
       else if (type.matches("Expenditures")){
           return 1;
       }
       else{
           return 2;
       }
   }


    static String returnString(TextView textView){
        return textView.getText().toString();
    }

    static String getDate(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("d MMM, yyyy ", Locale.UK);
        return df.format(c);
    }

    static String returnSumAsString(double a, double b){return String.valueOf(a+b); }

    static String returnExtractionAsString(double a, double b){return String.valueOf(b-a); }

    static String returnStringFromObj(PostPOJO postPOJO) {
        return postPOJO.getZero();
    }

    static void setChipGroupUncheck(ChipGroup chipGroup){
        chipGroup.clearCheck();
    }

    //string
    static double stringToDouble(String string){
        return Double.parseDouble(string);
    }

    static int stringToInteger(String string){
        return Integer.parseInt(string);
    }

    //ChipGroup
    static String returnChipText(ChipGroup chipGroup){
        String text = null;
        List<Integer> ids = chipGroup.getCheckedChipIds();
        for (Integer id:ids){
            Chip chip = chipGroup.findViewById(id);
            text = chip.getText().toString();
        }
        return text;
    }

    static int returnActiveChipId(ChipGroup chipGroup){
        int idChip = 0;
        List<Integer> ids = chipGroup.getCheckedChipIds();
        for (Integer id:ids){
            Chip chip = chipGroup.findViewById(id);
            idChip = chip.getId();
        }
        return idChip;
    }

    static String getSelectedItemFromSpinner(Spinner spinner){
        return  spinner.getSelectedItem().toString();
    }

    static double sumDouble (double basic , double value){
        return basic+value;
    }

    static double extractDouble (double basic , double value){
        return basic-value;
    }

    static double textToDouble(TextView view){
        return Double.parseDouble(view.getText().toString());
    }


    static void enableSubmitIfReady(Button button,EditText text1, EditText text2) {
        button.setEnabled(getTextFrom(text1).length() > 0 && getTextFrom(text2).length() > 0);
    }

    static String formatValue(double a) {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
        otherSymbols.setDecimalSeparator('.');
        DecimalFormat formatter = new DecimalFormat("###.##",otherSymbols);
        return (formatter.format(a));
    }

}
