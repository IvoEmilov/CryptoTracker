package com.example.cryptotracker;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class AdapterWallets extends RecyclerView.Adapter<AdapterWallets.ViewHolder> implements ItemTouchHelperAdapter{
    private ArrayList<Wallet> wallets;
    private ItemTouchHelper itemTouchHelper;
    private Context context;
    private Database db;

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Wallet fromItem = wallets.get(fromPosition);
        wallets.remove(fromItem);
        wallets.add(toPosition, fromItem);
        fromItem.setPosition(toPosition);
        db.updateWalletPosition(fromItem);
        System.out.println(fromItem.getWalletName()+" moved to position "+toPosition);
        if(fromPosition>toPosition){
            for(int i=toPosition+1;i<=fromPosition;i++){
                WalletActivity.wallets.get(i).setPosition(WalletActivity.wallets.get(i).getPosition()+1);
                System.out.println(WalletActivity.wallets.get(i).getWalletName() + " position changed to "+(WalletActivity.wallets.get(i).getPosition()));
                db.updateWalletPosition(WalletActivity.wallets.get(i));
            }
        }
        else{
            for(int i=fromPosition;i<toPosition;i++){
                WalletActivity.wallets.get(i).setPosition(WalletActivity.wallets.get(i).getPosition()-1);
                System.out.println(WalletActivity.wallets.get(i).getWalletName() + " position changed to "+(WalletActivity.wallets.get(i).getPosition()));
                db.updateWalletPosition(WalletActivity.wallets.get(i));
            }
        }
        WalletActivity.sortWallets();
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemSwiped(int position) {
        db.removeWallet(WalletActivity.wallets.get(position));
        wallets.remove(position);
        //WalletActivity.wallets.remove(position);
        for(Wallet wallet:WalletActivity.wallets){
            System.out.println("After removal: "+wallet.getWalletName());
        }
        for(int i=position;i<WalletActivity.wallets.size();i++){
            WalletActivity.wallets.get(i).setPosition(WalletActivity.wallets.get(i).getPosition()-1);
            System.out.println(WalletActivity.wallets.get(i).getWalletName() + " position changed to "+(WalletActivity.wallets.get(i).getPosition()));
            db.updateWalletPosition(WalletActivity.wallets.get(i));
        }
        notifyItemRemoved(position);
    }

    public void setItemTouchHelper(ItemTouchHelper itemTouchHelper){
        this.itemTouchHelper = itemTouchHelper;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener, GestureDetector.OnGestureListener{
        public TextView tvWalletName;
        ImageView imgWallet, imgCopyWalletAddress;
        private GestureDetector gestureDetector;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            gestureDetector = new GestureDetector(itemView.getContext(), this);

            tvWalletName = itemView.findViewById(R.id.tvWalletName);
            imgWallet = itemView.findViewById(R.id.imgWallet);
            imgCopyWalletAddress = itemView.findViewById(R.id.imgCopyAddress);
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

    public AdapterWallets(Context context, ArrayList<Wallet> wallets){
        this.context = context;
        this.wallets = wallets;
        db = new Database();
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
        switch(wallet.getBlockchain()){
            case "Bitcoin":
                holder.imgWallet.setImageResource(R.drawable.wallet_bitcoin);
                break;
            case "BNB":
                holder.imgWallet.setImageResource(R.drawable.wallet_bnb);
                break;
            case "Cosmos":
                holder.imgWallet.setImageResource(R.drawable.wallet_atom);
                break;
            case "Ethereum":
                holder.imgWallet.setImageResource(R.drawable.wallet_ethereum);
                break;
            case "Solana":
                holder.imgWallet.setImageResource(R.drawable.wallet_solana);
                break;
        }

        holder.imgCopyWalletAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("simple text", wallet.getWalletAddress());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, "Wallet Address: "+wallet.getWalletAddress()+" copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });

        holder.imgWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, WebviewActivity.class);
                intent.putExtra("blockchain", wallet.getBlockchain());
                intent.putExtra("address", wallet.getWalletAddress());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return wallets.size();
    }
}