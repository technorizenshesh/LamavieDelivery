package com.lamaviedelivery.welcome;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.lamaviedelivery.R;
import com.lamaviedelivery.databinding.ActivityWelcomeOneBinding;


public class WecomeFirstAct extends AppCompatActivity {
    ActivityWelcomeOneBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_welcome_one);
        initViews();
    }

    private void initViews() {
        binding.btnNext.setOnClickListener(v -> {
            startActivity(new Intent(WecomeFirstAct.this, WelcomeSecondAct.class));
            finish();
        });
    }

}
