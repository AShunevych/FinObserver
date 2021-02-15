package com.ashunevich.finobserver.TransactionsPackage;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "transactions")
public class Transaction_Item {

    public Transaction_Item(){}
    public Transaction_Item (String transactionAccount,String transactionCategory, double transactionValue,
                             String transactionCurrency, String transactionDate ,int imageInt) {
        this.transactionAccount = transactionAccount;
        this.transactionCategory = transactionCategory;
        this.transactionValue = transactionValue;
        this.transactionCurrency = transactionCurrency;
        this.transactionDate = transactionDate;
        this.imageInt = imageInt;
    }

    @PrimaryKey(autoGenerate = true)
    int itemID;
    public int getItemID() {
        return itemID;
    }
    public void setItemID(int itemID) {
        this.itemID = itemID;
    }


    String transactionAccount;//+
    public String getTransactionAccount() {
        return transactionAccount;
    }
    public void setTransactionAccount(String transactionAccount) { this.transactionAccount = transactionAccount; }


    String transactionDate;
    public String getTransactionDate() {
        return transactionDate;
    }
    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }


    String transactionCurrency;//+
    public String getTransactionCurrency() {
        return transactionCurrency;
    }
    public void setTransactionCurrency(String transactionCurrency) {
        this.transactionCurrency = transactionCurrency;
    }


    double transactionValue;//+
    public double getTransactionValue() {
        return transactionValue;
    }
    public void setTransactionValue(double transactionValue) {
        this.transactionValue = transactionValue;
    }


    String transactionCategory;//+
    public String getTransactionCategory() {
        return transactionCategory;
    }
    public void setTransactionCategory(String transactionCategory) {
        this.transactionCategory = transactionCategory;
    }


    int imageInt;//+
    public int getImageInt() { return imageInt; }
    public void setImageInt(int imageInt) {
        this.imageInt = imageInt;
    }


}
