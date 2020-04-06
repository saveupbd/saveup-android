package com.saveup.model;

/**
 * Created by AAPBD on 15/6/17.
 */

public class StoreCityData {


    String city_id,city_name;

    public  StoreCityData(String _id, String _name){

        this.city_id =_id;
        this.city_name = _name;
    }

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
}