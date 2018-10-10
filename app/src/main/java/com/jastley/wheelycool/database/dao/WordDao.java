package com.jastley.wheelycool.database.dao;

import com.jastley.wheelycool.database.entities.Word;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import io.reactivex.Maybe;

@Dao
public interface WordDao {

    @Insert
    void insertWord(Word word);

    @Query("SELECT * FROM Word")
    Maybe<List<Word>> getAllWords();

    @Query("DELETE FROM Word WHERE word = :word")
    void deleteWord(String word);
}
