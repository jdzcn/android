package com.example.myapplication;



import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.os.FileUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class dbHelper extends SQLiteOpenHelper{
    private static final int DB_VERSION = 1;
    /*
    private static final String DB_NAME = "product.db";

    public static final String CREATE_PRODUCT = "CREATE TABLE product (pid integer primary key autoincrement,name text, image text,sid integer ,  cid integer,gid integer, spec text, price integer)";
    public static final String CREATE_STYLE = "CREATE TABLE style ( sid integer primary key autoincrement, name text)";
    public static final String CREATE_CRAFT = "CREATE TABLE craft ( cid integer primary key autoincrement, name text, description text)";
    public static final String CREATE_GRAPH = "CREATE TABLE graph ( gid integer primary key autoincrement, name text, description text)";
    public static final String CREATE_TITLE = "CREATE TABLE title ( tid integer primary key autoincrement, name text,gid integer)";
    public static final String CREATE_VERSE = "CREATE TABLE verse ( vid integer primary key autoincrement, name text,gid integer)";
    */
    private static final String DB_NAME = "blog.db";
    public static final String CREATE_blog = "CREATE TABLE blog ( id integer primary key autoincrement, title text,cid integer,blog text)";
    public static final String CREATE_category = "CREATE TABLE category ( cid integer primary key autoincrement, name text)";
    private Context mContext;

    public dbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // create table Orders(Id integer primary key, CustomName text, OrderPrice integer, Country text);
        /*
        sqLiteDatabase.execSQL(CREATE_PRODUCT);
        sqLiteDatabase.execSQL(CREATE_STYLE);
        sqLiteDatabase.execSQL(CREATE_CRAFT);
        sqLiteDatabase.execSQL(CREATE_GRAPH);
        sqLiteDatabase.execSQL(CREATE_TITLE);
        sqLiteDatabase.execSQL(CREATE_VERSE);
        */
        sqLiteDatabase.execSQL(CREATE_blog);
        sqLiteDatabase.execSQL(CREATE_category);
        Log.e("MainActivity","database create successful");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        //sqLiteDatabase.execSQL(sql);
        //onCreate(sqLiteDatabase);
    }

}
