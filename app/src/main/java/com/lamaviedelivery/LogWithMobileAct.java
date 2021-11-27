package com.lamaviedelivery;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.lamaviedelivery.databinding.ActivityLoginMobileBinding;


public class LogWithMobileAct extends AppCompatActivity {
    ActivityLoginMobileBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login_mobile);
        initViews();
    }

    private void initViews() {
        binding.btnSubmit.setOnClickListener(v -> startActivity(new Intent(LogWithMobileAct.this,VerifyActivity.class)
        .putExtra("number",binding.etMobile.getText().toString())
        .putExtra("code",binding.ccp.getSelectedCountryCode())));
    }
}
