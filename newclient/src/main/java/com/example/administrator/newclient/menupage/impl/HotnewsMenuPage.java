package com.example.administrator.newclient.menupage.impl;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.newclient.HomeActivity;
import com.example.administrator.newclient.R;
import com.example.administrator.newclient.bean.Categories;
import com.example.administrator.newclient.menupage.BaseMenuPage;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.TabPageIndicator;

/**
 * Created by Administrator on 2016/4/12.
 */
//这给页面主要是实现侧边栏当点击新闻的时候出现的界面
public class HotnewsMenuPage extends BaseMenuPage {
    private Categories.NewsTypeInfo menupageInfo;
    private ViewPager vp_hotnewsmenupage_class;
    private TabPageIndicator indicator_hotnewsmenupage_title;

    public HotnewsMenuPage(Activity mActivity, Categories.NewsTypeInfo menuinfo) {
        super(mActivity);

        menupageInfo=menuinfo;
    }
    //初始化控件
    @Override
    public View initView() {
        final View view = View.inflate(mActivity, R.layout.hotnews_menupage, null);
        vp_hotnewsmenupage_class = (ViewPager) view.findViewById(R.id.vp_hotnewsmenupage_class);
        //找到ViewPagerIndicator的控件
        indicator_hotnewsmenupage_title = (TabPageIndicator) view.findViewById(R.id.indicator_hotnewsmenupage_title);
        return view;
    }

    @Override
    public void initData() {
        //使用ViewPagerIndicator的开源控件 实现页面上屏幕滑动的并且改变上面标题的变化效果
        vp_hotnewsmenupage_class.setAdapter(new MyhotnewsmenupageViewPager());
        indicator_hotnewsmenupage_title.setViewPager(vp_hotnewsmenupage_class);

        //如果vp 设置了Indicator，那么它的PageChangeListener 应该给到Indicator
        //这里主要是为了解决在新闻界面 在北京 中国 国际等的在它的第一张图的时候会划出侧边栏的问题 在这里做了判断 如果是第一个即北京的时候才能划出来 别的不能划出来
        indicator_hotnewsmenupage_title.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            final HomeActivity mActivity = (HomeActivity) HotnewsMenuPage.this.mActivity;
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position==0){
                    mActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                }else {
                    mActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                }
            }
            @Override
            public void onPageSelected(int position) {
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }
    class MyhotnewsmenupageViewPager extends PagerAdapter{
        //ViewPagerIndicator的需要
        @Override
        public CharSequence getPageTitle(int position) {
            return menupageInfo.children.get(position).title;
        }

        @Override
        public int getCount() {
            return menupageInfo.children.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            /*TextView textView = new TextView(mActivity);
            textView.setText(menupageInfo.children.get(position).title);
            textView.setTextColor(Color.RED);
            container.addView(textView);
            textView.setTextSize(20);
            return textView;*/
            HotNewsMenuPageDetail detail = new HotNewsMenuPageDetail(mActivity, menupageInfo.children.get(position));
            container.addView(detail.mRootView);
            return detail.mRootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
//            super.destroyItem(container, position, object);
        }
    }

}
