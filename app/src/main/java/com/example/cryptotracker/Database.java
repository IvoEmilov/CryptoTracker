package com.example.cryptotracker;

import android.view.View;
import android.view.animation.AnimationUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Database {
    private FirebaseDatabase database = FirebaseDatabase.getInstance(" https://cryptotracker-e9de5-default-rtdb.europe-west1.firebasedatabase.app/");
    //private DatabaseReference rootRef = database.getReference();
    //private DatabaseReference usersRef = database.getReference("users");
    private FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference userRef = database.getReference("users").child(currUser.getUid());

    public void initDatabase(final CallbackDB innitCB){
        //DatabaseReference userRef = database.getReference("users").child(currUser.getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    userRef.child("Name").setValue(currUser.getDisplayName());
                }
                innitCB.onInit();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

    public void getUserCoins(final CallbackDB callback){
        ArrayList<String> keys = new ArrayList<>();
        ArrayList<CoinDB> coinsDB = new ArrayList<>();
        DatabaseReference keysRef = database.getReference("users").child(currUser.getUid()).child("coins");
        keysRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    MainActivity.progressBar.setVisibility(View.GONE);
                    MainActivity.btnAddCoin.setVisibility(View.VISIBLE);
                }

                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    keys.add(ds.getKey());
                }
                for(String coin: keys){
                    DatabaseReference coinRef = database.getReference("users").child(currUser.getUid()).child("coins").child(coin);
                    coinRef.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            CoinDB coin = dataSnapshot.getValue(CoinDB.class);
                            coinsDB.add(coin);
                            if(coinsDB.size() == keys.size()){
                                callback.onSuccess(coinsDB);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

    public void getUserWallets(final CallbackDB callback){
        ArrayList<String> keys = new ArrayList<>();
        ArrayList<Wallet> walletsDB = new ArrayList<>();
        DatabaseReference keysRef = database.getReference("users").child(currUser.getUid()).child("wallets");
        keysRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    WalletActivity.pbWallets.setVisibility(View.GONE);
                    WalletActivity.btnAddWallet.setVisibility(View.VISIBLE);
                }

                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    keys.add(ds.getKey());
                }
                for(String walletKey: keys){
                    DatabaseReference walletRef = database.getReference("users").child(currUser.getUid()).child("wallets").child(walletKey);
                    walletRef.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Wallet wallet = dataSnapshot.getValue(Wallet.class);
                            walletsDB.add(wallet);
                            System.out.println("DB Result: Wallet = "+wallet.getWalletName());
                            if(walletsDB.size() == keys.size()){
                                callback.onSuccessWallets(walletsDB);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }
    public void addCoin(CoinDB coin){
        userRef.child("coins").child(coin.getCryptocurrency()).setValue(coin);
    }

    public void addWallet(Wallet wallet){
        userRef.child("wallets").child(wallet.getWalletAddress()).setValue(wallet);
    }

    public void setTransactions(CoinDB coin){
        userRef.child("coins").child(coin.getCryptocurrency()).child("transactions").setValue(coin.getTransactions());
    }
    
    public void updateCoinPosition(CoinDB coin){
        userRef.child("coins").child(coin.getCryptocurrency()).child("position").setValue(coin.getPosition());
    }
    public void removeCoin(CoinDB coin){
        userRef.child("coins").child(coin.getCryptocurrency()).removeValue();
    }

    public void updateWalletPosition(Wallet wallet){
        userRef.child("wallets").child(wallet.getWalletAddress()).child("position").setValue(wallet.getPosition());
    }
    public void removeWallet(Wallet wallet){
        userRef.child("wallets").child(wallet.getWalletAddress()).removeValue();
    }
}
