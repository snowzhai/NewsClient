package com.example.administrator.newclient.page.impl;

import android.app.Activity;
import android.view.Gravity;
import android.widget.TextView;

import com.example.administrator.newclient.page.BasePage;

/**
 * Created by snow on 2016/4/11.
 */
public class NewsPage extends BasePage{


    public NewsPage(Activity activity) {
        super(activity);
    }



    @Override
    public void initData() {
        tv_page_title.setText("新闻");


        TextView tv= new TextView(mActivity);
        tv.setText("新闻内容" );
        tv.setGravity(Gravity.CENTER);
        
        ll_page_content.addView(tv);
    }


}
