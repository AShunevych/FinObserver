package com.ashunevich.finobserver.TransactionsPackage;


import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface RoomTransactions_DAO {

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insert(Transaction_Item...items);

    @Query ("SELECT * FROM transactions")
    LiveData<List<Transaction_Item>> getAllTransactions();

    @Query("DELETE FROM transactions")
    void deleteAll();
}
