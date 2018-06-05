package ru.bakaystas.parrotwings;

import android.app.Application;
import android.content.Context;

import ru.bakaystas.parrotwings.di.component.ApplicationComponent;
import ru.bakaystas.parrotwings.di.component.DaggerApplicationComponent;
import ru.bakaystas.parrotwings.di.component.LoginComponent;
import ru.bakaystas.parrotwings.di.module.ApplicationModule;
import ru.bakaystas.parrotwings.di.module.LoginModule;

/**
 * Created by 1 on 20.04.2018.
 */

public class MyApplication extends Application {

    private static ApplicationComponent sApplicationComponent;
    private static MyApplication sMyApplicationInstance;

    public static MyApplication getInstance() {
        return sMyApplicationInstance;
    }

    public static MyApplication get(Context context) {
        return (MyApplication) context.getApplicationContext();
    }
    @Override
    public void onCreate() {
        super.onCreate();
        initComponent();
        sMyApplicationInstance = this;
    }

    private void initComponent() {
        sApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public static ApplicationComponent getsApplicationComponent(){
        return sApplicationComponent;
    }
}
