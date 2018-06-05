package ru.bakaystas.parrotwings.mvp.presenter;

import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import ru.bakaystas.parrotwings.HistoryActivity;
import ru.bakaystas.parrotwings.MyApplication;
import ru.bakaystas.parrotwings.model.ResponseHistoryTransaction;
import ru.bakaystas.parrotwings.model.Transaction;
import ru.bakaystas.parrotwings.mvp.contract.HistoryContract;
import ru.bakaystas.parrotwings.rest.api.APIService;
import ru.bakaystas.parrotwings.utils.Preferences;

/**
 * Created by 1 on 25.04.2018.
 */

public class HistoryPresenter implements HistoryContract.Presenter {
    private static final String LOG_TAG = "HistoryPresenter";
    public static final String TOKEN = "token";
    private String token;
    private List<Transaction> mTransactions;

    HistoryActivity historyActivity;

    @Inject
    APIService apiService;

    public HistoryPresenter(HistoryActivity historyActivity){
        MyApplication.getsApplicationComponent().inject(this);
        token = Preferences.getSharedPreferences().getString(TOKEN, " ");
        this.historyActivity = historyActivity;
    }

    public void getHistoryTransaction() {
        Log.d(LOG_TAG, "Token: " + token);
        apiService.historyTransactions(token).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<ResponseHistoryTransaction>() {
                    @Override
                    public void onSuccess(ResponseHistoryTransaction responseHistoryTransaction) {
                        mTransactions = responseHistoryTransaction.getTrans_token();
                        Log.d(LOG_TAG, "History: " + mTransactions.size());
                        historyActivity.updateAdapter(mTransactions);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(LOG_TAG, "Error!" + e.getMessage());
                    }
                });
    }
}
