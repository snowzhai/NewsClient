package com.example.administrator.newclient.menupage.impl;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.newclient.Constans;
import com.example.administrator.newclient.R;
import com.example.administrator.newclient.ShowNewsActivity;
import com.example.administrator.newclient.bean.Categories;
import com.example.administrator.newclient.bean.MenuDetialNews;
import com.example.administrator.newclient.utils.SharedPrefUtils;
import com.example.administrator.newclient.view.RefreshListView;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/4/12.
 */
public class HotNewsMenuPageDetail {
    Activity mActivity;
    protected View mRootView;
    private ViewPager vp_hotnewsdetail_picture;
    private String moreUrl;
    private MyListViewAdapter myListViewAdapter;

    private ArrayList<MenuDetialNews.MenuDetailData.NewsData> newsDataSource;

    Categories.NewsTypeInfo.ChildrenInfo childrenInfo;
    private MenuDetialNews menuDetialNews;
    private TextView tv_topnews_title;
    private CirclePageIndicator indicator_hotnewstop_indicator;
    private RefreshListView lv_hotnewsdetail_news;
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
        lv_hotnewsdetail_news = (RefreshListView) view.findViewById(R.id.lv_hotnewsdetail_news);
        //把头部抽取为单独的header layout，最终用它来填充一个view
        final View headerview = View.inflate(mActivity, R.layout.list_newsheader, null);
        vp_hotnewsdetail_picture = (ViewPager) headerview.findViewById(R.id.vp_hotnewsdetail_picture);
        tv_topnews_title = (TextView) headerview.findViewById(R.id.tv_topnews_title);
        indicator_hotnewstop_indicator = (CirclePageIndicator) headerview.findViewById(R.id.indicator_hotnewstop_indicator);
        lv_hotnewsdetail_news.addHeaderView(headerview);//将headview在listview中作为头部放在listview中 之后的位置啥的都会自动改变的 不用管

        lv_hotnewsdetail_news.setOnRefreshListener(new RefreshListView.RefreshListener() {
            @Override
            public void onRefresh() {
                Log.i("TAG","用户通过下拉操作，触发了更新，所以这里应该去更新");
                initData();//再次初始化数据
            }

            @Override
            public void onLoadMore() {
                Log.i("TAG","用户滑到了最底部，触发了更新，所以这里应该去加载更多");
                if (moreUrl.isEmpty()){
                    lv_hotnewsdetail_news.onLoadmoreComplete();
                    Toast.makeText(mActivity,"没有更多了，休息一会",Toast.LENGTH_LONG).show();
                }else {
                    moreUrl=Constans.SERVER_ADDR+moreUrl;
                    HttpUtils httpUtils = new HttpUtils();
                    httpUtils.send(HttpRequest.HttpMethod.GET, moreUrl, new RequestCallBack<String>(){
                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            Gson gson = new Gson();
                            MenuDetialNews menuDetialNews = gson.fromJson(responseInfo.result, MenuDetialNews.class);
                            newsDataSource.addAll(menuDetialNews.data.news);
                            myListViewAdapter.notifyDataSetChanged();
                            moreUrl= menuDetialNews.data.more;
                            lv_hotnewsdetail_news.onLoadmoreComplete();
                        }
                        @Override
                        public void onFailure(HttpException e, String s) {
                            Toast.makeText(mActivity, "加载失败，请稍后再试", Toast.LENGTH_SHORT).show();
                            lv_hotnewsdetail_news.onLoadmoreComplete();
                        }
                    });
                }
            }
        });

        lv_hotnewsdetail_news.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                final MenuDetialNews.MenuDetailData.NewsData newsData = newsDataSource.get(position - 2);
                final String url = newsData.url;
                Intent intent = new Intent(mActivity, ShowNewsActivity.class);
                intent.putExtra("url",url);
                mActivity.startActivity(intent);


                 SharedPreferences sp= mActivity.getSharedPreferences("readnewsids",mActivity.MODE_APPEND);
                 String readdis = sp.getString("readdis", "");
                 if (!readdis.contains(newsData.id+""))
                 {
                     readdis = readdis+","+ newsData.id;

                     final SharedPreferences.Editor edit = sp.edit();

                     edit.putString("readdis",readdis);

                     edit.commit();

                     //方法1
                     //myListViewAdapter.notifyDataSetChanged();
                     //方法2 直接修改传进来的view
                     final TextView tv_newsitme_title = (TextView) view.findViewById(R.id.tv_newsitme_title);
                     tv_newsitme_title.setTextColor(Color.GRAY);


                 }


            }
        });

        mRootView =view;

    }
    public void initData() {
        String url = Constans.SERVER_ADDR+ childrenInfo.url;
        final String jsonFromCache = SharedPrefUtils.getJsonFromCache(url, mActivity);
        if (jsonFromCache.isEmpty())
            getDataFromSever(url);
        else
            parseData(jsonFromCache);
    }

    private void getDataFromSever(String url) {
        HttpUtils httpUtils = new HttpUtils();
        final String key = url;
        httpUtils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String json =    responseInfo.result;
                SharedPrefUtils.saveJsonToCache(json,key,mActivity);
                parseData(json);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.i("哈哈",s.toString());
                lv_hotnewsdetail_news.onRefreshComplete();

                Toast.makeText( mActivity, "网络异常，请稍后重试", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void parseData(String json) {
        Gson gson = new Gson();//gson解析数据
        menuDetialNews = gson.fromJson(json, MenuDetialNews.class);
        moreUrl = menuDetialNews.data.more;
        newsDataSource = menuDetialNews.data.news;
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
        myListViewAdapter = new MyListViewAdapter();
        lv_hotnewsdetail_news.setAdapter(myListViewAdapter);
        lv_hotnewsdetail_news.onRefreshComplete();
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
            return newsDataSource.size();
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

            bitmapUtils.display(iv_newsitem_newsimage,newsDataSource.get(position).listimage);


            SharedPreferences sp= mActivity.getSharedPreferences("readnewsids",mActivity.MODE_PRIVATE);
            String readdis = sp.getString("readdis", "");


            tv_newsitme_title.setText(newsDataSource.get(position).title);

            if (readdis.contains(newsDataSource.get(position).id+""))
                tv_newsitme_title.setTextColor(Color.GRAY);
            else
                tv_newsitme_title.setTextColor(Color.BLACK);


            tv_newsitme_time.setText(newsDataSource.get(position).pubdate);

            return view;
        }
    }

}
