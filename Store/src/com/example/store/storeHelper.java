package com.example.store;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;


public class storeHelper extends SQLiteOpenHelper{
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "store.db";
    
    public static final String CREATE_STORE = "create table store (_id integer primary key AUTOINCREMENT, date date,itemid integer,price integer,number integer,amount integer,cost integer,remark text)";
    public static final String CREATE_ITEM = "create table item (itemid integer primary key AUTOINCREMENT,itemname text,price integer,cost integer)";
    
    private Context mContext;
    
    public storeHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mContext=context;
    }
 
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // create table Orders(Id integer primary key, CustomName text, OrderPrice integer, Country text);
        sqLiteDatabase.execSQL(CREATE_STORE);
        sqLiteDatabase.execSQL(CREATE_ITEM);
        Toast.makeText(mContext, "数据库创建成功!",Toast.LENGTH_SHORT).show();
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        //sqLiteDatabase.execSQL(sql);
        //onCreate(sqLiteDatabase);
    }
}