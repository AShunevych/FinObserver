package com.ashunevich.finobserver.AccountPackage;

import android.graphics.drawable.Drawable;

public class Account_NewtItem {
     String accountType;
     Double accountValue;

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

     String accountCurrency;
      Drawable image;

    public Account_NewtItem(Drawable image, String accountType, Double accountValue, String accountCurrency) {
        this.image = image;
        this.accountType = accountType;
        this.accountValue = accountValue;
        this.accountCurrency = accountCurrency;
    }
}
