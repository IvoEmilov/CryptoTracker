package com.example.cryptotracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class MainActivity extends AppCompatActivity {

    static ArrayList<CardItem> cardItems = new ArrayList<>();
    static ArrayList<CoinDB> coins = new ArrayList<>();
    private ImageView imgProfile;
    private LinearLayout btnWallets;
    static RecyclerView rvCoins;
    static Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    static ProgressBar progressBar;
    static CardView btnAddCoin;
    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgProfile = findViewById(R.id.imgProfile);
        progressBar = findViewById(R.id.progressBar);
        btnAddCoin = findViewById(R.id.btnAddCoin);
        btnWallets = findViewById(R.id.btnWallets);

        db = new Database();
        loadRecyclerView();

        db.initDatabase(new CallbackDB() {
            @Override
            public void onInit() {
                    db.getUserCoins(new CallbackDB() {
                        @Override
                        public void onInit() {}

                        @Override
                        public void onSuccess(ArrayList<CoinDB> coinsDB) {
                            coins=coinsDB;
                            sortCoins();
                            Scraper scraper = new Scraper(MainActivity.this, adapter, Boolean.TRUE);
                            scraper.execute();
                        }
                    });
            }
            @Override
            public void onSuccess(ArrayList<CoinDB> coinsDB) {

            }
        });
        /*
        db.test(new CallbackDB() {
            @Override
            public void onSuccess(ArrayList<CoinDB> coinsDB) {
                coins=coinsDB;
                System.out.println("Scraper TRIGGER");
                Scraper scraper = new Scraper(MainActivity.this, adapter, Boolean.TRUE);
                scraper.execute();
            }
        });
*/

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        btnAddCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCoin();
            }
        });

        btnWallets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, WalletActivity.class);
                startActivity(intent);
            }
        });
    }


    private void loadRecyclerView(){
        rvCoins = findViewById(R.id.rvCoins);
        //recyclerView.setHasFixedSize(true);//if I know it won't change in size
        layoutManager = new LinearLayoutManager(this);
        adapter = new Adapter(this, cardItems);
        rvCoins.setLayoutManager(layoutManager);
        ItemTouchHelper.Callback ithCallback = new RvItemTouchHelper(adapter, Boolean.TRUE, Boolean.TRUE);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(ithCallback);
        adapter.setItemTouchHelper(itemTouchHelper);
        itemTouchHelper.attachToRecyclerView(rvCoins);
        rvCoins.setAdapter(adapter);
    }

    private void addCoin(){
        //db.test();
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_addcoin);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView btnClose = dialog.findViewById(R.id.btnClose);
        Button btnAdd = dialog.findViewById(R.id.btnAdd);
        EditText txtCoin = dialog.findViewById(R.id.txtCoin);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CoinDB coin = new CoinDB(txtCoin.getText().toString(), adapter.getItemCount());
                //coins.add(txtCoin.getText().toString());
                coins.add(coin);
//                db.addCoin(coin);
                Scraper scraper = new Scraper(MainActivity.this, adapter, Boolean.FALSE);
                scraper.execute();

                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void sortCoins(){
        Collections.sort(coins, new Comparator<CoinDB>() {
            @Override
            public int compare(CoinDB o1, CoinDB o2) {
                    if( o1.getPosition() > o2.getPosition()) return 1;
                    else return -1;
            }
        });
    }
}