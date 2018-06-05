package ru.bakaystas.parrotwings.di.module;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import ru.bakaystas.parrotwings.di.ActivityScope;
import ru.bakaystas.parrotwings.mvp.presenter.LoginPresenter;

/**
 * Created by 1 on 20.04.2018.
 */

@Module
public class LoginModule {

    @ActivityScope
    @Provides
    public LoginPresenter provideLoginPresenter(){
        return new LoginPresenter();
    }
}
