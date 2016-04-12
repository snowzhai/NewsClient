package com.example.administrator.newclient.page.impl;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.newclient.HomeActivity;
import com.example.administrator.newclient.page.BasePage;

/**
 * Created by snow on 2016/4/11.
 */
public class SettingPage extends BasePage{


    public SettingPage(Activity activity) {
        super(activity);
    }



    @Override
    public void initData() {
        tv_page_title.setText("设置");
        ll_page_content.removeAllViews();
        TextView tv= new TextView(mActivity);
        tv.setText("设置内容" );
        tv.setGravity(Gravity.CENTER);
        ll_page_content.addView(tv);

        HomeActivity homeActivity= (HomeActivity) mActivity;
        homeActivity.setSilidingMenuEnable(false);
        //不显示上边的按钮
        bt_page_left.setVisibility(View.INVISIBLE);
        bt_page_rigth.setVisibility(View.INVISIBLE);
    }


}
