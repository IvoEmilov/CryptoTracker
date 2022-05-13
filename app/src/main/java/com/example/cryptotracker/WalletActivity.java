package com.example.cryptotracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class WalletActivity extends AppCompatActivity {
    static ArrayList<Wallet> wallets = new ArrayList();
    private LinearLayout btnCoins;
    private RecyclerView rvWallets;
    private RecyclerView.Adapter adapterWallets;
    private RecyclerView.LayoutManager lmWallets;
    static ProgressBar pbWallets;
    static CardView btnAddWallet;
    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        btnCoins = findViewById(R.id.btnCoins);
        btnAddWallet = findViewById(R.id.btnAddWallet);
        pbWallets = findViewById(R.id.pbWallets);
        //wallets.add(new Wallet("Kraken Wallet","wsdsef44523847dhsvf","Bitcoin","$350", "0.0083443 BTC"));

        db = new Database();

        loadRecyclerView();

        btnCoins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnAddWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addWallet();
            }
        });
    }

    private void loadRecyclerView(){
        rvWallets = findViewById(R.id.rvWallets);
        //recyclerView.setHasFixedSize(true);//if I know it won't change in size
        lmWallets = new LinearLayoutManager(this);
        adapterWallets = new AdapterWallets(this, wallets);
        rvWallets.setLayoutManager(lmWallets);
        rvWallets.setAdapter(adapterWallets);
    }

    private void addWallet(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_addwallet);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView btnClose = dialog.findViewById(R.id.btnClose);
        Button btnAdd = dialog.findViewById(R.id.btnAdd);
        EditText txtWalletName = dialog.findViewById(R.id.txtWalletName);
        EditText txtWalletAddress = dialog.findViewById(R.id.txtWalletAddress);

        Spinner spinner = dialog.findViewById(R.id.spinner_blockchains);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.blockchains, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                coins.add(txtCoin.getText().toString());
                db.addCoin(txtCoin.getText().toString());
                Scraper scraper = new Scraper(MainActivity.this, adapter, Boolean.FALSE);
                scraper.execute();
                   */
                Wallet wallet = new Wallet(txtWalletName.getText().toString(), txtWalletAddress.getText().toString(), spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString(),"10","$20");
                wallets.add(wallet);
                db.addWallet(wallet);
                WalletScraper walletScraper = new WalletScraper(WalletActivity.this, adapterWallets, Boolean.FALSE);
                walletScraper.execute();

                adapterWallets.notifyDataSetChanged();

                dialog.dismiss();
            }
        });

        dialog.show();
    }
}