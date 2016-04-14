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
 * Created by Lan on 2016/4/14.
 */
public class RefreshListView extends ListView {

    private static final String TAG ="RefreshListView" ;
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

    public RefreshListView(Context context) {
        super(context);

        initHeader(context);
        initFootter(context);
    }



    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initHeader(context);
        initFootter(context);

    }

    private void initFootter(Context context) {


        footerView = View.inflate(context, R.layout.refreshlist_footer, null);

        footerView.measure(0,0);
        footerViewHeight = footerView.getMeasuredHeight();
        //默认情况下，将下拉刷新的headerview 隐藏起来
        footerView.setPadding(0,-footerViewHeight,0,0); //
        addFooterView(footerView);

        setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                Log.i(TAG,getLastVisiblePosition()+" getLastVisiblePosition() ：getCount() "+getCount() +"isLoadingmore"+isLoadingMore);
                if ((scrollState==SCROLL_STATE_IDLE||scrollState==SCROLL_STATE_FLING)&&
                        getLastVisiblePosition()==getCount()-1){ //当前显示的是最后一个item


                    if (isLoadingMore!=true) {
                        Log.i(TAG, "已经拖到最底部，需要去加载更多");

                        footerView.setPadding(0, 0, 0, 0); //
                        setSelection(getCount() - 1);//拖到底部的时候，让footer自动显示

                        isLoadingMore = true;
                        refreshListener.onLoadMore();//为什么写后面？如果没有更多，这里会立即调用complete，改false

                    }

                }


            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });


    }


    private void initHeader(Context context) {
      /*  TextView tvvv= new TextView(context);
        tvvv.setText( "header");
        tvvv.setTextSize(30);
        tvvv.setTextColor(Color.RED);
        tvvv.setGravity(Gravity.CENTER);
*/
        headerview = View.inflate(context, R.layout.refreshlist_header, null);

        //找到headerview里的所有控件，后续使用

        iv_refresh_header_arrow = (ImageView) headerview.findViewById(R.id.iv_refresh_header_arrow);
        pb_refresh_header_loading = (ProgressBar) headerview.findViewById(R.id.pb_refresh_header_loading);
        tv_refresh_header_hint = (TextView) headerview.findViewById(R.id.tv_refresh_header_hint);
        tv_refresh_header_updatetime = (TextView) headerview.findViewById(R.id.tv_refresh_header_updatetime);


        //小技巧

        //得到该控件的高度  onMeasure  onLayout onDraw
        //需要先measue之后，getheigth才会获取正确的高度
        headerview.measure(0,0);
        headerHeigth = headerview.getMeasuredHeight();

        //默认情况下，将下拉刷新的headerview 隐藏起来
        headerview.setPadding(0,-headerHeigth,0,0); //

        addHeaderView(headerview);

        //创建一个旋转动画待用
        rotateAnimation = new RotateAnimation(0,180, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setDuration(200);
        rotateAnimation.setFillAfter(true);//停留在最后一帧


    }

    float startx;
    float starty;
    float endx;
    float endy;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()){

            case MotionEvent.ACTION_DOWN:

                startx =ev.getRawX();
                starty=ev.getRawY();


                Log.i(TAG,"startx:"+startx+"starty"+starty);
                break;

            case MotionEvent.ACTION_MOVE:


                if (startx==0&&starty==0){

                    startx=ev.getRawX();
                    starty=ev.getRawY();

                }

                if (current_state==REFRESHING)
                    break;

                endx=ev.getRawX();
                endy=ev.getRawY();

                Log.i(TAG,"endx:"+endx+"endy"+endy);

                float dx= Math.abs(startx-endx);
                float dy= Math.abs(starty-endy);

                if(dy>dx){  //竖直方向处理

                    if(endy>starty){
                        headerview.setPadding(0, (int) (-headerHeigth+dy),0,0); //

                        if (-headerHeigth+dy>0&&current_state==NEED_PULL){
                            current_state=NEED_RELEASE;
                            //需要去更新当前headview的一些显示内容
                             updateHeaderView();
                            Log.i(TAG,"状态变为需要松手");
                        }
                    }
                }

                break;

            case MotionEvent.ACTION_UP:
                if (current_state==NEED_RELEASE){
                    current_state=REFRESHING;
                    Log.i(TAG,"状态变为正在刷新");
                    headerview.setPadding(0, 0,0,0); //
                    //要去更新header里显示的内容
                     updateHeaderView();

                    //在这里去更新listview的内容
                    //这里调用一个函数，该函数让使用者去实现。
                    if (refreshListener!=null){
                        refreshListener.onRefresh();
                    }


                }else if (current_state==NEED_PULL){

                    Log.i(TAG,"松手的时候，没有完全拉出来，则需要缩回去，重新隐藏掉");
                    headerview.setPadding(0, -headerHeigth,0,0); //
                }
                break;

        }



        return super.onTouchEvent(ev);
    }

    private void updateHeaderView() {

        switch (current_state){

            case NEED_RELEASE:

                tv_refresh_header_hint.setText("请松开手刷新");
                iv_refresh_header_arrow.startAnimation(rotateAnimation);


                break;

            case REFRESHING:
                tv_refresh_header_hint.setText("正在刷新");

                pb_refresh_header_loading.setVisibility(View.VISIBLE);

                iv_refresh_header_arrow.clearAnimation();
                iv_refresh_header_arrow.setVisibility(View.INVISIBLE);

                break;


        }

    }


    private   RefreshListener refreshListener;

    public interface RefreshListener{

        public void onRefresh();

        public void onLoadMore();
    }

    public void setOnRefreshListener(RefreshListener listener){

        refreshListener=listener;

    }

    public void onRefreshComplete(){

        headerview.setPadding(0, -headerHeigth,0,0); //
        tv_refresh_header_updatetime.setText(new Date().toLocaleString());
        tv_refresh_header_hint.setText("下拉刷新");
        pb_refresh_header_loading.setVisibility(View.INVISIBLE);
        iv_refresh_header_arrow.setVisibility(View.VISIBLE);
        current_state= NEED_PULL;


    }

    public void onLoadmoreComplete(){

        isLoadingMore =false;
        footerView.setPadding(0, -footerViewHeight,0,0); //


    }


}
