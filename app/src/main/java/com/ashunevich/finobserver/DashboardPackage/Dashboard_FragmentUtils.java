package com.ashunevich.finobserver.DashboardPackage;


import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


abstract class Dashboard_FragmentUtils extends Dashboard_Fragment{

    public static String PREFERENCE_NAME ="APP_PREF";
    public static String BALANCE="BALANCE";
    public static String INCOME="INCOME";
    public static String EXPENDITURES="EXPENDITURES";



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
