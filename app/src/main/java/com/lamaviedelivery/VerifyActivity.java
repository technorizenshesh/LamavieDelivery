package com.lamaviedelivery;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.lamaviedelivery.databinding.ActivityVerifyBinding;
import com.lamaviedelivery.model.LoginModel;
import com.lamaviedelivery.retrofit.ApiClient;
import com.lamaviedelivery.retrofit.Constant;
import com.lamaviedelivery.retrofit.LamavieDeliveryInterface;
import com.lamaviedelivery.utils.DataManager;
import com.lamaviedelivery.utils.NetworkAvailablity;
import com.lamaviedelivery.utils.SessionManager;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class VerifyActivity extends AppCompatActivity {
    public String TAG = "VerifyActivity";
    ActivityVerifyBinding binding;
    String refreshedToken = "", userId = "", number = "", code = "";
    LamavieDeliveryInterface apiInterface;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(LamavieDeliveryInterface.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_verify);
        initViews();
    }

    private void initViews() {
        if (getIntent() != null) {
            refreshedToken = getIntent().getStringExtra("refreshedToken");
            userId = getIntent().getStringExtra("user_id");
            code = getIntent().getStringExtra("code");
            number = getIntent().getStringExtra("number");

            binding.tvText.setText(getString(R.string.otp_verify_one) + " " + code + "" +
                    number + " " + getString(R.string.otp_verify_one));
        }
        binding.btnSubmit.setOnClickListener(v -> {
            if(NetworkAvailablity.checkNetworkStatus(VerifyActivity.this)) chkOtp();
            else Toast.makeText(this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();

        });

        binding.btnBack.setOnClickListener(v -> finish());

    }


    public void chkOtp() {
        DataManager.getInstance().showProgressMessage(VerifyActivity.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("otp", "123456");
        map.put("register_id", refreshedToken);

        Log.e(TAG, "VerifyOtp Request " + map);
        Call<LoginModel> loginCall = apiInterface.chkOtpValid(map);
        loginCall.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    LoginModel data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "Login Response :" + responseString);
                    if (data.status.equals("1")) {
                        Toast.makeText(VerifyActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                        SessionManager.writeString(VerifyActivity.this, Constant.USER_INFO, responseString);
                        startActivity(new Intent(VerifyActivity.this, HomeAct.class).putExtra("home","1").addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    } else if (data.status.equals("0")) Toast.makeText(VerifyActivity.this, data.message, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });

    }

}