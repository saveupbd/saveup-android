package com.saveup.model;

import java.util.ArrayList;

/**
 * @author AAPBD on 12,March,2017
 * @version 5.0
 * @Project MultiVendor
 * @Package com.aapbd.model
 */


public class CountryListData {

    String country_id,country_name;

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public ArrayList<CityListData> getCityListDataArrayList() {
        return cityListDataArrayList;
    }

    public void setCityListDataArrayList(ArrayList<CityListData> cityListDataArrayList) {
        this.cityListDataArrayList = cityListDataArrayList;
    }

    ArrayList<CityListData> cityListDataArrayList;

   public  CountryListData (String _ic, String _name, ArrayList<CityListData> data){

       this.country_id = _ic;
       this.country_name = _name;
       this.cityListDataArrayList = data;
   }
}
