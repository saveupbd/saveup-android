package com.saveup.model;

/**
 * @author AAPBD on 30,April,2017
 * @version 5.0
 * @Project MultiVendor
 * @Package com.aapbd.model;
 */


public class OrderData {

    String order_id;
    String user_id;
    String shipping_name;
    String order_subtotal;
    String order_total;
    String coupon_code;
    String order_date;
    String expire_day;

    long orderDBID = -1;

    String offLineRedeemTime;


    public long getOrderDBID() {
        return orderDBID;
    }

    public void setOrderDBID(long orderDBID) {
        this.orderDBID = orderDBID;
    }

    public OrderData(String order_id, String user_id, String shipping_name, String order_total, String order_date, String expire_day,String coupon) {
        this.order_id = order_id;
        this.user_id = user_id;
        this.shipping_name = shipping_name;
        this.order_total = order_total;
        this.order_date = order_date;
        this.expire_day = expire_day;
        this.coupon_code = coupon;
    }

    public OrderData() {

    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getShipping_name() {
        return shipping_name;
    }

    public void setShipping_name(String shipping_name) {
        this.shipping_name = shipping_name;
    }

    public String getOrder_total() {
        return order_total;
    }

    public void setOrder_total(String order_total) {
        this.order_total = order_total;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getOrder_subtotal() {
        return order_subtotal;
    }

    public void setOrder_subtotal(String order_subtotal) {
        this.order_subtotal = order_subtotal;
    }

    public String getCoupon_code() {
        return coupon_code;
    }

    public void setCoupon_code(String coupon_code) {
        this.coupon_code = coupon_code;
    }

    public String getExpire_day() {
        return expire_day;
    }

    public void setExpire_day(String expire_day) {
        this.expire_day = expire_day;
    }

    public String getOffLineRedeemTime() {
        return offLineRedeemTime;
    }

    public void setOffLineRedeemTime(String offLineRedeemTime) {
        this.offLineRedeemTime = offLineRedeemTime;
    }
}
