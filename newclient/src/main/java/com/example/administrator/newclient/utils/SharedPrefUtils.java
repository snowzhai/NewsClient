package com.example.administrator.newclient.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/4/15.
 */
//保存缓存信息的工具类
public class SharedPrefUtils {
    //保存json信息
    public static void saveJsonToCache(String json, String key, Context ctx){
        SharedPreferences jsoncache = ctx.getSharedPreferences("jsoncache", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = jsoncache.edit();
        edit.putString(key,json);
        edit.commit();

    }
    //得到json的缓存信息
    public static String getJsonFromCache(String key, Context ctx){
        SharedPreferences jsoncache = ctx.getSharedPreferences("jsoncache", Context.MODE_PRIVATE);
        return jsoncache.getString(key,"");
    }
}
