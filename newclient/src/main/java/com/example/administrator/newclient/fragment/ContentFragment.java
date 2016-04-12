package com.example.administrator.newclient.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.example.administrator.newclient.R;
import com.example.administrator.newclient.page.BasePage;
import com.example.administrator.newclient.page.impl.GovmentPage;
import com.example.administrator.newclient.page.impl.HomePage;
import com.example.administrator.newclient.page.impl.NewsPage;
import com.example.administrator.newclient.page.impl.SettingPage;
import com.example.administrator.newclient.page.impl.SmartServicePage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/11.
 */
public class ContentFragment extends Fragment {
    private RadioGroup rg_contentfragment_bottom;
    private ViewPager vp_contentfragment_pager;
    List<BasePage> pagelist;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content, null);
        vp_contentfragment_pager = (ViewPager) view.findViewById(R.id.vp_contentfragment_pager);
        rg_contentfragment_bottom = (RadioGroup) view.findViewById(R.id.rg_contentfragment_bottom);
        rg_contentfragment_bottom.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_contentfragment_home:
                        vp_contentfragment_pager.setCurrentItem(0,false);
                        pagelist.get(0).initData();
                        break;
                    case  R.id.rb_contentfragment_newscenter:
                        vp_contentfragment_pager.setCurrentItem(1,false);
                        pagelist.get(1).initData();
                        break;
                    case  R.id.rb_contentfragment_gov:
                        vp_contentfragment_pager.setCurrentItem(2,false);
                        pagelist.get(2).initData();
                        break;
                    case  R.id.rb_contentfragment_service:
                        vp_contentfragment_pager.setCurrentItem(3,false);
                        pagelist.get(3).initData();
                        break;
                    case  R.id.rb_contentfragment_setting:
                        vp_contentfragment_pager.setCurrentItem(4,false);
                        pagelist.get(4).initData();
                        break;
                }
            }
        });

        pagelist = new ArrayList<>();
        pagelist.add(new HomePage(getActivity()));
        pagelist.add(new NewsPage(getActivity()));
        pagelist.add(new GovmentPage(getActivity()));
        pagelist.add(new SmartServicePage(getActivity()));
        pagelist.add(new SettingPage(getActivity()));
        vp_contentfragment_pager.setAdapter(new MyFragmentViewPagerAdapter());
        //因为RadioGroup为一个多个的单选组按钮 所以可以在开始的时候就设置一个处于开启的状态 的单选按钮
        rg_contentfragment_bottom.check(R.id.rb_contentfragment_home);//让home成为默认的页面
        return view;
    }
    class MyFragmentViewPagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(pagelist.get(position).getmRootView());
            return pagelist.get(position).getmRootView();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
//            super.destroyItem(container, position, object);
        }
    }
    public NewsPage getNewsPage(){

        if (pagelist!=null)
            return (NewsPage) pagelist.get(1);//这里默认返回1是指当侧边栏点击时默认让新闻中心刷新view

        return  null;

    }


}
