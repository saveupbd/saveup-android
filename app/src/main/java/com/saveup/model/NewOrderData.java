package com.saveup.model;

/**
 * @author AAPBD on 30,April,2017
 * @version 5.0
 * @Project MultiVendor
 * @Package com.aapbd.model;
 */


public class NewOrderData {

    String order_id;
    String user_id;
    String order_total;
    String user_payable_amount;
    String user_savings;
    String dagte;
    String merchant_name;
    String status;

    public NewOrderData(String order_id, String user_id, String order_total, String user_payable_amount, String user_savings, String dagte, String merchant_name, String status) {
        this.order_id = order_id;
        this.user_id = user_id;
        this.order_total = order_total;
        this.user_payable_amount = user_payable_amount;
        this.user_savings = user_savings;
        this.dagte = dagte;
        this.merchant_name = merchant_name;
        this.status = status;
    }

    public NewOrderData() {

    }

    public String getOrder_id() {
        return order_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getOrder_total() {
        return order_total;
    }

    public String getUser_payable_amount() {
        return user_payable_amount;
    }

    public String getUser_savings() {
        return user_savings;
    }

    public String getDagte() {
        return dagte;
    }

    public String getMerchant_name() {
        return merchant_name;
    }

    public String getStatus() {
        return status;
    }
}
