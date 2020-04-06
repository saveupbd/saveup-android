package com.saveup.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.saveup.model.OrderData;

import java.util.ArrayList;

import static com.saveup.db.DBUtils.DATABASE_NAME;


public class DatabaseHandler extends SQLiteOpenHelper {


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DBUtils.DATABASE_VERSION);
    }

    final String CREATE_ORDER_TABLE = "CREATE TABLE " + DBUtils.TABLE_ORDER_Table + "("

            + DBUtils.KEY_ROWID + " INTEGER PRIMARY KEY , "
            + DBUtils.KEY_ORDER_ID + " TEXT , "
            + DBUtils.KEY_USER_ID + " TEXT , "
            + DBUtils.KEY_SHIPPING_NAME + " TEXT , "
            + DBUtils.KEY_ORDER_SUBTOTAL + " TEXT , "
            + DBUtils.KEY_ORDER_TOTAL + " TEXT , "
            + DBUtils.KEY_COUPON_CODE + " TEXT , "
            + DBUtils.KEY_ORDER_DATE + " TEXT , "
            + DBUtils.KEY_EXPIRE_DATE + " TEXT " + ")";

    final String CREATE_REDEEM_ORDER_TABLE = "CREATE TABLE " + DBUtils.TABLE_REDEMEED_ORDER_Table + "("

            + DBUtils.KEY_ROWID + " INTEGER PRIMARY KEY , "
            + DBUtils.KEY_ORDER_ID + " TEXT , "
            + DBUtils.KEY_USER_ID + " TEXT , "
            + DBUtils.KEY_SHIPPING_NAME + " TEXT , "
            + DBUtils.KEY_ORDER_SUBTOTAL + " TEXT , "
            + DBUtils.KEY_ORDER_TOTAL + " TEXT , "
            + DBUtils.KEY_COUPON_CODE + " TEXT , "
            + DBUtils.KEY_ORDER_DATE + " TEXT , "
            + DBUtils.KEY_OFFLINEREDEEMEDTIME + " TEXT , "
            + DBUtils.KEY_EXPIRE_DATE + " TEXT " + ")";


    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_ORDER_TABLE);
        db.execSQL(CREATE_REDEEM_ORDER_TABLE);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        //  db.execSQL("DROP TABLE IF EXISTS " + TABLE_CountryTable);
        db.execSQL("DROP TABLE IF EXISTS " + DBUtils.TABLE_ORDER_Table);
        db.execSQL("DROP TABLE IF EXISTS " + DBUtils.TABLE_REDEMEED_ORDER_Table);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
