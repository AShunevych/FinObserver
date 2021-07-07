package com.ashunevich.finobserver.data;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;


@Dao
public interface DashboardDAO extends FactoryDAO<DashboardAccountItem> {

    @Query("DELETE FROM active_accounts")
     void deleteAll();

    @Query("SELECT * FROM active_accounts ORDER BY accountID DESC" )
    LiveData<List<DashboardAccountItem>> getAllAccounts();

    @Query ("UPDATE active_accounts SET accountValue =:value WHERE accountID =:id ")
    void updateAccountAfterTransaction(int id, double value);
}
