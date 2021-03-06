package com.ashunevich.finobserver.transactions;


import com.ashunevich.finobserver.UtilsPackage.FactoryDAO;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface RoomTransactionsDAO extends FactoryDAO<TransactionItem> {

    @Query ("SELECT * FROM transactions ORDER BY itemID DESC")
    LiveData<List<TransactionItem>> getAllTransactions();

    @Query("DELETE FROM transactions")
    void deleteAll();
}
