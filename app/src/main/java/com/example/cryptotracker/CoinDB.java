package com.example.cryptotracker;

import android.view.SurfaceControl;

import java.util.ArrayList;

public class CoinDB {

    private String cryptocurrency;
    private String symbol;
    private int position;
    private ArrayList<CoinTransaction> transactions = new ArrayList<>();


    public CoinDB(String cryptocurrency, int position){
        this.cryptocurrency=cryptocurrency;
        this.position = position;
        System.out.println("initial item position: "+position);
        this.transactions=new ArrayList<>();
    }

    public CoinDB(){
    }

    public String getCryptocurrency() {
        return cryptocurrency;
    }

    public void setCryptocurrency(String cryptocurrency) {
        this.cryptocurrency = cryptocurrency;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public ArrayList<CoinTransaction> getTransactions() {
        return transactions;
    }

    public void addCoinTransaction(CoinTransaction transaction){
        transactions.add(transaction);
    }
}