//
//    public void addCountryInfo(CountryInfo tInfo) {
//        final SQLiteDatabase db = getWritableDatabase();
//
//        final ContentValues values = new ContentValues();
//        values.put(KEY_countryId, tInfo.getId());
//        values.put(KEY_countryName, tInfo.getName());
//        values.put(KEY_currency_code, tInfo.getCurrency_code());
//        values.put(KEY_countryFlag, tInfo.getFlag());
//        values.put(KEY_calling_code, tInfo.getCalling_code());
//        values.put(KEY_iso2, tInfo.getIso2());
//        // Inserting Row
//        db.insert(TABLE_CountryTable, null, values);
//        db.close(); // Closing database connection
//    }

    // order data
    public void unRedeemedData(OrderData data) {

        final SQLiteDatabase db = getWritableDatabase();
        final ContentValues values = new ContentValues();

        values.put(DBUtils.KEY_ORDER_ID, data.getOrder_id());
        values.put(DBUtils.KEY_USER_ID, data.getUser_id());
        values.put(DBUtils.KEY_SHIPPING_NAME, data.getShipping_name());
        values.put(DBUtils.KEY_ORDER_SUBTOTAL, data.getOrder_subtotal());
        values.put(DBUtils.KEY_ORDER_TOTAL, data.getOrder_total());
        values.put(DBUtils.KEY_COUPON_CODE, data.getCoupon_code());
        values.put(DBUtils.KEY_ORDER_DATE, data.getOrder_date());
        values.put(DBUtils.KEY_EXPIRE_DATE, data.getExpire_day());
        db.insert(DBUtils.TABLE_ORDER_Table, null, values);

        db.close();
    }


    public void addRedeemOrderDATA(OrderData data) {

        final SQLiteDatabase db = getWritableDatabase();
        final ContentValues values = new ContentValues();

        values.put(DBUtils.KEY_ORDER_ID, data.getOrder_id());
        values.put(DBUtils.KEY_USER_ID, data.getUser_id());
        values.put(DBUtils.KEY_SHIPPING_NAME, data.getShipping_name());
        values.put(DBUtils.KEY_ORDER_SUBTOTAL, data.getOrder_subtotal());
        values.put(DBUtils.KEY_ORDER_TOTAL, data.getOrder_total());
        values.put(DBUtils.KEY_COUPON_CODE, data.getCoupon_code());
        values.put(DBUtils.KEY_ORDER_DATE, data.getOrder_date());
        values.put(DBUtils.KEY_EXPIRE_DATE, data.getExpire_day());
        db.insert(DBUtils.TABLE_REDEMEED_ORDER_Table, null, values);

        db.close();
    }


    public void mirgateIntoRedeemTable(OrderData data) {

        final SQLiteDatabase db = getWritableDatabase();
        final ContentValues values = new ContentValues();

        values.put(DBUtils.KEY_ORDER_ID, data.getOrder_id());
        values.put(DBUtils.KEY_USER_ID, data.getUser_id());
        values.put(DBUtils.KEY_SHIPPING_NAME, data.getShipping_name());
        values.put(DBUtils.KEY_ORDER_SUBTOTAL, data.getOrder_subtotal());
        values.put(DBUtils.KEY_ORDER_TOTAL, data.getOrder_total());
        values.put(DBUtils.KEY_COUPON_CODE, data.getCoupon_code());
        values.put(DBUtils.KEY_ORDER_DATE, data.getOrder_date());
        values.put(DBUtils.KEY_EXPIRE_DATE, data.getExpire_day());
        values.put(DBUtils.KEY_OFFLINEREDEEMEDTIME, data.getOffLineRedeemTime());

        db.insert(DBUtils.TABLE_REDEMEED_ORDER_Table, null, values);

        db.close();
    }


//

    //    public Vector<CountryInfo> getAllCountryInfo() {
