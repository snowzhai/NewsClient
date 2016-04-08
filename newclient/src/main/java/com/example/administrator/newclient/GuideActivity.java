package com.example.administrator.newclient;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends Activity {

    private ViewPager vp_guide_show;
    List<MyPageInfo> pageInfoList;
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
        vp_guide_show = (ViewPager) findViewById(R.id.vp_guide_show);
        for (int i=0;i<3;i++){
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(imgid[i]);
            MyPageInfo myPageInfo = new MyPageInfo();
            myPageInfo.iv=imageView;
            myPageInfo.pageTitle="page"+i;
            pageInfoList.add(myPageInfo);
        }
        vp_guide_show.setAdapter(new MyPagerAdapter());

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
}
