package com.saveup.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.saveup.activity.RegistrationActivity;
import com.saveup.android.R;
import com.saveup.model.CityListData;

import java.util.ArrayList;

/**
 * @author AAPBD on 12,March,2017
 * @version 5.0
 * @Project MultiVendor
 * @Package com.aapbd.adapter
 */


public class CountryCityBaseAdapter extends BaseAdapter {
    Context context;
    ArrayList<CityListData> countryNames;
    LayoutInflater inflter;

    public CountryCityBaseAdapter(Context applicationContext, ArrayList<CityListData> countryNames) {
        this.context = applicationContext;
        this.countryNames = countryNames;
        inflter = (LayoutInflater.from(applicationContext));
    }


    @Override
    public int getCount() {
        return (null != countryNames ? countryNames.size() : 0);
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.spinner_rows, null);
        TextView names = view.findViewById(R.id.textView);
        System.out.println("countryNames " +countryNames.get(i).getCity_name());
        if (context  != null && context instanceof RegistrationActivity) {
            names.setText(countryNames.get(i).getCity_name());

        } else {
            names.setText(countryNames.get(i).getCity_name());
            names.setTextColor(Color.BLACK);
        }

        return view;
    }
}