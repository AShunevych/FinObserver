package com.ashunevich.finobserver.database;

import android.content.Context;

import com.ashunevich.finobserver.data.TransactionBoardItem;
import com.ashunevich.finobserver.data.TransactionsDAO;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {TransactionBoardItem.class},version = 1,exportSchema = false)
public abstract class RoomTransactionsDatabase extends RoomDatabase {

    public abstract TransactionsDAO transactions_dao();
    private static RoomTransactionsDatabase INSTANCE;

    public static RoomTransactionsDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RoomTransactionsDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RoomTransactionsDatabase.class, "transactions")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
