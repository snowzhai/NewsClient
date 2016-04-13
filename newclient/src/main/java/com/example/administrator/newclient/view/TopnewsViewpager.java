package com.example.administrator.newclient.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2016/4/13.
 */
//为了解决在新闻中心 新闻那一栏在北京哪里的滑动框处的没滑到边上就能划出侧边栏的问题 在此做判断
//根据划出图片的位置来改变父控件的事件拦截机制
public class TopnewsViewpager extends ViewPager {

    private float startx = 0;
    private float starty = 0;
    private float endx = 0;
    private float endy = 0;

    public TopnewsViewpager(Context context) {
        super(context);
    }

    public TopnewsViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startx = ev.getRawX();//绝对坐标
                starty = ev.getRawY();
                getParent().requestDisallowInterceptTouchEvent(true);//不让父控件处理这个事件
                break;
            case MotionEvent.ACTION_MOVE:
                endx = ev.getRawX();
                endy = ev.getRawY();
                float dx = Math.abs(startx - endx);
                float dy = Math.abs(starty - endy);
                if (dx > dy) {//当水瓶方向的移动距离大于竖直方向的移动距离的时候就判定是水平方向的移动
                    if (endx > startx) {//右划的时候
                        if (getCurrentItem() == 0) {
                            Log.i("哈哈哈1",""+getCurrentItem()+"--"+getChildCount());
                            //如果是在北京界面的第一个滑动框的时候就让父控件处理
                            getParent().requestDisallowInterceptTouchEvent(false);//让父控件拦截
                        }else {
                            Log.i("哈哈哈2","ACTION_MOVE"+getCurrentItem()+"--"+getChildCount());

                            getParent().requestDisallowInterceptTouchEvent(true);//不用让父控件处理
                        }
                    } else {
                        Log.i("哈哈哈3","ACTION_MOVE"+getCurrentItem()+"--"+getChildCount());

                        //左划   getAdapter().getCount()为得到整个侧滑的全部图片的张数 而不是北京，中国的单独的张数
                        if (getCurrentItem() == getAdapter().getCount() - 1) {//到最后一个page的时候，应该放父控件来处理就让父控件处理
                            getParent().requestDisallowInterceptTouchEvent(false);//让父控件拦截
                            Log.i("哈哈哈4","ACTION_MOVE"+getCurrentItem()+"--"+getChildCount());

                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                break;

        }
        return super.onTouchEvent(ev);
    }
}
