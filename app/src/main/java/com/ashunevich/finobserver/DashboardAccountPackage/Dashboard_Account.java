package com.ashunevich.finobserver.DashboardAccountPackage;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "active_accounts")
public class Dashboard_Account {


    @PrimaryKey (autoGenerate = true)
    int accountID;

    @ColumnInfo(name = "accountName")
     String accountName;

    @ColumnInfo(name = "accountValue")
     double accountValue;

    @ColumnInfo(name = "accountCurrency")
     String accountCurrency;

    @ColumnInfo(name = "imageID")
     int imageID;


    public Dashboard_Account() {

    }

    public Dashboard_Account(int id, String accountName,double accountValue,String accountCurrency,int imageID) {
        this.accountID = id;
        this.accountName = accountName;
        this.accountValue = accountValue;
        this.accountCurrency = accountCurrency;
        this.imageID = imageID;
    }

    public Dashboard_Account(String accountName,double accountValue,String accountCurrency,int imageID) {
        this.accountName = accountName;
        this.accountValue = accountValue;
        this.accountCurrency = accountCurrency;
        this.imageID = imageID;
    }

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Double getAccountValue() {
        return accountValue;
    }

    public void setAccountValue(Double accountValue) {
        this.accountValue = accountValue;
    }

    public String getAccountCurrency() {
        return accountCurrency;
    }

    public void setAccountCurrency(String accountCurrency) {
        this.accountCurrency = accountCurrency;
    }


}
