package com.lamaviedelivery.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.lamaviedelivery.R;
import com.lamaviedelivery.adapter.StatusAdapter;
import com.lamaviedelivery.databinding.FragmentOrderStatusBinding;
import com.lamaviedelivery.listener.StatusListener;
import com.lamaviedelivery.model.OrderStatus;
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

public class OrderStatusBottomSheet extends BottomSheetDialogFragment {
    public String TAG = "OrderStatusBottomSheet";
    FragmentOrderStatusBinding binding;
    BottomSheetDialog dialog;
    private BottomSheetBehavior<View> mBehavior;
    StatusListener listener;


    private LamavieDeliveryInterface apiInterface;
    ArrayList<OrderStatus.Result.StatusHistory>arrayList;
    StatusAdapter adapter;
    String orderId="";


    public OrderStatusBottomSheet(String orderId) {
        this.orderId = orderId;
    }


    public OrderStatusBottomSheet callBack(StatusListener listener) {
        this.listener = listener;
        return this;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.fragment_order_status, null, false);
        dialog.setContentView(binding.getRoot());
        mBehavior = BottomSheetBehavior.from((View) binding.getRoot().getParent());
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        initViews();
        return dialog;
    }

    private void initViews() {
        apiInterface = ApiClient.getClient().create(LamavieDeliveryInterface.class);
        arrayList = new ArrayList<>();

        adapter = new StatusAdapter(getActivity(),arrayList);
        binding.rvOrder.setAdapter(adapter);

        if(NetworkAvailablity.checkNetworkStatus(getActivity())) getOrderStatus();
        else Toast.makeText(getActivity(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();

        binding.ivBack.setOnClickListener(v -> dialog.dismiss());

    }


    public void getOrderStatus(){
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("order_id", DataManager.getInstance().getUserData(getActivity()).result.id);
        Log.e(TAG, "Wallet Login Request " + map);
        Call<OrderStatus> loginCall = apiInterface.getStatus(map);
        loginCall.enqueue(new Callback<OrderStatus>() {
            @Override
            public void onResponse(Call<OrderStatus> call, Response<OrderStatus> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    OrderStatus data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "Wallet Login Response :" + responseString);
                    if (data.status.equals("1")) {
                        double amount = Double.parseDouble(data.result.price);
                        binding.tvtotal.setText("$" + String.format("%.2f",amount ));
                        binding.tvOrderId.setText("Order ID : " + data.result.id);
                        binding.tvDateTime.setText(data.result.pickupDate);
                        arrayList.clear();
                        arrayList.addAll(data.result.statusHistory);
                        adapter.notifyDataSetChanged();
                    } else if (data.status.equals("0")){
                        arrayList.clear();
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getActivity(), data.message, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<OrderStatus> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }



}
