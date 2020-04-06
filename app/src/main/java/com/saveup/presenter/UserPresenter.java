package com.saveup.presenter;

import android.content.Context;
import androidx.fragment.app.Fragment;
import android.util.Log;

import com.saveup.activity.FogetPasswordScreen;
import com.saveup.activity.LoginActivity;
import com.saveup.activity.MainActivity;
import com.saveup.activity.RegistrationActivity;
import com.saveup.android.BuildConfig;
import com.saveup.android.R;
import com.saveup.fragment.CartListFragment;
import com.saveup.fragment.ChangePasswordFargment;
import com.saveup.fragment.DealsListDetailsFragment;
import com.saveup.fragment.DealsListFragment;
import com.saveup.fragment.DealsReviewFragment;
import com.saveup.fragment.HomeScreenFragment;
import com.saveup.fragment.MainSubCategoryFragment;
import com.saveup.fragment.OrderDetailsFragment;
import com.saveup.fragment.OrderTabDynamicFragment;
import com.saveup.fragment.PaymentContinueFragment;
import com.saveup.fragment.ProductDetailsFragment;
import com.saveup.fragment.ProductListFragment;
import com.saveup.fragment.ProductReviewFragment;
import com.saveup.fragment.ProfileFragment;
import com.saveup.fragment.RedemptionFragment;
import com.saveup.fragment.RewardsFragment;
import com.saveup.fragment.SearchLoactionFragment;
import com.saveup.fragment.StoreListDetailsFragment;
import com.saveup.fragment.StoreListFragment;
import com.saveup.fragment.StoreProductListFragment;
import com.saveup.fragment.StoreReviewFragment;
import com.saveup.fragment.WishListFragment;
import com.saveup.services.ServiceFactory;
import com.saveup.utils.AppConstant;
import com.saveup.utils.SessionSave;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.OnErrorFailedException;
import rx.schedulers.Schedulers;

/**
 * Created by AAPBD on 20/6/17.
 */

public class UserPresenter {

    private static final String TAG = UserPresenter.class.getSimpleName();

    private Context mContext;
    private Fragment mFragment;

    public UserPresenter(Context context) {
        this.mContext = context;
    }

    public UserPresenter(Fragment fragment) {
        this.mFragment = fragment;
    }

