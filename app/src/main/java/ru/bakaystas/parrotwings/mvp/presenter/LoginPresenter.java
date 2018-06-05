package ru.bakaystas.parrotwings.mvp.presenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import java.io.InvalidClassException;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.bakaystas.parrotwings.LoginActivity;
import ru.bakaystas.parrotwings.MainActivity;
import ru.bakaystas.parrotwings.MyApplication;
import ru.bakaystas.parrotwings.di.module.LoginModule;
import ru.bakaystas.parrotwings.model.Reterns;
import ru.bakaystas.parrotwings.model.UserLogin;
import ru.bakaystas.parrotwings.mvp.contract.LoginContract;
import ru.bakaystas.parrotwings.rest.api.APIService;
import ru.bakaystas.parrotwings.utils.Preferences;

/**
 * Created by 1 on 20.04.2018.
 */

public class LoginPresenter implements LoginContract.Presenter {
    private static final String LOG_TAG = "LoginPresenter";
    private final static String TOKEN = "token";
    private final static String EMAIL = "email";

    private LoginActivity activity;

    private String token;

    @Inject
    APIService mApiService;

    public LoginPresenter(){
        MyApplication.getsApplicationComponent().inject(this);
    }

    public void setActivity(LoginActivity loginActivity){
        this.activity = loginActivity;
    }

    public void login(String email, String password){
        String emailField = email;
        String passwordField = password;

        if (!TextUtils.isEmpty(emailField) && !TextUtils.isEmpty(passwordField)) {
            UserLogin mUserLogin = new UserLogin();
            mUserLogin.setEmail(emailField);
            mUserLogin.setPassword(passwordField);
            tryLogin(mUserLogin);
        } else {
            activity.showMsg("Не все поля заполнены!");
            Log.d(LOG_TAG, "Не все поля заполнены иди не совпадают пароли!");
        }
    }

    private void tryLogin(final UserLogin userLogin) {
        mApiService.loginUser(userLogin)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableSingleObserver<Reterns>() {
                        @Override
                        public void onSuccess(Reterns reterns) {
                            activity.showMsg("User login!");
                            Log.d(LOG_TAG, "Log ged user: " + reterns.getId_token());
                            token = "bearer " + reterns.getId_token();
                            try {
                                Preferences.savePreference(TOKEN, token);
                                Preferences.savePreference(EMAIL, userLogin.getEmail());
                            } catch (InvalidClassException e) {
                                e.printStackTrace();
                            }

                            activity.startMainActivity();
                        }

                        @Override
                        public void onError(Throwable e) {
                            activity.showMsg("Error login: " + e.getMessage());
                            Log.d(LOG_TAG, "Error login: " + e.getMessage());
                        }
                    });
    }
}
