package com.lamaviedelivery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.lamaviedelivery.R;
import com.lamaviedelivery.databinding.ItemOrderHistoryBinding;
import com.lamaviedelivery.model.HistoryModel;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {
    Context context;
    ArrayList<HistoryModel.Result>arrayList;
    ArrayList<String>stringArrayList = new ArrayList<>();


    public HistoryAdapter(Context context, ArrayList<HistoryModel.Result> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderHistoryBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_order_history, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.tvName.setText(arrayList.get(position).userDetails.firstName + " " + arrayList.get(position).userDetails.lastName);
        holder.binding.tvPrice.setText("$"+arrayList.get(position).price);
        holder.binding.tvOrderId.setText("Id : " + arrayList.get(position).id);
        for(int i = 0;i<arrayList.get(position).itemDetails.size();i++){
            if (arrayList.get(position).itemDetails.size()!=0)
                stringArrayList.add(arrayList.get(position).itemDetails.get(i).childCateName);
        }
        if (arrayList.get(position).itemDetails.size()!=0) holder.binding.tvItems.setText(commaSeprated(stringArrayList));


        if(arrayList.get(position).status.equals("Confirmed")){
            holder.binding.btnStatus.setText(context.getString(R.string.confirmed));
            holder.binding.btnStatus.setBackgroundResource(R.color.color_green_one);
        }

        else if(arrayList.get(position).status.equals("Progress")){
            holder.binding.btnStatus.setText(arrayList.get(position).status);
            holder.binding.btnStatus.setBackgroundResource(R.color.color_yellow_one);
        }


        else if(arrayList.get(position).status.equals("Pickup")){
            holder.binding.btnStatus.setText(arrayList.get(position).status);
            holder.binding.btnStatus.setBackgroundResource(R.color.color_next);
        }


        else if(arrayList.get(position).status.equals("Delivered")){
            holder.binding.btnStatus.setText(arrayList.get(position).status);
            holder.binding.btnStatus.setBackgroundResource(R.color.color_green_one);

        }

        else if(arrayList.get(position).status.equals("Cancel")){
            holder.binding.btnStatus.setText(arrayList.get(position).status);
            holder.binding.btnStatus.setBackgroundResource(R.color.red);
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemOrderHistoryBinding binding;
        public MyViewHolder(@NonNull ItemOrderHistoryBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }


    public String commaSeprated(ArrayList<String> arrayList) {
        StringBuilder result = new StringBuilder();
        for (String string : arrayList) {
            result.append(string);
            result.append(",");
        }
        return result.length() > 0 ? /*result.substring(0, result.length() - 1)*/result.deleteCharAt(result.length() - 1).toString() : "";
    }
}
