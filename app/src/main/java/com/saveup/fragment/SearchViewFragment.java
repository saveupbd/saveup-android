package com.saveup.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import com.saveup.activity.MainActivity;
import com.saveup.adapter.SearchViewAdapter;
import com.saveup.android.R;

import java.util.ArrayList;

/**
 * Created by AAPBD on 6/5/17.
 */

public class SearchViewFragment extends Fragment {

    private String TAG = SearchViewFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    public ArrayList<String> stringArrayList = new ArrayList<>();
    private SearchViewAdapter homeCatergoryAdap;
    private SearchView search ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.searchviewfragment, container, false);

        setHasOptionsMenu(true);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setVisibility(View.GONE);

        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        try {

            final MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.search_layout.setVisibility(View.GONE);
            mainActivity.menu_Name.setVisible(false);
            mainActivity.toggle.setDrawerIndicatorEnabled(false);
            mainActivity.disableDrawer(true);
            final Drawable upArrow = getResources().getDrawable(R.drawable.back_icon);
            mainActivity.toggle.setHomeAsUpIndicator(upArrow);
            mainActivity.toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(search != null){
                        search.clearFocus();
                    }
                    mainActivity.onBackPressed();
                }
            });

            homeCatergoryAdap = new SearchViewAdapter(getActivity(), stringArrayList, SearchViewFragment.this);
            recyclerView.setAdapter(homeCatergoryAdap);


            homeCatergoryAdap.setOnItemClickListener(new SearchViewAdapter.MyClickListener() {
                @Override
                public void onItemClick(int position, View v) {

                    try {

                        if(search != null){
                            search.clearFocus();
                        }

                        Bundle bundle = new Bundle();
                        bundle.putString("main_category_id", "");
                        bundle.putString("sec_category_id", "");
                        bundle.putString("sub_category_id", "");
                        bundle.putString("sub_sec_category_id", "");
                        bundle.putString("title", ""+stringArrayList.get(position));
                        bundle.putString("page_title", stringArrayList.get(position));
                        bundle.putString("discount", "");
                        bundle.putString("price_min", "");
                        bundle.putString("price_max", "");
                        bundle.putString("availability", "");
                        bundle.putString("sort_order_by", "");
                        ProductListFragment fragment2 = new ProductListFragment();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.add(R.id.fragment_container, fragment2, "searchview");
                        fragment2.setArguments(bundle);
                        fragmentTransaction.addToBackStack("");
                        fragmentTransaction.commit();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        ((MainActivity) getActivity()).action_search.setVisible(true);
        ((MainActivity) getActivity()).action_search.expandActionView();

        // Enable Search Focus by Default

//        search = (SearchView) menu.findItem(R.id.action_search).getActionView();
        search = (SearchView) MenuItemCompat.getActionView(((MainActivity) getActivity()).action_search);
        search.setIconifiedByDefault(true);
        search.setFocusable(true);
        search.setIconified(false);
        search.requestFocusFromTouch();

        // Cursor Colour

        search.setOnCloseListener(new SearchView.OnCloseListener() {

            @Override
            public boolean onClose() {
                if(search != null){
                    search.clearFocus();
                }

                getActivity().onBackPressed();
                return false;
            }
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                try {

// in Fragment2, before adding Fragment3:
//                    FragmentManager fragmentManager1 = getFragmentManager();
//                    fragmentManager1.beginTransaction()
//                            .remove(SearchViewFragment.this) // "this" refers to current instance of Fragment2
//                            .commit();
//                    fragmentManager1.popBackStack();

                    Log.wtf(TAG, " query "+query);

                    Bundle bundle = new Bundle();
                    bundle.putString("main_category_id", "");
                    bundle.putString("sec_category_id", "");
                    bundle.putString("sub_category_id", "");
                    bundle.putString("sub_sec_category_id", "");
                    bundle.putString("title", ""+query);
                    bundle.putString("discount", "");
                    bundle.putString("price_min", "1");
                    bundle.putString("price_max", "");
                    bundle.putString("availability", "");
                    bundle.putString("sort_order_by", "1");


                    if(((MainActivity) getActivity()).latestDealSearch){

                        bundle.putString("page_title", "" + getResources().getString(R.string.latest_deals));
                        bundle.putString("view_all_btn_data", getResources().getString(R.string.latest_deals));

                    }else if(((MainActivity) getActivity()).hotDealSearch){

                        bundle.putString("page_title", ""+getResources().getString(R.string.hot_deals));
                        bundle.putString("view_all_btn_data", getResources().getString(R.string.hot_deals));

                    }else if(((MainActivity) getActivity()).giftDealSearch){

                        bundle.putString("page_title", ""+getResources().getString(R.string.gift_deals));
                        bundle.putString("view_all_btn_data", getResources().getString(R.string.gift_deals));


                    }else{
                        bundle.putString("page_title", ""+query);
                    }

                    ProductListFragment fragment2 = new ProductListFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.fragment_container, fragment2, "searchview");
                    fragment2.setArguments(bundle);
                    fragmentTransaction.addToBackStack("");
                    fragmentTransaction.commit();

                    search.clearFocus();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                if (query != null && query.length() > 0) {
                    homeCatergoryAdap.getFilter().filter(query);
                }

                return true;

            }

        });

        search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);


    }


}
