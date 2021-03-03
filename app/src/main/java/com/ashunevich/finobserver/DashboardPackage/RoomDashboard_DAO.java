package com.ashunevich.finobserver.DashboardPackage;

import com.ashunevich.finobserver.UtilsPackage.FactoryDAO;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;


@Dao
public interface RoomDashboard_DAO extends FactoryDAO<Dashboard_Account> {

    @Query("DELETE FROM active_accounts")
     void deleteAll();

    @Query("SELECT * FROM active_accounts ORDER BY accountID DESC" )
    LiveData<List<Dashboard_Account>> getAllAccounts();
}
