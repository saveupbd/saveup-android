package com.saveup.model;

public class OrderDetailsModel {
    private String order_id;
    private String product_id;
    private String product_title;
    private String product_image;
    private String order_qty;
    private String product_currency_symbol;
    private String product_currency_code;
    private String product_price;
    private String product_available_quantity;
    private String product_quantity;

    public OrderDetailsModel(String order_id, String product_id, String product_title, String product_image, String order_qty, String product_currency_symbol, String product_currency_code, String product_price, String product_available_quantity, String product_quantity) {
        this.order_id = order_id;
        this.product_id = product_id;
        this.product_title = product_title;
        this.product_image = product_image;
        this.order_qty = order_qty;
        this.product_currency_symbol = product_currency_symbol;
        this.product_currency_code = product_currency_code;
        this.product_price = product_price;
        this.product_available_quantity = product_available_quantity;
        this.product_quantity = product_quantity;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

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

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getOrder_qty() {
        return order_qty;
    }

    public void setOrder_qty(String order_qty) {
        this.order_qty = order_qty;
    }

    public String getProduct_currency_symbol() {
        return product_currency_symbol;
    }

    public void setProduct_currency_symbol(String product_currency_symbol) {
        this.product_currency_symbol = product_currency_symbol;
    }

    public String getProduct_currency_code() {
        return product_currency_code;
    }

    public void setProduct_currency_code(String product_currency_code) {
        this.product_currency_code = product_currency_code;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getProduct_available_quantity() {
        return product_available_quantity;
    }

    public void setProduct_available_quantity(String product_available_quantity) {
        this.product_available_quantity = product_available_quantity;
    }

    public String getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(String product_quantity) {
        this.product_quantity = product_quantity;
    }
}
