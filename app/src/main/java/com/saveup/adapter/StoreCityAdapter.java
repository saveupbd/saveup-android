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
import com.saveup.fragment.SearchLoactionFragment;
import com.saveup.model.StoreCityData;
import com.saveup.utils.SessionSave;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by AAPBD on 15/6/17.
 */

public class StoreCityAdapter extends RecyclerView.Adapter<StoreCityAdapter.SingleItemRowHolder> implements Filterable {

    private ArrayList<StoreCityData> itemsList;
    private Context mContext;
    private static MyClickListener myClickListener;
    private int position;
    private SearchLoactionFragment searchViewFragment;

    public StoreCityAdapter(Context context, ArrayList<StoreCityData> itemsList, SearchLoactionFragment fragment) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.searchViewFragment = fragment;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.serachiew_adapter, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, final int i) {

        holder.titleTxt.setText(itemsList.get(i).getCity_name());


    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView titleTxt;


        public SingleItemRowHolder(View view) {
            super(view);


            this.titleTxt = view.findViewById(R.id.address);

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
        StoreCityAdapter.myClickListener = myClickListener;
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

    private ArrayList<StoreCityData> getAutocomplete(String constraint) {

        ArrayList<StoreCityData> resultList = new ArrayList<>();
        ArrayList<StoreCityData> resultListFinal = new ArrayList<>();
        try {


            try {

                if (!(SessionSave.getSession("map_details", mContext).equals(""))) {

                    final JSONObject json = new JSONObject(SessionSave.getSession("map_details", mContext));

                    if (json.getString("status").equals("200")) {
                        JSONArray map_details = json.getJSONArray("map_details");  //


                        if (map_details.length() > 0) {

                            for (int i = 0; i < map_details.length(); i++) {

                                String city_id = map_details.getJSONObject(i).getString("city_id");
                                String city_name = map_details.getJSONObject(i).getString("city_name");

                                StoreCityData storeCityData = new StoreCityData(city_id, city_name);
                                resultList.add(storeCityData);

                            }


                        }
                        searchViewFragment.storeCityDataArrayList.clear();


                        Log.d("", "onTextChanged --->1: " + resultList.size());
                        for (int i = 0; i < resultList.size(); i++) {

                            Log.d("", "onTextChanged   --->  2: " + resultList.get(i).getCity_name());
                            if (resultList.get(i).getCity_name().toLowerCase().contains(constraint)) {

                                resultListFinal.add(new StoreCityData(resultList.get(i).getCity_id(), resultList.get(i).getCity_name()));
                                searchViewFragment.storeCityDataArrayList.add(new StoreCityData(resultList.get(i).getCity_id(), resultList.get(i).getCity_name()));
                            }
                        }


                    }


                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return resultListFinal;
    }

}