package com.example.cryptotracker;

import android.content.Context;
import android.gesture.Gesture;
import android.graphics.Color;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;


import java.text.DecimalFormat;
import java.util.ArrayList;

public class AdapterTransactions extends RecyclerView.Adapter<AdapterTransactions.ViewHolder> implements ItemTouchHelperAdapter{
    private ArrayList<CoinTransaction> coinTransactions;
    Context context;
    private String symbol;
    private ItemTouchHelper itemTouchHelper;

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        CoinTransaction fromTransaction = coinTransactions.get(fromPosition);
        coinTransactions.remove(fromTransaction);
        coinTransactions.add(toPosition, fromTransaction);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemSwiped(int position) {
        coinTransactions.remove(position);
        notifyItemRemoved(position);
    }

    public void setItemTouchHelper(ItemTouchHelper itemTouchHelper){
        this.itemTouchHelper = itemTouchHelper;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener, GestureDetector.OnGestureListener {
        private TextView tvBuySell, tvAmountCoin, tvUSD, tvDate;
        private GestureDetector gestureDetector;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            gestureDetector = new GestureDetector(itemView.getContext(), this);
            tvBuySell = itemView.findViewById(R.id.tvBuySell);
            tvAmountCoin = itemView.findViewById(R.id.tvAmountCoin);
            tvUSD = itemView.findViewById(R.id.tvUSD);
            tvDate = itemView.findViewById(R.id.tvDate);
        }

        @Override
        public boolean onDown(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {
            itemTouchHelper.startDrag(this);
        }

        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            gestureDetector.onTouchEvent(motionEvent);
            return true;
        }
    }

    public AdapterTransactions(Context context, ArrayList<CoinTransaction> coinTransactions, String symbol){
        this.context = context;
        this.coinTransactions = coinTransactions;
        this.symbol=symbol;
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
        CoinTransaction transaction = coinTransactions.get(position);
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(12);

        holder.tvBuySell.setText(transaction.getBuySell());
        if(transaction.getBuySell().equals("Buy")){
            holder.tvBuySell.setTextColor(Color.parseColor("#116540"));//green
            holder.tvUSD.setText(String.format("$%.2f", transaction.getUSD()));
            holder.tvAmountCoin.setText("+"+df.format(transaction.getAmountCoin())+" "+symbol);
        }
        else{
            holder.tvBuySell.setTextColor(Color.parseColor("#e57069"));//red
            holder.tvUSD.setText(String.format("$%.2f", transaction.getUSD()).replace("$-","-$"));
            holder.tvAmountCoin.setText(df.format(transaction.getAmountCoin())+" "+symbol);
        }
        holder.tvDate.setText(transaction.getDate());


    }

    @Override
    public int getItemCount() {
        return coinTransactions.size();
    }
}
