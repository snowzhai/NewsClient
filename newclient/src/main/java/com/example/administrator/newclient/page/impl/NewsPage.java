package com.example.administrator.newclient.page.impl;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.example.administrator.newclient.Constans;
import com.example.administrator.newclient.HomeActivity;
import com.example.administrator.newclient.bean.Categories;
import com.example.administrator.newclient.menupage.BaseMenuPage;
import com.example.administrator.newclient.menupage.impl.HotnewsMenuPage;
import com.example.administrator.newclient.menupage.impl.InteractNewsMenuPage;
import com.example.administrator.newclient.menupage.impl.PictureNewsMenuPage;
import com.example.administrator.newclient.menupage.impl.TopicNewsMenuPage;
import com.example.administrator.newclient.page.BasePage;
import com.example.administrator.newclient.utils.SharedPrefUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by snow on 2016/4/11.
 */
//这是当点击主界面的新闻中心时候调用的
public class NewsPage extends BasePage{
    List<BaseMenuPage> typelist;
    Categories categories;

    public NewsPage(Activity activity) {
        super(activity);
    }
    @Override
    public void initData() {
        tv_page_title.setText("新闻");

        
        //使用侧边栏
        final HomeActivity homeActivity= (HomeActivity) mActivity;
        homeActivity.setSilidingMenuEnable(true);
        //点击显示侧边栏
        bt_page_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeActivity.setSlidingMenuToggle();
            }
        });

        typelist=new ArrayList<>();
        final String url = Constans.SERVER_ADDR + "/categories.json";
        String jsonFromCache = SharedPrefUtils.getJsonFromCache(url, mActivity);
        Log.i("啊哈哈","使用SharedPrefUtils");

        if (jsonFromCache.isEmpty()){
            getDataFromServer(url);
            Log.i("啊哈哈","从网络上拿数据");

        }else {
            parseJsonData(jsonFromCache);
            Log.i("啊哈哈","从内存中拿数据");
        }
    }
    //从这里用xutils到服务器去拿数据 用来初始化侧滑菜单上的信息 10.0.2.2为模拟器专用的自动拿到当前IP
    private void getDataFromServer(String url) {
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.GET,url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                SharedPrefUtils.saveJsonToCache(result,Constans.SERVER_ADDR+"/categories.json",mActivity);
                parseJsonData(result);
                Log.i("啊哈哈","服务器去拿数据");

            }
            @Override
            public void onFailure(HttpException e, String s) {
                Log.i("哈mygod","gg");
            }
        });
    }
    //这里用的是Google的在github上的开源项目gson
    private void parseJsonData(String result) {
        Gson gson = new Gson();
        categories=gson.fromJson(result,Categories.class);
        ((HomeActivity) mActivity).getLeftMenuFragment().setCategories(categories);
        //当点击新闻中心的时候就会初始化旁边的侧滑菜单
        typelist.add( new HotnewsMenuPage(mActivity,categories.data.get(0)));
        typelist.add( new TopicNewsMenuPage(mActivity,categories.data.get(1)));
        typelist.add( new PictureNewsMenuPage(mActivity,categories.data.get(2)));
        typelist.add( new InteractNewsMenuPage(mActivity,categories.data.get(3)));
        //将新闻界面的默认界面改成新闻界面
        setNewsType(0);
    }
    //当点击新闻中心的时候会调用 在这里初始化左边侧滑菜单的父类BaseMenuPage
    public void setNewsType(int i){
        final BaseMenuPage page = typelist.get(i);
        page.initmRootView();
        page.initData();
        ll_page_content.removeAllViews();
        ll_page_content.addView(page.mRootView);
    }
}
