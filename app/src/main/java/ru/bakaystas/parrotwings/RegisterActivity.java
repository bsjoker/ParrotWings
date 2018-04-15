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
import ru.bakaystas.parrotwings.model.User;
import ru.bakaystas.parrotwings.model.UserLogin;
import ru.bakaystas.parrotwings.rest.api.APIService;
import ru.bakaystas.parrotwings.rest.api.ApiUtils;

/**
 * Created by 1 on 15.04.2018.
 */

public class RegisterActivity extends AppCompatActivity {
    private static final String LOG_TAG = "RegisterActivity";
    public static final String TOKEN = null;
    private APIService mApiService;
    private EditText username, email, password, repeatPassword;
    private TextView user, amount;
    private Button registerNewUser;
    private String token;

    SharedPreferences Prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mApiService = ApiUtils.getAPIService();

        username = (EditText)findViewById(R.id.edit_text_username);
        email = (EditText)findViewById(R.id.edit_text_email);
        password = (EditText) findViewById(R.id.edit_text_password);
        repeatPassword = (EditText) findViewById(R.id.edit_text_repeat_password);

        user = (TextView)findViewById(R.id.text_view_username);
        amount = (TextView)findViewById(R.id.text_view_amount);

        registerNewUser = (Button)findViewById(R.id.buttonRegisterNew);

        registerNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usernameField = username.getText().toString().trim();
                String emailField = email.getText().toString().trim();
                String passwordField = password.getText().toString().trim();
                String repeatPasswordField = repeatPassword.getText().toString().trim();
                if(!TextUtils.isEmpty(usernameField) && !TextUtils.isEmpty(emailField) && !TextUtils.isEmpty(passwordField) && repeatPasswordField.contains(passwordField)){
                    User user = new User();
                    user.setUsername(usernameField);
                    user.setEmail(emailField);
                    user.setPassword(passwordField);
                    tryRegisterUser(user);
                }else {
                    showMsg("Не все поля заполнены или не совпадают пароли!");
                }
            }
        });
    }

    private void tryRegisterUser(User user) {
        mApiService.createUser(user).enqueue(new Callback<Reterns>() {
            @Override
            public void onResponse(Call<Reterns> call, Response<Reterns> response) {
                if (response.isSuccessful()){
                    showMsg("User created!");
                    Log.d(LOG_TAG, "User created! " + response.body().getId_token());
                    token = response.body().getId_token();
                    Prefs = getSharedPreferences(TOKEN, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = Prefs.edit();
                    editor.putString("token", token);
                    editor.apply();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }else {
                    showMsg("Error register: " + response.code());
                    Log.d(LOG_TAG, "Error register: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Reterns> call, Throwable t) {
                Log.e(LOG_TAG, "Error: " + t.getMessage());
            }
        });
    }
    private void showMsg(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
