package com.example.cryptotracker;

import android.media.Image;
import android.net.Uri;

public class CardItem {
    private Uri imageURL;
    private String coinName;
    private String price;
    private String change24h;
    private String amount;
    private String pnl;

    public CardItem(Uri imageURL, String coinName, String price, String change24h, String amount, String pnl){
        this.imageURL = imageURL;
        this.coinName = coinName;
        this.price = price;
        this.change24h = change24h;
        this.amount = amount;
        this.pnl = pnl;
    }

    public Uri getImageURL() {
        return imageURL;
    }

    public String getCoinName() {
        return coinName;
    }

    public String getPrice() {
        return price;
    }

    public String getChange24h() {
        return change24h;
    }

    public String getAmount() {
        return amount;
    }

    public String getPnl() {
        return pnl;
    }
}
