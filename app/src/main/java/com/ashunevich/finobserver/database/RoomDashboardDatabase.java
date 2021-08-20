package com.ashunevich.finobserver.database;

import android.content.Context;


import com.ashunevich.finobserver.data.DashboardDAO;
import com.ashunevich.finobserver.data.DashboardAccountItem;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import static com.ashunevich.finobserver.utility.Utils.singleThreadExecutor;

@Database(entities = {DashboardAccountItem.class},version = 1,exportSchema = false)
public abstract class RoomDashboardDatabase extends RoomDatabase {
        public abstract DashboardDAO dashboard_dao();
        private static RoomDashboardDatabase INSTANCE;

        public static RoomDashboardDatabase getDatabase(final Context context) {
                if (INSTANCE == null) {
                        synchronized (RoomDashboardDatabase.class) {
                                if (INSTANCE == null) {
                                        INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                                RoomDashboardDatabase.class, "account_database")
                                             //   .createFromAsset ("assets/account_database")
                                                .fallbackToDestructiveMigration()
                                                .addCallback (sRoomDatabaseCallback)
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
                                DashboardDAO dao = INSTANCE.dashboard_dao ();
                                dao.deleteAll();
                                for(int i =0;i<8;i++){
                                        DashboardAccountItem item =
                                                new DashboardAccountItem ("name " + i,
                                                        25.0*i,"UAH","ic_bank_account_icon");
                                        dao.insert (item);
                                }
                        });
                }
        };



}
