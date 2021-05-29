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
    List<TransactionStatisticItem> itemList  = new ArrayList<>();
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
                    itemList.add (item);
                    Log.d ("SIZE_OF_ITEM_LIST", String.valueOf (itemList.size ()));
                }
        });
        }
    }

    private void initChartWithDelay(){
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(this::chartCreate, 200);
    }


    private void chartCreate(){
        if(itemList.size () !=0){
            for(int i=0;i<itemList.size ();i++) {
                TransactionStatisticItem item = itemList.get (i);
                data.add (new BarEntry (i, Double.valueOf (item.getTransactionValue ()).floatValue ()));
                sortedCategoriesList.add (item.getTransactionCategory ());
                Log.d ("SIZE_OF_DATA_LIST", String.valueOf (data.size ()));
            }

            chartAppearance ();
            barData = chartBarData (data);
            barData.setBarWidth (0.5f);

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
        xAxis.setLabelCount(sortedCategoriesList.size ());

        xAxis.setValueFormatter (new ValueFormatter () {
            @Override
            public String getFormattedValue(float value) {
                int valueInt = Math.round (value);
                Log.d ("CATEGORIES value",String.valueOf (value));
                if(valueInt < sortedCategoriesList.size ()){
                    return sortedCategoriesList.get (valueInt);
                }
               return "";
            }
        });
        xAxis.setAxisMaximum (sortedCategoriesList.size());

        xAxis.setDrawGridLines(true);
        xAxis.setTextColor (chartColor);

        YAxis axisLeft = binding.barChart.getAxisLeft();
        axisLeft.setAxisMinimum(0);
        axisLeft.setTextColor(chartColor);

        YAxis axisRight = binding.barChart.getAxisRight();
        axisRight.setAxisMinimum(0);
        axisRight.setTextColor(chartColor);

        binding.barChart.getLegend ().setTextColor (chartColor);
        binding.barChart.setTouchEnabled (false);
    }

    private void chartPrepareChart(BarData data) {
        data.setValueTextSize(10f);
        binding.barChart.setData(data);
        binding.barChart.notifyDataSetChanged();
        binding.barChart.invalidate();
    }

    private BarData chartBarData(List<BarEntry> barEntries) {
        BarDataSet set1 = new BarDataSet(barEntries, "Expenditures, UAH");
        set1.setColors (ColorTemplate.PASTEL_COLORS );


        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        return new BarData(dataSets);
    }


}
