package com.ashunevich.finobserver.dashboard;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
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
     int imageID;

    @Ignore
    protected AccountItem() {

    }

    protected AccountItem(int id, String accountName, double accountValue, String accountCurrency, int imageID) {
        this.accountID = id;
        this.accountName = accountName;
        this.accountValue = accountValue;
        this.accountCurrency = accountCurrency;
        this.imageID = imageID;
    }



    protected AccountItem(String accountName, double accountValue, String accountCurrency, int imageID) {
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


    protected int getImageID() {
        return imageID;
    }

    protected void setImageID(int imageID) {
        this.imageID = imageID;
    }


    protected String getAccountName() {
        return accountName;
    }

    protected void setAccountName(String accountName) {
        this.accountName = accountName;
    }


    protected Double getAccountValue() {
        return accountValue;
    }

    protected void setAccountValue(Double accountValue) {
        this.accountValue = accountValue;
    }


    protected String getAccountCurrency() {
        return accountCurrency;
    }

    protected void setAccountCurrency(String accountCurrency) {
        this.accountCurrency = accountCurrency;
    }


}
