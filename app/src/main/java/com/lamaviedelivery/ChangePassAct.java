package com.lamaviedelivery;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.lamaviedelivery.databinding.ActivityChangePassBinding;
import com.lamaviedelivery.retrofit.ApiClient;
import com.lamaviedelivery.retrofit.LamavieDeliveryInterface;
import com.lamaviedelivery.utils.DataManager;
import com.lamaviedelivery.utils.NetworkAvailablity;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePassAct extends AppCompatActivity {
    public String TAG = "ChangePassAct";
    ActivityChangePassBinding binding;
    LamavieDeliveryInterface apiInterface;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(LamavieDeliveryInterface.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_pass);
        initViews();
    }

    private void initViews() {
        binding.ivBack.setOnClickListener(v -> finish());

        binding.btnSave.setOnClickListener(v -> validation());
    }


    private void validation() {
         if (binding.edNewPass.getText().toString().equals("")) {
            binding.edNewPass.setError(getString(R.string.required));
            binding.edNewPass.setFocusable(true);
        } else if (binding.edConfirmPass.getText().toString().equals("")) {
            binding.edConfirmPass.setError(getString(R.string.required));
            binding.edConfirmPass.setFocusable(true);
        } else if (!binding.edConfirmPass.getText().toString().equals(binding.edNewPass.getText().toString())) {
            binding.edConfirmPass.setError(getString(R.string.password_does_not_matched));
            binding.edConfirmPass.setFocusable(true);
        } else {
            if (NetworkAvailablity.checkNetworkStatus(ChangePassAct.this)) ChangePassword();
            else
                Toast.makeText(this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
        }
    }


    public void ChangePassword() {
        DataManager.getInstance().showProgressMessage(ChangePassAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", String.valueOf(DataManager.getInstance().getUserData(ChangePassAct.this).result.id));
        map.put("password", binding.edNewPass.getText().toString());
        Log.e(TAG, "Change Password Request " + map);
        Call<Map<String, String>> loginCall = apiInterface.changePass( map);
        loginCall.enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    Map<String, String> data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "Change Password Response :" + responseString);
                    if (data.get("status").equals("1")) {
                        Toast.makeText(ChangePassAct.this, data.get("message"), Toast.LENGTH_SHORT).show();
                        finish();

                    } else if (data.get("status").equals("0")) {
                        Toast.makeText(ChangePassAct.this, data.get("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }
}
