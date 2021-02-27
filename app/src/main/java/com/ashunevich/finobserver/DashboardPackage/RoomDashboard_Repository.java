package com.ashunevich.finobserver.DashboardPackage;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.LiveData;

class RoomDashboard_Repository {
    private final RoomDashboard_DAO mDashboardDao;
    private final LiveData<List<Dashboard_Account>> mAllAccounts;

    RoomDashboard_Repository(Application application) {
        RoomDashboard_Database db = RoomDashboard_Database.getDatabase(application);
        mDashboardDao = db.dashboard_dao();
        mAllAccounts = mDashboardDao.getAllAccounts();
    }

    LiveData<List<Dashboard_Account>> getAllAccounts(){
        return mAllAccounts;
    }

    public void insert (Dashboard_Account account) {
        RoomDashboard_Database.dashboardDatabaseWriteExecutor.execute(() -> mDashboardDao.insert(account));
    }

    public void deleteAccount(Dashboard_Account account)  {
        RoomDashboard_Database.dashboardDatabaseWriteExecutor.execute(() -> mDashboardDao.delete(account));
    }

    public void deleteAll()  {
        RoomDashboard_Database.dashboardDatabaseWriteExecutor.execute(mDashboardDao::deleteAll);
    }

    public void updateEntity(Dashboard_Account account)  {
        RoomDashboard_Database.dashboardDatabaseWriteExecutor.execute(() -> mDashboardDao.update(account));
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
