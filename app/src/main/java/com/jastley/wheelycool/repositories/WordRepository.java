package com.jastley.wheelycool.repositories;

import android.util.Log;

import com.jastley.wheelycool.database.AppDatabase;
import com.jastley.wheelycool.database.entities.Word;
import com.jastley.wheelycool.di.App;
import com.jastley.wheelycool.utils.SingleLiveEvent;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.lifecycle.LiveData;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class WordRepository {

    SingleLiveEvent<String> snackbarMessage = new SingleLiveEvent<>();

    @Inject
    AppDatabase appDatabase;

    @Inject
    public WordRepository() {
        App.getApp().getAppComponent().inject(this);
    }

    public LiveData<List<Word>> getWordsList() {
        return appDatabase.getWordDao().getAllWords();
    }

    public void addWord (final String newWord) {

        final Word word = new Word();
        word.setWord(newWord);

        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                appDatabase.getWordDao().insertWord(word);
            }
        }).subscribeOn(Schedulers.io())
        .subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e("ADD_WORD", e.getMessage());
                snackbarMessage.postValue("An error occurred while trying to save word");
            }
        });

    }

    public void deleteWord(final String word) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                appDatabase.getWordDao().deleteWord(word);
            }
        }).subscribeOn(Schedulers.io())
        .subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e("DELETE_WORD", e.getMessage());
                snackbarMessage.postValue("An error occurred while trying to delete word");
            }
        });
    }

    public SingleLiveEvent<String> getSnackbarMessage() {
        return snackbarMessage;
    }
}
