package com.lamaviedelivery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.lamaviedelivery.R;
import com.lamaviedelivery.databinding.ItemTransactionBinding;


public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.MyViewHolder> {
    Context context;

    public TransactionAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTransactionBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_transaction, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemTransactionBinding binding;
        public MyViewHolder(@NonNull ItemTransactionBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
