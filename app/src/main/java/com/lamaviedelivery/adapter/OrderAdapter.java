package com.lamaviedelivery.adapter;

import android.content.Context;
import android.util.Log;
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
    ArrayList<String> stringArrayList = new ArrayList<>();
    double count =0.0;

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
        if(arrayList.get(position).extraOptions.size()!=0){
            holder.binding.layoutExtra.setVisibility(View.VISIBLE);
            stringArrayList.clear();
            for (int i=0;i<arrayList.get(position).extraOptions.size();i++){
                stringArrayList.add(arrayList.get(position).extraOptions.get(i).name);
                count = count + Double.parseDouble(arrayList.get(position).extraOptions.get(i).price);
            }

            holder.binding.tvItemName.setText(AddCommaValues());
            holder.binding.tvItemPrice.setText("$"+String.format("%.2f",count));
        }
        else {
            holder.binding.layoutExtra.setVisibility(View.GONE);

        }
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


    public String  AddCommaValues(){
        StringBuilder str = new StringBuilder("");

        // Traversing the ArrayList
        for (String eachstring : stringArrayList) {

            // Each element in ArrayList is appended
            // followed by comma
            str.append(eachstring).append(",");
        }

        // StringBuffer to String conversion
        String commaseparatedlist = str.toString();

        // By following condition you can remove the last
        // comma
        if (commaseparatedlist.length() > 0)
            commaseparatedlist
                    = commaseparatedlist.substring(
                    0, commaseparatedlist.length() - 1);

        Log.e("AddedString===",commaseparatedlist);
        return commaseparatedlist;
    }
}
