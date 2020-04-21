package com.codesense.driverapp.base;


import android.content.Context;

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
    ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        baseApplication = this;
        Fabric.with(this, new Crashlytics());
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {

        component = DaggerApplicationComponent.builder()
                .networkModule(new NetworkModule(this))
                .build();
        component.inject(this);

        return component;
    }

    public static BaseApplication getBaseApplication() {
        return baseApplication;
    }



    public ApplicationComponent getAppComponent(Context context) {
        return ((BaseApplication) context.getApplicationContext()).component;
    }
}
