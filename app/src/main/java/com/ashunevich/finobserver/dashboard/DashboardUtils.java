package com.ashunevich.finobserver.dashboard;


import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ashunevich.finobserver.UtilsPackage.PostPOJO;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.ashunevich.finobserver.UtilsPackage.Utils.getTextFrom;


abstract class DashboardUtils {

     static String PREFERENCE_NAME ="APP_PREF";
    //shared prefs constants
     static final String BALANCE="BALANCE";
     static final String INCOME="INCOME";
     static final String EXPENDITURES="EXPENDITURES";
     static final String TOTAL = "TOTAL";
    //

     static final String KEY_UPDATE = "UPDATE";
     static final String KEY_CREATE = "CREATE";
     static final String KEY_DIALOG = "DIALOG";


   protected static int getImageInt(String type){
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

    protected static String returnString(TextView textView){
        return textView.getText().toString();
    }

    protected static String getDate(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("d MMM, yyyy ", Locale.UK);
        return df.format(c);
    }

    protected static Double textToDouble(TextView view){
        return Double.parseDouble(view.getText().toString());
    }

    protected static String returnStringFromObj(PostPOJO postPOJO) {
        return postPOJO.getZero();
    }


    protected static void enableSubmitIfReady(Button button,EditText text1, EditText text2) {
        button.setEnabled(getTextFrom(text1).length() > 0 && getTextFrom(text2).length() > 0);
    }

}
