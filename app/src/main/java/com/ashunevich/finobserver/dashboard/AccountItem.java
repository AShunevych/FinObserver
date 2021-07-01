package com.ashunevich.finobserver.dashboard;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "active_accounts")
class AccountItem {

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

    protected AccountItem(int id, String accountName, double accountValue, String accountCurrency, String imageID) {
        this.accountID = id;
        this.accountName = accountName;
        this.accountValue = accountValue;
        this.accountCurrency = accountCurrency;
        this.imageID = imageID;
    }

    protected AccountItem(String accountName, double accountValue, String accountCurrency, String imageID) {
        this.accountName = accountName;
        this.accountValue = accountValue;
        this.accountCurrency = accountCurrency;
        this.imageID = imageID;
    }

    protected int getAccountID() {
        return accountID;
    }

    protected void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    protected String getImageID() {
        return imageID;
    }

    protected String getAccountName() {
        return accountName;
    }

    protected Double getAccountValue() {
        return accountValue;
    }

    protected String getAccountCurrency() {
        return accountCurrency;
    }

}
