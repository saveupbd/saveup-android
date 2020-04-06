package com.saveup.model;

/**
 * @author AAPBD on 08,April,2017
 * @version 5.0
 * @Project MultiVendor
 * @Package com.aapbd.model
 */


public class SubCategoryData {

    String sub_category_id;
    String sub_category_Name;
    String sub_category_image;

    public String getSub_category_id() {
        return sub_category_id;
    }

    public void setSub_category_id(String sub_category_id) {
        this.sub_category_id = sub_category_id;
    }

    public String getSub_category_Name() {
        return sub_category_Name;
    }

    public void setSub_category_Name(String sub_category_Name) {
        this.sub_category_Name = sub_category_Name;
    }

    public String getSub_category_image() {
        return sub_category_image;
    }

    public void setSub_category_image(String sub_category_image) {
        this.sub_category_image = sub_category_image;
    }



    public SubCategoryData(String _id, String _name , String _image){

        this.sub_category_id = _id;
        this.sub_category_Name = _name;
        this.sub_category_image = _image;
    }
}
