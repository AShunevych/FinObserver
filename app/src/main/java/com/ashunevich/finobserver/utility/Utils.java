package com.ashunevich.finobserver.utility;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.appcompat.app.AlertDialog;
import androidx.test.espresso.idling.CountingIdlingResource;


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

    public static final ExecutorService singleThreadExecutor =
            Executors.newSingleThreadExecutor();

    public static AlertDialog.Builder genericDialogOptions(
    Context context, String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title).setMessage(message);
        return builder;
    }

    //int
    public static int intFromImageType(String type){
       if(type.matches("Income")){
           return 0;
       }
       else if(type.matches("Expenses")){
           return 1;
       }
       else{
           return 2;
       }
   }

    public static int intFromString(String string){
        return Integer.parseInt(string);
    }

    //string
    public static String stringFromTextView(TextView textView){
        return textView.getText().toString();
    }

    public static String stringSumFromDoubles(double a, double b){return stringSetDoubleFormat(a+b); }

    public static String stringExtractionFromDoubles(double a, double b){return String.valueOf(b-a); }

    public static String stringFromObject(PostPOJO postPOJO) {
        return postPOJO.getZero();
    }

    public static String stringFromActiveChip(ChipGroup chipGroup){
        String text = null;
        List<Integer> ids = chipGroup.getCheckedChipIds();
        for(Integer id:ids){
            Chip chip = chipGroup.findViewById(id);
            text = chip.getText().toString();
        }
        return text;
    }

    public static String stringAsTextFromSpinner(Spinner spinner){
        return  spinner.getSelectedItem().toString();
    }

    public static String stringSetDoubleFormat(double a) {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
        otherSymbols.setDecimalSeparator('.');
        DecimalFormat formatter = new DecimalFormat("###.##",otherSymbols);
        return(formatter.format(a));
    }

    public static String stringFormat(String x){
        return stringSetDoubleFormat(Double.parseDouble(x));
    }

    public static String setTransferText(String accountName){
        return "Transfer from " + accountName;
    }

    //doubles
    public static double doubleFromString(String string){
        return Double.parseDouble(string);
    }

    public static double doubleSum(double basic , double value){
        return basic+value;
    }

    public static double doubleExtraction(double basic , double value){
        return basic-value;
    }

    public static double doubleFromTextView(TextView view){
        return Double.parseDouble(view.getText().toString());
    }

    //void
    public static void enableSubmitIfReady(Button button, EditText text1, EditText text2) {
        button.setEnabled(getTextFrom(text1).length() > 0 && getTextFrom(text2).length() > 0);
    }

    public static String stringDate(){
        SimpleDateFormat df = new SimpleDateFormat("d MMMM, yyyy ", Locale.UK);
        return df.format(Calendar.getInstance().getTime());
    }

    public static CountingIdlingResource countingIdlingResource(){
        return new CountingIdlingResource("countingRes");
    }

}
