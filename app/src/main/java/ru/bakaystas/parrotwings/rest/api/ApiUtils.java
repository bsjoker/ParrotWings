package ru.bakaystas.parrotwings.rest.api;

import ru.bakaystas.parrotwings.rest.RestClient;

/**
 * Created by 1 on 13.04.2018.
 */

public class ApiUtils {
    public static APIService getAPIService() {

        return RestClient.getClient().create(APIService.class);
    }
}
