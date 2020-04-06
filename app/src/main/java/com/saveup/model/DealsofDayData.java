package com.saveup.model;

/**
 * @author AAPBD on 11,March,2017
 * @version 5.0
 * @Project MultiVendor
 * @Package com.aapbd.model
 */


public class DealsofDayData {

    String product_id;
    String product_title;

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

    public String getProduct_type() {
        return product_type;
    }

    String product_price;
    String product_discount_price;
    String product_percentage;
    String product_image;
    String product_type;


    public  DealsofDayData ( String _id, String _title, String _price, String _discount_price, String _percentage, String product_type, String _image){

        this.product_id =_id;
        this.product_title =_title;
        this.product_price= _price;
        this.product_discount_price=_discount_price;
        this.product_percentage = _percentage;
        this.product_type = product_type;
        this.product_image =_image;
    }
}
