package com.saveup.services;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import rx.Observable;

/**
 * @author AAPBD on 24,January,2017
 * @version 5.0
 * @Project TravelSwitch
 * @Package com.aapbd.model
 */


public interface UserService {

    @POST("/api/home_page")
    Observable<JsonObject> homePageApi(@Body HashMap<String, Object> body);

    @POST("/api/user_login")
    Observable<JsonObject> userLogin(@Body HashMap<String, Object> body);

    @POST("/api/registration")
    Observable<JsonObject> userRegisteration(@Body HashMap<String, Object> body);

    @POST("/api/forgot_password")
    Observable<JsonObject> userForgotPassword(@Body HashMap<String, Object> body);

    @POST("/api/login_user_forgot_password")
    Observable<JsonObject> userResetPassword(@Body HashMap<String, Object> body);

    @POST("/api/facebook_login")
    Observable<JsonObject> userFacebookLogin(@Body HashMap<String, Object> body);

    @POST("/api/google_login")
    Observable<JsonObject> userGoogleLogin(@Body HashMap<String, Object> body);

    @POST("/api/product_list_by_category")
    Observable<JsonObject> productListbyCategory(@Body HashMap<String, Object> body);

    @POST("/api/product_search_by_filter")
    Observable<JsonObject> productListPage(@Body HashMap<String, Object> body);

    //saveup custom home page product list

    //latest product list api
    @POST("/api/latest_product_all")
    Observable<JsonObject> latestProductListPage(@Body HashMap<String, Object> body);

    //hot deals product list api
    @POST("/api/hot_product_all")
    Observable<JsonObject> hotDealsProductListPage(@Body HashMap<String, Object> body);

    //gift product list api
    @POST("/api/gift_product_all")
    Observable<JsonObject> giftProductListPage(@Body HashMap<String, Object> body);

    //saveup remove wishlist
    @POST("/api/remove_wishlist")
    Observable<JsonObject> removeWishlist(@Body HashMap<String, Object> body);

    //saveup product filter
    @POST("/api/filter")
    Observable<JsonObject> filterProductList(@Body HashMap<String, Object> body);

    //saveup product by category
    @POST("/api/product_by_category")
    Observable<JsonObject> productByCategory(@Body HashMap<String, Object> body);

    //filter merchant
    @POST("/api/categoy_merchant")
    Observable<JsonObject> filterMerchant(@Body HashMap<String, Object> body);

    //apply promo code
    @POST("/api/product_promo")
    Observable<JsonObject> applyPromoCode(@Body HashMap<String, Object> body);

    @POST("/api/product_detail_page")
    Observable<JsonObject> productDetailsPage(@Body HashMap<String, Object> body);

    @POST("/api/deal_detail")
    Observable<JsonObject> dealsDetailsPage(@Body HashMap<String, Object> body);

    @POST("/api/add_to_cart")
    Observable<JsonObject> addCartPage(@Body HashMap<String, Object> body);

    @POST("/api/update_product_cart")
    Observable<JsonObject> updateCartPage(@Body HashMap<String, Object> body);

    @POST("/api/order_by_otp")
    Observable<JsonObject> orderByOTP(@Body HashMap<String, Object> body);

    @POST("/api/order_otp_verified")
    Observable<JsonObject> orderVerifyByOTP(@Body HashMap<String, Object> body);

    @POST("/api/recharge-by-loyalty")
    Observable<JsonObject> redeem(@Body HashMap<String, Object> body);

    @POST("/api/deals_list")
    Observable<JsonObject> dealsListPage(@Body HashMap<String, Object> body);

    @POST("/api/add_to_dealcart")
    Observable<JsonObject> addDealsCartPage(@Body HashMap<String, Object> body);

    @POST("/api/cart_list")
    Observable<JsonObject> cartListPage(@Body HashMap<String, Object> body);

    @POST("/api/add_to_wishlist")
    Observable<JsonObject> addWishListPage(@Body HashMap<String, Object> body);

    @POST("/api/remove_productcart")
    Observable<JsonObject> removeFromCartPage(@Body HashMap<String, Object> body);

    @POST("/api/my_account")
    Observable<JsonObject> getProfilePage(@Body HashMap<String, Object> body);

    @POST("/api/my_wishlist")
    Observable<JsonObject> wishListPage(@Body HashMap<String, Object> body);

    @POST("/api/reset_password")
    Observable<JsonObject> resetPassword(@Body HashMap<String, Object> body);

    @POST("/api/shop_list")
    Observable<JsonObject> shopList(@Body HashMap<String, Object> body);

    @POST("/api/update_product_cart")
    Observable<JsonObject> updateFromCartPage(@Body HashMap<String, Object> body);

    @Multipart
    @POST("/api/update_my_account")
    Observable<JsonObject> editNoImageProfilePage(@PartMap() HashMap<String, RequestBody> body);

    @Multipart
    @POST("/api/update_my_account")
    Observable<JsonObject> editProfilePage(@PartMap() Map<String, RequestBody> attachments, @Part MultipartBody.Part file);


    @POST("/api/my_orders")
    Observable<JsonObject> orderList(@Body HashMap<String, Object> body);

    @POST("/api/orders_product_list")
    Observable<JsonObject> orderListDetails(@Body HashMap<String, Object> body);

    @POST("/api/redeem_by_user")
    Observable<JsonObject> redeemCouponByUser(@Body HashMap<String, Object> body);

//    @GET("/api/near_by_shop_list")
//    Observable<JsonObject> nearByStore();
    @POST("/api/nearmemap_search")
    Observable<JsonObject> nearByStore(@Body HashMap<String, Object> body);


    @POST("/api/shop_detail_by_id")
    Observable<JsonObject> storedetailsPage(@Body HashMap<String, Object> body);


    @POST("/api/product_write_review")
    Observable<JsonObject> productReview(@Body HashMap<String, Object> body);


    @POST("/api/deal_write_review")
    Observable<JsonObject> dealsReview(@Body HashMap<String, Object> body);


    @POST("/api/store_write_review")
    Observable<JsonObject> storeReview(@Body HashMap<String, Object> body);


    @POST("/api/product_cod_order")
    Observable<JsonObject> codOrder(@Body HashMap<String, Object> body);

    @POST("/api/product_cod_order_buy_now")
    Observable<JsonObject> buyNowOrder(@Body HashMap<String, Object> body);

    @POST("/api/deal_order_buy_now")
    Observable<JsonObject> dealsbuyNowOrder(@Body HashMap<String, Object> body);


    @POST("/api/product_paypal_order_success")
    Observable<JsonObject> paypalyCodOrder(@Body HashMap<String, Object> body);


    @POST("/api/product_paypal_order_success_buy_now")
    Observable<JsonObject> payPalBuyNowOrder(@Body HashMap<String, Object> body);

    @POST("/api/deal_paypal_order_success_buy_now")
    Observable<JsonObject> payPalDealsbuyNowOrder(@Body HashMap<String, Object> body);

    @POST("/api/category_list")
    Observable<JsonObject> getcateriesList(@Body HashMap<String, Object> body);

    @POST("/api/get_currency_values")
    Observable<JsonObject> getcurrencyValues(@Body HashMap<String, Object> body);

}
