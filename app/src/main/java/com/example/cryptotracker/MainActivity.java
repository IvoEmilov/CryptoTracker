package com.example.cryptotracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOError;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    static ArrayList<CardItem> cardItems = new ArrayList<>();
    static ArrayList<String> coins = new ArrayList<>();
    private ImageView imgProfile;
    static RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    static ProgressBar progressBar;
    static CardView btnAddCoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgProfile = findViewById(R.id.imgProfile);
        progressBar = findViewById(R.id.progressBar);
        btnAddCoin = findViewById(R.id.btnAddCoin);

        coins.add("Bitcoin");
        coins.add("Ethereum");
        coins.add("Atom");
        coins.add("BNB");

        loadRecyclerView();
        Scraper scraper = new Scraper(MainActivity.this, adapter, Boolean.TRUE);
        scraper.execute();

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
    }



    private void loadRecyclerView(){
        recyclerView = findViewById(R.id.recyclerView);
        //recyclerView.setHasFixedSize(true);//if I know it won't change in size
        layoutManager = new LinearLayoutManager(this);
        adapter = new Adapter(this, cardItems);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void addCoin(){
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
                coins.add(txtCoin.getText().toString());
                Scraper scraper = new Scraper(MainActivity.this, adapter, Boolean.FALSE);
                scraper.execute();
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}