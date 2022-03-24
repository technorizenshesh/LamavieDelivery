package com.lamaviedelivery;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.lamaviedelivery.adapter.HistoryAdapter;
import com.lamaviedelivery.databinding.ActivityOrderHistoryBinding;
import com.lamaviedelivery.model.HistoryModel;
import com.lamaviedelivery.retrofit.ApiClient;
import com.lamaviedelivery.retrofit.LamavieDeliveryInterface;
import com.lamaviedelivery.utils.DataManager;
import com.lamaviedelivery.utils.NetworkAvailablity;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryAct extends AppCompatActivity {
    ActivityOrderHistoryBinding binding;
    LamavieDeliveryInterface apiInterface;
    HistoryAdapter adapter;
    ArrayList<HistoryModel.Result> arrayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_history);
        apiInterface = ApiClient.getClient().create(LamavieDeliveryInterface.class);
        initViews();
    }

    private void initViews() {
        binding.ivBack.setOnClickListener(v -> finish());

        arrayList = new ArrayList<>();

        adapter = new HistoryAdapter(OrderHistoryAct.this, arrayList);
        binding.rvHistory.setAdapter(adapter);

    }


    private void getAllHistory() {
        Map<String, String> map = new HashMap<>();
        map.put("driver_id", DataManager.getInstance().getUserData(OrderHistoryAct.this).result.id + "");
        Log.e("MapMap", "GET History REQUEST" + map);
        Call<HistoryModel> subCategoryCall = apiInterface.getOrderHistory(map);
        subCategoryCall.enqueue(new Callback<HistoryModel>() {
            @Override
            public void onResponse(Call<HistoryModel> call, Response<HistoryModel> response) {
                try {
                    HistoryModel data = response.body();
                    String dataResponse = new Gson().toJson(response.body());
                    Log.e("MapMap", "GET PROVIDER LATLONG RESPONSE" + dataResponse);
                    if (data.status.equals("1")) {
                        binding.tvNotfound.setVisibility(View.GONE);
                        arrayList.clear();
                        arrayList.addAll(data.result);
                        adapter.notifyDataSetChanged();

                    } else if (data.status.equals("0")) {
                        arrayList.clear();
                        adapter.notifyDataSetChanged();
                        binding.tvNotfound.setVisibility(View.VISIBLE);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onFailure(Call<HistoryModel> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (NetworkAvailablity.checkNetworkStatus(OrderHistoryAct.this)) getAllHistory();
        else
            Toast.makeText(OrderHistoryAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
    }
}
