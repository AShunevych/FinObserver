package com.ashunevich.finobserver.viewmodel;

import android.app.Application;

import com.ashunevich.finobserver.data.TransactionsRepo;
import com.ashunevich.finobserver.data.TransactionBoardItem;
import com.ashunevich.finobserver.data.TransactionStatisticListener;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class RoomTransactionsViewModel extends AndroidViewModel {
    private final TransactionsRepo mRepo;
    private final LiveData<List<TransactionBoardItem>> mAllTransactions;

    public RoomTransactionsViewModel(Application application){
        super(application);
        mRepo = new TransactionsRepo (application);
        mAllTransactions  = mRepo.getAllTransactions();
    }

    public  LiveData<List<TransactionBoardItem>> getAllTransactions(){
        return mAllTransactions;
    }

    public void getAllTransactionInCategory (String category, TransactionStatisticListener listener){
        mRepo.getAllTransactionInCategory (category, listener);
    }


    public void insert(TransactionBoardItem item){mRepo.insertTransaction(item);}
    public void deleteAll(){mRepo.deleteAllTransactions();}


}
