package com.ashunevich.finobserver.data;

import androidx.room.ColumnInfo;


public class StatisticItem {

    @ColumnInfo(name = "value")
    double transactionValue;

    public double getTransactionValue() {
        return transactionValue;
    }

    public String getTransactionCategory() {
        return transactionCategory;
    }

    public StatisticItem(String transactionCategory, double transactionValue) {
        this.transactionValue = transactionValue;
        this.transactionCategory = transactionCategory;
    }
    @ColumnInfo(name = "category")
    String transactionCategory;
}
