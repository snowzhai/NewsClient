package com.example.administrator.newclient.menupage.impl;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.newclient.R;
import com.example.administrator.newclient.bean.Categories;
import com.example.administrator.newclient.menupage.BaseMenuPage;
import com.viewpagerindicator.TabPageIndicator;

/**
 * Created by Administrator on 2016/4/12.
 */
public class HotnewsMenuPage extends BaseMenuPage {
    private Categories.NewsTypeInfo menupageInfo;
    private ViewPager vp_hotnewsmenupage_class;
    private TabPageIndicator indicator_hotnewsmenupage_title;

    public HotnewsMenuPage(Activity mActivity, Categories.NewsTypeInfo menuinfo) {
        super(mActivity);

        menupageInfo=menuinfo;
    }
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
        //使用ViewPagerIndicator的开源控件 实现页面上屏幕滑动的小点变化效果
        vp_hotnewsmenupage_class.setAdapter(new MyhotnewsmenupageViewPager());
        indicator_hotnewsmenupage_title.setViewPager(vp_hotnewsmenupage_class);

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
