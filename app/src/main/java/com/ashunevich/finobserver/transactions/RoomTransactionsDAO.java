package com.ashunevich.finobserver.transactions;


import com.ashunevich.finobserver.utils.FactoryDAO;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface RoomTransactionsDAO extends FactoryDAO<TransactionBoardItem> {

    @Query ("SELECT * FROM transactions ORDER BY itemID DESC")
    LiveData<List<TransactionBoardItem>> getAllTransactions();

    @Query("DELETE FROM transactions")
    void deleteAll();

    @Query ("SELECT category, SUM (value) as value FROM transactions WHERE category =:transactionCategory GROUP BY category ")
    TransactionStatisticItem getAllTransactionInCategory(String transactionCategory);


}
