package com.ashunevich.finobserver.transactions;

import androidx.room.ColumnInfo;


class TransactionStatisticItem {

    @ColumnInfo(name = "value")
    double transactionValue;

    protected double getTransactionValue() {
        return transactionValue;
    }

    protected String getTransactionCategory() {
        return transactionCategory;
    }

    protected TransactionStatisticItem(String transactionCategory, double transactionValue) {
        this.transactionValue = transactionValue;
        this.transactionCategory = transactionCategory;
    }
    @ColumnInfo(name = "category")
    String transactionCategory;
}
