package com.lamaviedelivery;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.lamaviedelivery.databinding.ActivityGenderBinding;


public class GenderAt extends AppCompatActivity {
    ActivityGenderBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_gender);
        initViews();
    }

    private void initViews() {
        binding.btnBack.setOnClickListener(v -> finish());

       binding.btnNext.setOnClickListener(v -> {
                   startActivity(new Intent(GenderAt.this, LandingAct.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
           finish();
       });


    }
}
