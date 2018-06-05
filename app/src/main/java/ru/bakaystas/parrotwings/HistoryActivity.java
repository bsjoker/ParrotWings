package ru.bakaystas.parrotwings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.InvalidClassException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.bakaystas.parrotwings.common.HistoryAdapter;
import ru.bakaystas.parrotwings.common.MyItemClickListener;
import ru.bakaystas.parrotwings.di.component.HistoryComponent;
import ru.bakaystas.parrotwings.di.module.HistoryModule;
import ru.bakaystas.parrotwings.model.Transaction;
import ru.bakaystas.parrotwings.mvp.contract.HistoryContract;
import ru.bakaystas.parrotwings.mvp.presenter.HistoryPresenter;
import ru.bakaystas.parrotwings.utils.Preferences;

/**
 * Created by 1 on 15.04.2018.
 */

public class HistoryActivity extends AppCompatActivity implements HistoryContract.View {
    private static final String LOG_TAG = "HistoryActivity";

    private String name;
    private Integer amount;

    @BindView(R.id.transaction_from_history)
    Button history;

    @BindView(R.id.history_recycler_view)
    RecyclerView recyclerView;

    private List<Transaction> mTransactions;
    private HistoryAdapter mTransactionAdapter;

    @Inject
    HistoryPresenter historyPresenter;

    HistoryComponent historyComponent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        plusHistoryComponent();
        initView();

        name = "name";
        amount = 0;

        recyclerView.setLayoutManager(new LinearLayoutManager(HistoryActivity.this));

        mTransactions = new ArrayList<>();
        historyPresenter.getHistoryTransaction();
        mTransactionAdapter = new HistoryAdapter(mTransactions, new MyItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                name = mTransactions.get(position).getUsername();
                amount = mTransactions.get(position).getAmount();
                Log.d(LOG_TAG, "Name: " + name + ". Amount: " + amount);
                setResult(RESULT_OK);
            }
        });
        recyclerView.setAdapter(mTransactionAdapter);

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (amount != 0 && !name.contains("name")) {
                    try {
                        Preferences.savePreference("name", name);
                        Preferences.savePreference("amount", amount);
                    } catch (InvalidClassException e) {
                        e.printStackTrace();
                    }
                    finish();
                }
            }
        });
    }

    private void initView() {
        ButterKnife.bind(this);
    }

    public void plusHistoryComponent() {
        if(historyComponent == null){
            MyApplication.get(this).getsApplicationComponent().plusHistoryComponent(new HistoryModule(this)).inject(this);
        }
    }

    public void showMSG() {
        Toast.makeText(this, "Выберите транзакцию!", Toast.LENGTH_LONG).show();
    }

    public void updateAdapter(List<Transaction> transactions){
        mTransactions.clear();
        mTransactions.addAll(transactions);
        mTransactionAdapter.notifyDataSetChanged();
    }
}
