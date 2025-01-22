package com.daus.catering.database;

import android.content.Context;

import androidx.room.Room;

public class DatabaseClient {

    private static DatabaseClient mInstance;
    AppDatabase mAppDatabase;

    private DatabaseClient(Context context) {
        mAppDatabase = Room.databaseBuilder(context, AppDatabase.class, "catering_db")
                .fallbackToDestructiveMigration()
                .build();
    }

    public static synchronized DatabaseClient getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DatabaseClient(context);
        }
        return mInstance;
    }

    public AppDatabase getAppDatabase() {
        return mAppDatabase;
    }

}
