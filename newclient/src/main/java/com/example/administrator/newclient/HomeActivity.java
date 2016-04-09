package com.example.administrator.newclient;

import android.os.Bundle;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class HomeActivity extends SlidingFragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setBehindContentView(R.layout.layout_leftnemu);//给侧滑设置一个布局
        SlidingMenu slidingMenu = getSlidingMenu();//得到一个侧滑的菜单
        slidingMenu.setMode(SlidingMenu.LEFT);//设置划出来的方向
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        slidingMenu.setBehindOffset(400);//设置漏出来的页面的长度（像素）
    }
}
