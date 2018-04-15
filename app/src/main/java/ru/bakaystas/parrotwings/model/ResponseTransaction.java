package ru.bakaystas.parrotwings.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by 1 on 14.04.2018.
 */

public class ResponseTransaction {
    @SerializedName("trans_token")
    @Expose
    private Transaction trans_token;

    public Transaction getTrans_token() {
        return trans_token;
    }

    public void setTrans_token(Transaction trans_token) {
        this.trans_token = trans_token;
    }
}
