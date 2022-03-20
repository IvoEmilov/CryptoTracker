package com.example.cryptotracker;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private ArrayList<CardItem> cardItems;
    Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView imgCoin;
        public TextView tvCoin, tvPrice, tvAmount, tv24hChange, tvPNL;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCoin = itemView.findViewById(R.id.imgCoin);
            tvCoin = itemView.findViewById(R.id.tvCoin);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tv24hChange = itemView.findViewById(R.id.tv24hChange);
            tvPNL = itemView.findViewById(R.id.tvPNL);
        }
    }
    public Adapter(Context context, ArrayList<CardItem> cardItems){
        this.cardItems = cardItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CardItem cardItem = cardItems.get(position);

        Picasso.with(context).load(cardItem.getImageURL()).into(holder.imgCoin);
        holder.tvCoin.setText(cardItem.getCoinName());
        holder.tv24hChange.setText(cardItem.getChange24h());
        if(cardItem.getChange24h().charAt(0)=='+'){
            holder.tv24hChange.setTextColor(Color.GREEN);
        }
        else{
            holder.tv24hChange.setTextColor(Color.RED);
        }
        holder.tvPrice.setText(cardItem.getPrice());
        holder.tvAmount.setText(cardItem.getAmount());
        holder.tvPNL.setText(cardItem.getPnl());

    }

    @Override
    public int getItemCount() {
        return cardItems.size();
    }
}
