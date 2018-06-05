package ru.bakaystas.parrotwings;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InvalidClassException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.bakaystas.parrotwings.common.MyItemClickListener;
import ru.bakaystas.parrotwings.common.SearchRecipientAdapter;
import ru.bakaystas.parrotwings.di.component.MainComponent;
import ru.bakaystas.parrotwings.di.module.MainModule;
import ru.bakaystas.parrotwings.model.RecipientSearch;
import ru.bakaystas.parrotwings.mvp.contract.MainContract;
import ru.bakaystas.parrotwings.mvp.presenter.MainPresenter;
import ru.bakaystas.parrotwings.utils.Preferences;

import static java.lang.Math.abs;

public class MainActivity extends AppCompatActivity implements MainContract.View {
    private static final String LOG_TAG = "MainActivity";

    private String recipient;
    private List<RecipientSearch> mRecipients;
    public SearchRecipientAdapter mSearchRecipientAdapter;
    private boolean valid;
    MainComponent mainComponent;

    @BindView(R.id.edit_text_amount)
    EditText amount;

    @BindView(R.id.text_view_amount)
    TextView balance;

    @BindView(R.id.text_view_recipient)
    TextView tvRecipient;

    @BindView(R.id.text_view_username)
    TextView user;

    @BindView(R.id.buttonTransfer)
    Button transfer;

    @BindView(R.id.search_recycler_view)
    RecyclerView recyclerView;

    @Inject
    MainPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        plusMainComponent();
        initView();
        setupSearchView();

        mainPresenter.userInfo();

        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        mRecipients = new ArrayList<>();
        mSearchRecipientAdapter = new SearchRecipientAdapter(mRecipients, new MyItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                setRecipient(mRecipients.get(position));
                setResult(RESULT_OK);
                tvRecipient.setText("Получатель: " + mRecipients.get(position).getName());
            }
        });
        recyclerView.setAdapter(mSearchRecipientAdapter);

        transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainPresenter.validateForm();
                if (valid) {
                    mainPresenter.transfer(recipient, Integer.valueOf(amount.getText().toString().trim()));
                }
            }
        });
    }

    private void initView() {
        ButterKnife.bind(this);
    }

    public void plusMainComponent() {
        if (mainComponent == null) {
            MyApplication.get(this).getsApplicationComponent().plusMainComponent(new MainModule(this)).inject(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_history:
                Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_logout:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupSearchView() {
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        SearchView searchView = (SearchView) findViewById(R.id.search_view);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mSearchRecipientAdapter.getFilter().filter(query);
                Log.d(LOG_TAG, "TextSubmint");
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mSearchRecipientAdapter.getFilter().filter(newText);
                mainPresenter.searchRecipient(newText);
                Log.d(LOG_TAG, "TextChange");
                return true;
            }
        });
    }

    @Override
    public void setUserInfo(String mUser, String newBalance) {
        if (mUser == null) {
            balance.setText(newBalance);
        } else {
            balance.setText(newBalance);
            user.setText(mUser);
        }
    }

    @Override
    public void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void updateAdapter(List<RecipientSearch> list) {
        mRecipients.clear();
        mRecipients.addAll(list);
        mSearchRecipientAdapter.notifyDataSetChanged();
    }

    private void setRecipient(RecipientSearch mRecipient) {
        recipient = mRecipient.getName();
    }

    public String getRecipient() {
        return recipient;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public EditText getAmount() {
        return amount;
    }

    public TextView getTvRecipient() {
        return tvRecipient;
    }

    @Override
    protected void onResume() {
        super.onResume();
        amount.setText(null);
        Integer am = Preferences.getSharedPreferences().getInt("amount", 0);
        String nm = Preferences.getSharedPreferences().getString("name", "name");
        if (am != 0 && !nm.contains("name")) {
            am = abs(am);
            amount.setText(am.toString());
            recipient = nm;
            tvRecipient.setText("Получатель: " + recipient);
            try {
                Preferences.savePreference("amount", 0);
                Preferences.savePreference("name", "name");
            } catch (InvalidClassException e) {
                e.printStackTrace();
            }
        }
    }
}
