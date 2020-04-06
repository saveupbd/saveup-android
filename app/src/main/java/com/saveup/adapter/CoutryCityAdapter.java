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
import com.saveup.model.CountryListData;

import java.util.ArrayList;

/**
 * @author AAPBD on 12,March,2017
 * @version 5.0
 * @Project MultiVendor
 * @Package com.aapbd.adapter
 */

public class CoutryCityAdapter extends BaseAdapter {
    Context context;
    ArrayList<CountryListData> countryNames;
    LayoutInflater inflter;
    public CoutryCityAdapter(Context applicationContext, ArrayList<CountryListData> countryNames, String defaultText) {
        this.context = applicationContext;
        this.countryNames = countryNames;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return countryNames.size();
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

        if (context instanceof RegistrationActivity) {
            names.setText(countryNames.get(i).getCountry_name());

        } else {
            names.setText(countryNames.get(i).getCountry_name());
            names.setTextColor(Color.BLACK);
        }
        return view;
    }
}