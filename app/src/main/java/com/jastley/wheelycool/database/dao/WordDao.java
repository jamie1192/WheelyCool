package com.jastley.wheelycool.database.dao;

import com.jastley.wheelycool.database.entities.Word;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface WordDao {

    @Insert
    void insertWord(Word word);

    @Query("DELETE FROM Word")
    void deleteAllWords();

    @Query("SELECT * FROM Word")
    LiveData<List<Word>> getAllWords();

    @Query("DELETE FROM Word WHERE word = :word")
    void deleteWord(String word);
}
