package com.lamaviedelivery;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.lamaviedelivery.databinding.ActivityVerifyBinding;


public class VerifyActivity extends AppCompatActivity {
    ActivityVerifyBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_verify);
        initViews();
    }

    private void initViews() {
        if(getIntent()!=null){
            binding.tvText.setText(getString(R.string.otp_verify_one) + " " +  getIntent().getStringExtra("code") + ""+
                    getIntent().getStringExtra("number") + " " + getString(R.string.otp_verify_one));
        }
        binding.btnSubmit.setOnClickListener(v -> {
            startActivity(new Intent(VerifyActivity.this, HomeAct.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
        });

        binding.btnBack.setOnClickListener(v -> finish());

    }

}