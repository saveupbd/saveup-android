package com.saveup.model;

import java.util.ArrayList;

/**
 * develped by appbd
 * first level item
 */
public class Product {

    private String pName, pid;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    private ArrayList<SubCategory> mSubCategoryList;


    public Product(String pid, String pName, ArrayList<SubCategory> mSubCategoryList) {
        super();
        this.pName = pName;
        this.pid = pid;
        this.mSubCategoryList = mSubCategoryList;

    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public ArrayList<SubCategory> getmSubCategoryList() {
        return mSubCategoryList;
    }

    public void setmSubCategoryList(ArrayList<SubCategory> mSubCategoryList) {
        this.mSubCategoryList = mSubCategoryList;
    }

    /**
     * frist level item
     */

    public static class SubCategory {

        private String pSubCatName;
        private String pSubCatId;
        private ArrayList<SubCategoryOne> mSubOneListArray;

        public ArrayList<SubCategoryOne> getmSubOneListArray() {
            return mSubOneListArray;
        }

        public void setmSubOneListArray(ArrayList<SubCategoryOne> mSubOneListArray) {
            this.mSubOneListArray = mSubOneListArray;
        }

        public String getpSubCatId() {
            return pSubCatId;
        }

        public void setpSubCatId(String pSubCatId) {
            this.pSubCatId = pSubCatId;
        }

        public SubCategory(String pSubCatId1,
                           String pSubCatName, ArrayList<SubCategoryOne> mSubOneListArray) {
            super();
            this.pSubCatName = pSubCatName;
            this.pSubCatId = pSubCatId1;
            this.mSubOneListArray = mSubOneListArray;
        }

        public String getpSubCatName() {
            return pSubCatName;
        }

        public void setpSubCatName(String pSubCatName) {
            this.pSubCatName = pSubCatName;
        }


    }

    /**
     * Second level item
     */

    public static class SubCategoryOne {

        private String pSubCatName;
        private String pSubCatID;
        private ArrayList<ItemList> mItemListArray;

        public String getpSubCatID() {
            return pSubCatID;
        }

        public ArrayList<ItemList> getmItemListArray() {
            return mItemListArray;
        }

        public void setmItemListArray(ArrayList<ItemList> mItemListArray) {
            this.mItemListArray = mItemListArray;
        }

        public void setpSubCatID(String pSubCatID) {
            this.pSubCatID = pSubCatID;
        }

        public SubCategoryOne(String pSubCatID, String pSubCatName, ArrayList<ItemList> mItemListArray) {
            super();
            this.pSubCatName = pSubCatName;
            this.pSubCatID = pSubCatID;
            this.mItemListArray = mItemListArray;
        }

        public String getpSubCatName() {
            return pSubCatName;
        }

        public void setpSubCatName(String pSubCatName) {
            this.pSubCatName = pSubCatName;
        }


    }

    /**
     * third level item
     */
    public static class ItemList {

        private String itemName;
        private String itemId;


        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public String getItemId() {
            return itemId;
        }

        public void setItemId(String itemId) {
            this.itemId = itemId;
        }

        public ItemList(String itemId, String itemName) {
            super();
            this.itemName = itemName;

            this.itemId = itemId;
        }



    }


}
