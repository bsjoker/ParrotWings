package ru.bakaystas.parrotwings.di.component;

import dagger.Subcomponent;
import ru.bakaystas.parrotwings.RegisterActivity;
import ru.bakaystas.parrotwings.di.ActivityScope;
import ru.bakaystas.parrotwings.di.module.RegisterModule;
import ru.bakaystas.parrotwings.mvp.presenter.RegisterPresenter;

/**
 * Created by 1 on 24.04.2018.
 */

@Subcomponent(modules = {RegisterModule.class})
@ActivityScope
public interface RegisterComponent {
    RegisterActivity inject(RegisterActivity registerActivity);
    RegisterPresenter inject(RegisterPresenter registerModule);
}
