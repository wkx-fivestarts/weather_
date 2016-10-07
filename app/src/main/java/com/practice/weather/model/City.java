package com.practice.weather.model;

/**
 * Created by 36498 on 2016/10/3.
 */
public class City {
    private int id;
    private String cityName;
    private String cityCode;
    private int provinceId;

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id=id;
    }

    public String getCityName(){
        return cityName;
    }

    public void setCityName(String ctyName){

        this.cityName=cityName;
    }

    public String getCityCode(){

        return cityCode;
    }

    public  void setCityCode(String cityCode){

        this.cityCode=cityCode;
    }

    public int getProvinceId(){

        return provinceId;
    }

    public void  setProvinceId(int provinceId){
        this.provinceId=provinceId;
    }
}
