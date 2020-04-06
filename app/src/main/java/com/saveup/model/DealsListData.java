package com.saveup.model;

/**
 * Created by AAPBD on 19/4/17.
 */

public class DealsListData {

    String deal_id,deal_title,deal_original_price,deal_discount_price,deal_discount_percentage,deal_saving_price,deal_currency_code,deal_currency_symbol,
            deal_start_date,deal_end_date ,deal_expiry_date,deal_shop_id,deal_image,deal_activeorexpire_status,deal_status;

    int millisToGo;

    long startTime;
    long endTime;

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getMillisToGo() {
        return millisToGo;
    }

    public void setMillisToGo(int millisToGo) {
        this.millisToGo = millisToGo;
    }

    public String getDeal_id() {
        return deal_id;
    }

    public void setDeal_id(String deal_id) {
        this.deal_id = deal_id;
    }

    public String getDeal_title() {
        return deal_title;
    }

    public void setDeal_title(String deal_title) {
        this.deal_title = deal_title;
    }

    public String getDeal_original_price() {
        return deal_original_price;
    }

    public void setDeal_original_price(String deal_original_price) {
        this.deal_original_price = deal_original_price;
    }

    public String getDeal_discount_price() {
        return deal_discount_price;
    }

    public void setDeal_discount_price(String deal_discount_price) {
        this.deal_discount_price = deal_discount_price;
    }

    public String getDeal_discount_percentage() {
        return deal_discount_percentage;
    }

    public void setDeal_discount_percentage(String deal_discount_percentage) {
        this.deal_discount_percentage = deal_discount_percentage;
    }

    public String getDeal_saving_price() {
        return deal_saving_price;
    }

    public void setDeal_saving_price(String deal_saving_price) {
        this.deal_saving_price = deal_saving_price;
    }

    public String getDeal_currency_code() {
        return deal_currency_code;
    }

    public void setDeal_currency_code(String deal_currency_code) {
        this.deal_currency_code = deal_currency_code;
    }

    public String getDeal_currency_symbol() {
        return deal_currency_symbol;
    }

    public void setDeal_currency_symbol(String deal_currency_symbol) {
        this.deal_currency_symbol = deal_currency_symbol;
    }

    public String getDeal_start_date() {
        return deal_start_date;
    }

    public void setDeal_start_date(String deal_start_date) {
        this.deal_start_date = deal_start_date;
    }

    public String getDeal_end_date() {
        return deal_end_date;
    }

    public void setDeal_end_date(String deal_end_date) {
        this.deal_end_date = deal_end_date;
    }

    public String getDeal_expiry_date() {
        return deal_expiry_date;
    }

    public void setDeal_expiry_date(String deal_expiry_date) {
        this.deal_expiry_date = deal_expiry_date;
    }

    public String getDeal_shop_id() {
        return deal_shop_id;
    }

    public void setDeal_shop_id(String deal_shop_id) {
        this.deal_shop_id = deal_shop_id;
    }

    public String getDeal_image() {
        return deal_image;
    }

    public void setDeal_image(String deal_image) {
        this.deal_image = deal_image;
    }

    public String getDeal_activeorexpire_status() {
        return deal_activeorexpire_status;
    }

    public void setDeal_activeorexpire_status(String deal_activeorexpire_status) {
        this.deal_activeorexpire_status = deal_activeorexpire_status;
    }

    public String getDeal_status() {
        return deal_status;
    }

    public void setDeal_status(String deal_status) {
        this.deal_status = deal_status;
    }

    public  DealsListData(String _deal_id, String _deal_title, String _deal_original_price, String _deal_discount_price, String _deal_discount_percentage, String _deal_saving_price, String _deal_currency_code, String _deal_currency_symbol,
                          String _deal_start_date, String _deal_end_date , String _deal_expiry_date, String _deal_shop_id, String _deal_image, String _deal_activeorexpire_status, String _deal_status, long startTime, long endTime){
        this.deal_id = _deal_id;
        this.deal_title = _deal_title;
        this.deal_original_price = _deal_original_price;
        this.deal_discount_price = _deal_discount_price;
        this.deal_discount_percentage = _deal_discount_percentage;
        this.deal_saving_price = _deal_saving_price;
        this.deal_currency_code = _deal_currency_code;
        this.deal_currency_symbol = _deal_currency_symbol;
        this.deal_start_date = _deal_start_date;

        this.deal_end_date = _deal_end_date;
        this.deal_expiry_date = _deal_expiry_date;
        this.deal_shop_id = _deal_shop_id;
        this.deal_image = _deal_image;
        this.deal_activeorexpire_status = _deal_activeorexpire_status;
        this.deal_status = _deal_status;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
