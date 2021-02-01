package com.ashunevich.finobserver.DashboardAccountPackage;

import java.security.Policy;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import static android.icu.text.MessagePattern.ArgType.SELECT;

@Dao
public interface Dashboard_DAO {

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insert(Dashboard_Account... dashboard_accounts);

    @Delete
     void delete(Dashboard_Account account);

    @Query("DELETE FROM active_accounts")
     void deleteAll();

    @Update
     void update(Dashboard_Account... dashboard_accounts);

    @Query("SELECT * FROM active_accounts" )
    LiveData<List<Dashboard_Account>> getAllAccounts();
}
