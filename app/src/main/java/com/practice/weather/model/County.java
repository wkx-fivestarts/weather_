package com.practice.weather.model;

/**
 * Created by 36498 on 2016/10/3.
 */
public class County {
    private int id;
    private String countyName;
    private String countyCode;
    private int cityId;

    public int getId(){
        return id;
    }

    public void setId(){
        this.id=id;
    }

    public String getCountyName(){
        return countyName;
    }

    public void setCountyName(String countyName){

        this.countyName=countyName;
    }

    public String getCountyCode(){

        return countyCode;
    }

    public  void setCountyCode(String countyeCode){

        this.countyCode=countyCode;
    }

    public int getCityId(){

        return cityId;
    }

    public void  setCityId(int cityId){
        this.cityId=cityId;
    }

}