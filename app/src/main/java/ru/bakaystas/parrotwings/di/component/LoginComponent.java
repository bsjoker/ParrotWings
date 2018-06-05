package ru.bakaystas.parrotwings.di.component;

import dagger.Subcomponent;
import ru.bakaystas.parrotwings.LoginActivity;
import ru.bakaystas.parrotwings.di.ActivityScope;
import ru.bakaystas.parrotwings.di.module.LoginModule;
import ru.bakaystas.parrotwings.mvp.presenter.LoginPresenter;

/**
 * Created by 1 on 20.04.2018.
 */

@Subcomponent(modules = {LoginModule.class})
@ActivityScope
public interface LoginComponent {
    LoginActivity inject(LoginActivity loginActivity);
    LoginPresenter inject(LoginPresenter loginPresenter);
}
