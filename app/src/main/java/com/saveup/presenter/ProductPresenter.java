package com.saveup.presenter;

import android.content.Context;
import androidx.fragment.app.Fragment;
import android.util.Log;

import com.saveup.android.BuildConfig;
import com.saveup.fragment.MainSubCategoryFragment;
import com.saveup.fragment.ProductDetailsFragment;
import com.saveup.services.ServiceFactory;
import com.saveup.utils.AppConstant;
import com.google.gson.JsonObject;

import java.util.HashMap;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.OnErrorFailedException;
import rx.schedulers.Schedulers;

/**
 * Created by AAPBD on 20/6/17.
 */

public class ProductPresenter {

    private Context mContext;
    private Fragment mFragment;

    public ProductPresenter(Context context) {
        this.mContext = context;
    }

    public ProductPresenter(Fragment fragment) {
        this.mFragment = fragment;
    }

    public void productListbyCategory(String cat_id, String sub_cat_id, String user_id) {
        ServiceFactory mServiceFactory = new ServiceFactory(BuildConfig.BASE_URL);

        HashMap<String, Object> strMap = new HashMap<>();
        strMap.put("main_category_id", cat_id);
        strMap.put("sec_category_id", sub_cat_id);
        strMap.put("user_id", user_id);
        strMap.put("token", AppConstant.token);


        mServiceFactory.getUserService()
                .productListbyCategory(strMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();

                        try {

                            if (mFragment instanceof MainSubCategoryFragment)
                                ((MainSubCategoryFragment) mFragment).subCategoryData(null);
                        } catch (OnErrorFailedException ex) {
                            // Log the exception
                            ex.printStackTrace();
                        }

                    }

                    @Override
                    public void onNext(JsonObject mJsonObject) {

                        Log.d("Result", "onNext: " + mJsonObject);

                        if (mFragment instanceof MainSubCategoryFragment)
                            ((MainSubCategoryFragment) mFragment).subCategoryData(mJsonObject);


                    }
                });
    }


    public void productDetaislPage(String cat_id, String user_id) {
        ServiceFactory mServiceFactory = new ServiceFactory(BuildConfig.BASE_URL);

        HashMap<String, Object> strMap = new HashMap<>();
        strMap.put("product_id", cat_id);
        strMap.put("user_id", user_id);
        strMap.put("token", AppConstant.token);



        mServiceFactory.getUserService()
                .productDetailsPage(strMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();

                        try {

                            if (mFragment instanceof ProductDetailsFragment)
                                ((ProductDetailsFragment) mFragment).productDetailsData(null);
                        } catch (OnErrorFailedException ex) {
                            // Log the exception
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(JsonObject mJsonObject) {

                        Log.d("Result", "onNext: " + mJsonObject);

                        if (mFragment instanceof ProductDetailsFragment)
                            ((ProductDetailsFragment) mFragment).productDetailsData(mJsonObject);

                    }
                });
    }

}
