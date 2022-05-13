package com.example.cryptotracker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Parcelable;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> implements ItemTouchHelperAdapter{
    private ArrayList<CardItem> cardItems;
    private Context context;
    private ItemTouchHelper itemTouchHelper;
    private Database db;

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        CardItem fromItem = cardItems.get(fromPosition);
        cardItems.remove(fromItem);
        cardItems.add(toPosition, fromItem);
        fromItem.getCoin().setPosition(toPosition);
        db.updateCoinPosition(fromItem.getCoin());
        System.out.println(fromItem.getCoin().getCryptocurrency()+" moved to position "+toPosition);
        if(fromPosition>toPosition){
            for(int i=toPosition+1;i<=fromPosition;i++){
                MainActivity.cardItems.get(i).getCoin().setPosition(MainActivity.cardItems.get(i).getCoin().getPosition()+1);
                System.out.println(MainActivity.cardItems.get(i).getCoin().getCryptocurrency() + " position changed to "+(MainActivity.cardItems.get(i).getCoin().getPosition()));
                db.updateCoinPosition(MainActivity.cardItems.get(i).getCoin());
            }
        }
        else{
            for(int i=fromPosition;i<toPosition;i++){
                MainActivity.cardItems.get(i).getCoin().setPosition(MainActivity.cardItems.get(i).getCoin().getPosition()-1);
                System.out.println(MainActivity.cardItems.get(i).getCoin().getCryptocurrency() + " position changed to "+(MainActivity.cardItems.get(i).getCoin().getPosition()));
                db.updateCoinPosition(MainActivity.cardItems.get(i).getCoin());
            }
        }
        MainActivity.sortCoins();
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemSwiped(int position) {
        db.removeCoin(MainActivity.coins.get(position));
        cardItems.remove(position);
        MainActivity.coins.remove(position);
        for(CoinDB coin:MainActivity.coins){
            System.out.println("After removal: "+coin.getCryptocurrency());
        }
        for(int i=position;i<MainActivity.cardItems.size();i++){
            MainActivity.cardItems.get(i).getCoin().setPosition(MainActivity.cardItems.get(i).getCoin().getPosition()-1);
            System.out.println(MainActivity.cardItems.get(i).getCoin().getCryptocurrency() + " position changed to "+(MainActivity.cardItems.get(i).getCoin().getPosition()));
            db.updateCoinPosition(MainActivity.cardItems.get(i).getCoin());
        }
        notifyItemRemoved(position);
    }

    public void setItemTouchHelper(ItemTouchHelper itemTouchHelper){
        this.itemTouchHelper = itemTouchHelper;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener, GestureDetector.OnGestureListener{
        private CardView cardView;
        private ImageView imgCoin;
        private TextView tvCoin, tvPrice, tvValue, tv24hChange, tvHoldings;
        private GestureDetector gestureDetector;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            gestureDetector = new GestureDetector(itemView.getContext(), this);

            cardView = itemView.findViewById(R.id.cardView);
            imgCoin = itemView.findViewById(R.id.imgCoin);
            tvCoin = itemView.findViewById(R.id.tvCoin);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvHoldings = itemView.findViewById(R.id.tvHoldings);
            tv24hChange = itemView.findViewById(R.id.tv24hChange);
            tvValue = itemView.findViewById(R.id.tvValue);
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

    public Adapter(Context context, ArrayList<CardItem> cardItems){
        this.cardItems = cardItems;
        this.context = context;
        db = new Database();
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
        holder.tvCoin.setText(cardItem.getCoin().getCryptocurrency());
        holder.tv24hChange.setText(cardItem.getChange24h());
        if(cardItem.getChange24h().charAt(0)=='+'){
            holder.tv24hChange.setTextColor(Color.parseColor("#116540"));//green
        }
        else{
            holder.tv24hChange.setTextColor(Color.parseColor("#e57069"));//red
        }
        holder.tvPrice.setText(cardItem.getPrice());

        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(12);
        holder.tvHoldings.setText(df.format(cardItem.getHoldings())+" "+cardItem.getCoin().getSymbol());
        holder.tvValue.setText("$"+String.format("%.2f", cardItem.getValue()));
        holder.tvValue.setText(String.format("$%.2f",cardItem.getHoldings()*Double.parseDouble(cardItem.getPrice().substring(1).replace(",", ""))));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(context, TransactionsActivity.class);
                    intent.putExtra("crypto", cardItem.getCoin().getCryptocurrency());
                    intent.putExtra("symbol", cardItem.getCoin().getSymbol());
                    intent.putExtra("currPrice", cardItem.getPrice());
                    intent.putExtra("value", cardItem.getValue());
                    intent.putExtra("holdings", cardItem.getHoldings());
                    intent.putExtra("uri", cardItem.getImageURL().toString());
                    context.startActivity(intent);
                }
            }
        );
    }

    @Override
    public int getItemCount() {
        return cardItems.size();
    }
}
