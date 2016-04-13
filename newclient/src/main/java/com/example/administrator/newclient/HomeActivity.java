package com.example.administrator.newclient;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.example.administrator.newclient.fragment.ContentFragment;
import com.example.administrator.newclient.fragment.LeftMenuFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
//这个函数的主界面
public class HomeActivity extends SlidingFragmentActivity {

    private SlidingMenu slidingMenu;//SlidingMenu滑动开关
    private LeftMenuFragment leftMenuFragment;
    private ContentFragment contentFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setBehindContentView(R.layout.layout_leftnemu);//给侧滑设置一个布局
        //得到一个侧滑的菜单
        slidingMenu = getSlidingMenu();
        slidingMenu.setMode(SlidingMenu.LEFT);//设置划出来的方向
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        slidingMenu.setBehindOffset(350);//设置漏出来的页面的长度（像素）
        //得到碎片管理者
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //左边侧滑的碎片
        leftMenuFragment = new LeftMenuFragment();
        //右边正文的碎片
        contentFragment = new ContentFragment();
        //将主页面的两个部分分成两个碎片 因为这两个部分逻辑太多 分开能更好的实现其逻辑 调理更清晰
        fragmentTransaction.replace(R.id.ll_leftmenu_newsclass, leftMenuFragment,"leftmenufragment");//将左边的替换为左划的碎片
        fragmentTransaction.replace(R.id.fl_main_content, contentFragment,"contentfragment");
        fragmentTransaction.commit();

    }
    public  SlidingMenu getMySlidingMenu(){
        return  slidingMenu;
    }
    public void setSilidingMenuEnable(boolean enable){
        if (enable){
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);//让侧滑菜单显示
        }else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);//让侧滑菜单不显示
        }

    }

    public LeftMenuFragment getLeftMenuFragment(){
        //返回成员变量
        //方法1；
//        return leftMenuFragment;
        //方法2：
        FragmentManager fragmentManager = getFragmentManager();
        LeftMenuFragment leftmenufragment = (LeftMenuFragment) fragmentManager.findFragmentByTag("leftmenufragment");
        return leftmenufragment;

    }

    public ContentFragment getContentFragment(){
        //返回成员变量
        //方法1；
//        return leftMenuFragment;
        //方法2：
        FragmentManager fragmentManager = getFragmentManager();
        final ContentFragment contentFragment = (ContentFragment) fragmentManager.findFragmentByTag("contentfragment");
        return contentFragment;

    }
    //设置主页面上显示侧边的侧滑菜单的显示和隐藏
    public void setSlidingMenuToggle(){
        slidingMenu.toggle();
    }

}
