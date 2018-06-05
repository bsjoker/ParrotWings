package ru.bakaystas.parrotwings.di.component;

import dagger.Subcomponent;
import ru.bakaystas.parrotwings.MainActivity;
import ru.bakaystas.parrotwings.di.ActivityScope;
import ru.bakaystas.parrotwings.di.module.MainModule;
import ru.bakaystas.parrotwings.mvp.presenter.MainPresenter;

/**
 * Created by 1 on 22.04.2018.
 */

@Subcomponent(modules = {MainModule.class})
@ActivityScope
public interface MainComponent {
    MainActivity inject(MainActivity mainActivity);
    MainPresenter inject(MainPresenter mainPresenter);
}
