package com.ashunevich.finobserver.UtilsPackage;

import com.ashunevich.finobserver.TransactionsPackage.TransactionItem;
import com.ashunevich.finobserver.TransactionsPackage.TransactionNewItem;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TransactionViewModel extends ViewModel {

    private final MutableLiveData<TransactionNewItem> selected = new MutableLiveData<>();


    public void setSelected (TransactionNewItem item){
        selected.setValue(item);
    }

    public LiveData<TransactionNewItem> getSelected() {
        return selected;
    }



}
