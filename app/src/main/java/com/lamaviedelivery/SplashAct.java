package com.lamaviedelivery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.lamaviedelivery.databinding.ActivitySplashBinding;
import com.lamaviedelivery.welcome.WecomeFirstAct;


public class SplashAct extends AppCompatActivity {
    ActivitySplashBinding binding;
    public static int SPLASH_TIME_OUT = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_splash);
        processNextActivity();
    }

    private void processNextActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashAct.this, WecomeFirstAct.class));
                    finish();
            }
        }, SPLASH_TIME_OUT);
    }

}