package com.ashunevich.finobserver.database;

import android.content.Context;

import com.ashunevich.finobserver.data.DashboardAccountItem;
import com.ashunevich.finobserver.data.DashboardDAO;
import com.ashunevich.finobserver.data.TransactionBoardItem;
import com.ashunevich.finobserver.data.TransactionsDAO;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import static com.ashunevich.finobserver.utility.Utils.singleThreadExecutor;

@Database(entities = {TransactionBoardItem.class},version = 1,exportSchema = false)
public abstract class RoomTransactionsDatabase extends RoomDatabase {

    public abstract TransactionsDAO transactions_dao();
    private static RoomTransactionsDatabase INSTANCE;

    public static RoomTransactionsDatabase getDatabase(final Context context) {
        if(INSTANCE == null) {
            synchronized(RoomTransactionsDatabase.class) {
                if(INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RoomTransactionsDatabase.class, "transactions")
                            .addCallback(sRoomDatabaseCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            singleThreadExecutor.execute(() -> {
                // Populate the database in the background.
                TransactionsDAO dao = INSTANCE.transactions_dao();
                dao.deleteAll();
                for(int i =0;i<10000;i++){
                   TransactionBoardItem item = new TransactionBoardItem(
                           "account  "+ i,"someCat",
                           25.0*i,"UAH","19 JUNE 200 ",0,"Income");
                    dao.insert(item);
                }
                });
        }
    };
}
