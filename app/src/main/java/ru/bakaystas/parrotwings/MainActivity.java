package ru.bakaystas.parrotwings;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.InvalidPreferencesFormatException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.bakaystas.parrotwings.model.LoggedUserInfo;
import ru.bakaystas.parrotwings.model.RecipientSearch;
import ru.bakaystas.parrotwings.model.ResponseTransaction;
import ru.bakaystas.parrotwings.rest.api.APIService;
import ru.bakaystas.parrotwings.rest.api.ApiUtils;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MainActivity";
    public static final String TOKEN = null;
    private APIService mApiService;
    private EditText amount;
    private TextView tvRecipient, user, balance;
    private Button info, transfer, search;
    private String token, recipient;
    private RecyclerView recyclerView;

    private List<RecipientSearch> mRecipients;
    private SharedPreferences Prefs;
    private SearchRecipientAdapter mSearchRecipientAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mApiService = ApiUtils.getAPIService();

        setupSearchView();

        Prefs = getSharedPreferences(TOKEN, Context.MODE_PRIVATE);
        token = Prefs.getString("token", "0");
        SharedPreferences.Editor ed = Prefs.edit();
        ed.putInt("amount", 0);
        ed.putString("name", "name");
        ed.apply();

        Log.d(LOG_TAG, token);

        amount = (EditText)findViewById(R.id.edit_text_amount);

        balance = (TextView)findViewById(R.id.text_view_amount);
        tvRecipient = (TextView)findViewById(R.id.text_view_recipient);
        user = (TextView)findViewById(R.id.text_view_username);

        transfer = (Button)findViewById(R.id.buttonTransfer);

        user.setText(Prefs.getString("email", "---"));

        recyclerView = (RecyclerView) findViewById(R.id.search_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        mRecipients = new ArrayList<>();
        mSearchRecipientAdapter = new SearchRecipientAdapter(mRecipients);
        recyclerView.setAdapter(mSearchRecipientAdapter);

        transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer amountField = Integer.valueOf(amount.getText().toString().trim());
                Log.d(LOG_TAG, "Recipient: " + recipient + ". Amount: " + amountField.toString());
                transfer(recipient, amountField);
            }
        });
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
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mSearchRecipientAdapter.getFilter().filter(newText);
                recyclerView.setVisibility(View.VISIBLE);
                return true;
            }
        });
    }

    private void transfer(String usernameField, Integer amount) {
        mApiService.transfer(token, usernameField, amount).enqueue(new Callback<ResponseTransaction>() {
            @Override
            public void onResponse(Call<ResponseTransaction> call, Response<ResponseTransaction> response) {
                if (response.isSuccessful()){
                    showMsg("Перевод " + response.body().getTrans_token().getAmount() + "PW пользователю " + response.body().getTrans_token().getUsername() + " успешно совершен!");
                    Log.d(LOG_TAG, "Transfer: " + response.body().getTrans_token().getBalance());
                    balance.setText("На Вашем счету: " + response.body().getTrans_token().getBalance());
                }else {
                    switch (response.code()){
                        case 400:
                            showMsg("Недостаточно средств для перевода!");
                            break;
                        case 401:
                            showMsg("Пользователь неавторизован!");
                    }
                    Log.d(LOG_TAG, "Error transfer: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseTransaction> call, Throwable t) {
                Log.d(LOG_TAG, "Transfer message error" + t.getMessage());
            }
        });
    }

    private void showMsg(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    private void getUserInfo() {
        mApiService.getLoggedUserInfo(token).enqueue(new Callback<LoggedUserInfo>() {
            @Override
            public void onResponse(Call<LoggedUserInfo> call, Response<LoggedUserInfo> response) {
                Log.d(LOG_TAG, "Logged user info: " + response.body().getEmail());
                if (response.isSuccessful()){
                    Log.d(LOG_TAG, "Logged user info: " + response.body().getEmail());
                    balance.setText("На Вашем счету: " + response.body().getBalance());
                    user.setText(response.body().getName());
                }else {
                    Log.d(LOG_TAG, "Error getLoggedUserInfo: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<LoggedUserInfo> call, Throwable t) {
                Log.e(LOG_TAG, "Error: " + t.getMessage());
            }
        });
    }

    private void hideRV(){
        recyclerView.setVisibility(View.INVISIBLE);
    }

    private class SearchRecipientHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private RecipientSearch mRecipient;
        private TextView mId;
        private TextView mName;

        SearchRecipientHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mId = (TextView) itemView.findViewById(R.id.recipient_id);
            mName = (TextView) itemView.findViewById(R.id.recipient_name);
        }

        void bindRecipient(RecipientSearch recipient) {
            mRecipient = recipient;
            mId.setText(String.valueOf(recipient.getId()));
            mName.setText(recipient.getName());
        }

        @Override
        public void onClick(View v) {
            setRecipient(mRecipient);
            setResult(RESULT_OK);
            hideRV();
            tvRecipient.setText("Получатель: " + mRecipient.getName());
        }
    }

    private void setRecipient(RecipientSearch mRecipient) {
        recipient = mRecipient.getName();
    }

    private class SearchRecipientAdapter extends RecyclerView.Adapter<SearchRecipientHolder> implements
            Filterable {

        private List<RecipientSearch> mRecipients;

        SearchRecipientAdapter(List<RecipientSearch> recipients) {
            mRecipients = recipients;
        }

        @Override
        public int getItemCount() {
            if (mRecipients != null)
                return mRecipients.size();

            return 0;
        }

        @Override
        public void onBindViewHolder(SearchRecipientHolder holder, int position) {
            RecipientSearch recipient = mRecipients.get(position);
            holder.bindRecipient(recipient);
        }

        @Override
        public SearchRecipientHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
            View v = inflater.inflate(R.layout.recipient_item, parent, false);

            return new SearchRecipientHolder(v);
        }

        @Override
        public Filter getFilter() {

            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    FilterResults filterResults = new FilterResults();

                    //List<RecipientSearch> recipientSearchList = searchRecipient(charSequence.toString());

                    //----------------------------- Заполняю массив вымышленными элементами, т.к. нет связи с сервером
                    List<RecipientSearch> rs = new ArrayList<>();
                    for (int i = 1; i<=4; i++){
                        RecipientSearch mRecipientSearch = new RecipientSearch();
                        mRecipientSearch.setId(i);
                        mRecipientSearch.setName("Ivan " + i);
                        rs.add(mRecipientSearch);
                    }
                    List<RecipientSearch> recipientSearchList = rs;
                    //-----------------------------

                    filterResults.values = recipientSearchList;
                    filterResults.count = recipientSearchList != null ? recipientSearchList.size() : 0;

                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence charSequence,
                                              FilterResults filterResults) {
                    mRecipients.clear();
                    if (filterResults.values != null) {
                        mRecipients.addAll((ArrayList<RecipientSearch>) filterResults.values);
                    }
                    notifyDataSetChanged();
                }
            };
        }

        private List<RecipientSearch> searchRecipient(String filter){

            mApiService.searchRecipient(token, filter).enqueue(new Callback<List<RecipientSearch>>() {
                @Override
                public void onResponse(Call<List<RecipientSearch>> call, Response<List<RecipientSearch>> response) {
                    mRecipients = response.body();
                }

                @Override
                public void onFailure(Call<List<RecipientSearch>> call, Throwable t) {

                }
            });
            return mRecipients;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Integer am = Prefs.getInt("amount", 0);
        String nm = Prefs.getString("name", "name");
        if (am>0 && !nm.contains("name")) {
            amount.setText(am);
            recipient = nm;
        }
    }
}
