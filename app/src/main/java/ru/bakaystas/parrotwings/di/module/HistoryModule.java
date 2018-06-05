package ru.bakaystas.parrotwings.di.module;

import dagger.Module;
import dagger.Provides;
import ru.bakaystas.parrotwings.HistoryActivity;
import ru.bakaystas.parrotwings.di.ActivityScope;
import ru.bakaystas.parrotwings.mvp.presenter.HistoryPresenter;

/**
 * Created by 1 on 25.04.2018.
 */

@Module
public class HistoryModule {
    private HistoryActivity historyActivity;

    public HistoryModule(HistoryActivity historyActivity){
        this.historyActivity = historyActivity;
    }

    @ActivityScope
    @Provides
    public HistoryPresenter provireHistoryPresenter (){
        return new HistoryPresenter(historyActivity);
    }
}
