package com.saveup.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.saveup.android.R;
import com.saveup.fragment.CartListFragment;
import com.saveup.model.CartData;
import com.saveup.presenter.UserPresenter;
import com.saveup.utils.ApplyFont;
import com.saveup.utils.SessionSave;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

/**
 * @author AAPBD on 16,April,2017
 * @version 5.0
 * @Project MultiVendor
 * @Package com.aapbd.adapter
 */


public class CartListItemAdater extends RecyclerView.Adapter<CartListItemAdater.SingleItemRowHolder> {

    private ArrayList<CartData> itemsList;
    private Context mContext;
    private MyClickListener myClickListener;
    private int count;
    private CartListFragment cartListFragment;

    public CartListItemAdater(Context context, ArrayList<CartData> itemsList, CartListFragment fragment) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.cartListFragment = fragment;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cartlistitem, viewGroup, false);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(final SingleItemRowHolder holder, final int i) {
        holder.deleteIconLay.setTag(i);
        holder.addItem.setTag(i);
        holder.removeItem.setTag(i);
        holder.cart_product_name.setText(itemsList.get(i).getCart_title());
        holder.cart_price.setText(SessionSave.getSession("currency_symbol", mContext) + " " + itemsList.get(i).getCart_price());
        holder.cart_original_price.setText(SessionSave.getSession("currency_symbol", mContext) + " " + itemsList.get(i).getCart_shipping_price());
        holder.cart_percentage.setText(SessionSave.getSession("currency_symbol", mContext) + " " +
                String.format("%.2f",(Double.valueOf( itemsList.get(i).getCart_total()) - Double.valueOf( itemsList.get(i).getCart_tax_price()) )));

        holder.addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (Integer) view.getTag();
                Log.d(" Cart ", "onClick: " + Double.valueOf(itemsList.get(pos).getProduct_available_qty()) + " holder " + Double.valueOf(holder.addEditTxt.getText().toString()));
                if (Double.valueOf(itemsList.get(pos).getProduct_available_qty()) > Double.valueOf(holder.addEditTxt.getText().toString())) {


                    if (cartListFragment != null) {

                        if(Double.valueOf(holder.addEditTxt.getText().toString()) +1 == 2) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(cartListFragment.getActivity());

                            builder.setTitle(R.string.purchasing_multiple_vouchers);
                            builder.setMessage(R.string.multiple_product_alert);

                            builder.setPositiveButton(cartListFragment.getActivity().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).setCancelable(true);
                            builder.show();
                        }

                        cartListFragment.showRemoveCartProgress(true);
                        UserPresenter userPresenter = new UserPresenter(cartListFragment);
                        userPresenter.updateFromCartPage("" + itemsList.get(pos).getCart_id(),"" + itemsList.get(pos).getCart_product_id(),"" + itemsList.get(pos).getProduct_size_id(),
                                "" + itemsList.get(pos).getProduct_color_id(),"" + (Double.valueOf(holder.addEditTxt.getText().toString()) + 1),"" + itemsList.get(pos).getCart_user_id(),pos);
                    }

                } else {
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.available_limit), Toast.LENGTH_LONG).show();
                }
            }
        });

        holder.removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (Integer) view.getTag();

                if (Double.valueOf(holder.addEditTxt.getText().toString()) > 1) {
                    if (cartListFragment != null && cartListFragment instanceof CartListFragment) {

                        cartListFragment.showRemoveCartProgress(true);
                        UserPresenter userPresenter = new UserPresenter(cartListFragment);
                        userPresenter.updateFromCartPage("" + itemsList.get(pos).getCart_id(),"" + itemsList.get(pos).getCart_product_id(),"" + itemsList.get(pos).getProduct_size_id(),
                                "" + itemsList.get(pos).getProduct_color_id(),"" +(Double.valueOf(holder.addEditTxt.getText().toString()) - 1),"" + itemsList.get(pos).getCart_user_id(),pos);

                    }

                }
            }
        });

        holder.addEditTxt.setText("" + itemsList.get(i).getCart_quantity());
        holder.cart_tax_price.setText(SessionSave.getSession("currency_symbol", mContext) +" " + itemsList.get(i).getCart_tax_price());


        holder.progressBar.setVisibility(View.GONE);
        holder.deleteIcon.setVisibility(View.VISIBLE);

        Glide.with(mContext)
                .load(itemsList.get(i).getCart_image())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.noimage)
                .into(holder.productimage);

        holder.deleteIconLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.progressBar.setVisibility(View.VISIBLE);
                holder.deleteIcon.setVisibility(View.GONE);

                int pos = (Integer) v.getTag();
                if (cartListFragment != null && cartListFragment instanceof CartListFragment) {
                    UserPresenter userPresenter = new UserPresenter(cartListFragment);
                    userPresenter.removeFromCartPage("" + itemsList.get(pos).getCart_id(), pos);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView cart_product_name, cart_price, cart_original_price, cart_tax_price,cart_percentage;
        private ImageView addItem, removeItem, productimage, deleteIcon;
        private EditText addEditTxt;
        private RelativeLayout deleteIconLay;
        private ProgressBar progressBar;

        public SingleItemRowHolder(View view) {
            super(view);

            this.cart_product_name = view.findViewById(R.id.cart_product_name);
            this.cart_price = view.findViewById(R.id.cart_price);
            this.cart_original_price = view.findViewById(R.id.cart_original_price);
            this.cart_percentage = view.findViewById(R.id.cart_percentage);
            this.cart_tax_price = view.findViewById(R.id.cart_tax_price);
            this.addItem = view.findViewById(R.id.addItem);
            this.removeItem = view.findViewById(R.id.removeItem);
            this.productimage = view.findViewById(R.id.productimage);
            this.deleteIcon = view.findViewById(R.id.deleteIcon);

            this.addEditTxt = view.findViewById(R.id.addEditTxt);
            this.deleteIconLay = view.findViewById(R.id.deleteIconLay);
            this.progressBar = view.findViewById(R.id.progressBar1);
            ApplyFont.applyFont(mContext, view.findViewById(R.id.cart_product_name));
            ApplyFont.applyBold(mContext, view.findViewById(R.id.cart_price));
            ApplyFont.applyBold(mContext, view.findViewById(R.id.cart_original_price));
            ApplyFont.applyBold(mContext, view.findViewById(R.id.cart_percentage));
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
        this.myClickListener = myClickListener;
    }

    public interface MyClickListener {
        void onItemClick(int position, View v);
    }


}
