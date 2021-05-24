package com.ashunevich.finobserver.transactions;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class RoomTransactionsViewModel extends AndroidViewModel {
    private final RoomTransactionsRepository mRepo;
    private final LiveData<List<TransactionBoardItem>> mAllTransactions;
    LiveData<TransactionStatisticItem> getAllTransactionInCategory;

    public RoomTransactionsViewModel(Application application){
        super(application);
        mRepo = new RoomTransactionsRepository(application);
        mAllTransactions  = mRepo.getAllTransactions();
    }

    LiveData<List<TransactionBoardItem>> getAllTransactions(){
        return mAllTransactions;
    }

    void getAllTransactionInCategory (String category, TransactionStatisticListener listener){
        mRepo.getAllTransactionInCategory (category, listener);
    }


    public void insert(TransactionBoardItem item){mRepo.insertTransaction(item);}
    public void deleteAll(){mRepo.deleteAllTransactions();}


}
