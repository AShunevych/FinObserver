package com.ashunevich.finobserver.transactions;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.LiveData;

import static com.ashunevich.finobserver.UtilsPackage.Utils.singleThreadExecutor;

public class RoomTransactionsRepository {
    private final RoomTransactionsDAO mTransactionsDao;
    private final LiveData<List<TransactionItem>> mAllTransactions;

    RoomTransactionsRepository(Application application){
            RoomTransactionsDatabase db = RoomTransactionsDatabase.getDatabase(application);
            mTransactionsDao = db.transactions_dao();
            mAllTransactions = mTransactionsDao.getAllTransactions();
    }

    LiveData<List<TransactionItem>> getAllTransactions(){
        return mAllTransactions;
    }

     void insertTransaction (TransactionItem item){
         singleThreadExecutor.execute(() -> mTransactionsDao.insert(item));
    }

     void deleteAllTransactions (){
         singleThreadExecutor.execute(mTransactionsDao::deleteAll);
    }
/*
  // !-----DEPRECATED ASYNC TASK CODE----!

  //!--- INSERT OPERATION ---!
    public void insertTransaction (Transaction_Item item){
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
    public void deleteAllTransactions ( ){
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
