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
    FirebaseDatabase database = FirebaseDatabase.getInstance(" https://cryptotracker-e9de5-default-rtdb.europe-west1.firebasedatabase.app/");
    DatabaseReference rootRef = database.getReference();
    DatabaseReference usersRef = database.getReference("users");
    FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();

    public void initDatabase(final CallbackDB innitCB){
        DatabaseReference userRef = database.getReference("users").child(currUser.getUid());
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
                //Toast.makeText(context, "Failed to fetch data from database!", Toast.LENGTH_SHORT).show();
            }
        });
    }
/*
    public void getUserCoins(final CallbackDB callbackDB){

        ArrayList<String> TestcoinsDB = new ArrayList<>();
        DatabaseReference coinsRef = database.getReference("users").child(currUser.getUid()).child("coins");
        coinsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String coin = ds.getKey();
                    System.out.println("DATABASE READ KEY: "+coin);
                    TestcoinsDB.add(coin);
                }
                /*
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    System.out.println(dataSnapshot.toString());
                    String coin = postSnapshot.getValue(String.class);
                    TestcoinsDB.add(coin);
                }
                //
                callbackDB.onSuccess(TestcoinsDB);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Toast.makeText(context, "Failed to fetch data from database!", Toast.LENGTH_SHORT).show();
            }
        });


        ArrayList<String> TestcoinsDB = new ArrayList<>();
        DatabaseReference coinsRef = database.getReference("users").child(currUser.getUid()).child("coins");
        coinsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    String coin = ds.getKey();
                    //System.out.println("CoinKey is "+coin);
                    DatabaseReference coinRef = database.getReference("users").child(currUser.getUid()).child("coins").child(coin);

                    coinRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            CoinDB coin = dataSnapshot.getValue(CoinDB.class);
                            System.out.println("Crypto is " + coin.getCryptocurrency());
                            TestcoinsDB.add(coin.getCryptocurrency());
                            //MainActivity.coins.add(coin);
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            //Toast.makeText(context, "Failed to fetch data from database!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                callbackDB.onSuccess(TestcoinsDB);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Toast.makeText(context, "Failed to fetch data from database!", Toast.LENGTH_SHORT).show();
            }
        });

    }
*/
    public void addCoin(CoinDB coin){
        /*
        System.out.println("DB Request add coin "+coin);
        rootRef = database.getReference("users");
        rootRef.child(currUser.getUid()).child("coins").push().setValue(coin);
         */

        //CoinDB coin = new CoinDB("Bitcoin");
        usersRef.child(currUser.getUid()).child("coins").child(coin.getCryptocurrency()).setValue(coin);
    }

    public void addWallet(Wallet wallet){
        System.out.println("DB Request to add wallet ");
        usersRef.child(currUser.getUid()).child("wallets").push().setValue(wallet);
    }

    public void addTransaction(CoinDB coin){
        usersRef.child(currUser.getUid()).child("coins").child(coin.getCryptocurrency()).child("transactions").setValue(coin.getTransactions());
    }

    public void getUserCoins(final CallbackDB callback){
/*
        rootRef = database.getReference("users");
        CoinDB coin = new CoinDB("Bitcoin");
        rootRef.child(currUser.getUid()).child("test").child(coin.getCryptocurrency()).setValue(coin);
        coin.addCoinTransaction(new CoinTransaction("Buy","233-333-11","13","1.2"));
        rootRef = database.getReference("users");
        rootRef.child(currUser.getUid()).child("test").child(coin.getCryptocurrency()).child("transactions").setValue(coin.getTransactions());
*/
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
                            //Toast.makeText(context, "Failed to fetch data from database!", Toast.LENGTH_SHORT).show();
                            System.out.println("Failed to read: Inner");
                        }
                    });
                }
            }


            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Toast.makeText(context, "Failed to fetch data from database!", Toast.LENGTH_SHORT).show();
                System.out.println("Failed to read: Outer");
            }
        });
    }

}
