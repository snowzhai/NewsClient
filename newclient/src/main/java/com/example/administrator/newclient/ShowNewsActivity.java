package com.example.administrator.newclient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class ShowNewsActivity extends Activity {

    private WebSettings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_news);
        //在ProgressBar布局中给它设了一个旋转的动画
        final ProgressBar pb_shownews_loading = (ProgressBar) findViewById(R.id.pb_shownews_loading);
        final Intent intent = getIntent();
        final String url = intent.getStringExtra("url");
        final WebView wv_shownews_content = (WebView) findViewById(R.id.wv_shownews_content);
        wv_shownews_content.setWebViewClient(new WebViewClient(){
            //在WebView完成的时候将progressbar隐藏掉
            @Override
            public void onPageFinished(WebView view, String url) {
                pb_shownews_loading.setVisibility(View.INVISIBLE);
                super.onPageFinished(view, url);
            }
        });
        wv_shownews_content.loadUrl(url);
        settings = wv_shownews_content.getSettings();//得到一个webview的设置
    }
    public void back(View v){
        finish();
    }

    //调整字体大小
    int current_choice=2;
    public void resize(View v){
        String []choices={"超大号字体","大号字体","正常字体","小号字体","超小号字体"};
        new AlertDialog.Builder(this)
                .setTitle("调整字体")
                .setSingleChoiceItems(choices, current_choice, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        current_choice=which;
                    }
                }).setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(current_choice){
                    case 0:
                        settings.setTextZoom(200);
                        break;
                    case 1:
                        settings.setTextZoom(150);
                        break;
                    case 2:
                        settings.setTextZoom(100);
                        break;
                    case 3:
                        settings.setTextZoom(70);
                        break;
                    case 4:
                        settings.setTextZoom(50);
                        break;
                }
            }
        })
                .setNegativeButton("取消",null)
                .show();
    }
    public void share(View v){

            ShareSDK.initSDK(this);
            OnekeyShare oks = new OnekeyShare();
            //关闭sso授权
            oks.disableSSOWhenAuthorize();

            // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
            //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
            // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
            oks.setTitle("我的测试分享");
            // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
            oks.setTitleUrl("http://sharesdk.cn");
            // text是分享文本，所有平台都需要这个字段
            oks.setText("我是分享文本");
            // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
            oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
            // url仅在微信（包括好友和朋友圈）中使用
            oks.setUrl("http://sharesdk.cn");
            // comment是我对这条分享的评论，仅在人人网和QQ空间使用
            oks.setComment("我是测试评论文本");
            // site是分享此内容的网站名称，仅在QQ空间使用
            oks.setSite(getString(R.string.app_name));
            // siteUrl是分享此内容的网站地址，仅在QQ空间使用
            oks.setSiteUrl("http://sharesdk.cn");

            // 启动分享GUI
            oks.show(this);

    }
}
