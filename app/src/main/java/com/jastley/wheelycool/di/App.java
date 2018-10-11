package com.jastley.wheelycool.di;

import android.app.Application;

import com.jastley.wheelycool.modules.RoomModule;

public class App extends Application {

    private static App app;
    AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        app = this;

        appComponent = DaggerAppComponent.builder()
                .roomModule(new RoomModule(this))
                .build();
    }

    public static App getApp() {
        return app;
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
