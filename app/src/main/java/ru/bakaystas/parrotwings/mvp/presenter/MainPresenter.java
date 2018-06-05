package ru.bakaystas.parrotwings.mvp.presenter;

import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import ru.bakaystas.parrotwings.MainActivity;
import ru.bakaystas.parrotwings.MyApplication;
import ru.bakaystas.parrotwings.model.LoggedUserInfo;
import ru.bakaystas.parrotwings.model.RecipientSearch;
import ru.bakaystas.parrotwings.model.ResponseLoggedUserInfo;
import ru.bakaystas.parrotwings.model.ResponseTransaction;
import ru.bakaystas.parrotwings.mvp.contract.MainContract;
import ru.bakaystas.parrotwings.rest.api.APIService;
import ru.bakaystas.parrotwings.utils.Preferences;

/**
 * Created by 1 on 22.04.2018.
 */

public class MainPresenter implements MainContract.Presenter {
    private static final String LOG_TAG = "MainPresenter";
    private final static String TOKEN = "token";
    private String token;

    MainActivity mainActivity;

    @Inject
    APIService apiService;

    public MainPresenter(MainActivity mainActivity) {
        MyApplication.getsApplicationComponent().inject(this);
        this.mainActivity = mainActivity;
        token = Preferences.getSharedPreferences().getString(TOKEN, " ");
    }

    public void userInfo() {
        updateData();
        getUserInfo();
    }

    @Override
    public void validateForm() {
        String mAmount = mainActivity.getAmount().getText().toString();
        mainActivity.setValid(true);
        if (TextUtils.isEmpty(mAmount)) {
            mainActivity.getAmount().setError("Required.");
            mainActivity.setValid(false);
            mainActivity.showMsg("Не хватает данных для перевода!");
        } else {
            mainActivity.getAmount().setError(null);
        }

        if (TextUtils.isEmpty(mainActivity.getRecipient())) {
            mainActivity.getTvRecipient().setTextColor(Color.RED);
            mainActivity.setValid(false);
            mainActivity.showMsg("Не хватает данных для перевода!");
        } else {
            mainActivity.getTvRecipient().setTextColor(Color.BLACK);
        }
    }

    public void transfer(String mRecipient, Integer mAmount) {
        String recipientField = mRecipient;
        Integer amountField = mAmount;

        if (!TextUtils.isEmpty(recipientField) && amountField != 0 && amountField != null) {
            doTransfer(recipientField, amountField);
        } else {
            mainActivity.showMsg("Не хватает данных для перевода!");
            Log.d(LOG_TAG, "Не хватает данных для перевода!");
        }
    }

    private void doTransfer(String recipientField, Integer amountField) {
        apiService.transfer(token, recipientField, amountField).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<ResponseTransaction>() {
                    @Override
                    public void onSuccess(ResponseTransaction responseTransaction) {
                        mainActivity.showMsg("Перевод " + responseTransaction.getTrans_token().getAmount() + "PW пользователю " + responseTransaction.getTrans_token().getUsername() + " успешно совершен!");
                        Log.d(LOG_TAG, "Transfer: " + responseTransaction.getTrans_token().getBalance());
                        mainActivity.setUserInfo(null, "На Вашем счету: " + responseTransaction.getTrans_token().getBalance());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(LOG_TAG, "Transfer message error" + e.getMessage());
                    }
                });
    }


    private void getUserInfo() {
        apiService.getLoggedUserInfo(token).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<ResponseLoggedUserInfo>() {
                    @Override
                    public void onSuccess(ResponseLoggedUserInfo loggedUserInfo) {
                        Log.d(LOG_TAG, "Logged user id: " + loggedUserInfo.getUser_info_token().getId());
                        Log.d(LOG_TAG, "Logged user name: " + loggedUserInfo.getUser_info_token().getUsername());
                        Log.d(LOG_TAG, "Logged user email: " + loggedUserInfo.getUser_info_token().getEmail());
                        Log.d(LOG_TAG, "Logged user balance: " + loggedUserInfo.getUser_info_token().getBalance());
                        mainActivity.setUserInfo(loggedUserInfo.getUser_info_token().getUsername(), "На Вашем счету: " + loggedUserInfo.getUser_info_token().getBalance());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(LOG_TAG, "Error: " + e.getMessage());
                    }
                });
    }

    public void searchRecipient(String filter) {
        Log.d(LOG_TAG, "Filter string: " + filter);
        apiService.searchRecipient(token, filter).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<List<RecipientSearch>>() {
                    @Override
                    public void onNext(List<RecipientSearch> list) {
                        mainActivity.updateAdapter(list);
                        Log.d(LOG_TAG, "SearchRecipient: " + list.size());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Log.d(LOG_TAG, "OnComplete!");
                    }
                });
    }

    public void updateData() {
        mainActivity.setUserInfo("Stas", "---");
    }
}
