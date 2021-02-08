package com.ashunevich.finobserver.DashboardAccountPackage;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Dashboard_Account.class},version = 2,exportSchema = false)
abstract class Persistence_Database extends RoomDatabase {
        public abstract Persistence_DAO dashboard_dao();

        private static Persistence_Database INSTANCE;

        public static Persistence_Database getDatabase(final Context context) {
                if (INSTANCE == null) {
                        synchronized (Persistence_Database.class) {
                                if (INSTANCE == null) {
                                        INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                                Persistence_Database.class, "account_database")
                                                .fallbackToDestructiveMigration()
                                                .build();
                                }
                        }
                }
                return INSTANCE;
        }


}
