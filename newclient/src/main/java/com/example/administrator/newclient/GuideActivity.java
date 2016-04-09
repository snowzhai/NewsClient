package com.example.administrator.newclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends Activity {

    private ViewPager vp_guide_show;
    List<MyPageInfo> pageInfoList;
    private View redpoint_guide_indicator;
    private Button bt_guide_enter;
    private LinearLayout ll_guide_indicator;

    class MyPageInfo{
        ImageView iv;
        String pageTitle;
    }
    int []imgid={R.drawable.guide_1,R.drawable.guide_2,R.drawable.guide_3};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pageInfoList=new ArrayList<>();
        bt_guide_enter = (Button) findViewById(R.id.bt_guide_enter);
        vp_guide_show = (ViewPager) findViewById(R.id.vp_guide_show);
        redpoint_guide_indicator = findViewById(R.id.redpoint_guide_indicator);
        ll_guide_indicator = (LinearLayout) findViewById(R.id.ll_guide_indicator);
        init();
        vp_guide_show.setAdapter(new MyPagerAdapter());
        //设置滑动页面监听器恩
        vp_guide_show.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //当页面滑动的时候调用 position 当前的页面张数   positionOffset  第二个页面进来的比例  positionOffsetPixels 第二个页面进来的宽度
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) redpoint_guide_indicator.getLayoutParams();
                layoutParams.leftMargin=position*40+(int)(positionOffset*40);
                redpoint_guide_indicator.setLayoutParams(layoutParams);
                Log.i("哈哈",position+"--"+positionOffset+"--"+positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                //选中页面的时候调用  焦点到当前在的页面时
                if (position==2){
                    bt_guide_enter.setVisibility(View.VISIBLE);//当到第3个页面的时候 将button的属性设置为可见的
                }else {
                    bt_guide_enter.setVisibility(View.GONE);//其它时 设置为不可见
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
                //
            }
        });
    }

    private void init() {
        //初始化3张图片
        for (int i=0;i<3;i++){
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(imgid[i]);
            MyPageInfo myPageInfo = new MyPageInfo();
            myPageInfo.iv=imageView;
            myPageInfo.pageTitle="page"+i;
            pageInfoList.add(myPageInfo);
            //初始化指示器的3个滑动的小点
            View view = new View(this);
            view.setBackgroundResource(R.drawable.greypoint);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
            if (i!=0){
                params.leftMargin=20;
            }
                view.setLayoutParams(params);//将布局设到view中
                ll_guide_indicator.addView(view);
        }
    }

    class MyPagerAdapter extends PagerAdapter{
        //返回显示页面的个数
        @Override
        public int getCount() {
            return pageInfoList.size();
        }
        //实例化item  返回值为一个存储这个页面数据的对象 即isViewFromObject中传入的object。
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            MyPageInfo myPageInfo = pageInfoList.get(position);//新建一个当前位置的myPageInfo（页面信息）
            container.addView(myPageInfo.iv);   //  将页面信息中的图片传入
            return myPageInfo;
        }
        //判断当前页面对应的view是否是在当前页面对应的对象（object）中。
        @Override
        public boolean isViewFromObject(View view, Object object) {
            MyPageInfo pageInfo= (MyPageInfo) object;
            return view==pageInfo.iv;
        }
        //销毁滑出屏幕的view
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            MyPageInfo PageInfo= (MyPageInfo) object;
            container.removeView(PageInfo.iv);
//            super.destroyItem(container, position, object);
        }
    }
    public void enterHome(View v){
        startActivity(new Intent(this,HomeActivity.class));
        finish();
    }
}
