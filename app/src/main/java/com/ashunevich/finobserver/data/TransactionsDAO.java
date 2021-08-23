package com.ashunevich.finobserver.data;


import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface TransactionsDAO extends FactoryDAO<TransactionBoardItem> {

    @Query("SELECT * FROM transactions ORDER BY itemID DESC")
    LiveData<List<TransactionBoardItem>> getAllTransactions();

    @Query("DELETE FROM transactions")
    void deleteAll();

    @Query("SELECT category, SUM(value) as value FROM transactions WHERE category =:transactionCategory GROUP BY category ")
    StatisticItem getAllTransactionInCategory(String transactionCategory);


}
