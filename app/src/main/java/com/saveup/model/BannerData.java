package com.saveup.model;

/**
 * @author AAPBD on 11,March,2017
 * @version 5.0
 * @Project MultiVendor
 * @Package com.aapbd.fragment
 */


public class BannerData {

    String banner_id, banner_title, banner_redirect_url, banner_image;

    public String getBanner_id() {
        return banner_id;
    }

    public void setBanner_id(String banner_id) {
        this.banner_id = banner_id;
    }

    public String getBanner_title() {
        return banner_title;
    }

    public void setBanner_title(String banner_title) {
        this.banner_title = banner_title;
    }

    public String getBanner_redirect_url() {
        return banner_redirect_url;
    }

    public void setBanner_redirect_url(String banner_redirect_url) {
        this.banner_redirect_url = banner_redirect_url;
    }

    public String getBanner_image() {
        return banner_image;
    }

    public void setBanner_image(String banner_image) {
        this.banner_image = banner_image;
    }

    public  BannerData(String _id, String _title, String _redirect_url, String _image){

        this.banner_id =_id;
        this.banner_title = _title;
        this.banner_redirect_url = _redirect_url;
        this.banner_image = _image;
    }


}
