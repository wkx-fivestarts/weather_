package com.practice.weather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 36498 on 2016/10/5.
 */
public class WeatherOpenHelper extends SQLiteOpenHelper {
    public static final String CREATE_PROVINCE = "create table Province("
            +"id integer primary key antoincrement,"//自动增加的主键
            +"province_name text,"
            +"povince_code text)";

    public static final String CREATE_CITY = "create table City("
            +"id integer primary key antoincrement,"
            +"city_name text,"
            +"city_code text,"
            +"province_id integer)";//city_id是County表关联City表的外键

    public static final String CREATE_COUNTY = "create table County("
            +"id integer primary key antoincrement,"
            +"county_name text,"
            +"county_code text,"
            +"city_id integer)";
    /*调用父类构造器*/
    public WeatherOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                             int version){
        super(context,name,factory,version);
    }
    /*创建3张表的实体类*/
    @Override
    public void  onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_PROVINCE);
        db.execSQL(CREATE_CITY);
        db.execSQL(CREATE_COUNTY);
    }
    /*当打开数据库时传入的版本号与当前的版本号不同时会调用该方法。*/
    @Override
    public  void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }
}


