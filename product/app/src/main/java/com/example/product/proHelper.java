package com.example.product;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.os.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class proHelper extends SQLiteOpenHelper{
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "product.db";

    public static final String CREATE_PRODUCT = "CREATE TABLE product (id integer primary key AUTOINCREMENT,name text NOT NULL, cid integer , images text, tags text, spec text, price integer)";
    public static final String CREATE_CATEGORY = "CREATE TABLE category ( id integer primary key AUTOINCREMENT, name text NOT NULL, description text)";
    public static final String CREATE_SUB_CATEGORY = "CREATE TABLE sub_category ( id integer primary key AUTOINCREMENT, name text NOT NULL, cid integer , description text)";
    public static final String CREATE_TAG = "CREATE TABLE tag ( id integer primary key AUTOINCREMENT, name text NOT NULL, description text)";
    private Context mContext;

    public proHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // create table Orders(Id integer primary key, CustomName text, OrderPrice integer, Country text);
        sqLiteDatabase.execSQL(CREATE_PRODUCT);
        sqLiteDatabase.execSQL(CREATE_CATEGORY);
        sqLiteDatabase.execSQL(CREATE_SUB_CATEGORY);
        sqLiteDatabase.execSQL(CREATE_TAG);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        //sqLiteDatabase.execSQL(sql);
        //onCreate(sqLiteDatabase);
    }

}
