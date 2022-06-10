package com.example.cryptotracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.ItemTouchHelper;
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
import java.util.Collections;
import java.util.Comparator;

public class WalletActivity extends AppCompatActivity {
    static ArrayList<Wallet> wallets = new ArrayList();
    private LinearLayout btnCoins;
    static NestedScrollView nestedSVWallets;
    private RecyclerView rvWallets;
    private AdapterWallets adapterWallets;
    private RecyclerView.LayoutManager lmWallets;
    static ProgressBar pbWallets;
    static CardView btnAddWallet;
    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        btnCoins = findViewById(R.id.btnCoins);
        btnAddWallet = findViewById(R.id.btnAddWallet);
        pbWallets = findViewById(R.id.pbWallets);

        db = new Database();
        loadRecyclerView();
        db.getUserWallets(new CallbackDB() {
            @Override
            public void onInit() {}

            @Override
            public void onSuccessWallets(ArrayList<Wallet> walletsDB) {
                wallets.clear();
                wallets.addAll(walletsDB);
                sortWallets();
                adapterWallets.notifyDataSetChanged();
                pbWallets.setVisibility(View.GONE);
                btnAddWallet.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(ArrayList<CoinDB> coinsDB) {}
        });

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
        nestedSVWallets = findViewById(R.id.nestedSVWallets);
        rvWallets = findViewById(R.id.rvWallets);
        rvWallets.setNestedScrollingEnabled(false);
        //recyclerView.setHasFixedSize(true);//if I know it won't change in size
        lmWallets = new LinearLayoutManager(this);
        adapterWallets = new AdapterWallets(this, wallets);
        rvWallets.setLayoutManager(lmWallets);
        ItemTouchHelper.Callback ithCallback = new RvItemTouchHelper(adapterWallets, Boolean.TRUE, Boolean.TRUE);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(ithCallback);
        adapterWallets.setItemTouchHelper(itemTouchHelper);
        itemTouchHelper.attachToRecyclerView(rvWallets);
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
                Wallet wallet = new Wallet(txtWalletName.getText().toString(), txtWalletAddress.getText().toString(), spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString(), WalletActivity.wallets.size());
                wallets.add(wallet);
                db.addWallet(wallet);
                adapterWallets.notifyDataSetChanged();

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public static void sortWallets(){
        Collections.sort(wallets, new Comparator<Wallet>() {
            @Override
            public int compare(Wallet o1, Wallet o2) {
                if( o1.getPosition() > o2.getPosition()) return 1;
                else return -1;
            }
        });
    }
}