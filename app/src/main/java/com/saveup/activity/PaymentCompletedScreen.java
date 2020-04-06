package com.saveup.activity;

import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.saveup.android.R;
import com.saveup.utils.ApplyFont;
import com.saveup.utils.SessionSave;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author AAPBD on 06,May,2017
 * @version 5.0
 * @Project MultiVendor
 * @Package com.aapbd.activity
 */


public class PaymentCompletedScreen extends BaseActivity {


    private TextView totalAmount, delivarydate, shippingAddressTxt;
    private LinearLayout backIcon;

    @Override
    public int setLayout() {
        return R.layout.paymentcompletedscreen;
    }

    @Override
    public void initialize() {

        backIcon = findViewById(R.id.backIconLay);
        totalAmount = findViewById(R.id.totalAmount);
        delivarydate = findViewById(R.id.delivarydate);
        shippingAddressTxt = findViewById(R.id.shippingAddressTxt);

        ApplyFont.applyMediumFont(this, findViewById(R.id.totalAmount));
        ApplyFont.applyBold(this, findViewById(R.id.delivarydate));
        ApplyFont.applyFont(this, findViewById(R.id.shippingAddressTxt));
        SessionSave.saveSession("cartCount","",PaymentCompletedScreen.this);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PaymentCompletedScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        try {


            Intent intent = getIntent();

            if (intent != null) {
                JSONObject json = new JSONObject(intent.getStringExtra("result"));
                if (json.getString("status").equals("200")) {
                    JSONArray shipping_details = json.getJSONArray("shipping_details");
                  //  SessionSave.saveSession("shipping_details",intent.getStringExtra("result"), PaymentCompletedScreen.this );

                    String text = "<font color=#000000>"
                            + shipping_details.getJSONObject(0).getString("ship_name").trim() + "</font>"
                            + "<br>"
                            + shipping_details.getJSONObject(0).getString("ship_email").trim()
                            + "</br>"
                            + "<br>"
                            + shipping_details.getJSONObject(0).getString("ship_phone").trim()  + "</br>"
                            + "<br>"
                            + shipping_details.getJSONObject(0).getString("ship_address1").trim()+ "</br>"
                            + "<br>"
                            + shipping_details.getJSONObject(0).getString("ship_address2").trim()+ "</br>"
                            + "<br>"
                            + shipping_details.getJSONObject(0).getString("ship_city_name").trim()+ "</br>"
                            + "<br>"
                            + shipping_details.getJSONObject(0).getString("ship_country_name").trim()+ "</br>"
                            + "<br>"
                            + shipping_details.getJSONObject(0).getString("ship_state").trim()+ "</br>"
                            + "<br>"
                            + shipping_details.getJSONObject(0).getString("ship_postalcode").trim()+ "</br>"
                            + "</font>";
                    shippingAddressTxt.setText(Html.fromHtml(text));
                    totalAmount.setText(getResources().getString(R.string.totalamount)+" " + SessionSave.getSession("currency_symbol", PaymentCompletedScreen.this) + " "+intent.getStringExtra("totalPrice"));

                } else {
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(PaymentCompletedScreen.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
