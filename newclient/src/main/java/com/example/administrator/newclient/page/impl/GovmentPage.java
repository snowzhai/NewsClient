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
public class GovmentPage extends BasePage{
    public GovmentPage(Activity activity) {
        super(activity);
    }
    @Override
    public void initData() {
        tv_page_title.setText("政务");
        ll_page_content.removeAllViews();
        TextView tv= new TextView(mActivity);
        tv.setText("政务内容" );
        tv.setGravity(Gravity.CENTER);
        ll_page_content.addView(tv);

        //禁用侧边栏
        HomeActivity homeActivity= (HomeActivity) mActivity;
        homeActivity.setSilidingMenuEnable(true);
        //不显示上边的按钮
        bt_page_left.setVisibility(View.INVISIBLE);
        bt_page_rigth.setVisibility(View.INVISIBLE);
    }


}
