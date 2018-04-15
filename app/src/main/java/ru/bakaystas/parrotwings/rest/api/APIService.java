package ru.bakaystas.parrotwings.rest.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import ru.bakaystas.parrotwings.model.LoggedUserInfo;
import ru.bakaystas.parrotwings.model.RecipientSearch;
import ru.bakaystas.parrotwings.model.ResponseHistoryTransaction;
import ru.bakaystas.parrotwings.model.ResponseTransaction;
import ru.bakaystas.parrotwings.model.Reterns;
import ru.bakaystas.parrotwings.model.Transaction;
import ru.bakaystas.parrotwings.model.User;
import ru.bakaystas.parrotwings.model.UserLogin;

/**
 * Created by 1 on 13.04.2018.
 */

public interface APIService {
    @POST ("/users")
    Call<Reterns> createUser(@Body User mUser);

    @POST ("/sessions/create")
    Call<Reterns> loginUser(@Body UserLogin mUserLogin);

    @GET ("/api/protected/user-info")
    Call<LoggedUserInfo> getLoggedUserInfo(@Header("Authorization") String token);

    @POST ("/api/protected/transactions")
    @FormUrlEncoded
    Call<ResponseTransaction> transfer(@Header("Authorization") String token, @Field("name") String mName, @Field("amount") Integer mAmount);

    @GET ("/api/protected/transactions")
    Call<ResponseHistoryTransaction> historyTransactions(@Header("Authorization") String token);

    @POST ("/api/protected/users/list")
    @FormUrlEncoded
    Call<List<RecipientSearch>> searchRecipient(@Header("Authorization") String token, @Field("filter") String mFilter);
}
