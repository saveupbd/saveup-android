package com.saveup.model;

/**
 * @author AAPBD on 12,March,2017
 * @version 5.0
 * @Project MultiVendor
 * @Package com.aapbd.model
 */


public class CityListData {


    String city_id,city_name,city_country_id;

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getCity_country_id() {
        return city_country_id;
    }

    public void setCity_country_id(String city_country_id) {
        this.city_country_id = city_country_id;
    }

    public  CityListData(String _id, String _name, String _country_id){

        this.city_id =_id;
        this.city_name = _name;
        this.city_country_id =_country_id;
    }
}
