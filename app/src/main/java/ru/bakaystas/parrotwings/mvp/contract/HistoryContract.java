package ru.bakaystas.parrotwings.mvp.contract;

import java.util.List;

import ru.bakaystas.parrotwings.model.Transaction;

/**
 * Created by 1 on 25.04.2018.
 */

public interface HistoryContract {
    interface View{
        void showMSG();
        void updateAdapter(List<Transaction> transactions);
    }

    interface Presenter{
        void getHistoryTransaction();
    }
}
