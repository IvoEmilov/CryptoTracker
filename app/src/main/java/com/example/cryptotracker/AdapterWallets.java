package com.example.cryptotracker;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class AdapterWallets extends RecyclerView.Adapter<AdapterWallets.ViewHolder>{
    private ArrayList<Wallet> wallets;
    Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvWalletName, tvWalletUSD, tvWalletAmount;
        ImageView imgWallet;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvWalletName = itemView.findViewById(R.id.tvWalletName);
            tvWalletUSD = itemView.findViewById(R.id.tvWalletUSD);
            tvWalletAmount = itemView.findViewById(R.id.tvWalletAmount);
            imgWallet = itemView.findViewById(R.id.imgWallet);
        }
    }

    public AdapterWallets(Context context, ArrayList<Wallet> wallets){
        this.context = context;
        this.wallets = wallets;
    }

    @NonNull
    @Override
    public AdapterWallets.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wallet, parent, false);
        AdapterWallets.ViewHolder viewHolder = new AdapterWallets.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterWallets.ViewHolder holder, int position) {
        Wallet wallet = wallets.get(position);
        holder.tvWalletName.setText(wallet.getWalletName());
        holder.tvWalletUSD.setText(wallet.getWalletUSD());
        holder.tvWalletAmount.setText(wallet.getWalletAmount());
        holder.imgWallet.setImageResource(R.drawable.wallet_bitcoin);
    }

    @Override
    public int getItemCount() {
        return wallets.size();
    }
}