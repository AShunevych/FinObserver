package com.ashunevich.finobserver.transactions;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import com.ashunevich.finobserver.R;

import com.ashunevich.finobserver.databinding.TransacationStatisticActivityBinding;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



import androidx.appcompat.app.AppCompatActivity;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;


public class TransactionStatisticActivity extends AppCompatActivity {
    TransacationStatisticActivityBinding binding;
    ArrayList<String> sortedCategoriesList = new ArrayList<> ();
    List<String>  categoriesList;
    List<BarEntry> data  = new ArrayList<>();
    RoomTransactionsViewModel model;
    BarData barData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        binding = TransacationStatisticActivityBinding.inflate (getLayoutInflater ());
        setContentView (binding.getRoot ());
        model = new ViewModelProvider (this).get (RoomTransactionsViewModel.class);
        initGetDataFromDB();
        initChartWithDelay();
    }

    void initGetDataFromDB(){
        RoomTransactionsViewModel model = new ViewModelProvider (this).get(RoomTransactionsViewModel.class);
        categoriesList = Arrays.asList (getResources ().getStringArray (R.array.expendituresCategory));

        for (String s : categoriesList) {
            model.getAllTransactionInCategory (s, item -> {
                if (item != null) {
                    sortedCategoriesList.add (item.getTransactionCategory ());
                    data.add (new BarEntry (sortedCategoriesList.indexOf (s),Double.valueOf (item.getTransactionValue ()).floatValue ()));
                    Log.d ("SIZE_OF_LIST", String.valueOf (data.size ()));
            }
        });
        }
    }

    private void initChartWithDelay(){
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(this::chartCreate, 400);
    }




    private void chartCreate(){
        if(data.size () !=0){
            chartAppearance ();
            barData = chartBarData (data);
            chartPrepareChart(barData);
        }
        binding.barChart.setVisibility (View.VISIBLE);
    }

    private void chartAppearance( ){
        int chartColor = ContextCompat.getColor(this, R.color.drawableColor);

        binding.barChart.getDescription().setEnabled(false);
        binding.barChart.setDrawValueAboveBar(false);
        Log.d ("CATEGORIES LIST_SIZE",String.valueOf (sortedCategoriesList.size ()));
        XAxis xAxis = binding.barChart.getXAxis();
        xAxis.setValueFormatter (new ValueFormatter () {
            @Override
            public String getFormattedValue(float value) {
                Log.d ("CATEGORIES value",String.valueOf (value));
                return sortedCategoriesList.get ((int)value);
            }
        });
        xAxis.setTextColor (chartColor);

        YAxis axisLeft = binding.barChart.getAxisLeft();
        axisLeft.setGranularity(10f);
        axisLeft.setAxisMinimum(0);
        axisLeft.setTextColor(chartColor);

        YAxis axisRight = binding.barChart.getAxisRight();
        axisRight.setGranularity(10f);
        axisRight.setAxisMinimum(0);
        axisLeft.setTextColor(chartColor);

        binding.barChart.getLegend ().setTextColor (chartColor);
        binding.barChart.setTouchEnabled (false);
    }

    private void chartPrepareChart(BarData data) {
        data.setValueTextSize(12f);
        binding.barChart.setData(data);
        binding.barChart.invalidate();
    }

    private BarData chartBarData(List<BarEntry> barEntries) {
        BarDataSet set1 = new BarDataSet(barEntries, "Expenditures, UAH");
        set1.setColors (ColorTemplate.COLORFUL_COLORS);


        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        return new BarData(dataSets);
    }


}
