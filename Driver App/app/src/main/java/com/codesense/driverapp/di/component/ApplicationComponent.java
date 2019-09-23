package com.codesense.driverapp.di.component;

import android.content.Context;

import com.codesense.driverapp.base.BaseApplication;
import com.codesense.driverapp.di.module.ActivityBindingModule;
import com.codesense.driverapp.di.module.NetworkModule;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import dagger.android.support.DaggerApplication;

@Component(modules = {AndroidSupportInjectionModule.class, ActivityBindingModule.class,
        NetworkModule.class})
@Singleton
public interface ApplicationComponent extends AndroidInjector<DaggerApplication> {

    void inject(BaseApplication application);
    void inject(Context context);
    //SharedPreferences getSharedPrefs();

    /*@Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);
        @BindsInstance
        Builder sharedPreferencesModule(SharedPreferencesModule sharedPreferencesModule);
        ApplicationComponent build();
    }*/
}
