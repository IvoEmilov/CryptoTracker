package com.example.cryptotracker;

import android.media.Image;
import android.net.Uri;

import java.io.Serializable;

public class CardItem {
    private Uri imageURL;
    private CoinDB coin;
    private String price;
    private String change24h;
    private Double holdings = 0.0;
    private Double value = 0.0;

    public CardItem(Uri imageURL, CoinDB coin, String price, String change24h){
        this.imageURL = imageURL;
        this.coin = coin;
        this.price = price;
        this.change24h = change24h;
        for(CoinTransaction transaction: coin.getTransactions()) {
            value += transaction.getUSD();
            holdings += transaction.getAmountCoin();
        }
    }

    public Uri getImageURL() {
        return imageURL;
    }

    public CoinDB getCoin(){
        return coin;
    }

    public String getPrice() {
        return price;
    }

    public String getChange24h() {
        return change24h;
    }

    public Double getHoldings() {
        return holdings;
    }

    public Double getValue() {
        return value;
    }

    public void setHoldings(Double holdings) {
        this.holdings = holdings;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
