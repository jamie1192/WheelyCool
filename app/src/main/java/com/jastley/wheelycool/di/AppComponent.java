package com.jastley.wheelycool.di;

import com.jastley.wheelycool.modules.RoomModule;
import com.jastley.wheelycool.repositories.WordRepository;
import com.jastley.wheelycool.ui.main.MainViewModel;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = { RoomModule.class })
public interface AppComponent {

    void inject(WordRepository wordRepository);
    void inject(MainViewModel mainViewModel);
}
