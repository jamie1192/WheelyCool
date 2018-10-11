package com.jastley.wheelycool.ui.main;

import com.jastley.wheelycool.database.entities.Word;
import com.jastley.wheelycool.di.App;
import com.jastley.wheelycool.repositories.WordRepository;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {

    LiveData<List<Word>> wordList;

    @Inject
    WordRepository repository;

    public MainViewModel() {
        App.getApp().getAppComponent().inject(this);
    }

    public LiveData<List<Word>> getWordList() {
        return wordList = repository.getWordsList();
    }

    public void addWord(String word) {
        repository.addWord(word);
    }

    public void deleteWord(String word) {
        repository.deleteWord(word);
    }
}
