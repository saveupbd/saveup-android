package com.saveup.model;

/**
 * @author AAPBD on 16,April,2017
 * @version 5.0
 * @Project MultiVendor
 * @Package com.aapbd.model
 */


public class ColorData {

    String color_id;
    String color_name;
    String color_code;
    String product_color_id;

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    String backgroundColor;

    public String getColor_id() {
        return color_id;
    }

    public void setColor_id(String color_id) {
        this.color_id = color_id;
    }

    public String getColor_name() {
        return color_name;
    }

    public void setColor_name(String color_name) {
        this.color_name = color_name;
    }

    public String getColor_code() {
        return color_code;
    }

    public void setColor_code(String color_code) {
        this.color_code = color_code;
    }

    public String getProduct_color_id() {
        return product_color_id;
    }

    public void setProduct_color_id(String product_color_id) {
        this.product_color_id = product_color_id;
    }

    public ColorData (String _color_id, String _color_name, String _color_code, String _product_color_id, String _backgroundcolor){

                this.color_id =_color_id;
                this.color_name =_color_name;
                this.color_code=_color_code;
                this.product_color_id =_product_color_id;
                this.backgroundColor = _backgroundcolor;

            }
}
