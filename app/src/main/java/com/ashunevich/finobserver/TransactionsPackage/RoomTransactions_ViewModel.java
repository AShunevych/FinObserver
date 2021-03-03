package com.ashunevich.finobserver.TransactionsPackage;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class RoomTransactions_ViewModel extends AndroidViewModel {
    private RoomTransactions_Repository mRepo;
    private LiveData<List<Transaction_Item>> mAllTransactions;

    public RoomTransactions_ViewModel(Application application){
        super(application);
        mRepo = new RoomTransactions_Repository(application);
        mAllTransactions  = mRepo.getAllTransactions();
    }

    LiveData<List<Transaction_Item>> getAllTransactions(){
        return mAllTransactions;
    }

    public void insert(Transaction_Item item){mRepo.insertTransaction(item);}
    public void deleteAll(){mRepo.deleteAllTransactions();}

}
