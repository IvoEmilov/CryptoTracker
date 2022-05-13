package com.example.cryptotracker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class CoinTransaction{
    private String buySell;
    private String date;
    private Double usd;
    private Double amountCoin;

    public CoinTransaction() {}

    public CoinTransaction(String buySell, String date, Double usd, Double amountCoin){
        this.buySell = buySell;
        this.date = date;
        this.usd = usd;
        this.amountCoin = amountCoin;
    }

    public String getBuySell() {
        return buySell;
    }

    public String getDate() {
        return date;
    }

    public Double getUSD() {
        return usd;
    }

    public Double getAmountCoin() {
        return amountCoin;
    }
}
