package com.example.administrator.newclient.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2016/4/12.
 */
public class HotNewsViewPager extends ViewPager {
    public HotNewsViewPager(Context context) {
        super(context);
    }

    public HotNewsViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int currentItem=this.getCurrentItem();//得到当前是哪个界面
        if (currentItem==0)//如果是第一个页面就不让它划出来
        {
            getParent().requestDisallowInterceptTouchEvent(false);//当当前页为北京的时候就不拦截滑动事件
        }else {
            getParent().requestDisallowInterceptTouchEvent(true);//当当前页不为北京时 拦该事件
        }
        return super.dispatchTouchEvent(ev);
    }
}
