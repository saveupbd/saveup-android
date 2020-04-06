package com.saveup.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.saveup.activity.MainActivity;
import com.saveup.adapter.NewOrderListAdapter;
import com.saveup.android.R;
import com.saveup.model.NewOrderData;
import com.saveup.presenter.UserPresenter;
import com.saveup.utils.SessionSave;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AAPBD on 26/5/17.
 */

public class RewardsFragment extends Fragment {

    @BindView(R.id.loyalty_point_container)
    LinearLayout loyalty_point_container;
    @BindView(R.id.info_container)
    LinearLayout info_container;
    @BindView(R.id.my_transactions_layout)
    LinearLayout my_transactions_layout;
    @BindView(R.id.transaction_text)
    TextView transaction_text;
    @BindView(R.id.main_container)
    ConstraintLayout main_container;

    @BindView(R.id.total_loyality_point)
    TextView total_loyality_point;

    @BindView(R.id.total_saving)
    TextView rewards_total_savings;

    @BindView(R.id.redeem_loyalty_points)
    Button redeem_loyalty_points;


    @BindView(R.id.progress)
    ProgressBar progress;

    private final String TAG = "RewardsFragment";
    private ArrayList<NewOrderData> orderDataArrayList = new ArrayList<>();
    private RecyclerView orderList;
    private LinearLayoutManager linearLayoutManager;
    private NewOrderListAdapter newOrderListAdapter;

    private boolean isTransactionsOnly = false;
    public boolean isFromProfile = false;

    public RewardsFragment(boolean isTransactionsOnly, boolean isFromProfile) {
        this.isTransactionsOnly = isTransactionsOnly;
        this.isFromProfile = isFromProfile;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rewardfragment, container, false);
        ButterKnife.bind(this, view);

        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        orderList = view.findViewById(R.id.storeList);
        orderList.setHasFixedSize(true);
        orderList.setLayoutManager(linearLayoutManager);
        orderList.setNestedScrollingEnabled(false);
        newOrderListAdapter = new NewOrderListAdapter(getActivity(),orderDataArrayList);
        orderList.setAdapter(newOrderListAdapter);

        if(isTransactionsOnly){
            main_container.setBackgroundColor(getResources().getColor(R.color.lineview));
            loyalty_point_container.setVisibility(View.GONE);
            info_container.setVisibility(View.GONE);
        }else{
            my_transactions_layout.setVisibility(View.GONE);
            orderList.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).setTag = "reward";
        ((MainActivity) getActivity()).searchIcon.setVisible(false);
        final MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.search_layout.setVisibility(View.GONE);
        mainActivity.menu_Name.setVisible(false);
        mainActivity.toggle.setDrawerIndicatorEnabled(false);
        mainActivity.toolbar.setVisibility(View.VISIBLE);
        mainActivity.disableDrawer(true);
        mainActivity.cartIconempty.setVisible(false);
     //   mainActivity.cartItem.setVisible(false);
        mainActivity.editIcon.setVisible(false);
        if(isTransactionsOnly){
            mainActivity.toolbar.setTitle("" + getResources().getString(R.string.my_transactions));
        }else{
            mainActivity.toolbar.setTitle("" + getResources().getString(R.string.rewards));
        }
        ((MainActivity) getActivity()).toolbar.setLogo(R.drawable.transpent_logo);
        final Drawable upArrow = getResources().getDrawable(R.drawable.back_icon);
        mainActivity.toggle.setHomeAsUpIndicator(upArrow);
        mainActivity.toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mainActivity.onBackPressed();
            }
        });

        redeem_loyalty_points.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).startRedemptionFragment();
            }
        });
        UserPresenter userPresenter = new UserPresenter(RewardsFragment.this);
        userPresenter.orderListPage(SessionSave.getSession("user_id", getActivity()), "" + 1);

    }

    public void orderListData(JsonObject jsonObject) {

        progress.setVisibility(View.INVISIBLE);

        try {

            if (jsonObject != null ) {

                JSONObject json = new JSONObject(jsonObject.toString());

                if (json.getString("status").equals("200")) {
                    total_loyality_point.setText(json.getString("total_loyality_point"));
                    String totalSavings = json.getString("total_savings");;
                    String[] totalSavingsArray = totalSavings.split("\\.");
                    int totalSavingInt = 0;
                    if(totalSavingsArray.length > 1 && Integer.valueOf(totalSavingsArray[1]) >= 5 ){
                        totalSavingInt = Integer.valueOf(totalSavingsArray[0])+1;
                    }else{
                        totalSavingInt = Integer.valueOf(totalSavingsArray[0]);
                    }
                    rewards_total_savings.setText(String.format(getString(R.string.rewards_total_savings),String.valueOf(totalSavingInt)));

                    orderDataArrayList.clear();

                    JSONArray product_order_cod_list = json.getJSONArray("order_list");
                    if (product_order_cod_list.length() > 0) {

                        for (int i = 0; i < product_order_cod_list.length(); i++) {

                            String order_id = product_order_cod_list.getJSONObject(i).getString("order_id");
                            String user_id = product_order_cod_list.getJSONObject(i).getString("user_id");
                            String order_total = product_order_cod_list.getJSONObject(i).getString("order_total");
                            String user_payable_amount = product_order_cod_list.getJSONObject(i).getString("user_payable_amount");
                            String user_savings = product_order_cod_list.getJSONObject(i).getString("user_savings");
                            String dagte = product_order_cod_list.getJSONObject(i).getString("dagte");
                            String merchant_name = product_order_cod_list.getJSONObject(i).getString("merchant_name");
                            String status = product_order_cod_list.getJSONObject(i).getString("status");

                            NewOrderData newOrderData = new NewOrderData(order_id, user_id, order_total, user_payable_amount,user_savings, dagte, merchant_name, status);

                            orderDataArrayList.add(newOrderData);
                        }
                        newOrderListAdapter.notifyDataSetChanged();
                    }
                    if(orderDataArrayList.isEmpty()){
                        //my_transactions_layout.setVisibility(View.VISIBLE);
                        transaction_text.setText(getString(R.string.no_transactions));
                    }
                }
                Log.d(TAG, "orderListData: " + jsonObject.toString());
            }

        } catch (Exception ex) {
            ex.printStackTrace();

        }

    }


}

