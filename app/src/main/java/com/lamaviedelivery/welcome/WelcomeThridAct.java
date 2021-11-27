package com.lamaviedelivery.welcome;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.lamaviedelivery.LandingAct;
import com.lamaviedelivery.R;
import com.lamaviedelivery.databinding.ActivityWelcomeThirdBinding;


public class WelcomeThridAct extends AppCompatActivity {
    ActivityWelcomeThirdBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_welcome_third);
        initViews();
    }

    private void initViews() {
        binding.btnNext.setOnClickListener(v -> {
            startActivity(new Intent(WelcomeThridAct.this, LandingAct.class));
            finish();
        });
    }
}
