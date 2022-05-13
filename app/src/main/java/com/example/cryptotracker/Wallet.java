package com.example.cryptotracker;

import android.media.Image;
import android.net.Uri;

public class Wallet {
    private String walletName;
    private String walletAddress;
    private String blockchain;
    private String walletUSD;
    private String walletAmount;

    public Wallet(String walletName, String walletAddress, String blockchain, String walletUSD, String walletAmount){
        this.walletName = walletName;
        this.walletAddress = walletAddress;
        this.blockchain = blockchain;
        this.walletUSD = walletUSD;
        this.walletAmount = walletAmount;
    }
    public String getWalletName() {
        return walletName;
    }

    public String getWalletAddress() {
        return walletAddress;
    }

    public String getBlockchain() {
        return blockchain;
    }

    public String getWalletUSD() {
        return walletUSD;
    }

    public String getWalletAmount() {
        return walletAmount;
    }
}
