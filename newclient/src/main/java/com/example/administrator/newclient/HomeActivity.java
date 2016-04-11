package com.example.administrator.newclient;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.example.administrator.newclient.fragment.ContentFragment;
import com.example.administrator.newclient.fragment.LeftMenuFragment;
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

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        LeftMenuFragment leftMenuFragment = new LeftMenuFragment();//左边侧滑的碎片
        ContentFragment contentFragment = new ContentFragment();//右边正文的碎片
        //将主页面的两个部分分成两个碎片 因为这两个部分逻辑太多 分开能更好的实现其逻辑 调理更清晰
        fragmentTransaction.replace(R.id.ll_leftmenu_newsclass,leftMenuFragment);
        fragmentTransaction.replace(R.id.fl_main_content,contentFragment);
        fragmentTransaction.commit();

    }
}
