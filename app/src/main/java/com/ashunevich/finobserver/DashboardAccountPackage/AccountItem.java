package com.ashunevich.finobserver.DashboardAccountPackage;

import android.graphics.drawable.Drawable;

public class AccountItem {
     String accountType;
     Double accountValue;
     String accountCurrency;
     Drawable image;


    public AccountItem() {

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
