package com.jastley.wheelycool.database;

import android.content.Context;

import com.jastley.wheelycool.database.dao.WordDao;
import com.jastley.wheelycool.database.entities.Word;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = { Word.class }, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract WordDao getWordDao();

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "WordDatabase.db")
                    .build();
        }
        return INSTANCE;
    }
}
