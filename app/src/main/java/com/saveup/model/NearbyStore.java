package com.saveup.model;

/**
 * Created by AAPBD on 3/5/17.
 */

public class NearbyStore {




    String store_id, store_name, store_img, store_status, store_latitude,store_longitude,product_count, deal_count,store_city_id;

    public String getStore_city_id() {
        return store_city_id;
    }

    public void setStore_city_id(String store_city_id) {
        this.store_city_id = store_city_id;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getStore_img() {
        return store_img;
    }

    public void setStore_img(String store_img) {
        this.store_img = store_img;
    }

    public String getStore_status() {
        return store_status;
    }

    public void setStore_status(String store_status) {
        this.store_status = store_status;
    }

    public String getStore_latitude() {
        return store_latitude;
    }

    public void setStore_latitude(String store_latitude) {
        this.store_latitude = store_latitude;
    }

    public String getStore_longitude() {
        return store_longitude;
    }

    public void setStore_longitude(String store_longitude) {
        this.store_longitude = store_longitude;
    }

    public String getProduct_count() {
        return product_count;
    }

    public void setProduct_count(String product_count) {
        this.product_count = product_count;
    }

    public String getDeal_count() {
        return deal_count;
    }

    public void setDeal_count(String deal_count) {
        this.deal_count = deal_count;
    }

    public  NearbyStore (String _store_id, String _store_name, String _store_img, String _store_status, String _store_latitude, String _store_longitude, String _product_count, String _deal_count, String store_city_id){

        this.store_id =_store_id;
        this.store_name =_store_name;
        this.store_img= _store_img;
        this.store_status=_store_status;
        this.store_latitude=_store_latitude;
        this.store_longitude=_store_longitude;
        this.product_count = _product_count;
        this.deal_count =_deal_count;
        this.store_city_id= store_city_id;

    }

    @Override
    public String toString() {
        return "NearbyStore{" +
                "store_id='" + store_id + '\'' +
                ", store_name='" + store_name + '\'' +
                ", store_img='" + store_img + '\'' +
                ", store_status='" + store_status + '\'' +
                ", store_latitude='" + store_latitude + '\'' +
                ", store_longitude='" + store_longitude + '\'' +
                ", product_count='" + product_count + '\'' +
                ", deal_count='" + deal_count + '\'' +
                ", store_city_id='" + store_city_id + '\'' +
                '}';
    }
}
