package ru.bakaystas.parrotwings.di.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.bakaystas.parrotwings.rest.RestClient;
import ru.bakaystas.parrotwings.rest.api.APIService;

/**
 * Created by 1 on 20.04.2018.
 */

@Module
public class RestModule {
    private RestClient mRestClient;

    public RestModule(){
        mRestClient = new RestClient();
    }

    @Singleton
    @Provides
    public RestClient provideRestClient(){ return mRestClient; }

    @Singleton
    @Provides
    public APIService provideApiService(){
        return mRestClient.createService(APIService.class);
    }
}
