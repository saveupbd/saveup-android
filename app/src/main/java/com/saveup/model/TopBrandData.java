package com.saveup.model;

import java.util.ArrayList;

/**
 * Created by  AAPBD on 12/4/17.
 */

public class TopBrandData {

    public String getSub_category_id() {
        return sub_category_id;
    }

    public void setSub_category_id(String sub_category_id) {
        this.sub_category_id = sub_category_id;
    }

    public String getSub_category_name() {
        return sub_category_name;
    }

    public void setSub_category_name(String sub_category_name) {
        this.sub_category_name = sub_category_name;
    }

    public String getSub_category_image() {
        return sub_category_image;
    }

    public void setSub_category_image(String sub_category_image) {
        this.sub_category_image = sub_category_image;
    }

    public String getSub_main_category_id() {
        return sub_main_category_id;
    }

    public void setSub_main_category_id(String sub_main_category_id) {
        this.sub_main_category_id = sub_main_category_id;
    }

    public String getSec_sub_main_category_id() {
        return sec_sub_main_category_id;
    }

    public void setSec_sub_main_category_id(String sec_sub_main_category_id) {
        this.sec_sub_main_category_id = sec_sub_main_category_id;
    }

    String sub_category_id;
    String sub_category_name;
    String sub_category_image;
    String sub_main_category_id;
    String sec_sub_main_category_id;

    public ArrayList<String> getSub_sec_category_id() {
        return sub_sec_category_id;
    }

    public void setSub_sec_category_id(ArrayList<String> sub_sec_category_id) {
        this.sub_sec_category_id = sub_sec_category_id;
    }

    ArrayList<String> sub_sec_category_id;
    public  TopBrandData ( String _id, String _sub_category_name, String _sub_category_image, String _sub_main_category_id, String _sec_sub_main_category_id, ArrayList<String>
                           _sub_sec_category_id){

        this.sub_category_id =_id;
        this.sub_category_name =_sub_category_name;
        this.sub_category_image= _sub_category_image;
        this.sub_main_category_id=_sub_main_category_id;
        this.sec_sub_main_category_id = _sec_sub_main_category_id ;
        this.sub_sec_category_id =_sub_sec_category_id;
    }
}
