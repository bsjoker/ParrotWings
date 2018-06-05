package ru.bakaystas.parrotwings.mvp.contract;

/**
 * Created by 1 on 25.04.2018.
 */

public interface RegisterContract {
    interface View{
        void showMsg(String msg);
        void startMainActivity();
    }

    interface Presenter{
        void registerNewUser(String username, String email, String password, String repeatPassword);

    }
}
