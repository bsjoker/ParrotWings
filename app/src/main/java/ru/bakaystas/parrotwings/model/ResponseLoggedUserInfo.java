package ru.bakaystas.parrotwings.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by 1 on 05.06.2018.
 */

public class ResponseLoggedUserInfo {
    @SerializedName("user_info_token")
    @Expose
    private LoggedUserInfo user_info_token;

    public LoggedUserInfo getUser_info_token() {
        return user_info_token;
    }

    public void setUser_info_token(LoggedUserInfo user_info_token) {
        this.user_info_token = user_info_token;
    }
}
