package com.ashunevich.finobserver.dashboard;

import android.app.Application;


import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;



public class RoomDashboardViewModel extends AndroidViewModel {
    private final RoomDashboardRepository mRepo;
    private final LiveData<List<AccountItem>> mAllAccounts;


    public RoomDashboardViewModel(@NonNull Application application) {
        super(application);
        mRepo = new RoomDashboardRepository(application);
        mAllAccounts = mRepo.getAllAccounts();
    }

    LiveData<List<AccountItem>> getAllAccounts() {
        return mAllAccounts;
    }

     void insert(AccountItem account) {
        mRepo.insert(account);
    }

    public void deleteAll() {mRepo.deleteAll();}

     void delete(AccountItem account) {mRepo.deleteAccount(account);}

     void update(AccountItem account) { mRepo.updateEntity(account);}

     void updateAccountAfterTransaction(int id, double value){ mRepo.updateAccountAfterTransaction (id,value);}

}
