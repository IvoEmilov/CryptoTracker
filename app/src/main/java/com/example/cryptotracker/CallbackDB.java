package com.example.cryptotracker;

import java.util.ArrayList;

public interface CallbackDB {
    void onInit();
    void onSuccessWallets(ArrayList<Wallet> walletsDB);
    void onSuccess(ArrayList<CoinDB> coinsDB);

}


