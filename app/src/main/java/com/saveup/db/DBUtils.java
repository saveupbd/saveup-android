package com.saveup.db;

public class DBUtils {

    // All Static variables
    // Database Version
    public static final int DATABASE_VERSION = 1;

    // Database Name
    public static final String DATABASE_NAME = "SaveUpDB";

    // All Track table name
    //private static final String TABLE_CountryTable = "CountryTable";
    public static final String TABLE_ORDER_Table = "OrderTable";

    public static final String TABLE_REDEMEED_ORDER_Table = "REDEMEEDTable";


    // allChannel Table Columns names

    // ====== for Country Table ================
    public static final String KEY_ROWID = "db_id";


    public static final String KEY_ORDER_ID = "orderid";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_SHIPPING_NAME = "shipping_name";
    public static final String KEY_ORDER_SUBTOTAL = "order_subtotal";
    public static final String KEY_ORDER_TOTAL = "order_total";
    public static final String KEY_COUPON_CODE = "coupon_code";
    public static final String KEY_ORDER_DATE = "order_date";
    public static final String KEY_EXPIRE_DATE = "expire_day";

    public static final String KEY_OFFLINEREDEEMEDTIME = "offlineredeemtime";


}
