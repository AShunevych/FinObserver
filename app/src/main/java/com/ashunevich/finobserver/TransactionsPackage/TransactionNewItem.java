package com.ashunevich.finobserver.TransactionsPackage;

public class TransactionNewItem {
   // transactionValue,transactionAccount,transactionCategory,transactionType

   String transactionValue;

    public TransactionNewItem(String transactionValue, String transactionAccount, String transactionCategory, String transactionType) {
        this.transactionValue = transactionValue;
        this.transactionAccount = transactionAccount;
        this.transactionCategory = transactionCategory;
        this.transactionType = transactionType;
    }

    String transactionAccount;
    String transactionCategory;
    String transactionType;

    public String getTransactionValue() {
        return transactionValue;
    }

    public String getTransactionAccount() {
        return transactionAccount;
    }

    public String getTransactionCategory() {
        return transactionCategory;
    }

    public String getTransactionType() {
        return transactionType;
    }



}
