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
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.bakaystas.parrotwings.common.Constants;
import ru.bakaystas.parrotwings.di.component.LoginComponent;
import ru.bakaystas.parrotwings.di.module.LoginModule;
import ru.bakaystas.parrotwings.model.Reterns;
import ru.bakaystas.parrotwings.model.UserLogin;
import ru.bakaystas.parrotwings.mvp.contract.LoginContract;
import ru.bakaystas.parrotwings.mvp.presenter.LoginPresenter;
import ru.bakaystas.parrotwings.rest.api.APIService;

/**
 * Created by 1 on 14.04.2018.
 */

public class LoginActivity extends AppCompatActivity implements LoginContract.View{

    private static final String LOG_TAG = "LoginActivity";
    LoginComponent loginComponent;

    @BindView(R.id.edit_text_email)
    EditText email;

    @BindView(R.id.edit_text_password)
    EditText password;

    @BindView(R.id.buttonLogin)
    Button login;

    @BindView(R.id.buttonCreate)
    Button create;

    @Inject
    LoginPresenter presenter;

//    public static void loginUser(Context context){
//        startActivity(context, Constants.ScreenMode.LOGIN);
//    }
//
//    public static void registerUser(Context context){
//        startActivity(context, Constants.ScreenMode.REGISTER);
//    }
//
//    private static void startActivity(Context context, Constants.ScreenMode screenMode) {
//        Intent intent = new Intent(context, LoginActivity.class);
//        intent.putExtra(Constants.EXTRA_MODE, screenMode);
//        context.startActivity(intent);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        plusLoginComponent();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.login(email.getText().toString().trim(), password.getText().toString().trim());
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        ButterKnife.bind(this);
    }

    public void plusLoginComponent(){
        if(loginComponent == null){
            MyApplication.get(this).getsApplicationComponent().plusLoginComponent(new LoginModule()).inject(this);
            //loginComponent = null;
        }
    }

    public void startMainActivity(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    @Inject
    void setActivity() {
        presenter.setActivity(this);
    }

    public void showMsg(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        email.setText(null);
        password.setText(null);
    }
}
