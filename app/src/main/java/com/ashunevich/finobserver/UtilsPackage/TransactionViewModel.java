package com.ashunevich.finobserver.UtilsPackage;

import com.ashunevich.finobserver.TransactionsPackage.Transaction_Item;

import java.util.ArrayList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TransactionViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<Transaction_Item>> selected = new MutableLiveData<>();


    public void setSelected (ArrayList<Transaction_Item> arrayList){
        selected.setValue(arrayList);
    }

    public LiveData<ArrayList<Transaction_Item>> getSelected() {
        return selected;
    }
}
