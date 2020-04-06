package com.saveup.fragment;

import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.saveup.activity.MainActivity;
import com.saveup.android.R;
import com.saveup.utils.SessionSave;
import com.saveup.views.RippleView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by AAPBD on 9/5/17.
 */

public class DealBuyNowFragment extends Fragment {

    private View progressBar, nestedScrollView;
    private TextView no_of_items, shipping_charges, total_charges;
    private String total_shopping_amount = "";
    private String total_amount = "";
    private RippleView button_continue;
    private TextView product_name, tax_charges,product_price, product_original_price, product_percentage;
    private ImageView addItem, removeItem, productimage;
    private EditText addEditTxt;
    private String colorId, sizeId,productName, productId, store_img, ProductId, productPrice,product_single_ship_amt,product_single_tax_amt,product_ship_amt,product_tax_amt, productdiscount, percentage;
    private int quanty=1,available_limit;
    private LinearLayout taxLay,additemLay;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.productbuynowfragment, container, false);

        progressBar = view.findViewById(R.id.progressBar);
        nestedScrollView = view.findViewById(R.id.processMainLay);
        no_of_items = view.findViewById(R.id.no_of_items);
        shipping_charges = view.findViewById(R.id.shipping_charges);
        total_charges = view.findViewById(R.id.total_charges);
        button_continue = view.findViewById(R.id.button_continue);
        button_continue.setVisibility(View.VISIBLE);
        product_name = view.findViewById(R.id.product_name);
        product_price = view.findViewById(R.id.product_price);
        product_original_price = view.findViewById(R.id.product_original_price);
        product_percentage = view.findViewById(R.id.product_percentage);
        addItem = view.findViewById(R.id.addItem);
        removeItem = view.findViewById(R.id.removeItem);
        productimage = view.findViewById(R.id.productimage);
        addEditTxt = view.findViewById(R.id.addEditTxt);
        taxLay= view.findViewById(R.id.taxLay);
        tax_charges= view.findViewById(R.id.tax_charges);
        taxLay.setVisibility(View.VISIBLE);
        additemLay= view.findViewById(R.id.additemLay);
        additemLay.setVisibility(View.VISIBLE);
        addEditTxt.setText(""+quanty);
        addEditTxt.setClickable(false);
        addEditTxt.setFocusable(false);
        System.out.println("onBackPressed  " + "onCreateView" + "onCreateView");
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
            ((MainActivity) getActivity()).searchIcon.setVisible(false);
         //   ((MainActivity) getActivity()).cartItem.setVisible(false);
            ((MainActivity) getActivity()).cartIconempty.setVisible(false);
            ((MainActivity) getActivity()).editIcon.setVisible(false);

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

                    mainActivity.onBackPressed();
                }
            });
            mainActivity.toolbar.setTitle(""+"Buy Now");
            mainActivity.toolbar.setLogo(R.drawable.transpent_logo);


            Bundle bundle = getArguments();

            if (bundle != null) {
                productId = bundle.getString("product_id");
                productName = bundle.getString("product_name");
                store_img = bundle.getString("product_img");
                sizeId = bundle.getString("product_size_id");
                colorId = bundle.getString("product_color_id");
                product_ship_amt = bundle.getString("product_ship_amt");
                product_single_ship_amt= bundle.getString("product_ship_amt");
                available_limit= bundle.getInt("available_limit");
                String[] priceList = bundle.getString("pricelist").split("-");
                if (priceList.length > 2) {
                    productPrice = priceList[0];
                    productdiscount = priceList[1];
                    percentage = priceList[2];
                }

                Glide.with(getActivity())
                        .load(store_img)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.drawable.noimage)
                        .into(productimage);

                product_tax_amt= ""+(Double.valueOf(productdiscount) *(Double.valueOf(bundle.getString("product_tax_amt"))/100));
                product_single_tax_amt= ""+(Double.valueOf(productdiscount) *(Double.valueOf(bundle.getString("product_tax_amt"))/100));
                product_name.setText(productName);
                product_price.setText(SessionSave.getSession("currency_symbol", getActivity()) + " " + productdiscount);
                product_original_price.setText(SessionSave.getSession("currency_symbol", getActivity()) + " " + productPrice);
                product_original_price.setPaintFlags(product_original_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                tax_charges.setText("" + SessionSave.getSession("currency_symbol", getActivity()) + " " + product_tax_amt);

                product_percentage.setText("" + percentage + " % OFF");
                no_of_items.setText("" + quanty);
                addEditTxt.setText(""+quanty);
                total_shopping_amount = product_ship_amt;
                total_amount = ""+ (Double.valueOf(productdiscount) + Double.valueOf(product_tax_amt) +Double.valueOf(product_ship_amt) ) ;
                shipping_charges.setText("" + SessionSave.getSession("currency_symbol", getActivity()) + " " + product_ship_amt);
                total_charges.setText("" + SessionSave.getSession("currency_symbol", getActivity()) + " " + total_amount);


                addItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(quanty >0 && available_limit>quanty) {
                            quanty++;
                            no_of_items.setText("" + quanty);
                            addEditTxt.setText("" + quanty);
                            total_shopping_amount = ""+Double.valueOf(product_single_ship_amt) * quanty;
                            product_tax_amt= ""+Double.valueOf(product_single_tax_amt) * quanty;
                            Double totalPrice = Double.valueOf(productdiscount) * quanty;
                            total_amount = ""+ (Double.valueOf(totalPrice) + Double.valueOf(product_tax_amt) +Double.valueOf(total_shopping_amount) ) ;
                            shipping_charges.setText("" + SessionSave.getSession("currency_symbol", getActivity()) + " " + total_shopping_amount);
                            tax_charges.setText("" + SessionSave.getSession("currency_symbol", getActivity()) + " " + product_tax_amt);

                         //   product_price.setText(SessionSave.getSession("currency_symbol", getActivity()) + " " + total_amount);
                            total_charges.setText("" + SessionSave.getSession("currency_symbol", getActivity()) + " " + total_amount);
                        }else {
                            Toast.makeText(getActivity(), getResources().getString(R.string.available_limit), Toast.LENGTH_LONG).show();

                        }

                    }
                });
                removeItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(quanty >1) {
                            quanty--;
                            no_of_items.setText("" + quanty);
                            addEditTxt.setText("" + quanty);
                            Double totalPrice = Double.valueOf(productdiscount) * quanty;
                            total_shopping_amount = ""+Double.valueOf(product_single_ship_amt) * quanty;
                            product_tax_amt= ""+Double.valueOf(product_single_tax_amt) * quanty;
                            total_amount = ""+ (Double.valueOf(totalPrice) + Double.valueOf(product_tax_amt) +Double.valueOf(total_shopping_amount) ) ;
                            shipping_charges.setText("" + SessionSave.getSession("currency_symbol", getActivity()) + " " + total_shopping_amount);
                            tax_charges.setText("" + SessionSave.getSession("currency_symbol", getActivity()) + " " + product_tax_amt);
                            total_charges.setText("" + SessionSave.getSession("currency_symbol", getActivity()) + " " + total_amount);
                        }else {
                            Toast.makeText(getActivity(), getResources().getString(R.string.available_limit), Toast.LENGTH_LONG).show();

                        }

                    }
                });
            }

            button_continue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ((MainActivity)getActivity()).mLinearView3.setVisibility(View.GONE);
                    Bundle bundle = new Bundle();
                    bundle.putString("itemcount",""+quanty);
                    bundle.putString("itemprice", total_amount);
                    bundle.putString("shipprice", total_shopping_amount);
                    bundle.putString("taxprice", product_tax_amt);
                    bundle.putString("product_id", productId);
                    bundle.putString("sizeId", sizeId);
                    bundle.putString("colorId", colorId);
                    bundle.putString("urltype", "deal");

                    System.out.println("itemcount " + SessionSave.getSession("cartCount", getActivity()) + " " +total_amount+" " + total_shopping_amount);
                    PaymentContinueFragment fragment2 = new PaymentContinueFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.fragment_container, fragment2);
                    fragment2.setArguments(bundle);
                    fragmentTransaction.addToBackStack("");
                    fragmentTransaction.commit();

                }
            });



        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

