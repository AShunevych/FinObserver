package com.ashunevich.finobserver.data;

import android.app.Application;

import com.ashunevich.finobserver.database.RoomTransactionsDatabase;

import java.util.List;

import androidx.lifecycle.LiveData;

import static com.ashunevich.finobserver.utility.Utils.singleThreadExecutor;

public class TransactionsRepo {
    private final TransactionsDAO mTransactionsDao;
    private final LiveData<List<TransactionBoardItem>> mAllTransactions;

    public TransactionsRepo(Application application){
            RoomTransactionsDatabase db = RoomTransactionsDatabase.getDatabase(application);
            mTransactionsDao = db.transactions_dao();
            mAllTransactions = mTransactionsDao.getAllTransactions();
    }

    public LiveData<List<TransactionBoardItem>> getAllTransactions(){
        return mAllTransactions;
    }

    public void insertTransaction(TransactionBoardItem item){
         singleThreadExecutor.execute(() -> mTransactionsDao.insert(item));
    }

    public  void getAllTransactionInCategory(String category, StatisticListener listener){
        singleThreadExecutor.execute(() -> listener.onReturned(mTransactionsDao.getAllTransactionInCategory(category)));
    }

    public void deleteAllTransactions(){
         singleThreadExecutor.execute(mTransactionsDao::deleteAll);
    }
/*
  // !-----DEPRECATED ASYNC TASK CODE----!

  //!--- INSERT OPERATION ---!
    public void insertTransaction(Transaction_Item item){
            new insertAsyncTask(mTransactionsDao).execute(item);
    }

    private static class insertAsyncTask extends AsyncTask<Transaction_Item,Void, Void>{
        private RoomTransactions_DAO mAsyncTaskDao;

        insertAsyncTask(RoomTransactions_DAO mTransactionDao){
            mAsyncTaskDao = mTransactionDao;
        }

        @Override
        protected Void doInBackground(final Transaction_Item...params){
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

     //!--- DELETE ALL ITEMS OPERATION ---!
    public void deleteAllTransactions( ){
        new deleteAllAsyncTask(mTransactionsDao).execute();
    }
    private static class deleteAllAsyncTask extends AsyncTask<Transaction_Item,Void, Void>{
        private RoomTransactions_DAO mAsyncTaskDao;

        deleteAllAsyncTask(RoomTransactions_DAO mTransactionDao){
            mAsyncTaskDao = mTransactionDao;
        }

        @Override
        protected Void doInBackground(final Transaction_Item...params){
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }
 */
}
