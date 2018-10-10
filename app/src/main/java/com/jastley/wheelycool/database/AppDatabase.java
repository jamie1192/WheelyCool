package com.jastley.wheelycool.database;

import com.jastley.wheelycool.database.entities.Word;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = { Word.class }, version = 1)
public abstract class AppDatabase extends RoomDatabase {

}
