package com.saveup.model;

/**
 * @author AAPBD on 18,April,2017
 * @version 5.0
 * @Project MultiVendor
 * @Package com.aapbd.model
 */


public class FilterSortData {

    String filter_name , filter_id, color_name;

    public String getColor_name() {
        return color_name;
    }

    public void setColor_name(String color_name) {
        this.color_name = color_name;
    }

    public String getFilter_id() {
        return filter_id;
    }

    public void setFilter_id(String filter_id) {
        this.filter_id = filter_id;
    }

    public String getFilter_name() {
        return filter_name;
    }

    public void setFilter_name(String filter_name) {
        this.filter_name = filter_name;
    }

    public FilterSortData(String _name, String _id, String color){

        this.filter_name = _name;
        this.filter_id = _id;
        this.color_name = color;
    }
}
