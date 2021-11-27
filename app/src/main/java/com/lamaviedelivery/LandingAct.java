package com.lamaviedelivery;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.lamaviedelivery.databinding.ActivityLandingBinding;


public class LandingAct extends AppCompatActivity {
    ActivityLandingBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_landing);
        initViews();
    }

    private void initViews() {

        binding.btnMobile.setOnClickListener(v -> startActivity(new Intent(LandingAct.this,LogWithMobileAct.class)));

        binding.tvDonHaveAccount.setOnClickListener(v -> startActivity(new Intent(LandingAct.this,SignupAct.class)));
    }
}
