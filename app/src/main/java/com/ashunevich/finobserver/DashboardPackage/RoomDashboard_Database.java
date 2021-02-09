package com.ashunevich.finobserver.DashboardPackage;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Dashboard_Account.class},version = 2,exportSchema = false)
abstract class RoomDashboard_Database extends RoomDatabase {
        public abstract RoomDashboard_DAO dashboard_dao();

        private static RoomDashboard_Database INSTANCE;

        public static RoomDashboard_Database getDatabase(final Context context) {
                if (INSTANCE == null) {
                        synchronized (RoomDashboard_Database.class) {
                                if (INSTANCE == null) {
                                        INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                                RoomDashboard_Database.class, "account_database")
                                                .fallbackToDestructiveMigration()
                                                .build();
                                }
                        }
                }
                return INSTANCE;
        }


}
