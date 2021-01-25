package com.ashunevich.finobserver.TransactionsPackage;

import android.graphics.drawable.Drawable;

public class TransactionItem {
    String transactionDate;
    String transactionAccount;

    public TransactionItem(String transactionDate, String transactionAccount,
                           String transactionCurrency, String transactionValue,
                           String transactionCategory, Drawable image, int itemIID) {
        this.transactionDate = transactionDate;
        this.transactionAccount = transactionAccount;
        this.transactionCurrency = transactionCurrency;
        this.transactionValue = transactionValue;
        this.transactionCategory = transactionCategory;
        this.image = image;
        this.itemIID = itemIID;
    }

    public int getItemIID() {
        return itemIID;
    }

    public void setItemIID(int itemIID) {
        this.itemIID = itemIID;
    }

    int itemIID;

    String transactionCurrency;
    String transactionValue;
    String transactionCategory;
    Drawable image;


    public String getTransactionAccount() {
        return transactionAccount;
    }

    public void setTransactionAccount(String transactionAccount) {
        this.transactionAccount = transactionAccount;
    }


    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionCurrency() {
        return transactionCurrency;
    }

    public void setTransactionCurrency(String transactionCurrency) {
        this.transactionCurrency = transactionCurrency;
    }

    public String getTransactionValue() {
        return transactionValue;
    }

    public void setTransactionValue(String transactionValue) {
        this.transactionValue = transactionValue;
    }

    public String getTransactionCategory() {
        return transactionCategory;
    }

    public void setTransactionCategory(String transactionCategory) {
        this.transactionCategory = transactionCategory;
    }



    public void setImage(Drawable image) {
        this.image = image;
    }

    public Drawable getImage() {
        return image;
    }



}
