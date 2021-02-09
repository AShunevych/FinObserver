package com.ashunevich.finobserver.DashboardPackage;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;

class RoomDashboard_Repository {
    private RoomDashboard_DAO mDashboardDao;
    private LiveData<List<Dashboard_Account>> mAllAccounts;

    RoomDashboard_Repository(Application application) {
        RoomDashboard_Database db = RoomDashboard_Database.getDatabase(application);
        mDashboardDao = db.dashboard_dao();
        mAllAccounts = mDashboardDao.getAllAccounts();
    }

    LiveData<List<Dashboard_Account>> getAllAcounts(){
        return mAllAccounts;
    }

    public void insert (Dashboard_Account account) {
        new insertAsyncTask(mDashboardDao).execute(account);
    }

    public void deleteAccount(Dashboard_Account account)  {
        new deleteWordAsyncTask(mDashboardDao).execute(account);
    }

    public void deleteAll()  {
        new deleteAllWordsAsyncTask(mDashboardDao).execute();
    }

    public void updateEntity(Dashboard_Account account)  {
        new updateWordAsyncTask(mDashboardDao).execute(account);
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


    private static class deleteAllWordsAsyncTask extends AsyncTask<Void, Void, Void> {
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


    private static class deleteWordAsyncTask extends AsyncTask<Dashboard_Account, Void, Void> {
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



}
