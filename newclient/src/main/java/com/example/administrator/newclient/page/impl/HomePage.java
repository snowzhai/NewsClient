package com.example.administrator.newclient.page.impl;

import android.app.Activity;
import android.view.Gravity;
import android.widget.TextView;

import com.example.administrator.newclient.HomeActivity;
import com.example.administrator.newclient.page.BasePage;

/**
 * Created by snow on 2016/4/11.
 */
public class HomePage extends BasePage{


    public HomePage(Activity activity) {
        super(activity);
    }



    @Override
    public void initData() {
        tv_page_title.setText("首页");
        ll_page_content.removeAllViews();
        TextView tv= new TextView(mActivity);
        tv.setText("首页内容" );
        tv.setGravity(Gravity.CENTER);
        ll_page_content.addView(tv);

        HomeActivity homeActivity= (HomeActivity) mActivity;
        homeActivity.setSilidingMenuEnable(false);
    }


}
