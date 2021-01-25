package com.ashunevich.finobserver.UtilsPackage;

import com.ashunevich.finobserver.TransactionsPackage.TransactionItem;
import com.ashunevich.finobserver.TransactionsPackage.TransactionNewItem;

import java.util.ArrayList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TransactionViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<TransactionItem>> selected = new MutableLiveData<>();


    public void setSelected (ArrayList<TransactionItem> arrayList){
        selected.setValue(arrayList);
    }

    public LiveData<ArrayList<TransactionItem>> getSelected() {
        return selected;
    }
}
