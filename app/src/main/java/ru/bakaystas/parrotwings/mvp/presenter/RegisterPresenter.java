package ru.bakaystas.parrotwings.mvp.presenter;

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
import ru.bakaystas.parrotwings.MainActivity;
import ru.bakaystas.parrotwings.MyApplication;
import ru.bakaystas.parrotwings.RegisterActivity;
import ru.bakaystas.parrotwings.di.module.RegisterModule;
import ru.bakaystas.parrotwings.model.Reterns;
import ru.bakaystas.parrotwings.model.User;
import ru.bakaystas.parrotwings.mvp.contract.RegisterContract;
import ru.bakaystas.parrotwings.rest.api.APIService;
import ru.bakaystas.parrotwings.utils.Preferences;

/**
 * Created by 1 on 24.04.2018.
 */

public class RegisterPresenter implements RegisterContract.Presenter {
    private static final String LOG_TAG = "RegisterPresenter";
    private final static String TOKEN = "token";
    private String token;
    private RegisterActivity registerActivity;

    @Inject
    APIService mApiService;

    public RegisterPresenter(RegisterActivity registerActivity){
        MyApplication.getsApplicationComponent().inject(this);
        this.registerActivity = registerActivity;
    }

    public void registerNewUser(String username, String email, String password, String repeatPassword){
        if(!TextUtils.isEmpty(username) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && repeatPassword.contains(password)){
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(password);
            tryRegisterUser(user);
        }else {
            registerActivity.showMsg("Не все поля заполнены или не совпадают пароли!");
        }
    }

    private void tryRegisterUser(User user) {
        mApiService.createUser(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<Reterns>() {
                    @Override
                    public void onSuccess(Reterns reterns) {
                        registerActivity.showMsg("User created!");
                    Log.d(LOG_TAG, "User created! " + reterns.getId_token());
                    token = "bearer " + reterns.getId_token();
                    try {
                        Preferences.savePreference(TOKEN, token);
                    } catch (InvalidClassException e) {
                        e.printStackTrace();
                    }
                    registerActivity.startMainActivity();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(LOG_TAG, "OnError! " + e.getMessage());
                    }
                });
    }

//    private void tryRegisterUser(User user) {
//        mApiService.createUser(user).enqueue(new Callback<Reterns>() {
//            @Override
//            public void onResponse(Call<Reterns> call, Response<Reterns> response) {
//                if (response.isSuccessful()){
//                    registerActivity.showMsg("User created!");
//                    Log.d(LOG_TAG, "User created! " + response.body().getId_token());
//                    token = "bearer " + response.body().getId_token();
//                    try {
//                        Preferences.savePreference(TOKEN, token);
//                    } catch (InvalidClassException e) {
//                        e.printStackTrace();
//                    }
//                    registerActivity.startMainActivity();
//                }else {
//                    registerActivity.showMsg("Error register: " + response.code());
//                    Log.d(LOG_TAG, "Error register: " + response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Reterns> call, Throwable t) {
//                Log.e(LOG_TAG, "Error: " + t.getMessage());
//            }
//        });
//    }
}
