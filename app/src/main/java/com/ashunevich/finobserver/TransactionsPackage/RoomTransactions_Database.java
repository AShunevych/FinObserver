package com.ashunevich.finobserver.TransactionsPackage;

import android.content.Context;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Transaction_Item.class},version = 1,exportSchema = false)
abstract class RoomTransactions_Database extends RoomDatabase {

    public abstract RoomTransactions_DAO transactions_dao();
    private static RoomTransactions_Database INSTANCE;
    static final ExecutorService transactionDatabaseWriteExecutor =
            Executors.newSingleThreadExecutor();

    public static RoomTransactions_Database getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RoomTransactions_Database.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RoomTransactions_Database.class, "transactions")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
