package ru.bakaystas.parrotwings.mvp.contract;

import java.util.List;

import ru.bakaystas.parrotwings.model.RecipientSearch;

/**
 * Created by 1 on 24.04.2018.
 */

public interface MainContract {
    interface View{
        void setUserInfo(String mUser, String newBalance);
        void showMsg(String msg);
        void updateAdapter(List<RecipientSearch> list);
    }

    interface Presenter{
        void userInfo();
        void validateForm();
        void transfer(String recipient, Integer amount);
        void searchRecipient(String filter);
    }
}
