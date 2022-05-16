package com.example.cryptotracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebviewActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        webView = findViewById(R.id.webView);

        Intent intent = getIntent();
        String blockchain = intent.getStringExtra("blockchain");
        String walletAddress = intent.getStringExtra("address");
        String url = setURL(blockchain, walletAddress);

        if(!url.equals("Error")){
            webView.getSettings().setDomStorageEnabled(true);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setLoadsImagesAutomatically(true);
            webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl(url);
        }
    }

    private String setURL(String blockchain, String walletAddress){
        switch (blockchain){
            case "Bitcoin": return "https://www.blockchain.com/btc/address/"+walletAddress;
            case "BNB": return "https://bscscan.com/address/"+walletAddress;
            case "Cosmos": return "https://atomscan.com/accounts/"+walletAddress;
            case "Ethereum": return "https://etherscan.io/address/"+walletAddress;
            case "Solana": return "https://explorer.solana.com/address/"+walletAddress;
            default: System.out.println("Error opening webview");
        }
        return "Error";
    }
}