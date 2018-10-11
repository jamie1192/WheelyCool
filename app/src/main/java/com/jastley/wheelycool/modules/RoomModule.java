package com.jastley.wheelycool.modules;

import android.app.Application;

import com.jastley.wheelycool.database.AppDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RoomModule {

    private AppDatabase appDatabase;

    public RoomModule(Application application) {
        this.appDatabase = AppDatabase.getAppDatabase(application);
    }

    @Singleton
    @Provides
    public AppDatabase providesAppDatabase() {
        return appDatabase;
    }
}
