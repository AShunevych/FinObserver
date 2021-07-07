package com.ashunevich.finobserver.viewmodel;

import android.app.Application;


import com.ashunevich.finobserver.data.DashboardAccountItem;
import com.ashunevich.finobserver.data.DashboardRepo;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;



public class RoomDashboardViewModel extends AndroidViewModel {
    private final DashboardRepo mRepo;
    private final LiveData<List<DashboardAccountItem>> mAllAccounts;


    public RoomDashboardViewModel(@NonNull Application application) {
        super(application);
        mRepo = new DashboardRepo (application);
        mAllAccounts = mRepo.getAllAccounts();
    }

    public LiveData<List<DashboardAccountItem>> getAllAccounts() {
        return mAllAccounts;
    }

    public void insert(DashboardAccountItem account) {
        mRepo.insert(account);
    }

    public void deleteAll() {mRepo.deleteAll();}

    public void delete(DashboardAccountItem account) {mRepo.deleteAccount(account);}

    public  void update(DashboardAccountItem account) { mRepo.updateEntity(account);}

    public void updateAccountAfterTransaction(int id, double value){ mRepo.updateAccountAfterTransaction (id,value);}

}
