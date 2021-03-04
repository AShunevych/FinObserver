package com.ashunevich.finobserver.dashboard;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.LiveData;

import static com.ashunevich.finobserver.UtilsPackage.Utils.singleThreadExecutor;

class RoomDashboardRepository {
    private final RoomDashboardDAO mDashboardDao;
    private final LiveData<List<AccountItem>> mAllAccounts;

    RoomDashboardRepository(Application application) {
        RoomDashboardDatabase db = RoomDashboardDatabase.getDatabase(application);
        mDashboardDao = db.dashboard_dao();
        mAllAccounts = mDashboardDao.getAllAccounts();
    }

    LiveData<List<AccountItem>> getAllAccounts(){
        return mAllAccounts;
    }

     void insert (AccountItem account) {
         singleThreadExecutor.execute(() -> mDashboardDao.insert(account));
    }

     void deleteAccount(AccountItem account)  {
         singleThreadExecutor.execute(() -> mDashboardDao.delete(account));
    }

     void deleteAll()  {
         singleThreadExecutor.execute(mDashboardDao::deleteAll);
    }

     void updateEntity(AccountItem account)  {
         singleThreadExecutor.execute(() -> mDashboardDao.update(account));
    }
     /*
    // !-----DEPRECATED ASYNC TASK CODE----!

    public void updateEntity(Dashboard_Account account)  {
        new updateWordAsyncTask(mDashboardDao).execute(account);
    }

     //!--- INSERT OPERATION --!
   public void insert (Dashboard_Account account) {
        new insertAsyncTask(mDashboardDao).execute(account);
    }
    private static class insertAsyncTask extends AsyncTask<Dashboard_Account, Void, Void> {

        private RoomDashboard_DAO mAsyncTaskDao;

        insertAsyncTask(RoomDashboard_DAO mDashboardDao) {
            mAsyncTaskDao = mDashboardDao;
        }

        @Override
        protected Void doInBackground(final Dashboard_Account... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

        //!--- DELETE ALL OPERATION ---!
     public void deleteAll()  {
        new deleteAllAccountsAsyncTask(mDashboardDao).execute();
    }
    private static class deleteAllAccountsAsyncTask extends AsyncTask<Void, Void, Void> {
        private RoomDashboard_DAO mAsyncTaskDao;

        deleteAllWordsAsyncTask(RoomDashboard_DAO mDashboardDao) {
            mAsyncTaskDao = mDashboardDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

     //!--- DELETE SINGLE ITEM OPERATION ---!
    public void deleteAccount(Dashboard_Account account)  {
        new deleteAccountAsyncTask(mDashboardDao).execute(account);
    }
    private static class deleteAccountAsyncTask extends AsyncTask<Dashboard_Account, Void, Void> {
        private RoomDashboard_DAO mAsyncTaskDao;

        deleteWordAsyncTask(RoomDashboard_DAO mDashboardDao) {
            mAsyncTaskDao = mDashboardDao;
        }

        @Override
        protected Void doInBackground(final Dashboard_Account... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }

    }

        //!--- UPDATE ITEM OPERATION ---!
    private static class updateWordAsyncTask extends AsyncTask<Dashboard_Account, Void, Void> {
        private RoomDashboard_DAO mAsyncTaskDao;

        updateWordAsyncTask(RoomDashboard_DAO mDashboardDao) {
            mAsyncTaskDao = mDashboardDao;
        }

        @Override
        protected Void doInBackground(final Dashboard_Account... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }
     */



}
