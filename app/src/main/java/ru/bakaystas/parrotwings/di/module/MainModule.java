package ru.bakaystas.parrotwings.di.module;

import dagger.Module;
import dagger.Provides;
import ru.bakaystas.parrotwings.MainActivity;
import ru.bakaystas.parrotwings.di.ActivityScope;
import ru.bakaystas.parrotwings.mvp.presenter.MainPresenter;

/**
 * Created by 1 on 22.04.2018.
 */

@Module
public class MainModule {
    private MainActivity mainActivity;

    public MainModule(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    @ActivityScope
    @Provides
    public MainPresenter provideMainPresenter(){
        return new MainPresenter(mainActivity);
    }
}
