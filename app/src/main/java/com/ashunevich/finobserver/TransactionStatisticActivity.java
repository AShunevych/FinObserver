package com.ashunevich.finobserver;

import android.os.Bundle;
import android.util.Log;

import com.ashunevich.finobserver.data.StatisticItem;
import com.ashunevich.finobserver.databinding.TransacationStatisticActivityBinding;
import com.ashunevich.finobserver.viewmodel.RoomTransactionsViewModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
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

import static com.ashunevich.finobserver.utility.Utils.initAfterDelay;
import static com.ashunevich.finobserver.utility.ViewUtils.uiShowView;


public class TransactionStatisticActivity extends AppCompatActivity {
    private TransacationStatisticActivityBinding binding;
    private final List<String> sortedExpendituresCategoriesList = new ArrayList<>();
    private final List<String> sortedIncomeCategoriesList = new ArrayList<>();
    private final List<BarEntry> expendituresData = new ArrayList<>();
    private final List<BarEntry> incomeData = new ArrayList<>();
    private final List<StatisticItem> expendituresItems = new ArrayList<>();
    private final List<StatisticItem> incomeItems  = new ArrayList<>();
    private int [] colors;
    private int chartColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TransacationStatisticActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initLoadDataFromDB(getResources().getStringArray(R.array.Expenses),expendituresItems);
        initLoadDataFromDB(getResources().getStringArray(R.array.Income),incomeItems);
        initLoadResources();
        initAfterDelay(this::initChartSetup, 300);
    }

    private  void initLoadDataFromDB(String [] categoriesNames, List<StatisticItem> items){
        RoomTransactionsViewModel model = new ViewModelProvider(this).get(RoomTransactionsViewModel.class);
        for(String s : categoriesNames) {
            model.getAllTransactionInCategory(s, item -> {
                if(item != null) {
                    items.add(item);
                }
            });
        }
    }

    private void initLoadResources(){
        colors = getResources().getIntArray(R.array.CustomColors);
        chartColor = ContextCompat.getColor(this, R.color.drawableColor);
    }

    private void initChartSetup(){
        setupChartFactory(incomeItems,incomeData,
                sortedIncomeCategoriesList,
                binding.incomeChart,getResources().getString(R.string.incomeUAH));

        setupChartFactory(expendituresItems,expendituresData,
                sortedExpendituresCategoriesList,
                binding.expendituresChart,getResources().getString(R.string.expUAH));
    }

    private void setupChartFactory(List<StatisticItem> items,
                                  List<BarEntry> barEntryList,
                                  List<String> categoriesList,
                                  BarChart chart, String chartDescription ){

        if(items.size() !=0){
            for(int i = 0; i< items.size(); i++) {
                StatisticItem item = items.get(i);
                barEntryList.add(new BarEntry(i, Double.valueOf(item.getTransactionValue()).floatValue()));
                categoriesList.add(item.getTransactionCategory());
            }
        }

        setupChartDescription(chart.getDescription(), chartDescription,chart.getWidth(),chart.getHeight());
        setupChartAxis(chart.getXAxis(),chart.getAxisLeft(),chart.getAxisRight(), categoriesList);
        setupChartLegend(chart.getLegend(), categoriesList);

        BarData barData = setupBarData(barEntryList,chartDescription);
        setupChart(chart,barEntryList,barData);
    }

    private void setupChartDescription(Description chartDescription, String description, int width, int height ) {
        chartDescription.setEnabled(true);
        chartDescription.setText(description);
        chartDescription.setTextSize(10f);
        chartDescription.setPosition(width-50,height-900);
        chartDescription.setTextColor(chartColor);
    }

    private void setupChartAxis(XAxis xAxis,YAxis axisLeft, YAxis axisRight, List<String> categoriesList) {
        xAxis.setLabelCount(categoriesList.size());
        xAxis.setTextSize(10f);
        xAxis.setAxisMaximum(categoriesList.size());
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawLabels(false);
        xAxis.setTextColor(chartColor);

        axisLeft.setAxisMinimum(0);
        axisLeft.setGranularity(5f);
        axisLeft.setTextColor(chartColor);
        axisLeft.setDrawGridLines(false);
        axisLeft.setDrawAxisLine(false);

        axisRight.setAxisMinimum(0);
        axisRight.setGranularity(5f);
        axisRight.setTextColor(chartColor);

        if(categoriesList == sortedExpendituresCategoriesList){
            axisLeft.setDrawLabels(true);
            axisRight.setDrawLabels(false);
        }
        else{
            axisLeft.setDrawLabels(false);
            axisRight.setDrawLabels(true);
        }
    }

    private void setupChartLegend(Legend chartLegend, List<String> categoriesList) {
        List<LegendEntry> entries = new ArrayList<>();

        for(int i=0;i<categoriesList.size();i++){
            entries.add
                   (new LegendEntry
                           (categoriesList.get(i),Legend.LegendForm.SQUARE,10f,10f,null,
                                    colors[i]));
            Log.d("TAG", String.valueOf(categoriesList.size()));
        }

        chartLegend.setCustom(entries);
        chartLegend.setEnabled(true);
        chartLegend.setTextSize(10f);
        chartLegend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        chartLegend.setFormToTextSpace(5f);
        chartLegend.setWordWrapEnabled(true);
        chartLegend.setTextColor(chartColor);

        if(categoriesList == sortedExpendituresCategoriesList){
            chartLegend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        }
        else{
            chartLegend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        }

    }

    private void setupChart(BarChart chart,  List<BarEntry> barEntryList, BarData data) {
        if(barEntryList.size()<4){
            data.setBarWidth(0.5f);
        }
        else{
            data.setBarWidth(1f);
        }

        chart.setDrawValueAboveBar(false);
        chart.setTouchEnabled(false);
        chart.setData(data);
        chart.notifyDataSetChanged();
        chart.invalidate();
        uiShowView(chart);
    }

    private BarData setupBarData(List<BarEntry> barEntries, String name) {
        BarDataSet set1 = new BarDataSet(barEntries, name);
        set1.setColors(colors);
        set1.setValueTextSize(10f);

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        return new BarData(dataSets);
    }

}
