package com.example.administrator.newclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

public class ShowNewsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_news);


        final Intent intent = getIntent();

        final String url = intent.getStringExtra("url");

        final WebView wv_shownews_content = (WebView) findViewById(R.id.wv_shownews_content);

        wv_shownews_content.loadUrl(url);



    }

    public void back(View v){

        finish();

    }
}
