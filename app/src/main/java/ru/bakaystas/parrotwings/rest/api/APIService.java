package ru.bakaystas.parrotwings.rest.api;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import ru.bakaystas.parrotwings.model.RecipientSearch;
import ru.bakaystas.parrotwings.model.ResponseHistoryTransaction;
import ru.bakaystas.parrotwings.model.ResponseLoggedUserInfo;
import ru.bakaystas.parrotwings.model.ResponseTransaction;
import ru.bakaystas.parrotwings.model.Reterns;
import ru.bakaystas.parrotwings.model.User;
import ru.bakaystas.parrotwings.model.UserLogin;

/**
 * Created by 1 on 13.04.2018.
 */

public interface APIService {
    @POST ("/users")
    Single<Reterns> createUser(@Body User mUser);

    @POST ("/sessions/create")
    Single<Reterns> loginUser(@Body UserLogin mUserLogin);

    @GET ("/api/protected/user-info")
    Single<ResponseLoggedUserInfo> getLoggedUserInfo(@Header("Authorization") String token);

    @POST ("/api/protected/transactions")
    @FormUrlEncoded
    Single<ResponseTransaction> transfer(@Header("Authorization") String token, @Field("name") String mName, @Field("amount") Integer mAmount);

    @GET ("/api/protected/transactions")
    Single<ResponseHistoryTransaction> historyTransactions(@Header("Authorization") String token);

    @POST ("/api/protected/users/list")
    @FormUrlEncoded
    Observable<List<RecipientSearch>> searchRecipient(@Header("Authorization") String token, @Field("filter") String mFilter);
}
