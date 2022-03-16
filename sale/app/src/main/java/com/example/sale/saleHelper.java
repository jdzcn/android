package com.example.sale;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class saleHelper extends SQLiteOpenHelper{
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "sale.db";
    
    public static final String CREATE_SALE = "create table sale (id integer primary key AUTOINCREMENT, date date,pid integer,price integer,number integer,amount integer,cost integer,remark text)";
    public static final String CREATE_PRODUCT = "create table product (pid integer primary key AUTOINCREMENT,name text,price integer,cost integer,img blob)";
    
    private Context mContext;
    
    public saleHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mContext=context;
    }
 
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // create table Orders(Id integer primary key, CustomName text, OrderPrice integer, Country text);
        sqLiteDatabase.execSQL(CREATE_SALE);
        sqLiteDatabase.execSQL(CREATE_PRODUCT);
        //Toast.makeText(mContext, "���ݿⴴ���ɹ�!",Toast.LENGTH_SHORT).show();
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        //sqLiteDatabase.execSQL(sql);
        //onCreate(sqLiteDatabase);
    }
}