package com.example.administrator.newclient.page.impl;

import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.newclient.Constans;
import com.example.administrator.newclient.HomeActivity;
import com.example.administrator.newclient.bean.Categories;
import com.example.administrator.newclient.menupage.BaseMenuPage;
import com.example.administrator.newclient.menupage.impl.HotnewsMenuPage;
import com.example.administrator.newclient.menupage.impl.InteractNewsMenuPage;
import com.example.administrator.newclient.menupage.impl.PictureNewsMenuPage;
import com.example.administrator.newclient.menupage.impl.TopicNewsMenuPage;
import com.example.administrator.newclient.page.BasePage;
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
public class NewsPage extends BasePage{
    List<BaseMenuPage> typelist;

    public NewsPage(Activity activity) {
        super(activity);
    }
    @Override
    public void initData() {
        tv_page_title.setText("新闻");
        ll_page_content.removeAllViews();

        TextView tv= new TextView(mActivity);
        tv.setText("新闻内容" );
        tv.setGravity(Gravity.CENTER);
        
        ll_page_content.addView(tv);
        typelist=new ArrayList<>();
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

        //从这里用xutils到服务器去拿数据  10.0.2.2为模拟器专用的自动拿到当前IP
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.GET, Constans.SERVER_ADDR+"/categories.json", new RequestCallBack<String>() {
            Categories categories;
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                //这里用的是Google的在github上的开源项目gson
                Gson gson = new Gson();
                categories=gson.fromJson(result,Categories.class);
                ((HomeActivity) mActivity).getLeftMenuFragment().setCategories(categories);

                Log.i("哈哈",categories.toString());
                typelist.add( new HotnewsMenuPage(mActivity,categories.data.get(0)));
                typelist.add( new TopicNewsMenuPage(mActivity,categories.data.get(1)));
                typelist.add( new PictureNewsMenuPage(mActivity,categories.data.get(2)));
                typelist.add( new InteractNewsMenuPage(mActivity,categories.data.get(3)));

            }
            @Override
            public void onFailure(HttpException e, String s) {
                Log.i("哈mygod","gg");
            }
        });


    }
    public void setNewsType(int i){
        final BaseMenuPage page = typelist.get(i);
        page.initmRootView();
        page.initData();
        ll_page_content.removeAllViews();
        ll_page_content.addView(page.mRootView);
    }
}
