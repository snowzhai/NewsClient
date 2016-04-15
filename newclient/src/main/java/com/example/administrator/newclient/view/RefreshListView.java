package com.example.administrator.newclient.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.newclient.R;

import java.util.Date;

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
    private RotateAnimation rotateAnimation;
    private View footerView;
    private int footerViewHeight;
    private  boolean isLoadingMore =false;


    //人自己调用的
    public RefreshListView(Context context) {
        super(context);
        initHeader(context);
        initFootter(context);

    }
    //系统当编译manifast文件时调用的
    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeader(context);
        initFootter(context);
    }
    public void initFootter(Context context){
        footerView=View.inflate(context, R.layout.refreshlist_footer,null);
        footerView.measure(0,0);
        footerViewHeight = footerView.getMeasuredHeight();
        //默认情况下，将下拉刷新的headerview 隐藏起来
        footerView.setPadding(0,-footerViewHeight,0,0); //
        addFooterView(footerView);//添加listview的脚步控件
        setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                    //当当前的item为listvie的最后一个控件时候 就显示最后面的正在加载的脚步控件
                if ((scrollState==SCROLL_STATE_IDLE||scrollState==SCROLL_STATE_FLING)&&getLastVisiblePosition()==getCount()-1){

                    if (isLoadingMore!=true){
                        Log.i("哈哈", "已经拖到最底部，需要去加载更多");
                        footerView.setPadding(0,0,0,0);//如果拖到最后面的时候就显示控件
                        setSelection(getCount()-1);//拖到底部的时候 让footer自动显示
                        isLoadingMore=true;
                        refreshListener.onLoadMore();

                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

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
        rotateAnimation=new RotateAnimation(0,180, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setDuration(200);
        rotateAnimation.setFillAfter(true);//将动画停在最后一帧

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
                if (current_state == REFRESHING)
                    break;

                endx = ev.getRawX();
                endy = ev.getRawY();
                float dx = Math.abs( startx- endx);
                float dy = Math.abs(starty -endy );
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

                    if(refreshListener!=null){
                        refreshListener.onRefresh();
                    }
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
                iv_refresh_header_arrow.startAnimation(rotateAnimation);

                break;
            case REFRESHING:
                tv_refresh_header_hint.setText("正在刷新");
                pb_refresh_header_loading.setVisibility(View.VISIBLE);//将进度条设置为可见的
                iv_refresh_header_arrow.clearAnimation();
                iv_refresh_header_arrow.setVisibility(View.INVISIBLE);//将下拉的图片设置为不可见的
                break;
        }
    }

    //这里定义了一个接口 用于向使用者暴露方法 使用者用的时候可以使用匿名内部类实现
    private   RefreshListener refreshListener;
    public interface RefreshListener{
        public void onRefresh();
        public void onLoadMore();
    }
    public void setOnRefreshListener(RefreshListener listener){
        refreshListener=listener;
    }
    //刷新完成时
    public void onRefreshComplete(){
        headerview.setPadding(0,-headerHeigth,0,0);
        tv_refresh_header_updatetime.setText(new Date().toLocaleString());
        tv_refresh_header_hint.setText("下拉刷新");
        pb_refresh_header_loading.setVisibility(View.INVISIBLE);
        iv_refresh_header_arrow.setVisibility(View.VISIBLE);
        current_state=NEED_PULL;

    }
    public void onLoadmoreComplete(){
        isLoadingMore=false;
        footerView.setPadding(0,-footerViewHeight,0,0);
    }
}
