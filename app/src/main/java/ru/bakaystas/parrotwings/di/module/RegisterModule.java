package ru.bakaystas.parrotwings.di.module;

import dagger.Module;
import dagger.Provides;
import ru.bakaystas.parrotwings.RegisterActivity;
import ru.bakaystas.parrotwings.di.ActivityScope;
import ru.bakaystas.parrotwings.mvp.presenter.RegisterPresenter;

/**
 * Created by 1 on 24.04.2018.
 */

@Module
public class RegisterModule {
    private RegisterActivity registerActivity;

    public RegisterModule(RegisterActivity registerActivity){
        this.registerActivity = registerActivity;
    }

    @ActivityScope
    @Provides
    public RegisterPresenter provideRegisterPresenter(){
        return new RegisterPresenter(registerActivity);
    }
}
