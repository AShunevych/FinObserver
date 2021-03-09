package com.ashunevich.finobserver.dashboard;


import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.ashunevich.finobserver.UtilsPackage.PostPOJO;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

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

    //Operations keys constants
     static final String KEY_UPDATE = "UPDATE";
     static final String KEY_CREATE = "CREATE";
     static final String KEY_DIALOG = "DIALOG";


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

    static String returnStringFromObj(PostPOJO postPOJO) {
        return postPOJO.getZero();
    }

    protected static void setChipGroupUncheck(ChipGroup chipGroup){
        chipGroup.clearCheck();
    }

    //string
    protected static double stringToDouble(String string){
        return Double.parseDouble(string);
    }

    protected static int stringToInteger(String string){
        return Integer.parseInt(string);
    }

    //ChipGroup
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

    protected static String getSelectedItemFromSpinner(Spinner spinner){
        return  spinner.getSelectedItem().toString();
    }



    static Double textToDouble(TextView view){
        return Double.parseDouble(view.getText().toString());
    }


    static void enableSubmitIfReady(Button button,EditText text1, EditText text2) {
        button.setEnabled(getTextFrom(text1).length() > 0 && getTextFrom(text2).length() > 0);
    }

}
