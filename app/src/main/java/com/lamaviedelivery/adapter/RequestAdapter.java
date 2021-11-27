package com.lamaviedelivery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.lamaviedelivery.R;
import com.lamaviedelivery.databinding.ItemRequestBinding;
import com.lamaviedelivery.databinding.ItemTransactionBinding;
import com.lamaviedelivery.model.BookingModel;

import java.util.ArrayList;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.MyViewHolder> {
    Context context;
    ArrayList<BookingModel>arrayList;


    public RequestAdapter(Context context, ArrayList<BookingModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRequestBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_request, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if(arrayList.get(position).getType().equals("new request")){
            holder.binding.btnStatus.setText(arrayList.get(position).getType());
            holder.binding.btnStatus.setBackgroundResource(R.color.color_green_one);
        }

       else if(arrayList.get(position).getType().equals("pending")){
            holder.binding.btnStatus.setText(arrayList.get(position).getType());
            holder.binding.btnStatus.setBackgroundResource(R.color.color_yellow_one);
        }


        else if(arrayList.get(position).getType().equals("progress")){
            holder.binding.btnStatus.setText("Order Detail");
            holder.binding.btnStatus.setBackgroundResource(R.color.color_next);
        }


        else if(arrayList.get(position).getType().equals("complete")){
            holder.binding.btnStatus.setVisibility(View.GONE);

        }


    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemRequestBinding binding;
        public MyViewHolder(@NonNull ItemRequestBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
