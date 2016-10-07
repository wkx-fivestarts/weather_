package com.practice.weather.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.practice.weather.Adapter.WeatherAdapter;
import com.practice.weather.R;
import com.practice.weather.model.HourlyWeather;
import com.practice.weather.util.HttpCallbackListener;
import com.practice.weather.util.HttpUtil;
import com.practice.weather.util.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 36498 on 2016/10/3.
 */
public class WeatherActivity extends AppCompatActivity implements View.OnClickListener {

    // 城市切换按钮
    private Button citySwitch;
    // 刷新数据按钮
    private Button weatherRefresh;
    // 城市名
    private TextView cityName;
    // 白天夜晚天气
    private TextView DayNightWeather;
    // 温度
    private TextView temp;
    // 日出时间
    private TextView sunriseTime = (TextView) findViewById(R.id.sunriseTime);
    // 日落时间
    private TextView sunsetTime = (TextView) findViewById(R.id.sunsetTime);
    // 风力
    private TextView wind;
    // 降水概率
    private TextView pop;
    // 发布时间
    private TextView updateTime;

    // 天气预测列表
    private ListView listview;

    public static List<HourlyWeather> weatherList = new ArrayList<>();

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
         setContentView(R.layout.weather_layout);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        init();
    }

    private void init() {
        citySwitch = (Button) findViewById(R.id.citySwitch);
        weatherRefresh = (Button) findViewById(R.id.weatherRefresh);
        citySwitch.setOnClickListener(this);
        weatherRefresh.setOnClickListener(this);
        cityName = (TextView) findViewById(R.id.cityName);
        DayNightWeather = (TextView) findViewById(R.id.DayNightWeather);
        temp = (TextView) findViewById(R.id.temp);
        wind = (TextView) findViewById(R.id.wind);
        pop = (TextView) findViewById(R.id.pop);
        updateTime = (TextView) findViewById(R.id.updateTime);
        listview = (ListView) findViewById(R.id.hourlyForecast);
        sharedPreferences = getSharedPreferences("Weather", Context.MODE_PRIVATE);

        String countyName = getIntent().getStringExtra("CountyName");
        // 当countyName不为空
        if (!TextUtils.isEmpty(countyName)) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("CountyName", countyName);
            editor.commit();
        } else {
            countyName = sharedPreferences.getString("CountyName", "");
        }
        weatherRefresh.setText("同步中……");
        queryFromServer(countyName);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.citySwitch:
                Intent intent = new Intent(this, ChooseAreaActivity.class);
                intent.putExtra("ChooseArea", true);
                startActivity(intent);
                finish();
                break;
            case R.id.weatherRefresh:
                weatherRefresh.setText("同步中……");
                String countyName = sharedPreferences.getString("CountyName", "");
                if (!TextUtils.isEmpty(countyName)) {
                    queryFromServer(countyName);
                }
                break;
        }
    }

    private void queryFromServer(final String countyName) {
        try {
            String url = "http://apis.baidu.com/heweather/weather/free?city=";
            String name = new String(countyName.getBytes("UTF-8"), "iso-8859-1");
            HttpUtil.sendHttpRequest(url + name, new HttpCallbackListener(){
                @Override
                public void onFinish(String response) {
                    Utility.handleWeatherResponse(WeatherActivity.this, response);
                     runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showWeather();
                        }
                    });
                }

                @Override
                public void onError(Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(WeatherActivity.this, "同步失败", Toast.LENGTH_LONG).show();
                            weatherRefresh.setText("更新数据");
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showWeather() {
        cityName.setText(sharedPreferences.getString("cityName", "未知"));
        sunriseTime.setText("日出：" + sharedPreferences.getString("sunriseTime", "未知"));
        sunsetTime.setText("日落：" + sharedPreferences.getString("sunsetTime", "未知"));
        DayNightWeather.setText("日：" + sharedPreferences.getString("dayWeather", "未知") +
                " 夜：" + sharedPreferences.getString("nightWeather", "未知"));
        temp.setText("温度：" + sharedPreferences.getString("temp", "未知"));
        wind.setText("风力：" + sharedPreferences.getString("wind", "未知"));
        pop.setText("降水概率：" + sharedPreferences.getString("pop", "未知"));
        updateTime.setText("发布时间:" + sharedPreferences.getString("updateTime", "未知"));
        WeatherAdapter adapter = new WeatherAdapter(this, R.layout.hourly_weather_layout, weatherList);
        listview.setAdapter(adapter);
        Toast.makeText(WeatherActivity.this, "已经是最新数据了", Toast.LENGTH_SHORT).show();
        weatherRefresh.setText("更新数据");
    }

}

