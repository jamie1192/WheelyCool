package com.jastley.wheelycool.database.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Word {

    //For the purpose of this exercise, setting the word as the PK so no duplicates
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "word")
    private String word;

    @NonNull
    public String getWord() {
        return word;
    }

    public void setWord(@NonNull String word) {
        this.word = word;
    }
}
