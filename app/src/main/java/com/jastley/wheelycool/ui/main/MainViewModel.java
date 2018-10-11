package com.jastley.wheelycool.ui.main;

import com.jastley.wheelycool.database.entities.Word;
import com.jastley.wheelycool.di.App;
import com.jastley.wheelycool.repositories.WordRepository;
import com.jastley.wheelycool.utils.SingleLiveEvent;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {

    private LiveData<List<Word>> wordList;

    //SingleLiveEvent class for error/snackbar messages to prevent firing more than once in app/fragment lifecycle
    private SingleLiveEvent<String> snackBarMessage;

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

    public SingleLiveEvent<String> getSnackBarMessage() {
        return snackBarMessage = repository.getSnackbarMessage();
    }
}
