package com.example.administrator.newclient.menupage.impl;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
import com.viewpagerindicator.CirclePageIndicator;

/**
 * Created by Administrator on 2016/4/12.
 */
public class HotNewsMenuPageDetail {
    Activity mActivity;
    protected View mRootView;
    private ViewPager vp_hotnewsdetail_picture;

    Categories.NewsTypeInfo.ChildrenInfo childrenInfo;
    private MenuDetialNews menuDetialNews;
    private TextView tv_topnews_title;
    private CirclePageIndicator indicator_hotnewstop_indicator;
    private ListView lv_hotnewsdetail_news;
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
        lv_hotnewsdetail_news = (ListView) view.findViewById(R.id.lv_hotnewsdetail_news);
        //把头部抽取为单独的header layout，最终用它来填充一个view
        final View headerview = View.inflate(mActivity, R.layout.list_newsheader, null);
        vp_hotnewsdetail_picture = (ViewPager) headerview.findViewById(R.id.vp_hotnewsdetail_picture);
        tv_topnews_title = (TextView) headerview.findViewById(R.id.tv_topnews_title);
        indicator_hotnewstop_indicator = (CirclePageIndicator) headerview.findViewById(R.id.indicator_hotnewstop_indicator);
        lv_hotnewsdetail_news.addHeaderView(headerview);//将headview在listview中作为头部放在listview中 之后的位置啥的都会自动改变的 不用管
        mRootView=view;
    }
    public void initData() {
        //开源Google框架获得gson数据
        String url = Constans.SERVER_ADDR+ childrenInfo.url;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();//gson解析数据
                menuDetialNews = gson.fromJson(responseInfo.result, MenuDetialNews.class);
                Log.i("哈哈", menuDetialNews.toString());
                vp_hotnewsdetail_picture.setAdapter(new MyViewPagerAdapter());
                indicator_hotnewstop_indicator.setViewPager(vp_hotnewsdetail_picture);//实现图片上的小点的移动
                //有indicator_hotnewstop_indicator的话setOnPageChangeListener应该设在indicator上
                indicator_hotnewstop_indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        tv_topnews_title.setText(menuDetialNews.data.topnews.get(position).title);
                    }

                    @Override
                    public void onPageSelected(int position) {

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
                lv_hotnewsdetail_news.setAdapter(new MyListViewAdapter());
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
            //为了解决图片不能及时下载的问题 采取了预加载的方式
            bitmapUtils.configDefaultLoadingImage(R.drawable.topnews_item_default);
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
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);//将图片填满布局
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
    class MyListViewAdapter extends BaseAdapter{
        private final BitmapUtils bitmapUtils;

        public MyListViewAdapter() {

            bitmapUtils = new BitmapUtils(mActivity);
            bitmapUtils.configDefaultLoadingImage(R.drawable.pic_item_list_default);

        }
        @Override
        public int getCount() {
            return menuDetialNews.data.news.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(mActivity, R.layout.list_news_item, null);
            final ImageView iv_newsitem_newsimage = (ImageView) view.findViewById(R.id.iv_newsitem_newsimage);

            final TextView tv_newsitme_title = (TextView) view.findViewById(R.id.tv_newsitme_title);
            final TextView tv_newsitme_time = (TextView) view.findViewById(R.id.tv_newsitme_time);

            bitmapUtils.display(iv_newsitem_newsimage,menuDetialNews.data.news.get(position).listimage);
            tv_newsitme_title.setText(menuDetialNews.data.news.get(position).title);

            tv_newsitme_time.setText(menuDetialNews.data.news.get(position).pubdate);

            return view;
        }
    }

}
