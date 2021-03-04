package com.ashunevich.finobserver.transactions;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class RoomTransactionsViewModel extends AndroidViewModel {
    private RoomTransactionsRepository mRepo;
    private LiveData<List<TransactionItem>> mAllTransactions;

    public RoomTransactionsViewModel(Application application){
        super(application);
        mRepo = new RoomTransactionsRepository(application);
        mAllTransactions  = mRepo.getAllTransactions();
    }

    LiveData<List<TransactionItem>> getAllTransactions(){
        return mAllTransactions;
    }

    public void insert(TransactionItem item){mRepo.insertTransaction(item);}
    public void deleteAll(){mRepo.deleteAllTransactions();}

}