//        final Vector<CountryInfo> trackList = new Vector<CountryInfo>();
//        trackList.removeAllElements();
//        // Select All Queryb
//        final String selectQuery = "SELECT * FROM " + TABLE_CountryTable;
//
//
//        final SQLiteDatabase db = getWritableDatabase();
//        final Cursor cursor = db.rawQuery(selectQuery, null);
//
//        // looping through all rows and adding to list
//        if (cursor.moveToFirst()) {
//            do {
//                CountryInfo eInfo = new CountryInfo();
//                eInfo.setId(cursor.getString(1));
//                eInfo.setName(cursor.getString(2));
//                eInfo.setCurrency_code(cursor.getString(3));
//                eInfo.setFlag(cursor.getString(4));
//                eInfo.setCalling_code(cursor.getString(5));
//                eInfo.setIso2(cursor.getString(6));
//                trackList.addElement(eInfo);
//                eInfo = null;
//            } while (cursor.moveToNext());
//        }
//
//        // return contact list
//        return trackList;
//
//
//    }
//
    public ArrayList<OrderData> getAllOrders() {
        final ArrayList<OrderData> orderList = new ArrayList<OrderData>();
        orderList.clear();
        final String selectQuery = "SELECT * FROM " + DBUtils.TABLE_ORDER_Table;


        final SQLiteDatabase db = getWritableDatabase();
        final Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                OrderData eInfo = new OrderData();
                eInfo.setOrderDBID(cursor.getLong(cursor.getColumnIndex(DBUtils.KEY_ROWID)));
                eInfo.setOrder_id(cursor.getString(cursor.getColumnIndex(DBUtils.KEY_ORDER_ID)));
                eInfo.setUser_id(cursor.getString(cursor.getColumnIndex(DBUtils.KEY_USER_ID)));
                eInfo.setShipping_name(cursor.getString(cursor.getColumnIndex(DBUtils.KEY_SHIPPING_NAME)));
                eInfo.setOrder_subtotal(cursor.getString(cursor.getColumnIndex(DBUtils.KEY_ORDER_SUBTOTAL)));
                eInfo.setOrder_total(cursor.getString(cursor.getColumnIndex(DBUtils.KEY_ORDER_TOTAL)));
                eInfo.setCoupon_code(cursor.getString(cursor.getColumnIndex(DBUtils.KEY_COUPON_CODE)));
                eInfo.setOrder_date(cursor.getString(cursor.getColumnIndex(DBUtils.KEY_ORDER_DATE)));
                eInfo.setExpire_day(cursor.getString(cursor.getColumnIndex(DBUtils.KEY_EXPIRE_DATE)));
                orderList.add(eInfo);
                eInfo = null;
            } while (cursor.moveToNext());
        }

        if (db.isOpen()) {
            db.close();
        }
        // return contact list
        return orderList;
    }


    public ArrayList<OrderData> getAllREDEEMEDOrders() {
        final ArrayList<OrderData> orderList = new ArrayList<OrderData>();
        orderList.clear();
        final String selectQuery = "SELECT * FROM " + DBUtils.TABLE_REDEMEED_ORDER_Table;


        final SQLiteDatabase db = getWritableDatabase();
        final Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                OrderData eInfo = new OrderData();
                eInfo.setOrderDBID(cursor.getLong(cursor.getColumnIndex(DBUtils.KEY_ROWID)));
                eInfo.setOrder_id(cursor.getString(cursor.getColumnIndex(DBUtils.KEY_ORDER_ID)));
                eInfo.setUser_id(cursor.getString(cursor.getColumnIndex(DBUtils.KEY_USER_ID)));
                eInfo.setShipping_name(cursor.getString(cursor.getColumnIndex(DBUtils.KEY_SHIPPING_NAME)));
                eInfo.setOrder_subtotal(cursor.getString(cursor.getColumnIndex(DBUtils.KEY_ORDER_SUBTOTAL)));
                eInfo.setOrder_total(cursor.getString(cursor.getColumnIndex(DBUtils.KEY_ORDER_TOTAL)));
                eInfo.setCoupon_code(cursor.getString(cursor.getColumnIndex(DBUtils.KEY_COUPON_CODE)));
                eInfo.setOrder_date(cursor.getString(cursor.getColumnIndex(DBUtils.KEY_ORDER_DATE)));
                eInfo.setExpire_day(cursor.getString(cursor.getColumnIndex(DBUtils.KEY_EXPIRE_DATE)));
                orderList.add(eInfo);
                eInfo = null;
            } while (cursor.moveToNext());
        }

        if (db.isOpen()) {
            db.close();
        }
        // return contact list
        return orderList;
    }


    public void deleteOrderTableInfo() {

        final SQLiteDatabase db = getWritableDatabase();
        final String query1 = "DELETE " + " FROM " + DBUtils.TABLE_ORDER_Table
                + " where " + DBUtils.KEY_ROWID + ">-1";

        db.execSQL(query1);
        db.close();
    }

    public void deleteRedeemTableInfo() {

        final SQLiteDatabase db = getWritableDatabase();
        final String query1 = "DELETE " + " FROM " + DBUtils.TABLE_REDEMEED_ORDER_Table
                + " where " + DBUtils.KEY_ROWID + ">-1";

        db.execSQL(query1);
        db.close();
    }


    public boolean deleteUnRedeemObject(long keyID) {
        final SQLiteDatabase db = getWritableDatabase();

        return db.delete( DBUtils.TABLE_ORDER_Table, DBUtils.KEY_ROWID + "=" + keyID, null) > 0;
    }


}
