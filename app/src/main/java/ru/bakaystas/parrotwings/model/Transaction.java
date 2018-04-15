package ru.bakaystas.parrotwings.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by 1 on 14.04.2018.
 */

public class Transaction {
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("amount")
    @Expose
    private Integer amount;

    @SerializedName("balance")
    @Expose
    private Integer balance;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }
}
