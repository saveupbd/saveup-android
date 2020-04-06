package com.saveup.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.saveup.android.R;
import com.saveup.fragment.SearchViewFragment;
import com.saveup.utils.SessionSave;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by AAPBD on 6/5/17.
 */

public class SearchViewAdapter extends RecyclerView.Adapter<SearchViewAdapter.SingleItemRowHolder> implements Filterable {

    private ArrayList<String> itemsList;
    private Context mContext;
    private static MyClickListener myClickListener;
    private int position;
    private SearchViewFragment searchViewFragment;

    private String TAG = SearchViewAdapter.class.getSimpleName();

    public SearchViewAdapter(Context context, ArrayList<String> itemsList, SearchViewFragment fragment) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.searchViewFragment = fragment;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.searchitem, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, final int i) {


        Log.d(TAG, "onBindViewHolder: " + itemsList.get(i));


        holder.textViewTitle.setText("" + itemsList.get(i));
    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected TextView textViewTitle;


        public SingleItemRowHolder(View view) {
            super(view);


            this.textViewTitle = view.findViewById(R.id.textViewTitle);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (myClickListener != null) {
                myClickListener.onItemClick(getAdapterPosition(), v);
            }
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        SearchViewAdapter.myClickListener = myClickListener;
    }

    public interface MyClickListener {
        void onItemClick(int position, View v);
    }

    /**
     * Returns the filter for the current set of autocomplete results.
     */
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                // Skip the autocomplete query if no constraints are given.
                if (constraint != null) {
                    // Query the autocomplete API for the (constraint) search string.
                    itemsList = getAutocomplete(constraint.toString());
                    if (itemsList != null) {
                        // The API successfully returned results.
                        results.values = itemsList;
                        results.count = itemsList.size();
                    }
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    // The API returned at least one result, update the data.
                    notifyDataSetChanged();
                } else {
                    // The API did not return any results, invalidate the data set.
                    //notifyDataSetInvalidated();
                    if (itemsList != null) {
                        itemsList.clear();
                        notifyDataSetChanged();
                    }
                    //notifyItemRangeRemoved(0, getItemCount());

                }
            }
        };
        return filter;
    }

    private ArrayList<String> getAutocomplete(String constraint) {
        Log.i(TAG, "Starting autocomplete query for: " + constraint);

        ArrayList<String> resultList = new ArrayList<>();
        ArrayList<String> resultListFinal = new ArrayList<>();
        try {


            if (!SessionSave.getSession("category_list", mContext).equals("")) {
                JSONObject json = new JSONObject(SessionSave.getSession("category_list", mContext));
                Log.i(TAG, "category_list--->" + json);

                if (json.getString("status").equals("200")) {


                    JSONArray category_details = json.getJSONArray("categories_list");

                    resultList.clear();
                    if (category_details.length() > 0) {

                        for (int i = 0; i < category_details.length(); i++) {

                            String category_id = category_details.getJSONObject(i).getString("category_id");
                            String category_name = category_details.getJSONObject(i).getString("category_name");

                            JSONArray sub_category_list = category_details.getJSONObject(i).getJSONArray("sub_category_list");


                            if (sub_category_list.length() > 0) {
                                for (int j = 0; j < sub_category_list.length(); j++) {

                                    String category_name2 = sub_category_list.getJSONObject(j).getString("category_name");
                                    JSONArray sub_category_list1 = sub_category_list.getJSONObject(j).getJSONArray("sub_category_list");

                                    if (sub_category_list1.length() > 0) {

                                        for (int m = 0; m < sub_category_list1.length(); m++) {

                                            String category_name3 = sub_category_list1.getJSONObject(m).getString("category_name");
                                            JSONArray sub_category_list2 = sub_category_list1.getJSONObject(m).getJSONArray("sub_category_list");

                                            if (sub_category_list2.length() > 0) {

                                                for (int k = 0; k < sub_category_list2.length(); k++) {

                                                    String category_name4 = sub_category_list2.getJSONObject(k).getString("category_name");

                                                    resultList.add(category_name4);

                                                }
                                            }
                                            resultList.add(category_name3);
                                        }
                                    }

                                    resultList.add(category_name2);
                                }


                            }

                            resultList.add(category_name);
                        }
                        if (searchViewFragment instanceof SearchViewFragment) {
                            searchViewFragment.stringArrayList.clear();
                        }
                        for (int i = 0; i < resultList.size(); i++) {

                            if (resultList.get(i).contains(constraint)) {
                                resultListFinal.add(resultList.get(i));
                                if (searchViewFragment instanceof SearchViewFragment) {
                                    searchViewFragment.stringArrayList.add(resultList.get(i));
                                }
                            }
                        }


                    }
                }
            }

        } catch (Exception ex) {
            Log.i(TAG, "resultListFinal  ex --->" + ex.getMessage());

            ex.printStackTrace();
        }
        Log.i(TAG, "resultListFinal--->" + resultListFinal.toString());

        return resultListFinal;
    }


}