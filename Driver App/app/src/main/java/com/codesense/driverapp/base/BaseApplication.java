package com.codesense.driverapp.base;


import com.codesense.driverapp.di.component.ApplicationComponent;
import com.codesense.driverapp.di.component.DaggerApplicationComponent;
import com.codesense.driverapp.di.module.NetworkModule;
import com.crashlytics.android.Crashlytics;

import javax.inject.Singleton;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;
import io.fabric.sdk.android.Fabric;

@Singleton
public class BaseApplication extends DaggerApplication {

    private static BaseApplication baseApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        baseApplication = this;
        Fabric.with(this, new Crashlytics());
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {

        ApplicationComponent component = DaggerApplicationComponent.builder()
                .networkModule(new NetworkModule(this))
                .build();
        component.inject(this);

        return component;
    }

    public static BaseApplication getBaseApplication() {
        return baseApplication;
    }
}
