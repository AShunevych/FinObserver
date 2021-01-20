package com.ashunevich.finobserver.DashboardAccountPackage;

import android.graphics.drawable.Drawable;

public class AccountNewtItem {
    public String accountType;
    public Double accountValue;

    public String getAccountType() {
        return accountType;
    }

    public Double getAccountValue() {
        return accountValue;
    }

    public String getAccountCurrency() {
        return accountCurrency;
    }

    public Drawable getImage() {
        return image;
    }

    public String accountCurrency;
    public  Drawable image;

    public AccountNewtItem(Drawable image, String accountType, Double accountValue, String accountCurrency) {
        this.image = image;
        this.accountType = accountType;
        this.accountValue = accountValue;
        this.accountCurrency = accountCurrency;
    }
}
