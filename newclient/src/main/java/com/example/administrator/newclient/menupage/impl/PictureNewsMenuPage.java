package com.example.administrator.newclient.menupage.impl;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.newclient.bean.Categories;
import com.example.administrator.newclient.menupage.BaseMenuPage;

/**
 * Created by Administrator on 2016/4/12.
 */
public class PictureNewsMenuPage extends BaseMenuPage {
    private Categories.NewsTypeInfo menupageInfo;
     //构造函数 其中 传来了一个ategories.NewsTypeInfo menuinfo用于接收里面的信息
    public PictureNewsMenuPage(Activity mActivity, Categories.NewsTypeInfo menuinfo) {
        super(mActivity);
        menupageInfo=menuinfo;
    }

    @Override
    public View initView() {

        TextView tvvv = new TextView(mActivity);
        tvvv.setText(menupageInfo.title + "类型");
        tvvv.setGravity(Gravity.CENTER);


        return tvvv;
    }

    @Override
    public void initData() {

    }
}
