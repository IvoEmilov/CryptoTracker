package com.example.cryptotracker;

import android.media.Image;
import android.net.Uri;

public class Wallet {
    private String walletName;
    private String walletAddress;
    private String blockchain;
    private int position;

    public Wallet(String walletName, String walletAddress, String blockchain, int position){
        this.walletName = walletName;
        this.walletAddress = walletAddress;
        this.blockchain = blockchain;
        this.position = position;
    }

    public Wallet(){}

    public String getWalletName() {
        return walletName;
    }

    public String getWalletAddress() {
        return walletAddress;
    }

    public String getBlockchain() {
        return blockchain;
    }


    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
