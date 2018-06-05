package ru.bakaystas.parrotwings.mvp.presenter;

import ru.bakaystas.parrotwings.mvp.view.MvpView;

/**
 * Created by 1 on 20.04.2018.
 */

public interface MvpPresenter<V extends MvpView> {
    void attachView(V mvpView);
    void viewIsReady();
    void detachView();
    void destroy();
}
