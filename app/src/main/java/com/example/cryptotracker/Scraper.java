package com.example.cryptotracker;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class Scraper extends AsyncTask<Void, Void, Void> {
    private Context context;
    private RecyclerView.Adapter adapter;
    private boolean initFlag;
    private boolean existingItemFlag = Boolean.FALSE;
    private int existingItemPosition;
    private Database db = new Database();


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
            MainActivity.cardItems.clear();
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
        } else {
            if(!existingItemFlag){
                adapter.notifyItemInserted(MainActivity.cardItems.size() - 1);
                MainActivity.rvCoins.scrollToPosition(MainActivity.cardItems.size() - 1);
            }
            else{
                final float y = MainActivity.rvCoins.getChildAt(existingItemPosition).getY();
                MainActivity.nestedSV.smoothScrollTo(0, Math.round(y));
                Toast.makeText(context,"Cryptocurrency is already present.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected Void doInBackground(Void... voids) {
        if(initFlag == Boolean.TRUE){
            for (CoinDB coin : MainActivity.coins) {
                queryExecute(coin);
            }
        }
        else{
                queryExecute(MainActivity.coins.get(MainActivity.coins.size()-1));
        }

        return null;
    }

    private void queryExecute(CoinDB coin){
        Double value = 0.0, holdings=0.0;

        try {
//            String query = "https://www.google.com/search?q=coinmarketcap+" + coin.getCryptocurrency();
//            Document document = Jsoup.connect(query).get();
//            //Log.d("Document", document.text());
//            Element element = document.select("div.yuRUbf > a").first();
//            String url = element.attr("href"); //1st Google result
            Document document;
            Element element;
            String url = "https://coinmarketcap.com/currencies/" + coin.getCryptocurrency();
            document = Jsoup.connect(url).get();
            //Image
            element = document.select("div.sc-f70bb44c-0.jImtlI > img").first();
            String imgURL = element.attr("src");
            Log.d("imgUrl", imgURL);
            //Coin Name + Symbol
            element = document.select("div.sc-f70bb44c-0.eseBKW").first();
            String symbol = element.select("span.sc-f70bb44c-0.dXQGRd").text();
            //element.select("small.nameSymbol").remove(); //removes BTC abrv
            String coinNameFull = element.select("span.sc-f70bb44c-0.jltoa").text();
            String[] coinNameArr = coinNameFull.split("\\s+");
            String coinName = coinNameArr[0];
            //Get value and holdings of crypto for cardItemView
            if(!initFlag){
                for(CardItem cardItem:MainActivity.cardItems){
                    if(cardItem.getCoin().getCryptocurrency().equals(coinName)){
                        System.out.println(coinName+" already exists in the list");
                        MainActivity.coins.remove(coin);
                        existingItemFlag = Boolean.TRUE;
                        existingItemPosition = cardItem.getCoin().getPosition();
                        return;
                    }
                }
                for(int i=0;i<MainActivity.coins.size();i++){
                    if(MainActivity.coins.get(i).getCryptocurrency().equals(coin.getCryptocurrency())){
                            MainActivity.coins.get(i).setCryptocurrency(coinName);
                            MainActivity.coins.get(i).setSymbol(symbol);
                    }
                }
            }
            //Price
            element = document.select("div.sc-f70bb44c-0.flfGQp").first();
            String price = element.select("span").text();
            Log.d("price", price);
            if(price.startsWith("BGN")){
                price = price.substring(3);
                Double decimalPrice = Double.parseDouble(price.replace(",",""));
                decimalPrice *= 0.54;
                price = "$"+String.format("%.2f", decimalPrice);
                System.out.println("New price = "+price);
            }
            //24h Change
            element = document.select("div.sc-f70bb44c-0.cOoglq").first();
            String change24h = element.text().split("\\s+")[0];
            Log.d("24h", change24h);
            try {
                String color = element.select("p").first().attr("color");
                if(color.equals("green")){
                    change24h = "+" + change24h;
                }
                else{
                    change24h = "-" + change24h;
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            MainActivity.cardItems.add(new CardItem(Uri.parse(imgURL), coin,price, change24h));
            db.addCoin(coin);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
