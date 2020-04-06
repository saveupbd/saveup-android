package com.saveup.model;

/**
 * @author AAPBD on 15,April,2017
 * @version 5.0
 * @Project MultiVendor
 * @Package com.aapbd.model
 */


public class ReviewData {

    String review_title,review_comments,ratings,review_date,user_id,user_name,user_img;

    public String getReview_title() {
        return review_title;
    }

    public void setReview_title(String review_title) {
        this.review_title = review_title;
    }

    public String getReview_comments() {
        return review_comments;
    }

    public void setReview_comments(String review_comments) {
        this.review_comments = review_comments;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

    public String getReview_date() {
        return review_date;
    }

    public void setReview_date(String review_date) {
        this.review_date = review_date;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_img() {
        return user_img;
    }

    public void setUser_img(String user_img) {
        this.user_img = user_img;
    }

    public  ReviewData (String _title, String _comments, String _ratings, String _date, String _id, String _name, String _img){

        this.review_title =_title;
        this.review_comments =_comments;
        this.ratings =_ratings;
        this.review_date =_date;
        this.user_id =_id;
        this.user_name =_name;
        this.user_img =_img;

    }
}
