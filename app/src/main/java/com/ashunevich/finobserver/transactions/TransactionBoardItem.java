package com.ashunevich.finobserver.transactions;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "transactions")
public class TransactionBoardItem {

    @Ignore
    public TransactionBoardItem(){}

    public TransactionBoardItem(String transactionAccount, String transactionCategory, double transactionValue,
                                String transactionCurrency, String transactionDate , int imageInt, String transactionType) {
        this.transactionAccount = transactionAccount;
        this.transactionCategory = transactionCategory;
        this.transactionValue = transactionValue;
        this.transactionCurrency = transactionCurrency;
        this.transactionDate = transactionDate;
        this.imageInt = imageInt;
        this.transactionType = transactionType;
    }

    @PrimaryKey(autoGenerate = true)
    int itemID;
    public int getItemID() {
        return itemID;
    }
    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    @ColumnInfo(name = "account")
    String transactionAccount;//+
    public String getTransactionAccount() {
        return transactionAccount;
    }


    @ColumnInfo(name = "date")
    String transactionDate;
    public String getTransactionDate() {
        return transactionDate;
    }


    @ColumnInfo(name = "currency")
    String transactionCurrency;//+
    public String getTransactionCurrency() {
        return transactionCurrency;
    }


    @ColumnInfo(name = "value")
    double transactionValue;//+
    public Double getTransactionValue() {
        return transactionValue;
    }


    @ColumnInfo(name = "category")
    String transactionCategory;//+
    public String getTransactionCategory() {
        return transactionCategory;
    }


    //TODO (1) add  with string type Income / Expenditure / Transfer

    @ColumnInfo(name = "imageValue")
    int imageInt;//+
    public int getImageInt() { return imageInt; }


    @ColumnInfo(name = "type")
    String transactionType;
    public String getTransactionType() { return transactionType; }

}
