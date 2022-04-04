package com.lamaviedelivery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lamaviedelivery.R;
import com.lamaviedelivery.databinding.ItemStatusBinding;
import com.lamaviedelivery.databinding.ItemTransactionBinding;
import com.lamaviedelivery.model.OrderStatus;

import java.util.ArrayList;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.MyViewHolder> {
    Context context;
    ArrayList<OrderStatus.Result.StatusHistory> arrayList;

    public StatusAdapter(Context context, ArrayList<OrderStatus.Result.StatusHistory> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemStatusBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_status, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (arrayList.get(position).status.equals("Confirmed"))
            holder.binding.ivStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.confirmed_active));
        else if (arrayList.get(position).status.equals("Pickup"))
            holder.binding.ivStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.pickup_active));
        else if (arrayList.get(position).status.equals("Progress"))
            holder.binding.ivStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.progress_active));
        else if (arrayList.get(position).status.equals("Shipped"))
            holder.binding.ivStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.shipped_active));
        else if (arrayList.get(position).status.equals("Delivered"))
            holder.binding.ivStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.delivered_active));

        holder.binding.tvStatusName.setText(arrayList.get(position).status);
        holder.binding.tvStatusName.setText(arrayList.get(position).description);
        holder.binding.tvTime.setText(arrayList.get(position).statusTime);


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemStatusBinding binding;

        public MyViewHolder(@NonNull ItemStatusBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}

