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


    //int
    static int intFromImageType(String type){
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

    static int intFromString(String string){
        return Integer.parseInt(string);
    }

    //string
    static String stringFromTextView(TextView textView){
        return textView.getText().toString();
    }

    static String stringDate(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("d MMMM, yyyy ", Locale.UK);
        return df.format(c);
    }

    static String stringSumFromDoubles(double a, double b){return stringSetDoubleFormat (a+b); }

    static String stringExtractionFromDoubles(double a, double b){return String.valueOf(b-a); }

    static String stringFromObject(PostPOJO postPOJO) {
        return postPOJO.getZero();
    }

    static String stringFromActiveChip(ChipGroup chipGroup){
        String text = null;
        List<Integer> ids = chipGroup.getCheckedChipIds();
        for (Integer id:ids){
            Chip chip = chipGroup.findViewById(id);
            text = chip.getText().toString();
        }
        return text;
    }

    static String stringAsTextFromSpinner(Spinner spinner){
        return  spinner.getSelectedItem().toString();
    }

    static String stringSetDoubleFormat(double a) {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
        otherSymbols.setDecimalSeparator('.');
        DecimalFormat formatter = new DecimalFormat("###.##",otherSymbols);
        return (formatter.format(a));
    }

    static String stringFormat(String x){
        double a = Double.parseDouble (x);
        return stringSetDoubleFormat(a);
    }


    //doubles
    static double doubleFromString(String string){
        return Double.parseDouble(string);
    }

    static double doubleSum(double basic , double value){
        return basic+value;
    }

    static double doubleExtraction(double basic , double value){
        return basic-value;
    }

    static double doubleFromTextView(TextView view){
        return Double.parseDouble(view.getText().toString());
    }

    //void
    static void enableSubmitIfReady(Button button,EditText text1, EditText text2) {
        button.setEnabled(getTextFrom(text1).length() > 0 && getTextFrom(text2).length() > 0);
    }

    static void setChipGroupUncheck(ChipGroup chipGroup){
        chipGroup.clearCheck();
    }
}
