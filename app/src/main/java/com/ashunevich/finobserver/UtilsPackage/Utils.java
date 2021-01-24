package com.ashunevich.finobserver.UtilsPackage;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Utils  {

    public static String getDate(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(Calendar.getInstance());
    }

}
