package com.saveup.model;

/**
 * Created by AAPBD on 12/4/17.
 */

public class SubBannerData {

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

    public  SubBannerData(String _id,  String _image){

        this.banner_id =_id;

        this.banner_image = _image;
    }

}
