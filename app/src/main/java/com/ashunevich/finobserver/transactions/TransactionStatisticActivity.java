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
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        binding = TransacationStatisticActivityBinding.inflate (getLayoutInflater ());
        setContentView (binding.getRoot ());
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
        chartConstructor(incomeItems,incomeData,sortedIncomeCategoriesList,binding.incomeChart,"Income, UAH");
        chartConstructor(expendituresItems,expendituresData,sortedExpendituresCategoriesList,binding.expendituresChart,"Expenditures, UAH");
    }


    private void chartConstructor(List<TransactionStatisticItem> items,
                                  List<BarEntry> barEntryList,
                                  List<String> categoriesList,
                                  BarChart chart, String name ){

        if(items.size () !=0){
            for(int i = 0; i< items.size (); i++) {
                TransactionStatisticItem item = items.get (i);
                barEntryList.add (new BarEntry (i, Double.valueOf (item.getTransactionValue ()).floatValue ()));
                categoriesList.add (item.getTransactionCategory ());
                Log.d ("SIZE_OF_DATA_LIST", String.valueOf (barEntryList.size ()));
            }

            chartConstructorAppearance (chart,categoriesList);
            BarData barData = chartConstructorBarData (barEntryList,name);
            barData.setBarWidth (0.5f);

            chartConstructorInit (chart,barData);
        }
        chart.setVisibility (View.VISIBLE);

    }

    private void chartConstructorAppearance(BarChart chart, List<String> categoriesList ){
        int chartColor = ContextCompat.getColor(this, R.color.drawableColor);

        chart.getDescription().setEnabled(false);
        chart.setDrawValueAboveBar(false);



        XAxis xAxis = chart.getXAxis();
        xAxis.setLabelCount(categoriesList.size ());

        xAxis.setValueFormatter (new ValueFormatter () {
            @Override
            public String getFormattedValue(float value) {
                int valueInt = Math.round (value);
                Log.d ("CATEGORIES value",String.valueOf (value));
                if(valueInt < categoriesList.size ()){
                    return categoriesList.get (valueInt);
                }
               return "";
            }
        });
    //    xAxis.setPosition(XAxis.XAxisPosition.TOP_INSIDE);
        xAxis.setTextSize (10f);
        xAxis.setAxisMaximum (categoriesList.size());
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor (chartColor);

        YAxis axisLeft = chart.getAxisLeft();
        axisLeft.setAxisMinimum(0);
        axisLeft.setGranularity(5f);
        axisLeft.setTextColor(chartColor);


        YAxis axisRight = chart.getAxisRight();
        axisRight.setAxisMinimum(0);
        axisRight.setGranularity(5f);

        axisRight.setTextColor(chartColor);


        chart.getLegend().setTextColor (chartColor);
        chart.getLegend().setVerticalAlignment (Legend.LegendVerticalAlignment.TOP);

        chart.setTouchEnabled (false);
    }

    private BarData chartConstructorBarData(List<BarEntry> barEntries, String name) {
        BarDataSet set1 = new BarDataSet(barEntries, name);
        set1.setColors (ColorTemplate.PASTEL_COLORS );

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
