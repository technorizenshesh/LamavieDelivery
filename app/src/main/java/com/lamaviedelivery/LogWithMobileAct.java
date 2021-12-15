package com.lamaviedelivery;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.lamaviedelivery.databinding.ActivityLoginMobileBinding;
import com.lamaviedelivery.retrofit.ApiClient;
import com.lamaviedelivery.retrofit.LamavieDeliveryInterface;
import com.lamaviedelivery.utils.DataManager;
import com.lamaviedelivery.utils.NetworkAvailablity;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LogWithMobileAct extends AppCompatActivity {
    public String TAG = "LogWithMobileAct";
    ActivityLoginMobileBinding binding;
    LamavieDeliveryInterface apiInterface;
    String refreshedToken="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(LamavieDeliveryInterface.class);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login_mobile);
        initViews();
    }

    private void initViews() {
        binding.btnSubmit.setOnClickListener(v -> {validation();});
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> {
            try {
                refreshedToken = instanceIdResult.getToken();
                Log.e("Token===", refreshedToken);
                // Yay.. we have our new token now.
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }


    private void validation() {
        if (binding.etMobile.getText().toString().equals("")) {
            binding.etMobile.setError(getString(R.string.please_enter_mobile_number));
            binding.etMobile.setFocusable(true);
        }
        else {
            if(NetworkAvailablity.checkNetworkStatus(LogWithMobileAct.this)) userLogin();
            else Toast.makeText(this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();


        }
    }

    public void userLogin() {
        DataManager.getInstance().showProgressMessage(LogWithMobileAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("mobile", binding.etMobile.getText().toString());
        // map.put("country_code", "+"+binding.ccp.getSelectedCountryCode());
        map.put("type", "DRIVER");
        //   map.put("otp_time",DataManager.getCurrentTime123());
        Log.e(TAG, "Login Request " + map);
        Call<Map<String,String>> loginCall = apiInterface.userLogin(map);
        loginCall.enqueue(new Callback<Map<String,String>>() {
            @Override
            public void onResponse(Call<Map<String,String>> call, Response<Map<String,String>> response) {
                DataManager.getInstance().hideProgressMessage();

                try {
                    Map<String,String> data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "Login Response :" + responseString);
                    if (data.get("status").equals("1")) {
                        Toast.makeText(LogWithMobileAct.this, data.get("message"), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LogWithMobileAct.this, VerifyActivity.class)
                                .putExtra("user_id",data.get("user_id"))
                                .putExtra("number", binding.etMobile.getText().toString())
                                .putExtra("code",binding.ccp.getSelectedCountryCode()+"")
                                .putExtra("refreshedToken",refreshedToken));
                        //    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
//();
                    } else if (data.get("status").equals("0"))
                        Toast.makeText(LogWithMobileAct.this, data.get("message"), Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Map<String,String>> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });

    }


}

