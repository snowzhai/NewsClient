package com.example.administrator.newclient.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.newclient.R;

/**
 * Created by Administrator on 2016/4/14.
 */
public class RefreshListView extends ListView {
    private View headerview;
    private int headerHeigth;


    private final int NEED_PULL =0;  //需要继续下拉
    private final int NEED_RELEASE =1;  //需要松手
    private final int REFRESHING=2;   //显示正在更新

    private  int current_state=NEED_PULL;
    private ImageView iv_refresh_header_arrow;
    private ProgressBar pb_refresh_header_loading;
    private TextView tv_refresh_header_hint;
    private TextView tv_refresh_header_updatetime;
    //人自己调用的
    public RefreshListView(Context context) {
        super(context);
        initHeader(context);
    }
    //系统当编译manifast文件时调用的
    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeader(context);
    }

    public void initHeader(Context context){
        /*TextView tvvv= new TextView(context);
        tvvv.setText( "header");
        tvvv.setTextSize(30);
        tvvv.setTextColor(Color.RED);
        tvvv.setGravity(Gravity.CENTER);*/
        headerview= View.inflate(context, R.layout.refreshlist_header,null);
        iv_refresh_header_arrow = (ImageView) headerview.findViewById(R.id.iv_refresh_header_arrow);
        pb_refresh_header_loading = (ProgressBar) headerview.findViewById(R.id.pb_refresh_header_loading);
        tv_refresh_header_hint = (TextView) headerview.findViewById(R.id.tv_refresh_header_hint);
        tv_refresh_header_updatetime = (TextView) headerview.findViewById(R.id.tv_refresh_header_updatetime);

        //需要先measure后才能得到控件真正的高度
        headerview.measure(0,0);
        headerHeigth=headerview.getMeasuredHeight();
        headerview.setPadding(0,-headerHeigth,0,0);//默认情况下将下拉刷新的headview隐藏起来
        addHeaderView(headerview);
    }
    float startx;
    float starty;
    float endx;
    float endy;
    //重写onTouchEvent方法
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startx = ev.getRawX();
                starty = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (startx == 0 && starty == 0) {
                    startx = ev.getRawX();
                    starty = ev.getRawY();
                }
                if (current_state == REFRESHING) {
                    break;
                }
                endx = ev.getRawX();
                endy = ev.getRawY();
                float dx = Math.abs(endx - startx);
                float dy = Math.abs(endy - starty);
                if (dy > dx) {
                    if (endy > starty) {
                        headerview.setPadding(0, (int) (-headerHeigth + dy), 0, 0);//当人的手移动的时候动态改变刷新空间的位置
                        if (-headerHeigth + dy > 0 && current_state == NEED_PULL) {
                            current_state = NEED_RELEASE;//当刷新控件的位置完全漏出来的时候 而且是拉出来后 设置状态为松手刷新状态
                            updateHeaderView();
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (current_state==NEED_RELEASE){//当需要刷新的时候就将状态变为正在刷新的状态
                    current_state=REFRESHING;
                    Log.i("哈哈","状态变为正在刷新");
                    headerview.setPadding(0,0,0,0);//将位置隐藏掉
                    updateHeaderView();
                }else if (current_state==NEED_PULL){//如果还没有将控件都拉出来的时候 就让控件继续回去隐藏
                    Log.i("哈哈","松手的时候，没有完全拉出来，则需要缩回去，重新隐藏掉");
                    headerview.setPadding(0,-headerHeigth,0,0);
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void updateHeaderView() {
        switch (current_state){
            case NEED_RELEASE:
                tv_refresh_header_hint.setText("请松手刷新");
                break;
            case REFRESHING:
                tv_refresh_header_hint.setText("正在刷新");
                pb_refresh_header_loading.setVisibility(VISIBLE);//将进度条设置为可见的
                iv_refresh_header_arrow.setVisibility(INVISIBLE);//将下拉的图片设置为不可见的
                break;
        }
    }
}
