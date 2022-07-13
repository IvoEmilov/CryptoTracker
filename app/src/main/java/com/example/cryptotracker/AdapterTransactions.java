package com.example.cryptotracker;

import android.app.Activity;
import android.content.Context;
import android.gesture.Gesture;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;


import java.text.DecimalFormat;
import java.util.ArrayList;

public class AdapterTransactions extends RecyclerView.Adapter<AdapterTransactions.ViewHolder> implements ItemTouchHelperAdapter{
    private ArrayList<CoinTransaction> coinTransactions;
    private Context context;
    private String symbol;
    private Double currPrice;
    private ItemTouchHelper itemTouchHelper;
    private DecimalFormat df = new DecimalFormat("#");
    private Database db = new Database();

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        CoinTransaction fromTransaction = coinTransactions.get(fromPosition);
        coinTransactions.remove(fromTransaction);
        coinTransactions.add(toPosition, fromTransaction);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemSwiped(int position) {
        CoinTransaction coinTransaction = coinTransactions.get(position);
        coinTransactions.remove(coinTransaction);

        Double value = 0.0, holdings = 0.0;
        for(CoinTransaction transaction : coinTransactions){
            value += transaction.getUSD();
            holdings += transaction.getAmountCoin();
        }

        for(int i=0;i<MainActivity.coins.size();i++){
            if(MainActivity.coins.get(i).getSymbol().equals(symbol)){
                MainActivity.coins.get(i).removeCoinTransaction(coinTransaction);
                db.setTransactions(MainActivity.coins.get(i));
                MainActivity.cardItems.get(i).setHoldings(holdings);
                MainActivity.cardItems.get(i).setValue(value);
                MainActivity.adapter.notifyDataSetChanged();
            }
        }

        TextView tvTotalBalance = (TextView) ((Activity) context).findViewById(R.id.tvTotalBalance);
        TextView tvTotalInvested = (TextView) ((Activity) context).findViewById(R.id.tvTotalInvested);
        TextView tvHoldings = (TextView) ((Activity) context).findViewById(R.id.tvTotalHoldings);
        TextView tvAvgBuyPrice = (TextView) ((Activity) context).findViewById(R.id.tvAvgBuyPrice);
        TextView tvPNL = (TextView) ((Activity) context).findViewById(R.id.tvPNL);
        LinearLayout llPNL = (LinearLayout) ((Activity) context).findViewById(R.id.llPNL);

        tvTotalBalance.setText("$"+String.format("%.2f", holdings*currPrice));
        //tvTotalInvested.setText("$"+String.format("%.2f", value)+" Total investment");
        tvHoldings.setText(df.format(holdings)+" "+symbol);

        Double buyValue = 0.0;
        Double buyHoldings = 0.0;

        for(CoinTransaction transaction:coinTransactions){
            if(transaction.getBuySell().equals("Buy")){
                buyValue+=transaction.getUSD();
                buyHoldings+=transaction.getAmountCoin();
            }
        }
        tvTotalInvested.setText("$"+String.format("%.2f", buyValue)+" Total investment");

        try{
            Double buyAverage = buyValue/buyHoldings;

            if(!Double.isNaN(buyAverage)){
                tvAvgBuyPrice.setText(String.format("$%.2f", buyAverage));
            }
        }
        catch(ArithmeticException e){
            e.printStackTrace();
            tvAvgBuyPrice.setText("$0");
        }

        try{
            Double average = value/holdings;
            Double pnl = holdings*currPrice - holdings*average;

            if(pnl>0){
                llPNL.setBackground(context.getResources().getDrawable(R.drawable.colour_minty));
                tvPNL.setText(String.format("$%.2f", pnl));
            }
            else if(pnl<0){
                llPNL.setBackground(context.getResources().getDrawable(R.drawable.colour_red));
                tvPNL.setText(String.format("$%.2f", pnl).replace("$-","-$"));
            }
        }
        catch (ArithmeticException e){
            e.printStackTrace();
            tvPNL.setText("$0");
        }

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

    public AdapterTransactions(Context context, ArrayList<CoinTransaction> coinTransactions, String symbol, Double currPrice){
        this.context = context;
        this.coinTransactions = coinTransactions;
        this.symbol=symbol;
        this.currPrice = currPrice;
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
            holder.tvUSD.setText(String.format("-$%.2f", transaction.getUSD()));
            holder.tvAmountCoin.setText("+"+df.format(transaction.getAmountCoin())+" "+symbol);
        }
        else{
            holder.tvBuySell.setTextColor(Color.parseColor("#e57069"));//red
            holder.tvUSD.setText(String.format("$%.2f", transaction.getUSD()).replace("$-","$"));
            holder.tvAmountCoin.setText(df.format(transaction.getAmountCoin())+" "+symbol);
        }
        holder.tvDate.setText(transaction.getDate());


    }

    @Override
    public int getItemCount() {
        return coinTransactions.size();
    }
}
