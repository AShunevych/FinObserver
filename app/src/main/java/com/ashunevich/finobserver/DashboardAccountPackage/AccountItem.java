package com.ashunevich.finobserver.DashboardAccountPackage;

import android.graphics.drawable.Drawable;

public class AccountItem {
     String accountType;
     Double accountValue;
     String accountCurrency;
     Drawable image;

    public Double getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(Double totalBalance) {
        this.totalBalance = totalBalance;
    }

    Double totalBalance;

    public AccountItem() {

    }

    public AccountItem(Drawable image, String accountType, Double accountValue, String accountCurrency) {
        this.image = image;
        this.accountType = accountType;
        this.accountValue = accountValue;
        this.accountCurrency = accountCurrency;
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
