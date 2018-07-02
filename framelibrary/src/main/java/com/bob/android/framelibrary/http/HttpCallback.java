package com.bob.android.framelibrary.http;

import android.content.Context;
import android.content.pm.PackageManager;

import com.google.gson.Gson;

import java.util.Map;

import http.EngineCallBack;
import http.HttpUtils;
import util.GetDeviceConnectType;

/**
 * @package com.bob.android.framelibrary
 * @fileName HttpCallback
 * @Author Bob on 2018/4/25 17:30.
 * @Describe TODO
 */

public abstract class HttpCallback<T> implements EngineCallBack {
    @Override
    public void onPreExecute(Context context, Map<String, Object> params) {
        //在这里添加与项目逻辑有关的公用参数，是调用接口必须要添加的参数

//        params.put("app_name",context.getPackageName());
        params.put("packageName",context.getPackageName());
//        try {
//            params.put("version_name",context.getPackageManager().getPackageInfo(context.getPackageName(),0).versionName);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        params.put("ac", GetDeviceConnectType.getNetworkState(context));
         // ......
        onPreExecute();

    }

    @Override
    public void onSuccess(String result) {
        Gson gson = new Gson();
        T objResult = (T) gson.fromJson(result,
                HttpUtils.analysisClazzInfo(this));
        onSuccess(objResult);
    }

    /**
     * 开始执行
     */
    public  void onPreExecute(){

    }

    // 返回可以直接操作的对象
    public abstract void onSuccess(T result);

}
