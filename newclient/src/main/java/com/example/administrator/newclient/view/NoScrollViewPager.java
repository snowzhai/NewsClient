package com.example.administrator.newclient.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2016/4/11.
 */
//写这个类的主要目的是为了让控件（屏蔽）滑动事件
public class NoScrollViewPager extends ViewPager {
    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    //viewpager实际上如果要拦截的话，会直接在这里做拦截后的处理。所以如果禁用掉viewpager的移动 应该在这里和onTouchEvent 都返回false。
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
//        return super.onTouchEvent(ev);
    }
}
