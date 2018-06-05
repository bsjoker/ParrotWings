package ru.bakaystas.parrotwings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.bakaystas.parrotwings.di.component.RegisterComponent;
import ru.bakaystas.parrotwings.di.module.RegisterModule;
import ru.bakaystas.parrotwings.model.Reterns;
import ru.bakaystas.parrotwings.model.User;
import ru.bakaystas.parrotwings.mvp.contract.RegisterContract;
import ru.bakaystas.parrotwings.mvp.presenter.RegisterPresenter;
import ru.bakaystas.parrotwings.rest.api.APIService;

/**
 * Created by 1 on 15.04.2018.
 */

public class RegisterActivity extends AppCompatActivity implements RegisterContract.View{
    private static final String LOG_TAG = "RegisterActivity";

    @BindView(R.id.edit_text_username)
    EditText username;

    @BindView(R.id.edit_text_email)
    EditText email;

    @BindView(R.id.edit_text_password)
    EditText password;

    @BindView(R.id.edit_text_repeat_password)
    EditText repeatPassword;

    @BindView(R.id.buttonRegisterNew)
    Button registerNewUser;

    RegisterComponent registerComponent;

    @Inject
    RegisterPresenter registerPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
        plusRegisterComponent();

        registerNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerPresenter.registerNewUser(username.getText().toString().trim(),
                        email.getText().toString().trim(), password.getText().toString().trim(),
                        repeatPassword.getText().toString().trim());
            }
        });
    }

    private void initView() {
        ButterKnife.bind(this);
    }

    private void plusRegisterComponent() {
        if(registerComponent==null){
            MyApplication.get(this).getsApplicationComponent().plusRegisterComponent(new RegisterModule(this)).inject(this);
        }
    }

    public void startMainActivity(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public void showMsg(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
