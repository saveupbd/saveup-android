package com.saveup.model;

/**
 * Created by AAPBD on 12/4/17.
 */

public class AdsData {

    String ads_id;

    public String getAds_image() {
        return ads_image;
    }

    public void setAds_image(String ads_image) {
        this.ads_image = ads_image;
    }

    public String getAds_id() {
        return ads_id;
    }

    public void setAds_id(String ads_id) {
        this.ads_id = ads_id;
    }

    String ads_image;


    public AdsData(String _id, String _image) {

        this.ads_id = _id;
        this.ads_image = _image;
    }
}
