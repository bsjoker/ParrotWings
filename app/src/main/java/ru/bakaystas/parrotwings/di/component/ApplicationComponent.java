package ru.bakaystas.parrotwings.di.component;

import javax.inject.Singleton;

import dagger.Component;
import ru.bakaystas.parrotwings.LoginActivity;
import ru.bakaystas.parrotwings.di.module.ApplicationModule;
import ru.bakaystas.parrotwings.di.module.HistoryModule;
import ru.bakaystas.parrotwings.di.module.LoginModule;
import ru.bakaystas.parrotwings.di.module.MainModule;
import ru.bakaystas.parrotwings.di.module.RegisterModule;
import ru.bakaystas.parrotwings.di.module.RestModule;
import ru.bakaystas.parrotwings.mvp.presenter.HistoryPresenter;
import ru.bakaystas.parrotwings.mvp.presenter.LoginPresenter;
import ru.bakaystas.parrotwings.mvp.presenter.MainPresenter;
import ru.bakaystas.parrotwings.mvp.presenter.RegisterPresenter;

/**
 * Created by 1 on 20.04.2018.
 */

@Singleton
@Component(modules={ApplicationModule.class, RestModule.class})
public interface ApplicationComponent {
    LoginComponent plusLoginComponent(LoginModule loginModule);
    MainComponent plusMainComponent(MainModule mainModule);
    RegisterComponent plusRegisterComponent(RegisterModule registerModule);
    HistoryComponent plusHistoryComponent(HistoryModule historyModule);

    //Presenters
    void inject(LoginPresenter loginPresenter);
    void inject(MainPresenter mainPresenter);
    void inject(RegisterPresenter registerPresenter);
    void inject(HistoryPresenter historyPresenter);
}
