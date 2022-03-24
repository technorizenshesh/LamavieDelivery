package com.lamaviedelivery.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.lamaviedelivery.OrderDetailAct;
import com.lamaviedelivery.R;
import com.lamaviedelivery.adapter.RequestAdapter;
import com.lamaviedelivery.databinding.FragmentInprogressBinding;
import com.lamaviedelivery.listener.onPosListener;
import com.lamaviedelivery.model.BookingModel;
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

public class NewFragment extends Fragment implements onPosListener {
    public String TAG = "NewFragment";
    LamavieDeliveryInterface apiInterface;
    FragmentInprogressBinding binding;
    ArrayList<BookingModel.Result> arrayList;
    RequestAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_inprogress, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiInterface = ApiClient.getClient().create(LamavieDeliveryInterface.class);
        arrayList = new ArrayList<>();

        adapter = new RequestAdapter(getActivity(), arrayList,NewFragment.this);
        binding.rvInprogress.setAdapter(adapter);

        if (NetworkAvailablity.checkNetworkStatus(getActivity())) getBookings();
        else
            Toast.makeText(getActivity(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();

    }


    public void getBookings() {
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(getActivity()).result.id);
        map.put("status", "Pickup");
        Log.e(TAG, "get Current Booking Request" + map);
        Call<BookingModel> loginCall = apiInterface.getAllBooking(map);
        loginCall.enqueue(new Callback<BookingModel>() {
            @Override
            public void onResponse(Call<BookingModel> call, Response<BookingModel> response) {
                DataManager.getInstance().hideProgressMessage();

                try {
                    BookingModel data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "get Current Booking Response :" + responseString);
                    if (data.status.equals("1")) {
                        binding.tvNotFound.setVisibility(View.GONE);
                        arrayList.clear();
                        arrayList.addAll(data.result);
                        adapter.notifyDataSetChanged();

                    } else if (data.status.equals("0")) {
                        arrayList.clear();
                        adapter.notifyDataSetChanged();
                        binding.tvNotFound.setVisibility(View.VISIBLE);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<BookingModel> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    @Override
    public void onPos(int position) {
       // BookingAccept(arrayList.get(position).id);
        startActivity(new Intent(getActivity(), OrderDetailAct.class)
                .putExtra("orderId",arrayList.get(position).id));
    }


    public void BookingAccept(String id) {
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("order_id", id);
        map.put("status", "Pickup");
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

                        if (NetworkAvailablity.checkNetworkStatus(getActivity())) getBookings();
                        else
                            Toast.makeText(getActivity(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
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

}
