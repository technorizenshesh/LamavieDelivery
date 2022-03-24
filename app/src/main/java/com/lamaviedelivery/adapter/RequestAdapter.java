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
import com.lamaviedelivery.listener.onPosListener;
import com.lamaviedelivery.model.BookingModel;

import java.util.ArrayList;

public class  RequestAdapter extends RecyclerView.Adapter<RequestAdapter.MyViewHolder> {
    Context context;
    ArrayList<BookingModel.Result>arrayList;
    onPosListener listener;
    ArrayList<String>stringArrayList = new ArrayList<>();


    public RequestAdapter(Context context, ArrayList<BookingModel.Result> arrayList, onPosListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener =listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRequestBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_request, parent, false);
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


        if(arrayList.get(position).status.equals("Accept")){
            holder.binding.btnStatus.setText(context.getString(R.string.confirmed));
            holder.binding.btnStatus.setBackgroundResource(R.color.color_green_one);
        }

       else if(arrayList.get(position).status.equals("Pending")){
            holder.binding.btnStatus.setText(arrayList.get(position).status);
            holder.binding.btnStatus.setBackgroundResource(R.color.color_yellow_one);
        }


        else if(arrayList.get(position).status.equals("Pickup")){
            holder.binding.btnStatus.setText(arrayList.get(position).status);
            holder.binding.btnStatus.setBackgroundResource(R.color.color_next);
        }


        else if(arrayList.get(position).status.equals("complete")){
            holder.binding.btnStatus.setVisibility(View.GONE);

        }


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemRequestBinding binding;
        public MyViewHolder(@NonNull ItemRequestBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

            binding.btnStatus.setOnClickListener(v -> listener.onPos(getAdapterPosition()));
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
