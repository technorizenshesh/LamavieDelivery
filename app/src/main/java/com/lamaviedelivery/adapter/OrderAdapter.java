package com.lamaviedelivery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.lamaviedelivery.R;
import com.lamaviedelivery.databinding.ItemOrderBinding;
import com.lamaviedelivery.model.BookingModel;
import com.lamaviedelivery.model.OrderDetailModel;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {
    Context context;
    ArrayList<OrderDetailModel.Result.ItemDetail>arrayList ;

    public OrderAdapter(Context context, ArrayList<OrderDetailModel.Result.ItemDetail> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      ItemOrderBinding binding =  DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_order,parent,false);
      return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.tvPrice.setText("$"+arrayList.get(position).price);
        holder.binding.tvName.setText(arrayList.get(position).childCateName);
        holder.binding.tvQuantity1.setText(arrayList.get(position).quantity);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemOrderBinding binding;
        public MyViewHolder(@NonNull ItemOrderBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
