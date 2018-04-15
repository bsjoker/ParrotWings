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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.bakaystas.parrotwings.model.Reterns;
import ru.bakaystas.parrotwings.model.UserLogin;
import ru.bakaystas.parrotwings.rest.api.APIService;
import ru.bakaystas.parrotwings.rest.api.ApiUtils;

/**
 * Created by 1 on 14.04.2018.
 */

public class LoginActivity extends AppCompatActivity {

    private static final String LOG_TAG = "LoginActivity";
    public static final String TOKEN = null;
    private APIService mApiService;
    private EditText email, password, repeat_password;
    private TextView user, amount;
    private Button login, create;
    private String token;

    SharedPreferences Prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mApiService = ApiUtils.getAPIService();

        email = (EditText) findViewById(R.id.edit_text_email);
        password = (EditText) findViewById(R.id.edit_text_password);

        login = (Button) findViewById(R.id.buttonLogin);
        create = (Button) findViewById(R.id.buttonCreate);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailField = email.getText().toString().trim();
                String passwordField = password.getText().toString().trim();

                if (!TextUtils.isEmpty(emailField) && !TextUtils.isEmpty(passwordField)
                        && !TextUtils.isEmpty(passwordField)) {
                    UserLogin mUserLogin = new UserLogin();
                    mUserLogin.setEmail(emailField);
                    mUserLogin.setPassword(passwordField);
                    tryLogin(mUserLogin);
                } else {
                    showMsg("Не все поля заполнены!");
                    Log.d(LOG_TAG, "Не все поля заполнены иди не совпадают пароли!");
                }
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



    private void tryLogin(UserLogin userLogin) {
        mApiService.loginUser(userLogin).enqueue(new Callback<Reterns>() {
            @Override
            public void onResponse(Call<Reterns> call, Response<Reterns> response) {
                if (response.isSuccessful()) {
                    showMsg("User login!");
                    Log.d(LOG_TAG, "Logged user: " + response.body().getId_token());
                    token = "bearer " +response.body().getId_token();
                    Prefs = getSharedPreferences(TOKEN, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = Prefs.edit();
                    editor.putString("token", token);
                    editor.putString("email", email.getText().toString().trim());
                    editor.apply();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }else {
                    showMsg("Error login: " + response.code());
                    Log.d(LOG_TAG, "Error login: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Reterns> call, Throwable t) {
                token = "bearer";
                Prefs = getSharedPreferences(TOKEN, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = Prefs.edit();
                editor.putString("token", token);
                editor.apply();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                Log.e(LOG_TAG, "Error: " + t.getMessage());
            }
        });
    }
    private void showMsg(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        email.setText(null);
        password.setText(null);
    }
}
