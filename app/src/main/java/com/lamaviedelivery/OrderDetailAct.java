package com.lamaviedelivery;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.lamaviedelivery.adapter.OrderAdapter;
import com.lamaviedelivery.databinding.ActivityOrderDetailBinding;
import com.lamaviedelivery.databinding.FragmentInprogressBinding;
import com.lamaviedelivery.model.BookingModel;
import com.lamaviedelivery.model.OrderDetailModel;
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

public class OrderDetailAct extends AppCompatActivity {
    public String TAG = "OrderDetailAct";
    ActivityOrderDetailBinding binding;
    LamavieDeliveryInterface apiInterface;
    OrderDetailModel model;
    String orderId="";
    double total = 0.00, tax = 10.00, subTotal = 0.00;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(LamavieDeliveryInterface.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_detail);
        initViews();
    }

    private void initViews() {
        binding.ivBack.setOnClickListener(v -> finish());

        if (getIntent() != null) {
            orderId = getIntent().getStringExtra("orderId");

            if (NetworkAvailablity.checkNetworkStatus(OrderDetailAct.this)) getOrderDetail(orderId);
            else
                Toast.makeText(OrderDetailAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
        }

        binding.btnAccept.setOnClickListener(v -> {
            if (NetworkAvailablity.checkNetworkStatus(OrderDetailAct.this)) BookingAccept(orderId,"Accept");
            else
                Toast.makeText(OrderDetailAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
        });


        binding.btnDecline.setOnClickListener(v -> {
            if (NetworkAvailablity.checkNetworkStatus(OrderDetailAct.this)) BookingAccept(orderId,"Cancel");
            else
                Toast.makeText(OrderDetailAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
        });

        binding.layoutTrack.setOnClickListener(v -> {
            startActivity(new Intent(OrderDetailAct.this,TrackAct.class).putExtra("OrderDetail",model));
        });


    }

    public void BookingAccept(String id,String status) {
        DataManager.getInstance().showProgressMessage(OrderDetailAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("order_id", id);
        map.put("status", status);
        Log.e(TAG, "get Current Booking Request" + map);
        Call<Map<String, String>> loginCall = apiInterface.requestAcceptCancel(map);
        loginCall.enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                DataManager.getInstance().hideProgressMessage();

                try {
                    Map<String, String> data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "get Current Booking Response :" + responseString);
                    if (data.get("status").equals("1")) {

                        if (NetworkAvailablity.checkNetworkStatus(OrderDetailAct.this)) getOrderDetail(orderId);
                        else
                            Toast.makeText(OrderDetailAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
                    } else if (data.get("status").equals("0")) {

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


    public void getOrderDetail(String id) {
        DataManager.getInstance().showProgressMessage(OrderDetailAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("order_id", id);
        Log.e(TAG, "get Current OrderDetail Request" + map);
        Call<OrderDetailModel> loginCall = apiInterface.getOrderDetailsss(map);
        loginCall.enqueue(new Callback<OrderDetailModel>() {
            @Override
            public void onResponse(Call<OrderDetailModel> call, Response<OrderDetailModel> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    model = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "get Current OrderDetail Response :" + responseString);
                    if (model.status.equals("1")) {
                        binding.tvOrderId.setText("Order ID :" + model.result.id);
                        binding.tvDateTime.setText(DataManager.convertDateToString3(model.result.pickupDate) + " at " + DataManager.convertStringAmPm(model.result.pickupTime));

                        subTotal = Double.parseDouble(model.result.price);
                        total = subTotal + tax;

                        binding.tvSubtotal.setText("$" + String.format("%.2f", subTotal));
                        binding.tvTax.setText("$" + String.format("%.2f", tax));
                        binding.tvtotal.setText("$" + String.format("%.2f", total));
                        binding.rvOrder.setAdapter(new OrderAdapter(OrderDetailAct.this, (ArrayList<OrderDetailModel.Result.ItemDetail>) model.result.itemDetails));

                        if(model.result.status.equals("Accept")) {
                            binding.layoutButton.setVisibility(View.VISIBLE);
                            binding.layoutTrack.setVisibility(View.GONE);
                        }
                        else {
                            binding.layoutButton.setVisibility(View.GONE);
                            binding.layoutTrack.setVisibility(View.VISIBLE);
                        }



                    } else if (model.status.equals("0")) {
                        Toast.makeText(OrderDetailAct.this, model.message, Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<OrderDetailModel> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }




}
