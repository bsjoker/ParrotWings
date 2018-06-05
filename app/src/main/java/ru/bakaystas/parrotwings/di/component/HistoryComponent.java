package ru.bakaystas.parrotwings.di.component;

import dagger.Subcomponent;
import ru.bakaystas.parrotwings.HistoryActivity;
import ru.bakaystas.parrotwings.di.ActivityScope;
import ru.bakaystas.parrotwings.di.module.HistoryModule;
import ru.bakaystas.parrotwings.mvp.presenter.HistoryPresenter;

/**
 * Created by 1 on 25.04.2018.
 */

@Subcomponent(modules = {HistoryModule.class})
@ActivityScope
public interface HistoryComponent {
    HistoryActivity inject(HistoryActivity historyActivity);
    HistoryPresenter inject(HistoryPresenter historyPresenter);
}
