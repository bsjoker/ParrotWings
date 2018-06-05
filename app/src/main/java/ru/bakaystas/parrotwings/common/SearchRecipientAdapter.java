package ru.bakaystas.parrotwings.common;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.bakaystas.parrotwings.R;
import ru.bakaystas.parrotwings.model.RecipientSearch;

/**
 * Created by 1 on 24.04.2018.
 */

public class SearchRecipientAdapter extends RecyclerView.Adapter<SearchRecipientAdapter.SearchRecipientHolder> implements
        Filterable {
    private static final String LOG_TAG = "SearchAdapter";
    private List<RecipientSearch> mRecipients;
    MyItemClickListener myItemClickListener;

    public static class SearchRecipientHolder extends RecyclerView.ViewHolder {

        //public RecipientSearch mRecipient;
        public TextView mName;

        public SearchRecipientHolder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.recipient_name);
        }
    }

    public SearchRecipientAdapter(List<RecipientSearch> recipients, MyItemClickListener listener) {
        this.mRecipients = recipients;
        this.myItemClickListener = listener;
    }

    @Override
    public SearchRecipientAdapter.SearchRecipientHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.recipient_item, parent, false);

        final SearchRecipientHolder vh = new SearchRecipientHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myItemClickListener.onItemClick(view, vh.getPosition() );
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(SearchRecipientAdapter.SearchRecipientHolder holder, int position) {
        RecipientSearch recipient = mRecipients.get(position);
        holder.mName.setText(recipient.getName());
    }

    @Override
    public int getItemCount() {
        if (mRecipients != null) {
            return mRecipients.size();
        } else {
            return 0;
        }
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();
                Log.d(LOG_TAG, "Filter!");
                List<RecipientSearch> recipientSearchList = new ArrayList<>();//mainPresenter.searchRecipient(charSequence.toString());
                Log.d(LOG_TAG, "Size list: " + recipientSearchList.size());

                filterResults.values = recipientSearchList;
                filterResults.count = recipientSearchList != null ? recipientSearchList.size() : 0;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence,
                                          FilterResults filterResults) {
                Log.d(LOG_TAG, "Publish!" + mRecipients.size());
                mRecipients.clear();
                Log.d(LOG_TAG, "Publish2!");
                if (filterResults.values != null) {
                    mRecipients.addAll((ArrayList<RecipientSearch>) filterResults.values);
                }
                notifyDataSetChanged();
            }
        };
    }


}