package com.ashunevich.finobserver.database;

import android.content.Context;


import com.ashunevich.finobserver.data.DashboardDAO;
import com.ashunevich.finobserver.data.DashboardAccountItem;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {DashboardAccountItem.class},version = 2,exportSchema = false)
public abstract class RoomDashboardDatabase extends RoomDatabase {
        public abstract DashboardDAO dashboard_dao();
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
