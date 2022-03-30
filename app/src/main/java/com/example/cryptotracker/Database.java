package com.example.cryptotracker;

import android.content.Context;
import android.telecom.Call;
import android.widget.Toast;

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
    DatabaseReference myRef = database.getReference();
    FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();

    public void initDatabase(){
        //myRef.child("users").child(currUser.getUid()).setValue(currUser.getDisplayName());// Unique user ID
        //myRef.child("coins").child(currUser.getUid()).setValue(MainActivity.coins);//set initial coins
        myRef.child("users").child(currUser.getUid()).child("Name").setValue(currUser.getDisplayName());// Unique user ID
        myRef.child("users").child(currUser.getUid()).child("coins").setValue("Bitcoin");
        myRef.child("users").child(currUser.getUid()).child("coins_test").push().setValue("Bitcoin");
        myRef.child("users").child(currUser.getUid()).child("coins_test").push().setValue("Ethereum");
        myRef.child("users").child(currUser.getUid()).child("coins_test").push().setValue("Luna");
    }
/*
    public void getCoins(final CallbackDB callbackDB){
        // Read from the database
        ArrayList<String> coinsDB = new ArrayList<>();
        myRef = database.getReference("coins").child(currUser.getUid());
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    String coin = postSnapshot.getValue(String.class);
                    System.out.println("Fetching data for"+coin);
                    coinsDB.add(coin);
                }
                callbackDB.onSuccess(coinsDB);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Toast.makeText(context, "Failed to fetch data from database!", Toast.LENGTH_SHORT).show();
            }
        });
    }
*/
    public void getUserCoins(final CallbackDB callbackDB){
        // Read from the database
        ArrayList<String> TestcoinsDB = new ArrayList<>();
        myRef = database.getReference("users").child(currUser.getUid()).child("coins_test");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    //System.out.println("We entered here");
                    System.out.println(dataSnapshot.toString());
                    String coin = postSnapshot.getValue(String.class);
                    //System.out.println("DB TEST COINS: "+coin);
                    TestcoinsDB.add(coin);
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

    public void addCoin(String coin){
        System.out.println("DB Request add coin "+coin);
        myRef = database.getReference("users");
        myRef.child(currUser.getUid()).child("coins_test").push().setValue(coin);
    }


}
