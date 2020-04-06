package com.saveup.model;

/**
 * @author AAPBD on 16,April,2017
 * @version 5.0
 * @Project MultiVendor
 * @Package com.aapbd.fragment
 */


public class CartData {

    String cart_id, cart_product_id, cart_user_id, cart_title, cart_quantity, cart_image, cart_price, cart_total;
    String product_purchase_qty, product_available_qty, cart_shipping_price, product_color_id, product_size_id, cart_tax_price;

    public String getProduct_color_id() {
        return product_color_id;
    }

    public void setProduct_color_id(String product_color_id) {
        this.product_color_id = product_color_id;
    }

    public String getProduct_size_id() {
        return product_size_id;
    }

    public void setProduct_size_id(String product_size_id) {
        this.product_size_id = product_size_id;
    }

    public String getProduct_purchase_qty() {
        return product_purchase_qty;
    }

    public void setProduct_purchase_qty(String product_purchase_qty) {
        this.product_purchase_qty = product_purchase_qty;
    }

    public String getProduct_available_qty() {
        return product_available_qty;
    }

    public void setProduct_available_qty(String product_available_qty) {
        this.product_available_qty = product_available_qty;
    }

    public String getCart_shipping_price() {
        return cart_shipping_price;
    }

    public void setCart_shipping_price(String cart_shipping_price) {
        this.cart_shipping_price = cart_shipping_price;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public String getCart_product_id() {
        return cart_product_id;
    }

    public void setCart_product_id(String cart_product_id) {
        this.cart_product_id = cart_product_id;
    }

    public String getCart_user_id() {
        return cart_user_id;
    }

    public void setCart_user_id(String cart_user_id) {
        this.cart_user_id = cart_user_id;
    }

    public String getCart_title() {
        return cart_title;
    }

    public void setCart_title(String cart_title) {
        this.cart_title = cart_title;
    }

    public String getCart_quantity() {
        return cart_quantity;
    }

    public void setCart_quantity(String cart_quantity) {
        this.cart_quantity = cart_quantity;
    }

    public String getCart_image() {
        return cart_image;
    }

    public void setCart_image(String cart_image) {
        this.cart_image = cart_image;
    }

    public String getCart_price() {
        return cart_price;
    }

    public void setCart_price(String cart_price) {
        this.cart_price = cart_price;
    }

    public String getCart_total() {
        return cart_total;
    }

    public void setCart_total(String cart_total) {
        this.cart_total = cart_total;
    }

    public String getCart_tax_price() {
        return cart_tax_price;
    }

    public void setCart_tax_price(String cart_tax_price) {
        this.cart_tax_price = cart_tax_price;
    }

    public CartData(String _cart_id, String _cart_product_id, String _cart_user_id, String _cart_title, String _cart_quantity, String _cart_image, String _cart_price, String _cart_total,
                    String _product_purchase_qty, String _product_available_qty, String _cart_shipping_price, String _product_size_id, String _product_color_id, String _cart_tax_price) {

        this.cart_id = _cart_id;
        this.cart_product_id = _cart_product_id;
        this.cart_user_id = _cart_user_id;
        this.cart_title = _cart_title;
        this.cart_quantity = _cart_quantity;
        this.cart_image = _cart_image;
        this.cart_price = _cart_price;
        this.cart_total = _cart_total;
        this.product_purchase_qty = _product_purchase_qty;
        this.product_available_qty = _product_available_qty;
        this.cart_shipping_price = _cart_shipping_price;
        this.product_size_id = _product_size_id;
        this.product_color_id = _product_color_id;
        this.cart_tax_price = _cart_tax_price;


    }
}
