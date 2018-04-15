package ru.bakaystas.parrotwings.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 1 on 15.04.2018.
 */

public class ResponseHistoryTransaction {
    @SerializedName("trans_token")
    @Expose
    private List<Transaction> trans_token;

    public List<Transaction> getTrans_token() {
        return trans_token;
    }

    public void setTrans_token(List<Transaction> trans_token) {
        this.trans_token = trans_token;
    }
}
