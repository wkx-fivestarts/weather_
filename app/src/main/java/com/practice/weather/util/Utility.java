package com.practice.weather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.practice.weather.activity.WeatherActivity;
import com.practice.weather.model.City;
import com.practice.weather.model.County;
import com.practice.weather.model.HourlyWeather;
import com.practice.weather.model.Province;
import com.practice.weather.db.WeatherDB;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by 36498 on 2016/10/3.
 */
/*解析的规则就是先按逗号分隔，再按单竖线分隔，接着将解析出来的数据设置到实体类中，
发最后调用CoolWeatherDB中的三个save()方法将数据存储到相应的表中。 */
public class Utility {
    // 保存服务器返回的省级数据
    public synchronized static boolean handleProvincesResponse(WeatherDB weatherDB,
                                                               String response){
        if (!TextUtils.isEmpty(response)){
            String[] allProvinces =response.split(",");
            if (allProvinces!=null&&allProvinces.length>0){
                for (String p:allProvinces){
                    String[] array=p.split("\\|");
                    Province province=new Province();
                    province.setProvinceCode(array[0]);
                    province.setProvinceName(array[1]);

                    weatherDB.saveProvince (province);
                }
                return true;
            }
        }
        return false;
    }

    // 保存服务器返回的市级数据
    public static boolean handleCitiesResponse(WeatherDB weatherDB,
                                               String response,int provinceId) {
        if (!TextUtils.isEmpty(response)) {
            String[] allCities = response.split(",");
            if (allCities != null && allCities.length > 0) {
                for (String p : allCities) {
                    String[] array = p.split("\\|");
                    City city = new City();
                    city.setCityCode(array[0]);
                    city.setCityName(array[1]);
                    city.setProvinceId(provinceId);
                    weatherDB.saveCity(city);
                }
                return true;
            }
        }
        return false;
    }
    // 保存服务器返回的县级数据
    public static boolean handleCountiesResponse(WeatherDB weatherDB,
                                               String response,int cityId) {
        if (!TextUtils.isEmpty(response)) {
            String[] allCounties = response.split(",");
            if (allCounties != null && allCounties.length > 0) {
                for (String p : allCounties) {
                    String[] array = p.split("\\|");
                    County county = new County();
                    county.setCountyCode(array[0]);
                    county.setCountyName(array[1]);
                    county.setCityId(cityId);
                    weatherDB.saveCounty(county);
                }
                return true;
            }
        }
        return false;
    }
    // 处理服务器返回的json数据
    public static void handleWeatherResponse(Context context, String response) {
        try {
            JSONObject jsonobject = new JSONObject(response);
            JSONArray title = jsonobject.getJSONArray("HeWeather data service 3.0");
            JSONObject first_object = (JSONObject) title.get(0);

            JSONObject basic = (JSONObject) first_object.get("basic");

            //更新时间
            JSONObject update = (JSONObject) basic.get("update");
            JSONArray daily_forecast = (JSONArray) first_object.get("daily_forecast");
            JSONObject daily_forecast_first = (JSONObject) daily_forecast.get(0);
            JSONObject cond = (JSONObject) daily_forecast_first.get("cond");
            //温度
            JSONObject temp = (JSONObject) daily_forecast_first.get("tmp");

            JSONObject astro = (JSONObject) daily_forecast_first.get("astro");

            JSONObject wind = (JSONObject) daily_forecast_first.get("wind");

            JSONArray hourly_forecast = (JSONArray) first_object.get("hourly_forecast");

            WeatherActivity.weatherList.clear();

            for (int i = 0; i < hourly_forecast.length(); i++) {
                JSONObject json = hourly_forecast.getJSONObject(i);
                JSONObject json_wind = (JSONObject) json.get("wind");
                String date = json.getString("date");
                String[] array = date.split(" ");
                String dir = json_wind.getString("dir");
                String sc = json_wind.getString("sc");
                String hourly_clock = array[1];
                String hourly_temp = "温度：" + json.getString("tmp") + "℃";
                String hourly_pop = "降水概率：" + json.getString("pop");
                String hourly_wind = "风力：" + dir + " " + sc + "级";
               HourlyWeather weather = new HourlyWeather(hourly_clock, hourly_temp, hourly_pop, hourly_wind);
                WeatherActivity.weatherList.add(weather);
            }
            //日出
            String sunriseTime = astro.getString("sr");
            //日落
            String sunsetTime = astro.getString("ss");
            //白天天气
            String dayWeather = cond.getString("txt_d");
            //夜晚天气
            String nightWeather = cond.getString("txt_n");
            //风力
            String windText = wind.getString("dir") + " " + wind.getString("sc") + "级";
            //降水概率
            String pop = daily_forecast_first.getString("pop");
            //温度
            String tempText = temp.getString("min") + "℃~" + temp.getString("max") + "℃";
            //更新时间
            String updateTime = update.getString("loc");
            //城市名
            String cityName = basic.getString("city");
            saveWeatherInfo(context, cityName, sunriseTime, sunsetTime, dayWeather, nightWeather,
                    windText, pop, tempText, updateTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void saveWeatherInfo(Context context, String cityName, String sunriseTime,
                                        String sunsetTime, String dayWeather, String nightWeather,
                                        String windText, String pop, String tempText,
                                        String updateTime) {
        SharedPreferences.Editor editor = context.getSharedPreferences("Weather",
                Context.MODE_PRIVATE).edit();//实例化SharedPreferences。Editor对象
        /*保存数据*/
        editor.putString("cityName", cityName);
        editor.putString("sunriseTime", sunriseTime);
        editor.putString("sunsetTime", sunsetTime);
        editor.putString("dayWeather", dayWeather);
        editor.putString("nightWeather", nightWeather);
        editor.putString("wind", windText);
        editor.putString("pop", pop);
        editor.putString("temp", tempText);
        editor.putString("updateTime", updateTime);
        editor.commit();//提交当前数据
    }
}

