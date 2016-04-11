package com.example.administrator.newclient.page.impl;

import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.newclient.HomeActivity;
import com.example.administrator.newclient.bean.Categories;
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
    List<TextView> typelist;

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

        //使用侧边栏
        final HomeActivity homeActivity= (HomeActivity) mActivity;
        homeActivity.setSilidingMenuEnable(true);

        bt_page_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeActivity.setSlidingMenuToggle();
            }
        });

        //从这里用xutils到服务器去拿数据
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.GET, "http://192.168.3.27/Aday10Network/2categories.json", new RequestCallBack<String>() {
            Categories categories;
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                Gson gson = new Gson();
                categories=gson.fromJson(result,Categories.class);
                ((HomeActivity) mActivity).getLeftMenuFragment().setCategories(categories);

                Log.i("哈哈",categories.toString());
                for (int i=0;i<categories.data.size();i++){
                    TextView tvvv= new TextView(mActivity);
                    tvvv.setText(categories.data.get(i).title+"类型");
                    tvvv.setGravity(Gravity.CENTER);
                    typelist.add(tvvv);
                }
            }
            @Override
            public void onFailure(HttpException e, String s) {
                Log.i("哈mygod","gg");
            }
        });
        typelist=new ArrayList<>();

    }
    public void setNewsType(int i){
        final TextView textView = typelist.get(i);
        ll_page_content.removeAllViews();
        ll_page_content.addView(textView);
    }
}
