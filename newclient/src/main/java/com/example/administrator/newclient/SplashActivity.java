package com.example.administrator.newclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
/*
* time：2016.4.8
* author：snow
* */
//实现开启app的动画界面
public class SplashActivity extends Activity {

    private RelativeLayout rl_splash_bg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        rl_splash_bg = (RelativeLayout) findViewById(R.id.rl_splash_bg);

        AnimationSet as = new AnimationSet(true);//组合动画的时候需要
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(3000);

        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(3000);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);//透明度由全透0到实体1
        alphaAnimation.setDuration(3000);
        as.addAnimation(rotateAnimation);
        as.addAnimation(scaleAnimation);
        as.addAnimation(alphaAnimation);
        rl_splash_bg.setAnimation(as);
        as.start();
        //设置动画监听器
        as.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //动画开始的时候调用
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                    //动画结束的时候调用
                startActivity(new Intent(SplashActivity.this,GuideActivity.class));
                finish();//将当前的activity从任务栈清除
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                //动画重复的时候调用
            }
        });
    }
}
