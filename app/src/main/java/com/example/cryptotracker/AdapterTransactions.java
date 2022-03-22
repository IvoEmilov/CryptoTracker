package com.example.cryptotracker;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class AdapterTransactions extends RecyclerView.Adapter<AdapterTransactions.ViewHolder>{
    private ArrayList<TransactionItem> transactionItems;
    Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvBuySell, tvAmountCoin, tvUSD, tvDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBuySell = itemView.findViewById(R.id.tvBuySell);
            tvAmountCoin = itemView.findViewById(R.id.tvAmountCoin);
            tvUSD = itemView.findViewById(R.id.tvUSD);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }

    public AdapterTransactions(Context context, ArrayList<TransactionItem> transactionItems){
        this.context = context;
        this.transactionItems = transactionItems;
    }

    @NonNull
    @Override
    public AdapterTransactions.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_transaction, parent, false);
        AdapterTransactions.ViewHolder viewHolder = new AdapterTransactions.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterTransactions.ViewHolder holder, int position) {
        TransactionItem transaction = transactionItems.get(position);
        holder.tvBuySell.setText(transaction.getBuySell());
        holder.tvDate.setText(transaction.getDate());
        holder.tvUSD.setText(transaction.getUSD());
        holder.tvAmountCoin.setText(transaction.getAmountCoin());
    }

    @Override
    public int getItemCount() {
        return transactionItems.size();
    }
}
