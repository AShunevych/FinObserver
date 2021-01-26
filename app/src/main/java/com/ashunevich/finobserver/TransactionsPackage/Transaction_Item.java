package com.ashunevich.finobserver.TransactionsPackage;

import android.graphics.drawable.Drawable;

public class Transaction_Item {
    String transactionDate;
    String transactionAccount;

    public Transaction_Item(String transactionDate, String transactionAccount,
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


    int itemIID;

    String transactionCurrency;
    String transactionValue;
    String transactionCategory;
    Drawable image;


    public String getTransactionAccount() {
        return transactionAccount;
    }


    public String getTransactionDate() {
        return transactionDate;
    }



    public String getTransactionCurrency() {
        return transactionCurrency;
    }



    public String getTransactionValue() {
        return transactionValue;
    }



    public String getTransactionCategory() {
        return transactionCategory;
    }


    public Drawable getImage() {
        return image;
    }



}
