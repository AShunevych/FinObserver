package com.ashunevich.finobserver.DashboardAccountPackage;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.ashunevich.finobserver.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.core.content.ContextCompat;

abstract class Dashboard_FragmentUtils extends Dashboard_Fragment{

    public static String PREFERENCE_NAME ="APP_PREF";
    public static String BALANCE="BALANCE";
    public static String INCOME="INCOME";
    public static String EXPENDITURES="EXPENDITURES";

    protected static Drawable getTypeImage(String type, Context context){
        Drawable drawable;
        if(type.matches("Income")){
            drawable = ContextCompat.getDrawable(context, R.drawable.ic_arrow_drop_up);
        }
        else {
            drawable = ContextCompat.getDrawable(context,R.drawable.ic_arrow_drop_down);
        }
        return drawable;
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
