package com.saveup.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by AAPBD on 26/5/17.
 */

public class RedemptionFragment extends Fragment {

    @BindView(R.id.phn_recharge)
    CardView phn_recharge;
    @BindView(R.id.bKash)
    CardView bKash;

    private final String TAG = "RedemptionFragment";

    public RedemptionFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.redemptionfragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).setTag = "redemption";
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
        mainActivity.toolbar.setTitle("" + getResources().getString(R.string.redemption));
        ((MainActivity) getActivity()).toolbar.setLogo(R.drawable.transpent_logo);
        final Drawable upArrow = getResources().getDrawable(R.drawable.back_icon);
        mainActivity.toggle.setHomeAsUpIndicator(upArrow);
        mainActivity.toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mainActivity.onBackPressed();
            }
        });

        phn_recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPayAmountDialog(false);
            }
        });

        bKash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPayAmountDialog(true);
            }
        });


        UserPresenter userPresenter = new UserPresenter(RedemptionFragment.this);
        userPresenter.orderListPage(SessionSave.getSession("user_id", getActivity()), "" + 1);
    }

    Dialog dialog;
    TextView titleTxt;
    TextView messageTxt;
    String operator = "";
    ImageView operatorImage;
    TextInputLayout number_input_layout;
    TextInputEditText number_input;
    Spinner amount;
    ProgressBar progress1;
    TextView cancel;
    TextView proceed;
    TextView ok;

    private void chooseOperator(){
        Dialog dialog = new Dialog(getActivity(), R.style.dialogwinddow);
        dialog.setContentView(R.layout.choose_operator_layout);
        TextView cancel = dialog.findViewById(R.id.cancelTxt);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        CardView gp = dialog.findViewById(R.id.gp);
        gp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                updateNumberOperator("grameenphone");
            }
        });
        CardView banglalink = dialog.findViewById(R.id.banglalink);
        banglalink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                updateNumberOperator("banglalink");
            }
        });
        CardView airtel = dialog.findViewById(R.id.airtel);
        airtel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                updateNumberOperator("airtel");
            }
        });
        CardView robi = dialog.findViewById(R.id.robi);
        robi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                updateNumberOperator("robi");
            }
        });
        CardView teletalk = dialog.findViewById(R.id.teletalk);
        teletalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                updateNumberOperator("teletalk");
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    private void updateNumberOperator(String operatorText){
        char[] currentNumber = number_input.getText().toString().toCharArray();

        switch (operatorText){
            case "grameenphone":
                currentNumber[2] = '7';
                break;
            case "banglalink":
                currentNumber[2] = '9';
                break;
            case "airtel":
                currentNumber[2] = '8';
                break;
            case "robi":
                currentNumber[2] = '6';
                break;
            case "teletalk":
                currentNumber[2] = '5';
                break;
        }

        if(operator != "bkash"){
            operator = operatorText;
        }
        number_input.setText(String.valueOf(currentNumber));
        number_input.setSelection(currentNumber.length);

    }



    private void showPayAmountDialog(boolean isBkash){

        if(dialog != null){
            dialog.dismiss();
        }

        dialog = new Dialog(getActivity(), R.style.dialogwinddow);
        dialog.setContentView(R.layout.redemption_info_layout);
        titleTxt = dialog.findViewById(R.id.titleTxt);
        operatorImage = dialog.findViewById(R.id.operator);
        if(isBkash){
            operatorImage.setImageDrawable(getResources().getDrawable(R.drawable.bkash));
            titleTxt.setText(getText(R.string.redeem_by_bkash));
            operator = "bkash";
        }else{
            operatorImage.setImageDrawable(getResources().getDrawable(R.drawable.mobile_recharge));
            titleTxt.setText(getText(R.string.redeem_by_recharge));
        }
        number_input_layout = dialog.findViewById(R.id.number_input_layout);
        number_input = dialog.findViewById(R.id.number_input);
        number_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().startsWith("017") || s.toString().startsWith("013")){
                    operatorImage.setImageDrawable(getResources().getDrawable(R.drawable.gp));
                  //  operatorImage.setBorderColor(getResources().getColor(R.color.green_dark));
                    if(isBkash){
                        operator = "bkash";
                    }else{
                        operator = "grameenphone";
                    }
                }else if(s.toString().startsWith("019") || s.toString().startsWith("014")){
                    operatorImage.setImageDrawable(getResources().getDrawable(R.drawable.banglalink));
                  //  operatorImage.setBorderColor(getResources().getColor(R.color.green_dark));
                    if(isBkash){
                        operator = "bkash";
                    }else{
                        operator = "banglalink";
                    }
                }else if(s.toString().startsWith("015")){
                    operatorImage.setImageDrawable(getResources().getDrawable(R.drawable.teletalk));
                  //  operatorImage.setBorderColor(getResources().getColor(R.color.green_dark));
                    if(isBkash){
                        operator = "bkash";
                    }else{
                        operator = "teletalk";
                    }
                }else if(s.toString().startsWith("016")){
                    operatorImage.setImageDrawable(getResources().getDrawable(R.drawable.robi));
                 //   operatorImage.setBorderColor(getResources().getColor(R.color.green_dark));
                    if(isBkash){
                        operator = "bkash";
                    }else{
                        operator = "robi";
                    }
                }else if(s.toString().startsWith("018")){
                    operatorImage.setImageDrawable(getResources().getDrawable(R.drawable.airtel));
                  //  operatorImage.setBorderColor(getResources().getColor(R.color.green_dark));
                    if(isBkash){
                        operator = "bkash";
                    }else{
                        operator = "airtel";
                    }
                }else if(s.length() >= 3){
                    operatorImage.setImageDrawable(getResources().getDrawable(R.drawable.error));
                  //  operatorImage.setBorderColor(getResources().getColor(R.color.transparent));
                    operator = "";
                }else{
                  //  operatorImage.setBorderColor(getResources().getColor(R.color.transparent));
                    if(isBkash){
                        operatorImage.setImageDrawable(getResources().getDrawable(R.drawable.bkash));
                    }else{
                        operatorImage.setImageDrawable(getResources().getDrawable(R.drawable.mobile_recharge));
                    }
                }
                validateInput();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        operatorImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!operator.isEmpty() && number_input.getText().toString().length() >2){
                    chooseOperator();
                }
            }
        });

        cancel = dialog.findViewById(R.id.cancelTxt);
        proceed = dialog.findViewById(R.id.proceedTxt);
        progress1 = dialog.findViewById(R.id.progress1);
        amount = dialog.findViewById(R.id.amount);
        amount.setSelection(0);
        amount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                hideKeyboard();
            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(number_input.getText().toString().trim().isEmpty()){
                    number_input_layout.setError(getString(R.string.empty_number));
                }else if(operator.isEmpty()){
                    number_input_layout.setError(getString(R.string.invalid_number));
                    operatorImage.setImageDrawable(getResources().getDrawable(R.drawable.error));
                }else{
                    showDialogProgress(true);
                    //dialog.dismiss();
                     UserPresenter userPresenter = new UserPresenter(RedemptionFragment.this);
                    userPresenter.redeem(SessionSave.getSession("user_id", getActivity()), operator,number_input.getText().toString(), amount.getSelectedItem().toString());
                }
            }
        });
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public void redeemData(JsonObject jsonObject){

        try {
            if (jsonObject != null) {
                showDialogProgress(false);
                JSONObject json = new JSONObject(jsonObject.toString());
                if (json.getString("status").equals("200")) {
                    // TestOTP = json.get("code").toString();
                    showResultDialog( "Success!",json.get("message").toString());
                }else{
                    //TODO
                    showResultDialog( "Success!",json.get("message").toString());
                }
            }else{
                showResultDialog("Error","Something went wrong, please try later");
            }
        }catch (Exception ex) {
            ex.printStackTrace();
            showResultDialog("Error","Something went wrong, please try later");
        }
    }

    private void showResultDialog(String title, String message){

        if(dialog != null){
            dialog.dismiss();
        }

        dialog = new Dialog(getActivity(), R.style.dialogwinddow);
        dialog.setContentView(R.layout.result_layout);

        titleTxt = dialog.findViewById(R.id.titleTxt);
        titleTxt.setText(title);

        messageTxt = dialog.findViewById(R.id.message);
        messageTxt.setText(message);

        ok = dialog.findViewById(R.id.okTxt);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                hideKeyboard();
            }
        });
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void validateInput(){

        if(number_input.getText().length() == 11 && !operator.isEmpty()){
            proceed.setEnabled(true);
            proceed.setTextColor(getResources().getColor(R.color.green_dark));
        }else{
            proceed.setEnabled(false);
            proceed.setTextColor(getResources().getColor(R.color.light_grey));
        }
    }

    private void showDialogProgress(boolean show){
        if(show){
            progress1.setVisibility(View.VISIBLE);
            //progressBar.setVisibility(View.VISIBLE);
            cancel.setClickable(false);
            proceed.setClickable(false);
            //promoCodeApplyButton.setBackgroundColor(getResources().getColor(R.color.yellow));
            //promoCodeApplyButton.setEnabled(false);
            number_input.setEnabled(false);
            amount.setEnabled(false);
            //promoCodeET.setEnabled(false);
        }else{
            progress1.setVisibility(View.INVISIBLE);
            // progressBar.setVisibility(View.INVISIBLE);
            cancel.setClickable(true);
            proceed.setClickable(true);
            //promoCodeApplyButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            //promoCodeApplyButton.setEnabled(true);
            number_input.setEnabled(true);
            amount.setEnabled(true);
            // promoCodeET.setEnabled(true);
        }
    }

    private void hideKeyboard(){
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }


}

