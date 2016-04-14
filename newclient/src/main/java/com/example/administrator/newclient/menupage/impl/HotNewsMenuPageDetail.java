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
 * Created by Lan on 2016/4/12.
 */
public class HotNewsMenuPageDetail {

    private static final String TAG ="HotNewsMenuPageDetail" ;
    Activity mActivity;
    protected View mRootView;
    private ViewPager vp_hotnewsdetail_picture;

    Categories.NewsTypeInfo.ChildrenInfo childrenInfo;
    private MenuDetialNews menuDetialNews;
    private TextView tv_topnews_title;
    private CirclePageIndicator indicator_hotnewstop_indicator;
    private RefreshListView lv_hotnewsdetail_news;
    private String moreUrl;
    private ArrayList<MenuDetialNews.MenuDetailData.NewsData> newsDataSource;
    private MyListViewAdapter myListViewAdapter;

    public HotNewsMenuPageDetail(Activity mActivity, Categories.NewsTypeInfo.ChildrenInfo childrenInfo) {
        this.mActivity = mActivity;
        this.childrenInfo=childrenInfo;

        initView();
        initData();
    }

    public void initView(){

        final View view = View.inflate(mActivity, R.layout.hotnews_detailpage, null);

        lv_hotnewsdetail_news = (RefreshListView) view.findViewById(R.id.lv_hotnewsdetail_news);


        //把头部抽取为单独的header layout，最终用它来填充一个view
        final View headerview = View.inflate(mActivity, R.layout.list_newsheader, null);

        vp_hotnewsdetail_picture = (ViewPager) headerview.findViewById(R.id.vp_hotnewsdetail_picture);

        tv_topnews_title = (TextView) headerview.findViewById(R.id.tv_topnews_title);

        indicator_hotnewstop_indicator = (CirclePageIndicator) headerview.findViewById(R.id.indicator_hotnewstop_indicator);


        lv_hotnewsdetail_news.addHeaderView(headerview);

        lv_hotnewsdetail_news.setOnRefreshListener(new RefreshListView.RefreshListener() {
            @Override
            public void onRefresh() {
                //
                Log.i("TAG","用户通过下拉操作，触发了更新，所以这里应该去更新");
                initData();
            }

            @Override
            public void onLoadMore() {
                Log.i("TAG","用户滑到了最底部，触发了更新，所以这里应该去加载更多");

                if (moreUrl.isEmpty()){
                    lv_hotnewsdetail_news.onLoadmoreComplete();
                    Toast.makeText(mActivity, "没有更多了，休息一会", Toast.LENGTH_SHORT).show();
                }else{

                    moreUrl= Constans.SERVER_ADDR+moreUrl;


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


    public void initData(){


        String url = Constans.SERVER_ADDR+ childrenInfo.url;

        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>()
        {


            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                Log.i(TAG,responseInfo.result);

                //Gson 解析数据

                Gson gson = new Gson();
                menuDetialNews = gson.fromJson(responseInfo.result, MenuDetialNews.class);
//                Log.i(TAG, menuDetialNews.toString());

                moreUrl = menuDetialNews.data.more;

                newsDataSource = menuDetialNews.data.news;

                vp_hotnewsdetail_picture.setAdapter(new MyViewPagerAdapter());

                indicator_hotnewstop_indicator.setViewPager(vp_hotnewsdetail_picture);

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

            @Override
            public void onFailure(HttpException e, String s) {
                Log.i(TAG,e.toString());

                lv_hotnewsdetail_news.onRefreshComplete();

                Toast.makeText( mActivity, "网络异常，请稍后重试", Toast.LENGTH_SHORT).show();
            }
        }
        );

    }



    class MyViewPagerAdapter extends PagerAdapter{


        private BitmapUtils bitmapUtils;

        public MyViewPagerAdapter( ) {
            this.bitmapUtils =   new BitmapUtils(mActivity);
            bitmapUtils.configDefaultLoadFailedImage(R.drawable.topnews_item_default);
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

            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            imageView.setBackgroundResource(R.drawable.topnews_item_default);
            final String url = menuDetialNews.data.topnews.get(position).topimage;
            //可以去下载，然后封装一个bitmap
            Log.d(TAG,url);
            bitmapUtils.display(imageView,url);

            container.addView(imageView);

            return imageView;// super.instantiateItem(container, position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

             container.removeView((View) object);
            //  super.destroyItem(container, position, object);
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


            final View view = View.inflate(mActivity, R.layout.list_news_item,null);

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
