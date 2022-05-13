package com.example.cryptotracker;

import java.util.ArrayList;

public interface CallbackDB {
    void onInit();
    //void onSuccess(ArrayList<String> coinsDB);
    void onSuccess(ArrayList<CoinDB> coinsDB);

}


