package com.example.cryptotracker;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.view.animation.AnimationUtils;

import androidx.recyclerview.widget.RecyclerView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;

public class Scraper extends AsyncTask<Void, Void, Void> {
    Context context;
    RecyclerView.Adapter adapter;
    private boolean initFlag;


    public Scraper(Context context, RecyclerView.Adapter adapter, boolean initFlag) {
        this.context = context;
        this.adapter = adapter;
        this.initFlag = initFlag;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (initFlag == Boolean.TRUE) {
            MainActivity.progressBar.setVisibility(View.VISIBLE);
            MainActivity.progressBar.startAnimation(AnimationUtils.loadAnimation(context, android.R.anim.fade_in));
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (initFlag == Boolean.TRUE) {
            MainActivity.progressBar.setVisibility(View.GONE);
            MainActivity.progressBar.startAnimation(AnimationUtils.loadAnimation(context, android.R.anim.fade_out));
            MainActivity.btnAddCoin.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
            MainActivity.coins.clear();
        } else {
            //adapter.notifyItemRangeChanged(0, MainActivity.cardItems.size()-1);
            adapter.notifyItemInserted(MainActivity.cardItems.size() - 1);
            //adapter.notifyDataSetChanged();
            MainActivity.coins.clear();
            //Toast.makeText(context,"Coin added successfully!", Toast.LENGTH_SHORT).show();
            MainActivity.recyclerView.scrollToPosition(MainActivity.cardItems.size() - 1);
        }
    }

    @Override
    protected Void doInBackground(Void... voids) {
        for (String coin : MainActivity.coins) {
            try {
                String query = "https://www.google.com/search?q=coinmarketcap+" + coin;
                Document document = Jsoup.connect(query).get();
                Element element = document.select("div.yuRUbf > a").first();
                String url = element.attr("href"); //1st Google result

                document = Jsoup.connect(url).get();
                //Image
                element = document.select("div.sc-16r8icm-0.gpRPnR.nameHeader > img").first();
                String imgURL = element.attr("src");
                //Coin Name
                element = document.select("h2.sc-1q9q90x-0.jCInrl.h1").first();
                element.select("small.nameSymbol").remove(); //removes BTC abrv
                String coinName = element.text();
                System.out.println(coinName);
                //Price
                element = document.select("div.priceValue").first();
                String price = element.select("span").text();
                System.out.println(price);
                //24h Change
                element = document.select("span.sc-15yy2pl-0").first();
                String change24h = element.text();
                try {
                    element.select("span.icon-Caret-up").first().remove();
                    change24h = "+" + change24h;
                } catch (NullPointerException e) {
                    //e.printStackTrace();
                    change24h = "-" + change24h;
                }
                MainActivity.cardItems.add(new CardItem(Uri.parse(imgURL), coinName, price, change24h, "None", "None"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
