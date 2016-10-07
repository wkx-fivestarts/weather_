package com.practice.weather.util;

/**
 * Created by 36498 on 2016/10/3.
 */
/*在联网访问数据成功或失败后，都用接口来回调服务的返回结果*/
public interface HttpCallbackListener {
    void onFinish(String response);

    void onError(Exception e);
}
