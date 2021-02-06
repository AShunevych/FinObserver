package com.ashunevich.finobserver.DashboardAccountPackage;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Dashboard_Account.class},version = 2,exportSchema = false)
abstract class Dashboard_Database extends RoomDatabase {
        public abstract Dashboard_DAO dashboard_dao();

        private static Dashboard_Database INSTANCE;

        public static Dashboard_Database getDatabase(final Context context) {
                if (INSTANCE == null) {
                        synchronized (Dashboard_Database.class) {
                                if (INSTANCE == null) {
                                        INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                                Dashboard_Database.class, "account_database")
                                                .fallbackToDestructiveMigration()
                                                .build();
                                }
                        }
                }
                return INSTANCE;
        }


}
