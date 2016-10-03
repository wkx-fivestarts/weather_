package com.practice.weather.util;

/**
 * Created by 36498 on 2016/10/3.
 */
public interface HttpCallbackListener {
    void onFinishi(String response);

    void onError(Exception e);
}
