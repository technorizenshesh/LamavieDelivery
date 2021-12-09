package com.lamaviedelivery;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.lamaviedelivery.databinding.ActivityOrderDetailBinding;
import com.lamaviedelivery.model.BookingModel;
import com.lamaviedelivery.utils.DataManager;

public class OrderDetailAct extends AppCompatActivity {
    ActivityOrderDetailBinding binding;
    BookingModel.Result model;
    double total = 0.00, tax = 10.00, subTotal = 0.00;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_detail);
        initViews();
    }

    private void initViews() {

        if (getIntent() != null)
            model = (BookingModel.Result) getIntent().getParcelableExtra("orderDetail");

        if (model != null) {
            binding.tvOrderId.setText("Order ID :" + model.id);
            binding.tvDateTime.setText(DataManager.convertDateToString3(model.pickupDate) + " at " + DataManager.convertStringAmPm(model.pickupTime));

            subTotal = Double.parseDouble(model.price);
            total = subTotal + tax;

            binding.tvSubtotal.setText("$" + String.format("%.2f", subTotal));
            binding.tvTax.setText("$" + String.format("%.2f", tax));
            binding.tvtotal.setText("$" + String.format("%.2f", total));
        }


    }
}
