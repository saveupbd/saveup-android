package com.saveup.model;

/**
 * @author AAPBD on 16,April,2017
 * @version 5.0
 * @Project MultiVendor
 * @Package com.aapbd.model
 */


public class SizeData {

    String size_id,size_name,product_size_id, backgroundcolor;

    public String getBackgroundcolor() {
        return backgroundcolor;
    }

    public void setBackgroundcolor(String backgroundcolor) {
        this.backgroundcolor = backgroundcolor;
    }

    public String getSize_id() {
        return size_id;
    }

    public void setSize_id(String size_id) {
        this.size_id = size_id;
    }

    public String getSize_name() {
        return size_name;
    }

    public void setSize_name(String size_name) {
        this.size_name = size_name;
    }

    public String getProduct_size_id() {
        return product_size_id;
    }

    public void setProduct_size_id(String product_size_id) {
        this.product_size_id = product_size_id;
    }

    public  SizeData (String _size_id, String _size_name, String _product_size_id, String _backgroundcolor){

        this.size_id = _size_id;
        this.size_name =_size_name;
        this.product_size_id= _product_size_id;
        this.backgroundcolor = _backgroundcolor;
    }
}
