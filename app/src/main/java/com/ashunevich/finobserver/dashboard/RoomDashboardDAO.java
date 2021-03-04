package com.ashunevich.finobserver.dashboard;

import com.ashunevich.finobserver.UtilsPackage.FactoryDAO;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;


@Dao
public interface RoomDashboardDAO extends FactoryDAO<AccountItem> {

    @Query("DELETE FROM AccountItem")
     void deleteAll();

    @Query("SELECT * FROM AccountItem ORDER BY accountID DESC" )
    LiveData<List<AccountItem>> getAllAccounts();
}
