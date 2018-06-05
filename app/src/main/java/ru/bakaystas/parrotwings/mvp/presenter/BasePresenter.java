package ru.bakaystas.parrotwings.mvp.presenter;

import ru.bakaystas.parrotwings.mvp.view.MvpView;

/**
 * Created by 1 on 20.04.2018.
 */

public abstract class BasePresenter<T extends MvpView> implements MvpPresenter<T>{
    private T view;

    @Override
    public void attachView(T mvpView) {
        view = mvpView;
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void destroy() {

    }

    protected boolean isViewAttached(){
        return view != null;
    }
}
