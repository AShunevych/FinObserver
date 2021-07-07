package com.ashunevich.finobserver.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "active_accounts")
public class DashboardAccountItem {

    @PrimaryKey (autoGenerate = true)
    int accountID;

    @ColumnInfo(name = "accountName")
     String accountName;

    @ColumnInfo(name = "accountValue")
     double accountValue;

    @ColumnInfo(name = "accountCurrency")
     String accountCurrency;

    @ColumnInfo(name = "imageID")
    String imageID;

    public DashboardAccountItem(int id, String accountName, double accountValue, String accountCurrency, String imageID) {
        this.accountID = id;
        this.accountName = accountName;
        this.accountValue = accountValue;
        this.accountCurrency = accountCurrency;
        this.imageID = imageID;
    }

    public DashboardAccountItem(String accountName, double accountValue, String accountCurrency, String imageID) {
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

    public String getImageID() {
        return imageID;
    }

    public String getAccountName() {
        return accountName;
    }

    public Double getAccountValue() {
        return accountValue;
    }

    public String getAccountCurrency() {
        return accountCurrency;
    }

}
