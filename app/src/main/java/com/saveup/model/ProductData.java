package com.saveup.model;

/**
 * @author AAPBD on 15,April,2017
 * @version 5.0
 * @Project MultiVendor
 * @Package com.aapbd.model
 */


public class ProductData {

    String product_id, product_title, product_price, product_discount_price, product_percentage, product_image, pro_no_of_purchase, pro_qty, merchant_name, product_type;
    boolean is_wishlist;

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_title() {
        return product_title;
    }

    public void setProduct_title(String product_title) {
        this.product_title = product_title;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getProduct_discount_price() {
        return product_discount_price;
    }

    public void setProduct_discount_price(String product_discount_price) {
        this.product_discount_price = product_discount_price;
    }

    public String getProduct_percentage() {
        return product_percentage;
    }

    public void setProduct_percentage(String product_percentage) {
        this.product_percentage = product_percentage;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getPro_no_of_purchase() {
        return pro_no_of_purchase;
    }

    public void setPro_no_of_purchase(String pro_no_of_purchase) {
        this.pro_no_of_purchase = pro_no_of_purchase;
    }

    public String getPro_qty() {
        return pro_qty;
    }

    public void setPro_qty(String pro_qty) {
        this.pro_qty = pro_qty;
    }

    public boolean is_wishlist() {
        return is_wishlist;
    }

    public void setIs_wishlist(boolean is_wishlist) {
        this.is_wishlist = is_wishlist;
    }

    public String getMerchant_name() {
        return merchant_name;
    }

    public void setMerchant_name(String merchant_name) {
        this.merchant_name = merchant_name;
    }

    public String getProduct_type() {
        return product_type;
    }

    public ProductData(String _product_id, String _product_title, String _product_price, String _product_discount_price, String _product_percentage,
                       String product_type, String _product_image, String _pro_no_of_purchase, String _pro_qty, boolean _is_wishlist, String _merchant_name) {
        this.product_id = _product_id;
        this.product_title = _product_title;
        this.product_price = _product_price;
        this.product_discount_price = _product_discount_price;
        this.product_percentage = _product_percentage;
        this.product_type = product_type;
        this.product_image = _product_image;
        this.pro_no_of_purchase = _pro_no_of_purchase;
        this.pro_qty = _pro_qty;
        this.is_wishlist = _is_wishlist;
        this.merchant_name = _merchant_name;

    }

}
