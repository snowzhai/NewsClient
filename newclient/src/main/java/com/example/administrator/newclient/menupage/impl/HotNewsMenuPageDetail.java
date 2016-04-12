package com.example.administrator.newclient.menupage.impl;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.administrator.newclient.Constans;
import com.example.administrator.newclient.R;
import com.example.administrator.newclient.bean.Categories;
import com.example.administrator.newclient.bean.MenuDetialNews;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * Created by Administrator on 2016/4/12.
 */
public class HotNewsMenuPageDetail {
    Activity mActivity;
    protected View mRootView;
    private ViewPager vp_hotnewsdetail_picture;

    Categories.NewsTypeInfo.ChildrenInfo childrenInfo;
    private MenuDetialNews menuDetialNews;
    public HotNewsMenuPageDetail(Activity mActivity, Categories.NewsTypeInfo.ChildrenInfo childrenInfo) {
        this.mActivity = mActivity;
        this.childrenInfo=childrenInfo;

        initView();
        initData();
    }
    //初始化窗口
    public void initView() {
        //这个布局为显示界面的布局
        View view = View.inflate(mActivity, R.layout.hotnews_detailpage, null);
        vp_hotnewsdetail_picture = (ViewPager) view.findViewById(R.id.vp_hotnewsdetail_picture);
        mRootView=view;
    }
    public void initData() {
        //开源Google框架获得json数据
        String url = Constans.SERVER_ADDR+ childrenInfo.url;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();//gson解析数据
                menuDetialNews = gson.fromJson(responseInfo.result, MenuDetialNews.class);
                Log.i("哈哈",menuDetialNews.toString());
                vp_hotnewsdetail_picture.setAdapter(new MyViewPagerAdapter());
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.i("哈哈",s.toString());
            }
        });
    }
    class MyViewPagerAdapter extends PagerAdapter{
        private BitmapUtils bitmapUtils;
        public MyViewPagerAdapter( ) {
            this.bitmapUtils =   new BitmapUtils(mActivity);

        }
        @Override
        public int getCount() {
            return menuDetialNews.data.topnews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(mActivity);
//            imageView.setImageResource(R.drawable.w22);
            String url = menuDetialNews.data.topnews.get(position).topimage;
            Log.d("哈哈",url);
            bitmapUtils.display(imageView,url);
            container.addView(imageView);
            return imageView;//super.instantiateItem(container, position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
//            super.destroyItem(container, position, object);
        }
    }
}
