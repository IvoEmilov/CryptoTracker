package com.example.cryptotracker;

public class TransactionItem {
    private String buySell;
    private String date;
    private String USD;
    private String amountCoin;

    public TransactionItem(String buySell, String date, String USD, String amountCoin){
        this.buySell = buySell;
        this.date = date;
        this.USD = USD;
        this.amountCoin = amountCoin;
    }

    public String getBuySell() {
        return buySell;
    }

    public String getDate() {
        return date;
    }

    public String getUSD() {
        return USD;
    }

    public String getAmountCoin() {
        return amountCoin;
    }

}