    public void userRegisterationAPI(String user, String email, String password,String birthDate, String country_id, String city_id, String deviceToken, String phoneNumber) {
        ServiceFactory mServiceFactory = new ServiceFactory(BuildConfig.BASE_URL);

        HashMap<String, Object> strMap = new HashMap<>();
        strMap.put("name", user);
        strMap.put("email", email);
        strMap.put("password", password);
        strMap.put("date_of_birth", birthDate);
        strMap.put("country_id", country_id);
        strMap.put("city_id", city_id);
        strMap.put("lang", AppConstant.defaultLanguageCode);
        strMap.put("token", AppConstant.token);
        strMap.put("device_token", deviceToken);
        strMap.put("device_type", "android");
        strMap.put("phone", phoneNumber);


        mServiceFactory.getUserService()
                .userRegisteration(strMap)
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
                            if (mContext != null && mContext instanceof RegistrationActivity)
                                ((RegistrationActivity) mContext).registerResult(null);

                        } catch (OnErrorFailedException ex) {
                            // Log the exception
                            ex.printStackTrace();
                        }

                    }

                    @Override
                    public void onNext(JsonObject mJsonObject) {

                        Log.d("Result", "onNext: " + mJsonObject);

                        if (mContext instanceof RegistrationActivity)
                            ((RegistrationActivity) mContext).registerResult(mJsonObject);


                    }
                });
    }


    public void userLoginAPI(String email, String password, String deviceToken) {
        ServiceFactory mServiceFactory = new ServiceFactory(BuildConfig.BASE_URL);

        HashMap<String, Object> strMap = new HashMap<>();
        strMap.put("email", email);
        strMap.put("password", password);

        Log.e("email", email);
        Log.e("password", password);
        strMap.put("lang", AppConstant.defaultLanguageCode);
        strMap.put("token", AppConstant.token);
        strMap.put("device_token", deviceToken);
        strMap.put("device_type", "android");

        mServiceFactory.getUserService()
                .userLogin(strMap)
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
                            if (mContext != null && mContext instanceof LoginActivity)
                                ((LoginActivity) mContext).loginResult(null);

                        } catch (OnErrorFailedException ex) {
                            // Log the exception
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(JsonObject mJsonObject) {

                        Log.d("Result", "onNext: " + mJsonObject);

                        if (mContext instanceof LoginActivity)
                            ((LoginActivity) mContext).loginResult(mJsonObject);


                    }
                });
    }

    public void userforgotPasswordAPI(String email, String password) {
        ServiceFactory mServiceFactory = new ServiceFactory(BuildConfig.BASE_URL);

        HashMap<String, Object> strMap = new HashMap<>();
        strMap.put("email", email);
        strMap.put("password", password);
        strMap.put("lang", AppConstant.defaultLanguageCode);
        strMap.put("token", AppConstant.token);

        mServiceFactory.getUserService()
                .userForgotPassword(strMap)
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
                            if (mContext instanceof FogetPasswordScreen)
                                ((FogetPasswordScreen) mContext).passwordResult(null);

                        } catch (OnErrorFailedException ex) {
                            // Log the exception
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(JsonObject mJsonObject) {

                        Log.d("Result", "onNext: " + mJsonObject);

                        if (mContext instanceof FogetPasswordScreen)
                            ((FogetPasswordScreen) mContext).passwordResult(mJsonObject);


                    }
                });
    }

    public void userresetPasswordAPI(String email) {
        ServiceFactory mServiceFactory = new ServiceFactory(BuildConfig.BASE_URL);

        HashMap<String, Object> strMap = new HashMap<>();
        strMap.put("email", email);

        mServiceFactory.getUserService()
                .userResetPassword(strMap)
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
//                            if (mContext instanceof FogetPasswordScreen)
//                                ((FogetPasswordScreen) mContext).passwordResult(null);

                        } catch (OnErrorFailedException ex) {
                            // Log the exception
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(JsonObject mJsonObject) {

                        Log.d("Result", "onNext: " + mJsonObject);

//                        if (mContext instanceof FogetPasswordScreen)
//                            ((FogetPasswordScreen) mContext).passwordResult(mJsonObject);


                    }
                });
    }

    public void homePageApi() {
        ServiceFactory mServiceFactory = new ServiceFactory(BuildConfig.BASE_URL);
        HashMap<String, Object> strMap = new HashMap<>();
        strMap.put("lang", AppConstant.defaultLanguageCode);
        strMap.put("token", AppConstant.token);

        mServiceFactory.getUserService()
                .homePageApi(strMap)
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
                            if (mFragment instanceof HomeScreenFragment)
                                ((HomeScreenFragment) mFragment).homePageData(null);

                        } catch (OnErrorFailedException ex) {
                            // Log the exception
                            ex.printStackTrace();
                        }

                    }

                    @Override
                    public void onNext(JsonObject mJsonObject) {

                        Log.d("Result", "onNext: " + mJsonObject);

                        if (mFragment instanceof HomeScreenFragment)
                            ((HomeScreenFragment) mFragment).homePageData(mJsonObject);


                    }
                });
    }

    public void userFacebookAPI(String user, String id, String name, String userImage, String deviceToken) {
        ServiceFactory mServiceFactory = new ServiceFactory(BuildConfig.BASE_URL);

        HashMap<String, Object> strMap = new HashMap<>();
        strMap.put("email", user);
        strMap.put("facebook_id", id);
        strMap.put("name", name);
        strMap.put("image", userImage);
        strMap.put("lang", AppConstant.defaultLanguageCode);
        strMap.put("token", AppConstant.token);
        strMap.put("device_token", deviceToken);
        strMap.put("device_type", "android");

        mServiceFactory.getUserService()
                .userFacebookLogin(strMap)
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
                            if (mContext instanceof LoginActivity)
                                ((LoginActivity) mContext).loginResult(null);


                        } catch (OnErrorFailedException ex) {
                            // Log the exception
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(JsonObject mJsonObject) {

                        Log.d("Result", "onNext: " + mJsonObject);

                        if (mContext instanceof LoginActivity)
                            ((LoginActivity) mContext).loginResult(mJsonObject);


                    }
                });
    }

    public void userGmailAPI(String user, String name, String userImage, String deviceToken) {
        ServiceFactory mServiceFactory = new ServiceFactory(BuildConfig.BASE_URL);

        HashMap<String, Object> strMap = new HashMap<>();
        strMap.put("email", user);
        strMap.put("name", name);
        strMap.put("image", userImage);
        strMap.put("lang", AppConstant.defaultLanguageCode);
        strMap.put("token", AppConstant.token);
        strMap.put("device_token", deviceToken);
        strMap.put("device_type", "android");

        mServiceFactory.getUserService()
                .userGoogleLogin(strMap)
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
                            if (mContext instanceof LoginActivity)
                                ((LoginActivity) mContext).loginResult(null);


                        } catch (OnErrorFailedException ex) {
                            // Log the exception
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(JsonObject mJsonObject) {

                        Log.d("Result", "onNext: " + mJsonObject);

                        if (mContext instanceof LoginActivity)
                            ((LoginActivity) mContext).loginResult(mJsonObject);


                    }
                });
    }

    public void productListbyCategory(String cat_id, String sub_cat_id, String user_id) {
        ServiceFactory mServiceFactory = new ServiceFactory(BuildConfig.BASE_URL);

        HashMap<String, Object> strMap = new HashMap<>();
        strMap.put("main_category_id", cat_id);
        strMap.put("sec_category_id", sub_cat_id);
        strMap.put("user_id", user_id);
        strMap.put("lang", AppConstant.defaultLanguageCode);
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
        Log.d("dealsDetaislPage", cat_id + "" + user_id);
        HashMap<String, Object> strMap = new HashMap<>();
        strMap.put("product_id", cat_id);
        strMap.put("user_id", user_id);
        strMap.put("lang", AppConstant.defaultLanguageCode);
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

    public void dealsDetaislPage(String deal_id) {
        ServiceFactory mServiceFactory = new ServiceFactory(BuildConfig.BASE_URL);

        HashMap<String, Object> strMap = new HashMap<>();
        strMap.put("dealid", deal_id);
        strMap.put("lang", AppConstant.defaultLanguageCode);
        strMap.put("token", AppConstant.token);

        mServiceFactory.getUserService()
                .dealsDetailsPage(strMap)
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

                            if (mFragment instanceof DealsListDetailsFragment)
                                ((DealsListDetailsFragment) mFragment).dealsDetailsData(null);
                        } catch (OnErrorFailedException ex) {
                            // Log the exception
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(JsonObject mJsonObject) {

                        Log.d("Result", "onNext: " + mJsonObject);

                        if (mFragment instanceof DealsListDetailsFragment)
                            ((DealsListDetailsFragment) mFragment).dealsDetailsData(mJsonObject);

                    }
                });
    }


    public void dealsListPage(String page_no, String title) {
        ServiceFactory mServiceFactory = new ServiceFactory(BuildConfig.BASE_URL);

        HashMap<String, Object> strMap = new HashMap<>();
        strMap.put("page_no", page_no);
        strMap.put("title", title);
        strMap.put("lang", AppConstant.defaultLanguageCode);
        strMap.put("token", AppConstant.token);

        mServiceFactory.getUserService()
                .dealsListPage(strMap)
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

                            if (mFragment instanceof DealsListFragment)
                                ((DealsListFragment) mFragment).productListData(null);
                        } catch (OnErrorFailedException ex) {
                            // Log the exception
                            ex.printStackTrace();
                        }

                    }

                    @Override
                    public void onNext(JsonObject mJsonObject) {

                        Log.d("Result", "onNext: " + mJsonObject);

                        if (mFragment instanceof DealsListFragment)
                            ((DealsListFragment) mFragment).productListData(mJsonObject);

                    }
                });
    }


    public void addCartPage(String product_id, String product_size_id, String product_color_id, String quantity, String user_id) {
        ServiceFactory mServiceFactory = new ServiceFactory(BuildConfig.BASE_URL);
        HashMap<String, Object> strMap = new HashMap<>();
        Log.d("card_item", "product_id" + product_id + "product_size_id" + product_size_id + "product_color_id" + product_color_id);
        strMap.put("product_id", product_id);
        strMap.put("product_size_id", product_size_id);
        strMap.put("product_color_id", product_color_id);
        strMap.put("quantity", quantity);
        strMap.put("user_id", user_id);
        strMap.put("lang", AppConstant.defaultLanguageCode);
        strMap.put("token", AppConstant.token);

        mServiceFactory.getUserService()
                .addCartPage(strMap)
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
                                ((ProductDetailsFragment) mFragment).addCartData(null);
                        } catch (OnErrorFailedException ex) {
                            // Log the exception
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(JsonObject mJsonObject) {

                        Log.d("Result", "onNext: " + mJsonObject);



                        if (mFragment instanceof ProductDetailsFragment)
                            ((ProductDetailsFragment) mFragment).addCartData(mJsonObject);

                    }
                });
    }


    public void updateCartPage(String cart_id, String product_id, String product_size_id, String product_color_id, String quantity, String user_id) {
        ServiceFactory mServiceFactory = new ServiceFactory(BuildConfig.BASE_URL);
        HashMap<String, Object> strMap = new HashMap<>();
        Log.d("card_item", cart_id);
        strMap.put("cart_id", cart_id);
        strMap.put("product_id", product_id);
        strMap.put("product_size_id", product_size_id);
        strMap.put("product_color_id", product_color_id);
        strMap.put("quantity", quantity);
        strMap.put("user_id", user_id);
        strMap.put("lang", AppConstant.defaultLanguageCode);
        strMap.put("token", AppConstant.token);

        mServiceFactory.getUserService()
                .updateCartPage(strMap)
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
                                ((ProductDetailsFragment) mFragment).addCartData(null);
                        } catch (OnErrorFailedException ex) {
                            // Log the exception
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(JsonObject mJsonObject) {

                        Log.d("Result", "onNext: " + mJsonObject);

                        if (mFragment instanceof ProductDetailsFragment)
                            ((ProductDetailsFragment) mFragment).addCartData(mJsonObject);

                    }
                });
    }

    public void orderByOTP(String user_id,String store_id, String productId, String ammount) {
        ServiceFactory mServiceFactory = new ServiceFactory(BuildConfig.BASE_URL);
        HashMap<String, Object> strMap = new HashMap<>();
        strMap.put("amount", ammount);
        strMap.put("user_id", user_id);
        strMap.put("store_id", store_id);
        strMap.put("product_id", productId);

        mServiceFactory.getUserService()
                .orderByOTP(strMap)
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
                                ((ProductDetailsFragment) mFragment).orderByOTPData(null);
                        } catch (OnErrorFailedException ex) {
                            // Log the exception
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(JsonObject mJsonObject) {

                        Log.d("Result", "order by otp onNext: " + mJsonObject);

                        if (mFragment instanceof ProductDetailsFragment)
                            ((ProductDetailsFragment) mFragment).orderByOTPData(mJsonObject);

                    }
                });
    }

    public void orderVerifyByOTP(String user_id,String storeId, String otpCode) {
        ServiceFactory mServiceFactory = new ServiceFactory(BuildConfig.BASE_URL);
        HashMap<String, Object> strMap = new HashMap<>();
        strMap.put("store_id", storeId);
        strMap.put("otp_code", otpCode);
        strMap.put("user_id", user_id);

        mServiceFactory.getUserService()
                .orderVerifyByOTP(strMap)
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
                                ((ProductDetailsFragment) mFragment).orderVerifiedOTPData(null);
                        } catch (OnErrorFailedException ex) {
                            // Log the exception
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(JsonObject mJsonObject) {

                        Log.d("Result", "orderVerifyByOTP onNext: " + mJsonObject);

                        if (mFragment instanceof ProductDetailsFragment)
                            ((ProductDetailsFragment) mFragment).orderVerifiedOTPData(mJsonObject);

                    }
                });
    }

    public void redeem(String user_id,String operator_name, String mobile_number, String ammount) {
        ServiceFactory mServiceFactory = new ServiceFactory(BuildConfig.BASE_URL);
        HashMap<String, Object> strMap = new HashMap<>();
        strMap.put("amount", ammount);
        strMap.put("user_id", user_id);
        strMap.put("operator_name", operator_name);
        strMap.put("mobile_number", mobile_number);

        mServiceFactory.getUserService()
                .redeem(strMap)
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

                            if (mFragment instanceof RedemptionFragment)
                                ((RedemptionFragment) mFragment).redeemData(null);
                        } catch (OnErrorFailedException ex) {
                            // Log the exception
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(JsonObject mJsonObject) {

                        Log.d("Result", "redeem onNext: " + mJsonObject);

                        if (mFragment instanceof RedemptionFragment)
                            ((RedemptionFragment) mFragment).redeemData(mJsonObject);

                    }
                });
    }



    public void productListPage(String page_no, String main_category_id, String sec_category_id, String sub_category_id,
                                String sub_sec_category_id, String price_min, String price_max, String availability, String sort_order_by, String user_id,
                                String title, String discount) {
        ServiceFactory mServiceFactory = new ServiceFactory(BuildConfig.BASE_URL);
        HashMap<String, Object> strMap = new HashMap<>();
        strMap.put("page_no", page_no);
        strMap.put("main_category_id", main_category_id);
        strMap.put("sec_category_id", sec_category_id);
        strMap.put("sub_category_id", sub_category_id);
        strMap.put("sub_sec_category_id", sub_sec_category_id);
        strMap.put("price_min", price_min);
        strMap.put("price_max", price_max);
        strMap.put("availability", availability);
        strMap.put("sort_order_by", sort_order_by);
        strMap.put("user_id", user_id);
        strMap.put("title", title);
        strMap.put("discount", discount);
        strMap.put("lang", AppConstant.defaultLanguageCode);
        strMap.put("token", AppConstant.token);

        mServiceFactory.getUserService()
                .productListPage(strMap)
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


                            if (mFragment instanceof ProductListFragment)
                                ((ProductListFragment) mFragment).productListData(null);
                        } catch (OnErrorFailedException ex) {
                            // Log the exception
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(JsonObject mJsonObject) {

                        Log.d("Result", "onNext: " + mJsonObject);

                        if (mFragment instanceof ProductListFragment)
                            ((ProductListFragment) mFragment).productListData(mJsonObject);

                    }
                });
    }

    //saveup custom home page product list

    //latest product list
    public void latestProductListPage(String title) {
        ServiceFactory mServiceFactory = new ServiceFactory(BuildConfig.BASE_URL);
        HashMap<String, Object> strMap = new HashMap<>();
        strMap.put("is_approved", 1);
        strMap.put("is_published", 1);
        strMap.put("title", title);

        mServiceFactory.getUserService()
                .latestProductListPage(strMap)
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


                            if (mFragment instanceof ProductListFragment)
                                ((ProductListFragment) mFragment).productListData(null);
                        } catch (OnErrorFailedException ex) {
                            // Log the exception
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(JsonObject mJsonObject) {

                        Log.d("Result", "onNext: " + mJsonObject);
                        Log.e("API_RESPONSE", mJsonObject + "");
                        if (mFragment instanceof ProductListFragment)
                            ((ProductListFragment) mFragment).productListData(mJsonObject);

                    }
                });
    }

    //hot deals product list
    public void hotDealsProductListPage(String title) {
        ServiceFactory mServiceFactory = new ServiceFactory(BuildConfig.BASE_URL);
        HashMap<String, Object> strMap = new HashMap<>();
        strMap.put("is_approved", 1);
        strMap.put("is_published", 1);
        strMap.put("featured", 1);
        strMap.put("title", title);

        mServiceFactory.getUserService()
                .hotDealsProductListPage(strMap)
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


                            if (mFragment instanceof ProductListFragment)
                                ((ProductListFragment) mFragment).productListData(null);
                        } catch (OnErrorFailedException ex) {
                            // Log the exception
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(JsonObject mJsonObject) {

                        Log.d("Result", "onNext: " + mJsonObject);
                        Log.e("API_RESPONSE", mJsonObject + "");
                        if (mFragment instanceof ProductListFragment)
                            ((ProductListFragment) mFragment).productListData(mJsonObject);

                    }
                });
    }

    //latest product list
    public void gitftProductListPage(String title) {
        ServiceFactory mServiceFactory = new ServiceFactory(BuildConfig.BASE_URL);
        HashMap<String, Object> strMap = new HashMap<>();
        strMap.put("is_approved", 1);
        strMap.put("is_published", 1);
        strMap.put("gift", 1);
        strMap.put("title", title);

        mServiceFactory.getUserService()
                .giftProductListPage(strMap)
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


                            if (mFragment instanceof ProductListFragment)
                                ((ProductListFragment) mFragment).productListData(null);
                        } catch (OnErrorFailedException ex) {
                            // Log the exception
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(JsonObject mJsonObject) {

                        Log.d("Result", "onNext: " + mJsonObject);
                        Log.e("API_RESPONSE", mJsonObject + "");

                        if (mFragment instanceof ProductListFragment)
                            ((ProductListFragment) mFragment).productListData(mJsonObject);

                    }
                });
    }

    //saveup filter api

    public void filterProductListPage(String sort, String pageTitle, Context context, String category_id) {
        ServiceFactory mServiceFactory = new ServiceFactory(BuildConfig.BASE_URL);
        HashMap<String, Object> strMap = new HashMap<>();
        strMap.put("sort", sort);
        strMap.put("is_approved", 1);
        strMap.put("is_published", 1);
        strMap.put("category_id", category_id);

        if (pageTitle.equalsIgnoreCase(context.getString(R.string.hot_deals))) {
            strMap.put("featured", 1);
        }

        if (pageTitle.equalsIgnoreCase(context.getString(R.string.gift_deals))) {
            strMap.put("gift", 1);
        }


        mServiceFactory.getUserService()
                .filterProductList(strMap)
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


                            if (mFragment instanceof ProductListFragment)
                                ((ProductListFragment) mFragment).productListData(null);
                        } catch (OnErrorFailedException ex) {
                            // Log the exception
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(JsonObject mJsonObject) {

                        Log.d("Result", "onNext: " + mJsonObject);
                        Log.e("API_RESPONSE", mJsonObject.toString());
                        if (mFragment instanceof ProductListFragment)
                            ((ProductListFragment) mFragment).productListData(mJsonObject);

                    }
                });
    }

    //filter merchant
    public void filterMerchant(String category_id) {

        ServiceFactory mServiceFactory = new ServiceFactory(BuildConfig.BASE_URL);
        HashMap<String, Object> strMap = new HashMap<>();
        strMap.put("category_id", category_id);

        mServiceFactory.getUserService()
                .filterMerchant(strMap)
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


                            if (mFragment instanceof StoreListFragment)
                                ((StoreListFragment) mFragment).storeListData(null);
                        } catch (OnErrorFailedException ex) {
                            // Log the exception
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(JsonObject mJsonObject) {

                        Log.d("Result", "onNext: " + mJsonObject);

                        if (mFragment instanceof StoreListFragment)
                            ((StoreListFragment) mFragment).storeListData(mJsonObject);

                    }
                });

    }

    //saveup get product by category id
    public void getProductListByCategoryId(String cat_id) {
        ServiceFactory mServiceFactory = new ServiceFactory(BuildConfig.BASE_URL);
        HashMap<String, Object> strMap = new HashMap<>();
        strMap.put("category_id", cat_id);

        mServiceFactory.getUserService()
                .productByCategory(strMap)
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
                                ((MainSubCategoryFragment) mFragment).productCategoryListData(null);
                        } catch (OnErrorFailedException ex) {
                            // Log the exception
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(JsonObject mJsonObject) {

                        Log.d("Result", "onNext: " + mJsonObject);
                        Log.e("API_RESPONSE", mJsonObject.toString());
                        if (mFragment instanceof MainSubCategoryFragment)
                            ((MainSubCategoryFragment) mFragment).productCategoryListData(mJsonObject);

                    }
                });
    }

    //saveup apply promo code
    public void applyCouponCode(String couponCode, String userId) {
        ServiceFactory mServiceFactory = new ServiceFactory(BuildConfig.BASE_URL);
        HashMap<String, Object> strMap = new HashMap<>();
        strMap.put("coupon_code", couponCode);
        strMap.put("user_id", userId);

        mServiceFactory.getUserService()
                .applyPromoCode(strMap)
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


                            if (mFragment instanceof PaymentContinueFragment)
                                ((PaymentContinueFragment) mFragment).CouponCodeResponse(null);
                            /*else if(mFragment instanceof ProductDetailsFragment){
                                ((ProductDetailsFragment) mFragment).CouponCodeResponse(null);
                            }*/
                        } catch (OnErrorFailedException ex) {
                            // Log the exception
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(JsonObject mJsonObject) {

                        Log.d("Result", "onNext: " + mJsonObject);
                        Log.e("API_RESPONSE", mJsonObject.toString());
                        if (mFragment instanceof PaymentContinueFragment)
                            ((PaymentContinueFragment) mFragment).CouponCodeResponse(mJsonObject);
                        /*else if(mFragment instanceof ProductDetailsFragment){
                            ((ProductDetailsFragment) mFragment).CouponCodeResponse(mJsonObject);
                        }*/
                    }
                });
    }

    public void productRefreshLoadListPage(String page_no, String main_category_id, String sec_category_id, String sub_category_id,
                                           String sub_sec_category_id, String price_min, String price_max, String availability, String sort_order_by, String user_id,
                                           String title, String discount) {
        ServiceFactory mServiceFactory = new ServiceFactory(BuildConfig.BASE_URL);
        HashMap<String, Object> strMap = new HashMap<>();
        strMap.put("page_no", page_no);
        strMap.put("main_category_id", main_category_id);
        strMap.put("sec_category_id", sec_category_id);
        strMap.put("sub_category_id", sub_category_id);
        strMap.put("sub_sec_category_id", sub_sec_category_id);
        strMap.put("price_min", price_min);
        strMap.put("price_max", price_max);
        strMap.put("availability", availability);
        strMap.put("sort_order_by", sort_order_by);
        strMap.put("user_id", user_id);
        strMap.put("title", title);
        strMap.put("discount", discount);
        strMap.put("lang", AppConstant.defaultLanguageCode);
        strMap.put("token", AppConstant.token);

        mServiceFactory.getUserService()
                .productListPage(strMap)
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


                            if (mFragment instanceof ProductListFragment)
                                ((ProductListFragment) mFragment).productRefreshListData(null);
                        } catch (OnErrorFailedException ex) {
                            // Log the exception
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(JsonObject mJsonObject) {

                        Log.d("Result", "onNext: " + mJsonObject);

                        if (mFragment instanceof ProductListFragment)
                            ((ProductListFragment) mFragment).productRefreshListData(mJsonObject);

                    }
                });
    }

    public void cartListPage(String user_id) {
        ServiceFactory mServiceFactory = new ServiceFactory(BuildConfig.BASE_URL);

        HashMap<String, Object> strMap = new HashMap<>();
        strMap.put("user_id", user_id);
        strMap.put("lang", AppConstant.defaultLanguageCode);
        strMap.put("token", AppConstant.token);

        mServiceFactory.getUserService()
                .cartListPage(strMap)
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

                            if (mFragment instanceof CartListFragment)
                                ((CartListFragment) mFragment).cartListData(null);
                            else if (mContext instanceof MainActivity)
                                ((MainActivity) mContext).cartListData(null);
                        } catch (OnErrorFailedException ex) {
                            // Log the exception
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(JsonObject mJsonObject) {

                        Log.d("Result", "onNext: " + mJsonObject);

                        if (mFragment instanceof CartListFragment)
                            ((CartListFragment) mFragment).cartListData(mJsonObject);
                        else if (mContext instanceof MainActivity)
                            ((MainActivity) mContext).cartListData(mJsonObject);
                    }
                });
    }

    //saveup add to wish list from product details

    public void addWishListPageFromProductDetails(String user_id, String product_id) {
        ServiceFactory mServiceFactory = new ServiceFactory(BuildConfig.BASE_URL);

        HashMap<String, Object> strMap = new HashMap<>();
        strMap.put("user_id", user_id);
        strMap.put("product_id", product_id);

        mServiceFactory.getUserService()
                .addWishListPage(strMap)
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
                                ((ProductDetailsFragment) mFragment).addWishListData(null);

                        } catch (OnErrorFailedException ex) {
                            // Log the exception
                            ex.printStackTrace();
                        }

                    }

                    @Override
                    public void onNext(JsonObject mJsonObject) {

                        Log.d("Result", "onNext: " + mJsonObject);

                        if (mFragment instanceof ProductDetailsFragment)
                            ((ProductDetailsFragment) mFragment).addWishListData(mJsonObject);

                    }
                });
    }

    public void addWishListPage(String user_id, String product_id, final int pos) {
        ServiceFactory mServiceFactory = new ServiceFactory(BuildConfig.BASE_URL);

        HashMap<String, Object> strMap = new HashMap<>();
        strMap.put("user_id", user_id);
        strMap.put("product_id", product_id);
        strMap.put("lang", AppConstant.defaultLanguageCode);
        strMap.put("token", AppConstant.token);

        mServiceFactory.getUserService()
                .addWishListPage(strMap)
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

                            if (mFragment instanceof ProductListFragment)
                                ((ProductListFragment) mFragment).addWishListData(null, pos);
                            else if (mFragment instanceof StoreProductListFragment)
                                ((StoreProductListFragment) mFragment).addWishListData(null, pos);
                        } catch (OnErrorFailedException ex) {
                            // Log the exception
                            ex.printStackTrace();
                        }

                    }

                    @Override
                    public void onNext(JsonObject mJsonObject) {

                        Log.d("Result", "onNext: " + mJsonObject);

                        if (mFragment instanceof ProductListFragment)
                            ((ProductListFragment) mFragment).addWishListData(mJsonObject, pos);
                        else if (mFragment instanceof StoreProductListFragment)
                            ((StoreProductListFragment) mFragment).addWishListData(mJsonObject, pos);
                    }
                });
    }

    //saveup remove wishlist
    public void removeWishList(String user_id, String product_id, final int pos) {
        ServiceFactory mServiceFactory = new ServiceFactory(BuildConfig.BASE_URL);

        HashMap<String, Object> strMap = new HashMap<>();
        strMap.put("user_id", user_id);
        strMap.put("product_id", product_id);

        mServiceFactory.getUserService()
                .removeWishlist(strMap)
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

                            if (mFragment instanceof WishListFragment) {
                                ((WishListFragment) mFragment).removeWishListData(null, pos);
                            } else if (mFragment instanceof ProductDetailsFragment) {
                                ((ProductDetailsFragment) mFragment).removeWishListData(null);
                            }
                        } catch (OnErrorFailedException ex) {
                            // Log the exception
                            ex.printStackTrace();
                        }

                    }

                    @Override
                    public void onNext(JsonObject mJsonObject) {

                        Log.d("Result", "onNext: " + mJsonObject);

                        if (mFragment instanceof WishListFragment) {
                            ((WishListFragment) mFragment).removeWishListData(mJsonObject, pos);
                        } else if (mFragment instanceof ProductDetailsFragment) {
                            ((ProductDetailsFragment) mFragment).removeWishListData(mJsonObject);
                        }

                    }
                });
    }

    public void removeWishListPage(String user_id, String product_id, final int pos) {
        ServiceFactory mServiceFactory = new ServiceFactory(BuildConfig.BASE_URL);

        HashMap<String, Object> strMap = new HashMap<>();
        strMap.put("user_id", user_id);
        strMap.put("product_id", product_id);
        strMap.put("lang", AppConstant.defaultLanguageCode);
        strMap.put("token", AppConstant.token);

        mServiceFactory.getUserService()
                .addWishListPage(strMap)
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

                            if (mFragment instanceof WishListFragment)
                                ((WishListFragment) mFragment).removeWishListData(null, pos);
                        } catch (OnErrorFailedException ex) {
                            // Log the exception
                            ex.printStackTrace();
                        }

                    }

                    @Override
                    public void onNext(JsonObject mJsonObject) {

                        Log.d("Result", "onNext: " + mJsonObject);

                        if (mFragment instanceof WishListFragment)
                            ((WishListFragment) mFragment).removeWishListData(mJsonObject, pos);

                    }
                });
    }

    public void removeFromCartPage(String cart_id, final int pos) {
        ServiceFactory mServiceFactory = new ServiceFactory(BuildConfig.BASE_URL);

        HashMap<String, Object> strMap = new HashMap<>();
        strMap.put("cart_id", cart_id);
        strMap.put("lang", AppConstant.defaultLanguageCode);
        strMap.put("token", AppConstant.token);

        mServiceFactory.getUserService()
                .removeFromCartPage(strMap)
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


                            if (mFragment instanceof CartListFragment)
                                ((CartListFragment) mFragment).removeCartData(null, pos);
                        } catch (OnErrorFailedException ex) {
                            // Log the exception
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(JsonObject mJsonObject) {

                        Log.d("Result", "onNext: " + mJsonObject);

                        if (mFragment instanceof CartListFragment)
                            ((CartListFragment) mFragment).removeCartData(mJsonObject, pos);

                    }
                });
    }

    public void dealsaddcartPage(String deal_id, String quantity, String user_id) {
        ServiceFactory mServiceFactory = new ServiceFactory(BuildConfig.BASE_URL);

        HashMap<String, Object> strMap = new HashMap<>();
        strMap.put("user_id", user_id);
        strMap.put("quantity", quantity);
        strMap.put("dealid", deal_id);
        strMap.put("lang", AppConstant.defaultLanguageCode);
        strMap.put("token", AppConstant.token);

        Log.d("dealid", deal_id);

        mServiceFactory.getUserService()
                .addDealsCartPage(strMap)
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


                            if (mFragment instanceof DealsListDetailsFragment)
                                ((DealsListDetailsFragment) mFragment).addCartData(null);
                        } catch (OnErrorFailedException ex) {
                            // Log the exception
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(JsonObject mJsonObject) {

                        Log.d("Result", "onNext: " + mJsonObject);

                        if (mFragment instanceof DealsListDetailsFragment)
                            ((DealsListDetailsFragment) mFragment).addCartData(mJsonObject);

                    }
                });


    }

    public void addProfilePage(String user_id) {
        ServiceFactory mServiceFactory = new ServiceFactory(BuildConfig.BASE_URL);
        HashMap<String, Object> strMap = new HashMap<>();
        strMap.put("user_id", user_id);
        strMap.put("lang", AppConstant.defaultLanguageCode);
        strMap.put("token", AppConstant.token);

        mServiceFactory.getUserService()
                .getProfilePage(strMap)
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


                            if (mFragment instanceof ProfileFragment)
                                ((ProfileFragment) mFragment).getProfileData(null, 0);
                            else if (mContext instanceof MainActivity)
                                ((MainActivity) mContext).getProfileData(null);

                        } catch (OnErrorFailedException ex) {
                            // Log the exception
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(JsonObject mJsonObject) {

                        Log.d("Result", "onNext: " + mJsonObject);

                        if (mFragment instanceof ProfileFragment)
                            ((ProfileFragment) mFragment).getProfileData(mJsonObject, 0);
                        else if (mContext instanceof MainActivity)
                            ((MainActivity) mContext).getProfileData(mJsonObject);

                    }
                });
    }

    public void wishListPage(String pageno, String user_id) {
        ServiceFactory mServiceFactory = new ServiceFactory(BuildConfig.BASE_URL);
        HashMap<String, Object> strMap = new HashMap<>();
        strMap.put("page_no", pageno);
        strMap.put("user_id", user_id);
        strMap.put("lang", AppConstant.defaultLanguageCode);
        strMap.put("token", AppConstant.token);

        mServiceFactory.getUserService()
                .wishListPage(strMap)
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


                            if (mFragment instanceof WishListFragment)
                                ((WishListFragment) mFragment).wishListData(null);
                        } catch (OnErrorFailedException ex) {
                            // Log the exception
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(JsonObject mJsonObject) {

                        Log.d("Result", "onNext: " + mJsonObject);

                        if (mFragment instanceof WishListFragment)
                            ((WishListFragment) mFragment).wishListData(mJsonObject);

                    }
                });
    }

    public void resetPassword(String user_id, String oldpassword, String newpassword) {
        ServiceFactory mServiceFactory = new ServiceFactory(BuildConfig.BASE_URL);
        HashMap<String, Object> strMap = new HashMap<>();
        strMap.put("user_id", user_id);
        strMap.put("old_password", oldpassword);
        strMap.put("new_password", newpassword);
        strMap.put("lang", AppConstant.defaultLanguageCode);
        strMap.put("token", AppConstant.token);


        mServiceFactory.getUserService()
                .resetPassword(strMap)
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


                            if (mFragment instanceof ChangePasswordFargment)
                                ((ChangePasswordFargment) mFragment).resetPasswordData(null);
                        } catch (OnErrorFailedException ex) {
                            // Log the exception
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(JsonObject mJsonObject) {

                        Log.d("Result", "onNext: " + mJsonObject);

                        if (mFragment instanceof ChangePasswordFargment)
                            ((ChangePasswordFargment) mFragment).resetPasswordData(mJsonObject);

                    }
                });
    }

    public void storeListPage(String pageno) {
        ServiceFactory mServiceFactory = new ServiceFactory(BuildConfig.BASE_URL);
        HashMap<String, Object> strMap = new HashMap<>();
        strMap.put("pageno", pageno);
        strMap.put("lang", AppConstant.defaultLanguageCode);
        strMap.put("token", AppConstant.token);

        mServiceFactory.getUserService()
                .shopList(strMap)
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


                            if (mFragment instanceof StoreListFragment)
                                ((StoreListFragment) mFragment).storeListData(null);
                        } catch (OnErrorFailedException ex) {
                            // Log the exception
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(JsonObject mJsonObject) {

                        Log.d("Result", "onNext: " + mJsonObject);

                        if (mFragment instanceof StoreListFragment)
                            ((StoreListFragment) mFragment).storeListData(mJsonObject);

                    }
                });
    }

    public void updateFromCartPage(String cart_id, String product_id, String product_size_id, String product_color_id, String quantity, String user_id, final int pos) {
        ServiceFactory mServiceFactory = new ServiceFactory(BuildConfig.BASE_URL);

        HashMap<String, Object> strMap = new HashMap<>();
        strMap.put("cart_id", cart_id);
        strMap.put("product_id", product_id);
        strMap.put("product_size_id", product_size_id);
        strMap.put("product_color_id", product_color_id);
        strMap.put("quantity", quantity);
        strMap.put("user_id", user_id);
        strMap.put("lang", AppConstant.defaultLanguageCode);
        strMap.put("token", AppConstant.token);

        Log.d("cart_id ", cart_id);
        mServiceFactory.getUserService()
                .updateFromCartPage(strMap)
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


                            if (mFragment instanceof CartListFragment)
                                ((CartListFragment) mFragment).udpateCartData(null, pos);
                        } catch (OnErrorFailedException ex) {
                            // Log the exception
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(JsonObject mJsonObject) {

                        Log.d("Result", "onNext: " + mJsonObject);
                        Log.e("API_RESPONSE", "position: " + pos);
                        Log.e("API_RESPONSE", mJsonObject.toString());
                        if (mFragment instanceof CartListFragment)
                            ((CartListFragment) mFragment).udpateCartData(mJsonObject, pos);

                    }
                });
    }

    public void editProfilePage(String user_id, String user_name, String user_phone, String user_email,
                                String user_address1, String user_address2, String user_country_id,
                                String user_city_id, String ship_id, String ship_name, String ship_email,
                                String ship_phone, String ship_address1, String ship_address2,
                                String ship_country_id, String ship_city_id,
                                String ship_state, String ship_postalcode, String user_image) {
        ServiceFactory mServiceFactory = new ServiceFactory(BuildConfig.BASE_URL);

        HashMap<String, RequestBody> strMap = new HashMap<>();
        strMap.put("user_id", RequestBody.create(MediaType.parse("text/plain"), user_id));
        strMap.put("user_name", RequestBody.create(MediaType.parse("text/plain"), user_name));
        strMap.put("user_email", RequestBody.create(MediaType.parse("text/plain"), user_email));
        strMap.put("user_phone", RequestBody.create(MediaType.parse("text/plain"), user_phone));
        strMap.put("user_address1", RequestBody.create(MediaType.parse("text/plain"), user_address1));
        strMap.put("user_address2", RequestBody.create(MediaType.parse("text/plain"), user_address2));
        strMap.put("user_country_id", RequestBody.create(MediaType.parse("text/plain"), user_country_id));
        strMap.put("user_city_id", RequestBody.create(MediaType.parse("text/plain"), user_city_id));
        strMap.put("ship_id", RequestBody.create(MediaType.parse("text/plain"), ship_id));
        strMap.put("ship_name", RequestBody.create(MediaType.parse("text/plain"), ship_name));
        strMap.put("ship_email", RequestBody.create(MediaType.parse("text/plain"), ship_email));
        strMap.put("ship_phone", RequestBody.create(MediaType.parse("text/plain"), ship_phone));
        strMap.put("ship_address1", RequestBody.create(MediaType.parse("text/plain"), ship_address1));
        strMap.put("ship_address2", RequestBody.create(MediaType.parse("text/plain"), ship_address2));
        strMap.put("ship_country_id", RequestBody.create(MediaType.parse("text/plain"), ship_country_id));
        strMap.put("ship_city_id", RequestBody.create(MediaType.parse("text/plain"), ship_city_id));
        strMap.put("ship_state", RequestBody.create(MediaType.parse("text/plain"), ship_state));
        strMap.put("ship_postalcode", RequestBody.create(MediaType.parse("text/plain"), ship_postalcode));
        strMap.put("lang", RequestBody.create(MediaType.parse("text/plain"), AppConstant.defaultLanguageCode));

        strMap.put("token", RequestBody.create(MediaType.parse("text/plain"), AppConstant.token));

        System.out.println("user_image " + user_image);


        if (!(user_image.equals(""))) {
            Log.e("API_RESPONSE", "image exists " + user_image);

            File file = new File(user_image);
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("profile_image", file.getName(), requestBody);
            mServiceFactory.getUserService()
                    .editProfilePage(strMap, fileToUpload)
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
                                Log.e("API_RESPONSE", "error");
                                if (mFragment instanceof ProfileFragment)
                                    ((ProfileFragment) mFragment).getProfileData(null, 1);
                            } catch (OnErrorFailedException ex) {
                                // Log the exception
                                ex.printStackTrace();
                            }
                        }

                        @Override
                        public void onNext(JsonObject mJsonObject) {
                            Log.e("API_RESPONSE", "res: " + mJsonObject);
                            Log.d("Result", "ProfileFragment: " + mJsonObject);
                            Log.d("Result", "ProfileFragment 22 : " + (mFragment instanceof ProfileFragment));
                            if (mFragment instanceof ProfileFragment)
                                ((ProfileFragment) mFragment).getProfileData(mJsonObject, 1);

                        }
                    });
        } else {

            Log.e("API_RESPONSE", "image null");

            mServiceFactory.getUserService()
                    .editNoImageProfilePage(strMap)
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


                                if (mFragment instanceof ProfileFragment)
                                    ((ProfileFragment) mFragment).getProfileData(null, 1);
                            } catch (OnErrorFailedException ex) {
                                // Log the exception
                                ex.printStackTrace();
                            }
                        }

                        @Override
                        public void onNext(JsonObject mJsonObject) {

                            Log.d("Result", "ProfileFragment: " + mJsonObject);
                            Log.d("Result", "ProfileFragment 22 : " + (mFragment instanceof ProfileFragment));
                            if (mFragment instanceof ProfileFragment)
                                ((ProfileFragment) mFragment).getProfileData(mJsonObject, 1);

                        }
                    });
        }
    }

    public void editProfilePage(String user_id, String user_name, String user_phone, String user_email,
                                String user_address1, String user_address2, String user_country_id,
                                String user_city_id, String ship_id, String ship_name, String ship_email,
                                String ship_phone, String ship_address1, String ship_address2,
                                String ship_country_id, String ship_city_id,
                                String ship_state, String ship_postalcode, File user_image) {
        ServiceFactory mServiceFactory = new ServiceFactory(BuildConfig.BASE_URL);

        HashMap<String, RequestBody> strMap = new HashMap<>();
        strMap.put("user_id", RequestBody.create(MediaType.parse("text/plain"), user_id));
        strMap.put("user_name", RequestBody.create(MediaType.parse("text/plain"), user_name));
        strMap.put("user_email", RequestBody.create(MediaType.parse("text/plain"), user_email));
        strMap.put("user_phone", RequestBody.create(MediaType.parse("text/plain"), user_phone));
        strMap.put("user_address1", RequestBody.create(MediaType.parse("text/plain"), user_address1));
        strMap.put("user_address2", RequestBody.create(MediaType.parse("text/plain"), user_address2));
        strMap.put("user_country_id", RequestBody.create(MediaType.parse("text/plain"), user_country_id));
        strMap.put("user_city_id", RequestBody.create(MediaType.parse("text/plain"), user_city_id));
        strMap.put("ship_id", RequestBody.create(MediaType.parse("text/plain"), ship_id));
        strMap.put("ship_name", RequestBody.create(MediaType.parse("text/plain"), ship_name));
        strMap.put("ship_email", RequestBody.create(MediaType.parse("text/plain"), ship_email));
        strMap.put("ship_phone", RequestBody.create(MediaType.parse("text/plain"), ship_phone));
        strMap.put("ship_address1", RequestBody.create(MediaType.parse("text/plain"), ship_address1));
        strMap.put("ship_address2", RequestBody.create(MediaType.parse("text/plain"), ship_address2));
        strMap.put("ship_country_id", RequestBody.create(MediaType.parse("text/plain"), ship_country_id));
        strMap.put("ship_city_id", RequestBody.create(MediaType.parse("text/plain"), ship_city_id));
        strMap.put("ship_state", RequestBody.create(MediaType.parse("text/plain"), ship_state));
        strMap.put("ship_postalcode", RequestBody.create(MediaType.parse("text/plain"), ship_postalcode));
        strMap.put("lang", RequestBody.create(MediaType.parse("text/plain"), AppConstant.defaultLanguageCode));

        strMap.put("token", RequestBody.create(MediaType.parse("text/plain"), AppConstant.token));

        System.out.println("user_image " + user_image);


        if (user_image != null) {
            Log.e("API_RESPONSE", "image exists " + user_image);

            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), user_image);
            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("profile_image", user_image.getName(), requestBody);
            mServiceFactory.getUserService()
                    .editProfilePage(strMap, fileToUpload)
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
                                Log.e("API_RESPONSE", "error " + e);
                                if (mFragment instanceof ProfileFragment)
                                    ((ProfileFragment) mFragment).getProfileData(null, 1);
                            } catch (OnErrorFailedException ex) {
                                // Log the exception
                                ex.printStackTrace();
                            }
                        }

                        @Override
                        public void onNext(JsonObject mJsonObject) {
                            Log.e("API_RESPONSE", "res: " + mJsonObject);
                            Log.d("Result", "ProfileFragment: " + mJsonObject);
                            Log.d("Result", "ProfileFragment 22 : " + (mFragment instanceof ProfileFragment));
                            if (mFragment instanceof ProfileFragment)
                                ((ProfileFragment) mFragment).getProfileData(mJsonObject, 1);

                        }
                    });
        } else {

            Log.e("API_RESPONSE", "image null");

            mServiceFactory.getUserService()
                    .editNoImageProfilePage(strMap)
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


                                if (mFragment instanceof ProfileFragment)
                                    ((ProfileFragment) mFragment).getProfileData(null, 1);
                            } catch (OnErrorFailedException ex) {
                                // Log the exception
                                ex.printStackTrace();
                            }
                        }

                        @Override
                        public void onNext(JsonObject mJsonObject) {

                            Log.d("Result", "ProfileFragment: " + mJsonObject);
                            Log.d("Result", "ProfileFragment 22 : " + (mFragment instanceof ProfileFragment));
                            if (mFragment instanceof ProfileFragment)
                                ((ProfileFragment) mFragment).getProfileData(mJsonObject, 1);

                        }
                    });
        }
    }


    public void orderListPage(String user_id, String page_no) {
        ServiceFactory mServiceFactory = new ServiceFactory(BuildConfig.BASE_URL);
        HashMap<String, Object> strMap = new HashMap<>();
        strMap.put("page_no", page_no);
        strMap.put("user_id", user_id);
        strMap.put("lang", AppConstant.defaultLanguageCode);
        strMap.put("token", AppConstant.token);

        mServiceFactory.getUserService()
                .orderList(strMap)
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


                            if (mFragment instanceof OrderTabDynamicFragment)
                                ((OrderTabDynamicFragment) mFragment).orderListData(null);
                        } catch (OnErrorFailedException ex) {
                            // Log the exception
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(JsonObject mJsonObject) {

                        Log.d("Result", "onNext: " + mJsonObject);

                        if (mFragment instanceof OrderTabDynamicFragment) {
                            ((OrderTabDynamicFragment) mFragment).orderListData(mJsonObject);
                        }else if(mFragment instanceof RewardsFragment){
                            ((RewardsFragment) mFragment).orderListData(mJsonObject);
                        }
                    }
                });
    }

    public void orderListDetails(String orderId) {
        ServiceFactory mServiceFactory = new ServiceFactory(BuildConfig.BASE_URL);
        HashMap<String, Object> strMap = new HashMap<>();
        strMap.put("order_id", orderId);

        mServiceFactory.getUserService()
                .orderListDetails(strMap)
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


                            if (mFragment instanceof OrderDetailsFragment)
                                ((OrderDetailsFragment) mFragment).showData(null);
                        } catch (OnErrorFailedException ex) {
                            // Log the exception
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(JsonObject mJsonObject) {

                        Log.d("Result", "onNext: " + mJsonObject);

                        if (mFragment instanceof OrderDetailsFragment)
                            ((OrderDetailsFragment) mFragment).showData(mJsonObject);

                    }
                });
    }

    public void redeemCouponByUser(String couponCode) {
        ServiceFactory mServiceFactory = new ServiceFactory(BuildConfig.BASE_URL);
        HashMap<String, Object> strMap = new HashMap<>();
        strMap.put("coupon_code", couponCode);

        mServiceFactory.getUserService()
                .redeemCouponByUser(strMap)
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

                            if (mFragment instanceof OrderDetailsFragment)
                                ((OrderDetailsFragment) mFragment).redeemCouponResponse(null);
                        } catch (OnErrorFailedException ex) {
                            // Log the exception
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(JsonObject mJsonObject) {

                        Log.d("Result", "onNext: " + mJsonObject);

                        if (mFragment instanceof OrderDetailsFragment)
                            ((OrderDetailsFragment) mFragment).redeemCouponResponse(mJsonObject);

                    }
                });
    }

    public void nearByStore(String lat, String lon) {
        ServiceFactory mServiceFactory = new ServiceFactory(BuildConfig.BASE_URL);
        HashMap<String, Object> strMap = new HashMap<>();
        strMap.put("latitude", lat);
        strMap.put("longitude", lon);

        mServiceFactory.getUserService()
                .nearByStore(strMap)
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
                            if (mFragment instanceof SearchLoactionFragment)
                                ((SearchLoactionFragment) mFragment).nearStoreData(null);

                        } catch (OnErrorFailedException ex) {
                            // Log the exception
                            ex.printStackTrace();
                        }

                    }

                    @Override
                    public void onNext(JsonObject mJsonObject) {
                        Log.d(TAG, "nearByStore: " + mJsonObject.toString());
                        if (mFragment instanceof SearchLoactionFragment)
                            ((SearchLoactionFragment) mFragment).nearStoreData(mJsonObject);


                    }
                });
    }

    public void shopdetailsPage(String shopid) {
        ServiceFactory mServiceFactory = new ServiceFactory(BuildConfig.BASE_URL);
        HashMap<String, Object> strMap = new HashMap<>();
        strMap.put("shopid", shopid);
        strMap.put("lang", AppConstant.defaultLanguageCode);
        strMap.put("user_id", SessionSave.getSession("user_id", mFragment.getActivity()));
        strMap.put("token", AppConstant.token);

        mServiceFactory.getUserService()
                .storedetailsPage(strMap)
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

                            if (mFragment instanceof StoreListFragment)
                                ((StoreListFragment) mFragment).storeDetailsView(null);
                        } catch (OnErrorFailedException ex) {
                            // Log the exception
                            ex.printStackTrace();
                        }

                    }

                    @Override
                    public void onNext(JsonObject mJsonObject) {

                        Log.d("Result", "shopdetailsPage onNext: " + mJsonObject);

                        if (mFragment instanceof StoreListFragment)
                            ((StoreListFragment) mFragment).storeDetailsView(mJsonObject);
                        else if (mFragment instanceof SearchLoactionFragment)
                            ((SearchLoactionFragment) mFragment).storeDetailsView(mJsonObject);
                    }
                });
    }

    public void shopdetailsBranchPage(String shopid) {
        ServiceFactory mServiceFactory = new ServiceFactory(BuildConfig.BASE_URL);

        HashMap<String, Object> strMap = new HashMap<>();
        strMap.put("shopid", shopid);
        strMap.put("lang", AppConstant.defaultLanguageCode);
        strMap.put("token", AppConstant.token);


        mServiceFactory.getUserService()
                .storedetailsPage(strMap)
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

                            if (mFragment instanceof StoreListDetailsFragment)
                                ((StoreListDetailsFragment) mFragment).storeDetailsUpdate(null);
                        } catch (OnErrorFailedException ex) {
                            // Log the exception
                            ex.printStackTrace();
                        }

                    }

                    @Override
                    public void onNext(JsonObject mJsonObject) {

                        Log.d("Result", "onNext: " + mJsonObject);

                        if (mFragment instanceof StoreListDetailsFragment)
                            ((StoreListDetailsFragment) mFragment).storeDetailsUpdate(mJsonObject);

                    }
                });
    }

    public void productcommentsPage(String user_id, String product_id, String title, String comments, String ratings) {
        ServiceFactory mServiceFactory = new ServiceFactory(BuildConfig.BASE_URL);

        HashMap<String, Object> strMap = new HashMap<>();
        strMap.put("user_id", user_id);
        strMap.put("product_id", product_id);
        strMap.put("title", title);
        strMap.put("comments", comments);
        strMap.put("ratings", ratings);
        strMap.put("lang", AppConstant.defaultLanguageCode);
        strMap.put("token", AppConstant.token);


        mServiceFactory.getUserService()
                .productReview(strMap)
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

                            if (mFragment instanceof ProductReviewFragment)
                                ((ProductReviewFragment) mFragment).reviewComments(null);
                        } catch (OnErrorFailedException ex) {
                            // Log the exception
                            ex.printStackTrace();
                        }

                    }

                    @Override
                    public void onNext(JsonObject mJsonObject) {

                        Log.d("Result", "onNext: " + mJsonObject);

                        if (mFragment instanceof ProductReviewFragment)
                            ((ProductReviewFragment) mFragment).reviewComments(mJsonObject);

                    }
                });
    }

    public void dealscommentsPage(String user_id, String deal_id, String title, String comments, String ratings) {
        ServiceFactory mServiceFactory = new ServiceFactory(BuildConfig.BASE_URL);

        HashMap<String, Object> strMap = new HashMap<>();
        strMap.put("user_id", user_id);
        strMap.put("deal_id", deal_id);
        strMap.put("title", title);
        strMap.put("comments", comments);
        strMap.put("ratings", ratings);
        strMap.put("lang", AppConstant.defaultLanguageCode);
        strMap.put("token", AppConstant.token);

        Log.d("dealid", deal_id);


        mServiceFactory.getUserService()
                .dealsReview(strMap)
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

                            if (mFragment instanceof DealsReviewFragment)
                                ((DealsReviewFragment) mFragment).reviewComments(null);
                        } catch (OnErrorFailedException ex) {
                            // Log the exception
                            ex.printStackTrace();
                        }

                    }

                    @Override
                    public void onNext(JsonObject mJsonObject) {

                        Log.d("Result", "onNext: " + mJsonObject);

                        if (mFragment instanceof DealsReviewFragment)
                            ((DealsReviewFragment) mFragment).reviewComments(mJsonObject);

                    }
                });
    }

    public void storecommentsPage(String user_id, String store_id, String title, String comments, String ratings) {
        ServiceFactory mServiceFactory = new ServiceFactory(BuildConfig.BASE_URL);

        HashMap<String, Object> strMap = new HashMap<>();
        strMap.put("user_id", user_id);
        strMap.put("store_id", store_id);
        strMap.put("title", title);
        strMap.put("comments", comments);
        strMap.put("ratings", ratings);
        strMap.put("lang", AppConstant.defaultLanguageCode);
        strMap.put("token", AppConstant.token);


        mServiceFactory.getUserService()
                .storeReview(strMap)
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

                            if (mFragment instanceof StoreReviewFragment)
                                ((StoreReviewFragment) mFragment).reviewComments(null);
                        } catch (OnErrorFailedException ex) {
                            // Log the exception
                            ex.printStackTrace();
                        }

                    }

                    @Override
                    public void onNext(JsonObject mJsonObject) {

                        Log.d("Result", "onNext: " + mJsonObject);

                        if (mFragment instanceof StoreReviewFragment)
                            ((StoreReviewFragment) mFragment).reviewComments(mJsonObject);

                    }
                });
    }

    public void paymentcodPage(String user_id, String ship_name, String ship_email, String ship_phone, String ship_address1, String ship_address2,
                               String ship_country_id, String ship_city_id, String ship_state,
                               String ship_postalcode) {
        ServiceFactory mServiceFactory = new ServiceFactory(BuildConfig.BASE_URL);


        HashMap<String, Object> strMap = new HashMap<>();
        strMap.put("user_id", user_id);
        strMap.put("ship_name", ship_name);
        strMap.put("ship_address1", ship_address1);
        strMap.put("ship_address2", ship_address2);
        strMap.put("ship_email", ship_email);
        strMap.put("ship_phone", ship_phone);
        strMap.put("ship_country_id", ship_country_id);
        strMap.put("ship_city_id", ship_city_id);
        strMap.put("ship_state", ship_state);
        strMap.put("ship_postalcode", ship_postalcode);
        strMap.put("lang", AppConstant.defaultLanguageCode);
        strMap.put("token", AppConstant.token);


        mServiceFactory.getUserService()
                .codOrder(strMap)
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

                            if (mFragment instanceof PaymentContinueFragment)
                                ((PaymentContinueFragment) mFragment).paymentContinue(null);
                        } catch (OnErrorFailedException ex) {
                            // Log the exception
                            ex.printStackTrace();
                        }

                    }

                    @Override
                    public void onNext(JsonObject mJsonObject) {

                        Log.d("Result", "onNext: " + mJsonObject);

                        if (mFragment instanceof PaymentContinueFragment)
                            ((PaymentContinueFragment) mFragment).paymentContinue(mJsonObject);

                    }
                });
    }

    //next tast
    public void paymentbuynowPage(String user_id, String product_id, String product_size_id,
                                  String product_color_id, String product_qty, String ship_name, String ship_email, String ship_phone, String ship_address1, String ship_address2,
                                  String ship_country_id, String ship_city_id, String ship_state,
                                  String ship_postalcode) {
        ServiceFactory mServiceFactory = new ServiceFactory(BuildConfig.BASE_URL);


        System.out.println(" product_id  " + product_id);
        HashMap<String, Object> strMap = new HashMap<>();
        strMap.put("user_id", user_id);
        strMap.put("product_id", product_id);
        strMap.put("product_size_id", product_size_id);
        strMap.put("product_color_id", product_color_id);
        strMap.put("product_qty", product_qty);
        strMap.put("ship_name", ship_name);
        strMap.put("ship_address1", ship_address1);
        strMap.put("ship_address2", ship_address2);
        strMap.put("ship_email", ship_email);
        strMap.put("ship_phone", ship_phone);
        strMap.put("ship_country_id", ship_country_id);
        strMap.put("ship_city_id", ship_city_id);
        strMap.put("ship_state", ship_state);
        strMap.put("ship_postalcode", ship_postalcode);
        strMap.put("lang", AppConstant.defaultLanguageCode);
        strMap.put("token", AppConstant.token);


        mServiceFactory.getUserService()
                .buyNowOrder(strMap)
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

                            if (mFragment instanceof PaymentContinueFragment)
                                ((PaymentContinueFragment) mFragment).paymentContinue(null);
                        } catch (OnErrorFailedException ex) {
                            // Log the exception
                            ex.printStackTrace();
                        }

                    }

                    @Override
                    public void onNext(JsonObject mJsonObject) {

                        Log.d("Result", "onNext: " + mJsonObject);

                        if (mFragment instanceof PaymentContinueFragment)
                            ((PaymentContinueFragment) mFragment).paymentContinue(mJsonObject);

                    }
                });
    }

    public void dealsbuynowPage(String user_id, String deal_id, String deal_qty,
                                String ship_name, String ship_email, String ship_phone, String ship_address1, String ship_address2,
                                String ship_country_id, String ship_city_id, String ship_state,
                                String ship_postalcode) {
        ServiceFactory mServiceFactory = new ServiceFactory(BuildConfig.BASE_URL);


        HashMap<String, Object> strMap = new HashMap<>();
        strMap.put("user_id", user_id);
        strMap.put("deal_id", deal_id);
        strMap.put("deal_qty", deal_qty);
        strMap.put("ship_name", ship_name);
        strMap.put("ship_address1", ship_address1);
        strMap.put("ship_address2", ship_address2);
        strMap.put("ship_email", ship_email);
        strMap.put("ship_phone", ship_phone);
        strMap.put("ship_country_id", ship_country_id);
        strMap.put("ship_city_id", ship_city_id);
        strMap.put("ship_state", ship_state);
        strMap.put("ship_postalcode", ship_postalcode);
        strMap.put("lang", AppConstant.defaultLanguageCode);
        strMap.put("token", AppConstant.token);


        mServiceFactory.getUserService()
                .dealsbuyNowOrder(strMap)
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

                            if (mFragment instanceof PaymentContinueFragment)
                                ((PaymentContinueFragment) mFragment).paymentContinue(null);
                        } catch (OnErrorFailedException ex) {
                            // Log the exception
                            ex.printStackTrace();
                        }

                    }

                    @Override
                    public void onNext(JsonObject mJsonObject) {

                        Log.d("Result", "onNext: " + mJsonObject);

                        if (mFragment instanceof PaymentContinueFragment)
                            ((PaymentContinueFragment) mFragment).paymentContinue(mJsonObject);

                    }
                });
    }

    public void paymentPaypalcodPage(String user_id, String ship_name, String ship_email, String ship_phone, String ship_address1, String ship_address2,
                                     String ship_country_id, String ship_city_id, String ship_state,
                                     String ship_postalcode, String transaction_id,
                                     String token_id, String payer_email, String payer_id,
                                     String payer_name) {
        ServiceFactory mServiceFactory = new ServiceFactory(BuildConfig.BASE_URL);


        HashMap<String, Object> strMap = new HashMap<>();
        strMap.put("user_id", user_id);
        strMap.put("ship_name", ship_name);
        strMap.put("ship_address1", ship_address1);
        strMap.put("ship_address2", ship_address2);
        strMap.put("ship_email", ship_email);
        strMap.put("ship_phone", ship_phone);
        strMap.put("ship_country_id", ship_country_id);
        strMap.put("ship_city_id", ship_city_id);
        strMap.put("ship_state", ship_state);
        strMap.put("ship_postalcode", ship_postalcode);
        strMap.put("transaction_id", transaction_id);
        strMap.put("token_id", token_id);
        strMap.put("payer_email", payer_email);
        strMap.put("payer_id", payer_id);
        strMap.put("payer_name", payer_name);
        strMap.put("lang", AppConstant.defaultLanguageCode);
        strMap.put("token", AppConstant.token);


        mServiceFactory.getUserService()
                .paypalyCodOrder(strMap)
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

                            if (mFragment instanceof PaymentContinueFragment)
                                ((PaymentContinueFragment) mFragment).paymentContinue(null);
                        } catch (OnErrorFailedException ex) {
                            // Log the exception
                            ex.printStackTrace();
                        }

                    }

                    @Override
                    public void onNext(JsonObject mJsonObject) {

                        Log.d("Result", "onNext: " + mJsonObject);

                        if (mFragment instanceof PaymentContinueFragment)
                            ((PaymentContinueFragment) mFragment).paymentContinue(mJsonObject);

                    }
                });
    }

    public void paymentPaypalbuynowPage(String user_id, String product_id, String product_size_id,
                                        String product_color_id, String product_qty, String ship_name, String ship_email, String ship_phone, String ship_address1, String ship_address2,
                                        String ship_country_id, String ship_city_id, String ship_state,
                                        String ship_postalcode, String transaction_id,
                                        String token_id, String payer_email, String payer_id,
                                        String payer_name) {
        ServiceFactory mServiceFactory = new ServiceFactory(BuildConfig.BASE_URL);


        System.out.println(" product_id  " + product_id);
        HashMap<String, Object> strMap = new HashMap<>();
        strMap.put("user_id", user_id);
        strMap.put("product_id", product_id);
        strMap.put("product_size_id", product_size_id);
        strMap.put("product_color_id", product_color_id);
        strMap.put("product_qty", product_qty);

        strMap.put("ship_name", ship_name);
        strMap.put("ship_address1", ship_address1);
        strMap.put("ship_address2", ship_address2);
        strMap.put("ship_email", ship_email);
        strMap.put("ship_phone", ship_phone);
        strMap.put("ship_country_id", ship_country_id);
        strMap.put("ship_city_id", ship_city_id);
        strMap.put("ship_state", ship_state);
        strMap.put("ship_postalcode", ship_postalcode);
        strMap.put("transaction_id", transaction_id);
        strMap.put("token_id", token_id);
        strMap.put("payer_email", payer_email);
        strMap.put("payer_id", payer_id);
        strMap.put("payer_name", payer_name);
        strMap.put("lang", AppConstant.defaultLanguageCode);
        strMap.put("token", AppConstant.token);


        mServiceFactory.getUserService()
                .payPalBuyNowOrder(strMap)
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

                            if (mFragment instanceof PaymentContinueFragment)
                                ((PaymentContinueFragment) mFragment).paymentContinue(null);
                        } catch (OnErrorFailedException ex) {
                            // Log the exception
                            ex.printStackTrace();
                        }

                    }

                    @Override
                    public void onNext(JsonObject mJsonObject) {

                        Log.d("Result", "onNext: " + mJsonObject);

                        if (mFragment instanceof PaymentContinueFragment)
                            ((PaymentContinueFragment) mFragment).paymentContinue(mJsonObject);

                    }
                });
    }

    public void dealsPayPalbuynowPage(String user_id, String deal_id, String deal_qty,
                                      String ship_name, String ship_email, String ship_phone, String ship_address1, String ship_address2,
                                      String ship_country_id, String ship_city_id, String ship_state,
                                      String ship_postalcode, String transaction_id,
                                      String token_id, String payer_email, String payer_id,
                                      String payer_name) {

        ServiceFactory mServiceFactory = new ServiceFactory(BuildConfig.BASE_URL);

        HashMap<String, Object> strMap = new HashMap<>();
        strMap.put("user_id", user_id);
        strMap.put("deal_id", deal_id);
        strMap.put("deal_qty", deal_qty);
        strMap.put("ship_name", ship_name);
        strMap.put("ship_address1", ship_address1);
        strMap.put("ship_address2", ship_address2);
        strMap.put("ship_email", ship_email);
        strMap.put("ship_phone", ship_phone);
        strMap.put("ship_country_id", ship_country_id);
        strMap.put("ship_city_id", ship_city_id);
        strMap.put("ship_state", ship_state);
        strMap.put("ship_postalcode", ship_postalcode);
        strMap.put("transaction_id", transaction_id);
        strMap.put("token_id", token_id);
        strMap.put("payer_email", payer_email);
        strMap.put("payer_id", payer_id);
        strMap.put("payer_name", payer_name);
        strMap.put("lang", AppConstant.defaultLanguageCode);
        strMap.put("token", AppConstant.token);


        mServiceFactory.getUserService()
                .payPalDealsbuyNowOrder(strMap)
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

                            if (mFragment instanceof PaymentContinueFragment)
                                ((PaymentContinueFragment) mFragment).paymentContinue(null);
                        } catch (OnErrorFailedException ex) {
                            // Log the exception
                            ex.printStackTrace();
                        }

                    }

                    @Override
                    public void onNext(JsonObject mJsonObject) {

                        Log.d("Result", "onNext: " + mJsonObject);

                        if (mFragment instanceof PaymentContinueFragment)
                            ((PaymentContinueFragment) mFragment).paymentContinue(mJsonObject);

                    }
                });
    }


    public void getcategoriesList(String shopid) {
        ServiceFactory mServiceFactory = new ServiceFactory(BuildConfig.BASE_URL);

        HashMap<String, Object> strMap = new HashMap<>();
        strMap.put("lang", AppConstant.defaultLanguageCode);
        strMap.put("token", AppConstant.token);


        mServiceFactory.getUserService()
                .getcateriesList(strMap)
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

                            if (mContext instanceof MainActivity)
                                ((MainActivity) mContext).getcategoryList(null);
                        } catch (OnErrorFailedException ex) {
                            // Log the exception
                            ex.printStackTrace();
                        }

                    }

                    @Override
                    public void onNext(JsonObject mJsonObject) {

                        Log.d("Result", "onNext: " + mJsonObject);
//                        category_list
                        SessionSave.saveSession("category_list", "" + mJsonObject, mContext);
                        if (mContext instanceof MainActivity)
                            ((MainActivity) mContext).getcategoryList(mJsonObject);

                    }
                });
    }

    public void getCurrencyValues(String Convert_from_Currency_Code, String amount) {
        ServiceFactory mServiceFactory = new ServiceFactory(BuildConfig.BASE_URL);

        HashMap<String, Object> strMap = new HashMap<>();
        strMap.put("Convert_from_Currency_Code", Convert_from_Currency_Code);
        strMap.put("amount", amount);
        strMap.put("token", AppConstant.token);


        mServiceFactory.getUserService()
                .getcurrencyValues(strMap)
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

                            if (mFragment instanceof PaymentContinueFragment)
                                ((PaymentContinueFragment) mFragment).getcurrencyValues(null);
                        } catch (OnErrorFailedException ex) {
                            // Log the exception
                            ex.printStackTrace();
                        }

                    }

                    @Override
                    public void onNext(JsonObject mJsonObject) {

                        Log.d("Result", "onNext: " + mJsonObject);
//                        category_list
                        if (mFragment instanceof PaymentContinueFragment)
                            ((PaymentContinueFragment) mFragment).getcurrencyValues(mJsonObject);

                    }
                });
    }

}
