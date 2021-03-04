package com.ashunevich.finobserver.dashboard;

import android.content.Context;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {AccountItem.class},version = 1,exportSchema = false)
abstract class RoomDashboardDatabase extends RoomDatabase {
        public abstract RoomDashboardDAO dashboard_dao();

        private static RoomDashboardDatabase INSTANCE;


        public static RoomDashboardDatabase getDatabase(final Context context) {
                if (INSTANCE == null) {
                        synchronized (RoomDashboardDatabase.class) {
                                if (INSTANCE == null) {
                                        INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                                RoomDashboardDatabase.class, "account_database")
                                                .fallbackToDestructiveMigration()
                                                .build();
                                }
                        }
                }
                return INSTANCE;
        }


}
