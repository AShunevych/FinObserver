package com.ashunevich.finobserver.transactions;

import androidx.room.ColumnInfo;


public class TransactionStatisticItem {
    @ColumnInfo(name = "value")
    double  transactionValue;//+

    public double getTransactionValue() {
        return transactionValue;
    }

    public String getTransactionCategory() {
        return transactionCategory;
    }

    public TransactionStatisticItem(String transactionCategory, double transactionValue) {
        this.transactionValue = transactionValue;
        this.transactionCategory = transactionCategory;
    }
    @ColumnInfo(name = "category")
    String transactionCategory;//+
}
