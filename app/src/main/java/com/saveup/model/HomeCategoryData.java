package com.saveup.model;

import java.util.ArrayList;

/**
 * @author AAPBD on 11,March,2017
 * @version 5.0
 * @Project MultiVendor
 * @Package com.aapbd.model;
 */


public class HomeCategoryData {

    String category_id, category_Name, category_image;
    ArrayList<SubCategoryData> mSubCategoryData;

    public ArrayList<SubCategoryData> getmSubCategoryData() {
        return mSubCategoryData;
    }

    public void setmSubCategoryData(ArrayList<SubCategoryData> mSubCategoryData) {
        this.mSubCategoryData = mSubCategoryData;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCategory_Name() {
        return category_Name;
    }

    public void setCategory_Name(String category_Name) {
        this.category_Name = category_Name;
    }

    public String getCategory_image() {
        return category_image;
    }

    public void setCategory_image(String category_image) {
        this.category_image = category_image;
    }

    public HomeCategoryData(String _id, String _name , String _image,ArrayList<SubCategoryData> mSubCategory ){

        this.category_id = _id;
        this.category_Name = _name;
        this.category_image = _image;
        this.mSubCategoryData =mSubCategory;
    }
}
