package com.ashunevich.finobserver.TransactionsPackage;



import java.util.ArrayList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class Transaction_ViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<Transaction_Item>> selected = new MutableLiveData<>();


    public void setSelected (ArrayList<Transaction_Item> arrayList){
        selected.setValue(arrayList);
    }

    public LiveData<ArrayList<Transaction_Item>> getSelected() {
        return selected;
    }
}
