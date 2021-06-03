package com.ashunevich.finobserver.transactions;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import com.ashunevich.finobserver.R;

import com.ashunevich.finobserver.databinding.TransacationStatisticActivityBinding;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.List;



import androidx.appcompat.app.AppCompatActivity;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;


public class TransactionStatisticActivity extends AppCompatActivity {
    private TransacationStatisticActivityBinding binding;
    private final List<String> sortedExpendituresCategoriesList = new ArrayList<> ();
    private final List<String> sortedIncomeCategoriesList = new ArrayList<> ();
    private final List<BarEntry> expendituresData = new ArrayList<>();
    private final List<BarEntry> incomeData = new ArrayList<>();
    private final List<TransactionStatisticItem> expendituresItems = new ArrayList<>();
    private final List<TransactionStatisticItem> incomeItems  = new ArrayList<>();
    private int [] colors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        binding = TransacationStatisticActivityBinding.inflate (getLayoutInflater ());
        setContentView (binding.getRoot ());
        colors = getResources ().getIntArray (R.array.CustomColors);
        initDownloadCategoryDataFromDB(getResources ().getStringArray (R.array.Expenses),expendituresItems);
        initDownloadCategoryDataFromDB(getResources ().getStringArray (R.array.Income),incomeItems);

        initChartWithDelay();
    }


    private  void initDownloadCategoryDataFromDB(String [] strings, List<TransactionStatisticItem> items){
        RoomTransactionsViewModel model = new ViewModelProvider (this).get(RoomTransactionsViewModel.class);
        for (String s : strings) {
            model.getAllTransactionInCategory (s, item -> {
                if (item != null) {
                    items.add (item);
                }
            });
        }
    }

    private void initChartWithDelay(){
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(this::createCharts, 300);
    }


    private void createCharts(){
        chartConstructor(incomeItems,incomeData,sortedIncomeCategoriesList,binding.incomeChart,getResources ().getString (R.string.incomeUAH));
        chartConstructor(expendituresItems,expendituresData,sortedExpendituresCategoriesList,binding.expendituresChart,getResources ().getString (R.string.expUAH));
    }


    private void chartConstructor(List<TransactionStatisticItem> items,
                                  List<BarEntry> barEntryList,
                                  List<String> categoriesList,
                                  BarChart chart, String chartDescription ){

        if(items.size () !=0){
            for(int i = 0; i< items.size (); i++) {
                TransactionStatisticItem item = items.get (i);
                barEntryList.add (new BarEntry (i, Double.valueOf (item.getTransactionValue ()).floatValue ()));
                categoriesList.add (item.getTransactionCategory ());
                Log.d ("SIZE_OF_DATA_LIST", String.valueOf (barEntryList.size ()));
            }
        }

        chartConstructorAppearance (chart,categoriesList,chartDescription);
        BarData barData = chartConstructorBarData (barEntryList,chartDescription);
        if(barEntryList.size ()<4){
            barData.setBarWidth(0.5f);
        }
        else{
            barData.setBarWidth (1f);
        }

        chartConstructorInit (chart,barData);
        chart.setVisibility (View.VISIBLE);

    }

    private void chartConstructorAppearance(BarChart chart, List<String> categoriesList, String description){
        int chartColor = ContextCompat.getColor(this, R.color.drawableColor);

        int width = chart.getWidth ();
        int height = chart.getHeight ();

        chart.getDescription().setEnabled(true);
        chart.getDescription().setText (description);
        chart.getDescription().setTextSize (11f);
        chart.getDescription().setPosition (width-50,height-900);
        chart.getDescription().setTextColor(chartColor);
        chart.setDrawValueAboveBar(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setLabelCount(categoriesList.size ());
        xAxis.setTextSize (10f);
        xAxis.setAxisMaximum (categoriesList.size());
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine (false);
        xAxis.setDrawLabels (false);
        xAxis.setTextColor (chartColor);

        YAxis axisLeft = chart.getAxisLeft();
        axisLeft.setAxisMinimum(0);
        axisLeft.setGranularity(5f);
        axisLeft.setTextColor(chartColor);
        axisLeft.setDrawGridLines (false);
        axisLeft.setDrawAxisLine (false);

        YAxis axisRight = chart.getAxisRight();
        axisRight.setAxisMinimum(0);
        axisRight.setGranularity(5f);
        axisLeft.setDrawGridLines (false);
        axisLeft.setDrawAxisLine (false);


        axisRight.setTextColor(chartColor);


        List<LegendEntry> entries = new ArrayList<> ();
        for(String s :categoriesList){
            entries.add
                    (new LegendEntry
                            (s,Legend.LegendForm.SQUARE,10f,10f,null,
                                    colors[categoriesList.indexOf (s)]));
        }

        chart.getLegend().setCustom (entries);
        chart.getLegend().setEnabled (true);
        chart.getLegend().setTextSize (10f);
        chart.getLegend().setHorizontalAlignment (Legend.LegendHorizontalAlignment.LEFT);
        chart.getLegend().setFormToTextSpace (5f);
        chart.getLegend().setWordWrapEnabled (true);
        chart.getLegend().setTextColor (chartColor);

        if(description.equals (getResources ().getString (R.string.expUAH))){
           chart.getLegend ().setVerticalAlignment (Legend.LegendVerticalAlignment.BOTTOM);
            axisLeft.setDrawLabels (true);
            axisRight.setDrawLabels (false);
        }
        else{
            chart.getLegend ().setVerticalAlignment (Legend.LegendVerticalAlignment.TOP);
            axisLeft.setDrawLabels (false);
            axisRight.setDrawLabels (true);
        }
        chart.setTouchEnabled (false);
    }

    private BarData chartConstructorBarData(List<BarEntry> barEntries, String name) {
        BarDataSet set1 = new BarDataSet(barEntries, name);
        set1.setColors (colors );

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        return new BarData(dataSets);
    }

    private void chartConstructorInit(BarChart chart, BarData data) {
        data.setValueTextSize(10f);
        chart.setData(data);
        chart.notifyDataSetChanged();
        chart.invalidate();
    }

}
