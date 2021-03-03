package com.ashunevich.finobserver.TransactionsPackage;


import com.ashunevich.finobserver.UtilsPackage.FactoryDAO;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface RoomTransactions_DAO extends FactoryDAO<Transaction_Item> {

    @Query ("SELECT * FROM transactions ORDER BY itemID DESC")
    LiveData<List<Transaction_Item>> getAllTransactions();

    @Query("DELETE FROM transactions")
    void deleteAll();
}
