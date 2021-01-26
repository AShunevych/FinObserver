package com.ashunevich.finobserver.AccountPackage;

import android.graphics.drawable.Drawable;

public class Account_Item {
     String accountType;
     Double accountValue;
     String accountCurrency;
     Drawable image;


    public Account_Item() {

    }


    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
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