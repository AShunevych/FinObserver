package com.ashunevich.finobserver.TransactionsPackage;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.LiveData;

public class RoomTransactions_Repository {
    private final RoomTransactions_DAO mTransactionsDao;
    private final LiveData<List<Transaction_Item>> mAllTransactions;

    RoomTransactions_Repository(Application application){
            RoomTransactions_Database db = RoomTransactions_Database.getDatabase(application);
            mTransactionsDao = db.transactions_dao();
            mAllTransactions = mTransactionsDao.getAllTransactions();
    }

    LiveData<List<Transaction_Item>> getAllTransactions(){
        return mAllTransactions;
    }

    public void insertTransaction (Transaction_Item item){
        RoomTransactions_Database.transactionDatabaseWriteExecutor.execute(() -> mTransactionsDao.insert(item));
    }

    public void deleteAllTransactions (){
        RoomTransactions_Database.transactionDatabaseWriteExecutor.execute(mTransactionsDao::deleteAll);
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
