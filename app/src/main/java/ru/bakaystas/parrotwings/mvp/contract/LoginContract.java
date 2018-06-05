package ru.bakaystas.parrotwings.mvp.contract;

import ru.bakaystas.parrotwings.mvp.presenter.MvpPresenter;
import ru.bakaystas.parrotwings.mvp.view.MvpView;

/**
 * Created by 1 on 20.04.2018.
 */

public interface LoginContract {
    interface View{
        void startMainActivity();
    }

    interface Presenter{
        void login(String email, String password);
    }
}
