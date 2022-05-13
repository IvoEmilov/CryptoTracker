package com.example.cryptotracker;

import android.media.Image;
import android.net.Uri;

import java.io.Serializable;

public class CardItem {
    private Uri imageURL;
    private String coinName;
    String symbol;
    private String price;
    private String change24h;
    private Double holdings;
    private Double value;

    public CardItem(Uri imageURL, String coinName, String symbol, String price, String change24h, Double holdings, Double value){
        this.imageURL = imageURL;
        this.coinName = coinName;
        this.symbol = symbol;
        this.price = price;
        this.change24h = change24h;
        this.holdings = holdings;
        this.value = value;
    }

    public Uri getImageURL() {
        return imageURL;
    }

    public String getCoinName() {
        return coinName;
    }

    public String getSymbol(){ return symbol;}

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
}
