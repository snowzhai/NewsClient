package com.example.administrator.newclient.menupage;

import android.app.Activity;
import android.view.View;

/**
 * Created by Administrator on 2016/4/12.
 */
//作为新闻中心页面的4个子主题的父类
public abstract class BaseMenuPage {
    protected Activity mActivity;
    public View mRootView;

    public BaseMenuPage(Activity mActivity) {
        this.mActivity = mActivity;
    }
    public void initmRootView(){
        mRootView=initView();
    }
    public abstract View initView();
    public  abstract void  initData();

}
