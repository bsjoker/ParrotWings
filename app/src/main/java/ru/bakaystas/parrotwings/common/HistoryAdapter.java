package ru.bakaystas.parrotwings.common;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import ru.bakaystas.parrotwings.R;
import ru.bakaystas.parrotwings.model.Transaction;

/**
 * Created by 1 on 25.04.2018.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.TransactionHolder> {

    private List<Transaction> mTransactions;
    MyItemClickListener myItemClickListener;
    private static final DateFormat orig = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final DateFormat target = new SimpleDateFormat("dd/MM/yy HH:mm", new Locale("ru", "RU"));

    public HistoryAdapter(List<Transaction> transactions, MyItemClickListener myItemClickListener) {
        mTransactions = transactions;
        this.myItemClickListener = myItemClickListener;
    }

    public class TransactionHolder extends RecyclerView.ViewHolder {

        private Transaction mTransaction;
        private TextView mDate, mName, mAmount, mBalance;

        TransactionHolder(View itemView) {
            super(itemView);
            mDate = (TextView) itemView.findViewById(R.id.transaction_date);
            mName = (TextView) itemView.findViewById(R.id.transaction_correspondent_name);
            mAmount = (TextView) itemView.findViewById(R.id.transaction_amount);
            mBalance = (TextView) itemView.findViewById(R.id.transaction_balance);
        }

        void bindTransaction(Transaction transaction) {
            mTransaction = transaction;
            try {
                mDate.setText(convertDate(transaction.getDate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            mName.setText(transaction.getUsername());
            mAmount.setText(transaction.getAmount().toString());
            mBalance.setText(transaction.getBalance().toString());
        }
    }

    @Override
    public int getItemCount() {
        if (mTransactions != null)
            return mTransactions.size();

        return 0;
    }

    @Override
    public void onBindViewHolder(HistoryAdapter.TransactionHolder holder, int position) {
        Transaction transaction = mTransactions.get(position);
        holder.bindTransaction(transaction);
    }

    @Override
    public HistoryAdapter.TransactionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.transaction_item, parent, false);

        final TransactionHolder vh = new TransactionHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setBackgroundColor(Color.rgb(227, 227, 227));
                Log.d("HistoryAdapter", "Position: " + vh.getPosition());
                myItemClickListener.onItemClick(view, vh.getPosition());
            }
        });
        return vh;
    }

    private static String convertDate(String Date) throws ParseException {
        return target.format(orig.parse(Date));
    }

}
