package ru.bakaystas.parrotwings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.bakaystas.parrotwings.model.RecipientSearch;
import ru.bakaystas.parrotwings.model.ResponseHistoryTransaction;
import ru.bakaystas.parrotwings.model.ResponseTransaction;
import ru.bakaystas.parrotwings.model.Transaction;
import ru.bakaystas.parrotwings.rest.api.APIService;
import ru.bakaystas.parrotwings.rest.api.ApiUtils;

/**
 * Created by 1 on 15.04.2018.
 */

public class HistoryActivity extends AppCompatActivity {
    private static final String LOG_TAG = "HistoryActivity";
    public static final String TOKEN = null;
    private APIService mApiService;

    private String token, name;
    private Integer amount;
    private Button history;

    private RecyclerView recyclerView;

    private List<Transaction> mTransactions;
    private SharedPreferences Prefs;
    private TransactionAdapter mTransactionAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        mApiService = ApiUtils.getAPIService();

        Prefs = getSharedPreferences(TOKEN, Context.MODE_PRIVATE);
        token = Prefs.getString("token", "0");

        name = "name";
        amount = 0;

        history = (Button)findViewById(R.id.transaction_from_history);

        recyclerView = (RecyclerView) findViewById(R.id.history_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(HistoryActivity.this));

        mTransactions = new ArrayList<>();
        getHistoryTransaction(token);
        mTransactionAdapter = new TransactionAdapter(mTransactions);
        recyclerView.setAdapter(mTransactionAdapter);

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (amount!=0 && !name.contains("name")) {
                    SharedPreferences.Editor editor = Prefs.edit();
                    editor.putString("name", name);
                    editor.putInt("amount", amount);
                    editor.apply();
                    finish();
                }else{
                    showMSG();
                }
            }
        });
    }

    private void showMSG(){
        Toast.makeText(this, "Выберите транзакцию!", Toast.LENGTH_LONG).show();
    }

//    private void sort(){
//        Collections.sort(mTransactions, new Comparator<Transaction>() {
//            @Override
//            public int compare(Transaction transaction, Transaction t1) {
//                return transaction.getUsername().compareTo(t1.getUsername());
//            }
//        });
//    }

    private void getHistoryTransaction(String token) {
        mApiService.historyTransactions(token).enqueue(new Callback<ResponseHistoryTransaction>() {
            @Override
            public void onResponse(Call<ResponseHistoryTransaction> call, Response<ResponseHistoryTransaction> response) {
                mTransactions = response.body().getTrans_token();
            }

            @Override
            public void onFailure(Call<ResponseHistoryTransaction> call, Throwable t) {

            }
        });
    }

    private class TransactionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Transaction mTransaction;
        private TextView mDate, mName, mAmount, mBalance;

        TransactionHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mDate = (TextView) itemView.findViewById(R.id.transaction_date);
            mName = (TextView) itemView.findViewById(R.id.transaction_correspondent_name);
            mAmount = (TextView) itemView.findViewById(R.id.transaction_amount);
            mBalance = (TextView) itemView.findViewById(R.id.transaction_balance);
        }

        void bindTransaction(Transaction transaction) {
            mTransaction = transaction;
            mDate.setText(transaction.getDate());
            mName.setText(transaction.getUsername());
            mAmount.setText(transaction.getAmount());
            mBalance.setText(transaction.getBalance());
        }

        @Override
        public void onClick(View v) {
            name = mTransaction.getUsername();
            amount = mTransaction.getAmount();
            setResult(RESULT_OK);
        }
    }

    private class TransactionAdapter extends RecyclerView.Adapter<HistoryActivity.TransactionHolder> {

        private List<Transaction> mTransactions;

        TransactionAdapter(List<Transaction> transactions) {
            mTransactions = transactions;
        }

        @Override
        public int getItemCount() {
            if (mTransactions != null)
                return mTransactions.size();

            return 0;
        }

        @Override
        public void onBindViewHolder(HistoryActivity.TransactionHolder holder, int position) {
            Transaction transaction = mTransactions.get(position);
            holder.bindTransaction(transaction);
        }

        @Override
        public HistoryActivity.TransactionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(HistoryActivity.this);
            View v = inflater.inflate(R.layout.transaction_item, parent, false);

            return new HistoryActivity.TransactionHolder(v);
        }

    }
}
