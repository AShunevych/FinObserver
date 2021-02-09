package com.ashunevich.finobserver.DashboardPackage;


import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


abstract class Dashboard_FragmentUtils extends Dashboard_Fragment{

    protected static String PREFERENCE_NAME ="APP_PREF";
    protected static String BALANCE="BALANCE";
    protected static String INCOME="INCOME";
    protected static String EXPENDITURES="EXPENDITURES";
    protected static String TOTAL_BALANCE = "BALANCE";


   protected static int getImageInt(String type){
       if(type.matches("Income")){
           return 0;
       }
       else{
           return 1;
       }
   }

    protected static String getDate(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
        return df.format(c);
    }

    protected static Double stringToDouble(TextView view){
        return Double.parseDouble(view.getText().toString());
    }

}
