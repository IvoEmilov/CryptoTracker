package com.example.cryptotracker;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.view.animation.AnimationUtils;

import androidx.recyclerview.widget.RecyclerView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class WalletScraper extends AsyncTask<Void, Void, Void> {
    Context context;
    RecyclerView.Adapter adapter;
    private boolean initFlag;


    public WalletScraper(Context context, RecyclerView.Adapter adapter, boolean initFlag) {
        this.context = context;
        this.adapter = adapter;
        this.initFlag = initFlag;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (initFlag == Boolean.TRUE) {
            WalletActivity.pbWallets.setVisibility(View.VISIBLE);
            WalletActivity.pbWallets.startAnimation(AnimationUtils.loadAnimation(context, android.R.anim.fade_in));
            //WalletActivity.wallets.clear();
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (initFlag == Boolean.TRUE) {
            WalletActivity.pbWallets.setVisibility(View.GONE);
            WalletActivity.pbWallets.startAnimation(AnimationUtils.loadAnimation(context, android.R.anim.fade_out));
            WalletActivity.btnAddWallet.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
            //MainActivity.coins.clear();
        } else {
            //adapter.notifyItemRangeChanged(0, MainActivity.cardItems.size()-1);
            adapter.notifyItemInserted(WalletActivity.wallets.size() - 1);
            //adapter.notifyDataSetChanged();
            //MainActivity.coins.clear();
            //Toast.makeText(context,"Coin added successfully!", Toast.LENGTH_SHORT).show();
            //WalletActivity.rvWallets.scrollToPosition(MainActivity.cardItems.size() - 1);
        }
    }

    @Override
    protected Void doInBackground(Void... voids) {
        for(int i=0; i<WalletActivity.wallets.size();i++){
            switch(WalletActivity.wallets.get(i).getBlockchain()){
                case "ATOM":
                    scrapeAtom(WalletActivity.wallets.get(i).getWalletAddress());
                    break;
                case "Bitcoin":
                    //scrapeBitcoin();
                    break;
                case "BNB":
                    //scrapeBNB();
                    break;
                case "Ethereum":
                    //scrapeEthereum();
                    break;
                case "Solana":
                    //scrapeSolana();
                    break;
                default:
                    System.out.println("Error defining blockhain for given wallet");
            }
        }
        return null;
    }

    private void scrapeAtom(String walletAddress){
        try {
            String query = "https://atomscan.com/accounts/cosmos1uujslzpfvm7h7zwzawk68dwrm8x6r6wqx3kk7t";
            Document document = Jsoup.connect(query).get();
            for (Element element : document.getAllElements() )
            {
                if (element.dataset().containsKey("v-6fb3444e")) {
                    System.out.println(element);
                }
            }
            //Element element = document.select("div > data-v-6fb3444e").first();
            //System.out.println(element);
            //String str = element.text();
            //System.out.print("COSMOS AMOUNT IS "+str);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
