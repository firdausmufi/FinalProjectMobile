package com.daus.catering.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.daus.catering.database.dao.DatabaseDao;


@Database(entities = {DatabaseModel.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DatabaseDao databaseDao();
}
