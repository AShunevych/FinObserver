package com.ashunevich.finobserver.TransactionsPackage;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

class Transaction_Utils {
     protected static void hideProgressBar(ProgressBar bar, TextView textView){
         bar.setVisibility(View.INVISIBLE);
         textView.setVisibility(View.INVISIBLE);
     }
}
