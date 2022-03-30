package com.example.cryptotracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.ArrayList;

public class TransactionsActivity extends AppCompatActivity {
    static ArrayList<TransactionItem> transactionItems = new ArrayList();
    private RecyclerView rvTransactions;
    private RecyclerView.Adapter adapterTransactions;
    private RecyclerView.LayoutManager lmTransactions;
    static ProgressBar pbTransactions;
    private ImageView imgIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        transactionItems.add(new TransactionItem("Buy", "2022-03-10 16:33:21", "$41500","0.0075430"));
        transactionItems.add(new TransactionItem("Buy", "2022-04-11 16:03:00", "$44000","0.0053424"));
        transactionItems.add(new TransactionItem("Buy", "2022-03-23 15:33:21", "$39000","0.0083231"));
        pbTransactions = findViewById(R.id.pbTransactions);
        imgIcon = findViewById(R.id.imgIconTs);
        loadRecyclerView();

        imgIcon.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void loadRecyclerView(){
        rvTransactions = findViewById(R.id.rvTransactions);
        //recyclerView.setHasFixedSize(true);//if I know it won't change in size
        lmTransactions = new LinearLayoutManager(this);
        adapterTransactions = new AdapterTransactions(this, transactionItems);
        rvTransactions.setLayoutManager(lmTransactions);
        rvTransactions.setAdapter(adapterTransactions);
    }
}