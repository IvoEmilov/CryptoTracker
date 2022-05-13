package com.example.cryptotracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TransactionsActivity extends AppCompatActivity {
    static ArrayList<CoinTransaction> coinTransactions = new ArrayList();
    private RecyclerView rvTransactions;
    private AdapterTransactions adapterTransactions;
    private RecyclerView.LayoutManager lmTransactions;
    private LinearLayout llPNL;
    static ProgressBar pbTransactions;
    private ImageView imgIcon, imgCoin;
    private Button btnTransactionDialog;
    private TextView tvHoldings, tvCrypto, tvTotalBalance, tvTotalInvested,tvAvgBuyPrice, tvPNL;
    private String crypto, symbol;
    private Double value, holdings, currPrice;
    private Calendar date;
    private Database db;
    private DecimalFormat df = new DecimalFormat("#");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        df.setMaximumFractionDigits(12);

        llPNL = findViewById(R.id.llPNL);
        pbTransactions = findViewById(R.id.pbTransactions);
        imgIcon = findViewById(R.id.imgIconTs);
        imgCoin = findViewById(R.id.imgCoin);
        tvTotalBalance = findViewById(R.id.tvTotalBalance);
        tvTotalInvested = findViewById(R.id.tvTotalInvested);
        tvHoldings = findViewById(R.id.tvTotalHoldings);
        tvCrypto = findViewById(R.id.tvCryptoName);
        tvAvgBuyPrice = findViewById(R.id.tvAvgBuyPrice);
        tvPNL = findViewById(R.id.tvPNL);
        btnTransactionDialog = findViewById(R.id.btnTransactionDialog);

        crypto = getIntent().getStringExtra("crypto");
        holdings = getIntent().getDoubleExtra("holdings", 0);
        value = getIntent().getDoubleExtra("value", 0);
        symbol = getIntent().getStringExtra("symbol");
        try{
            currPrice = Double.parseDouble(getIntent().getStringExtra("currPrice").substring(1).replace(",", ""));
        }
        catch(NumberFormatException e){
            e.printStackTrace();
        }

        Uri imgUrl = Uri.parse(getIntent().getStringExtra("uri"));
        for(CoinDB coin:MainActivity.coins){
            if(crypto.equals(coin.getCryptocurrency())){
                coinTransactions = coin.getTransactions();
                sortDates();
            }
        }

        tvCrypto.setText(crypto);
        tvTotalBalance.setText("$"+String.format("%.2f", holdings*currPrice));
        tvTotalInvested.setText("$"+String.format("%.2f", value)+" Total investment");
        tvHoldings.setText(df.format(holdings)+" "+symbol);
        Picasso.with(this).load(imgUrl).into(imgCoin);
        calculateAvgPNL();

        loadRecyclerView();
        db = new Database();


        imgIcon.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnTransactionDialog.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                addTransaction();
            }
        });

    }

    private void loadRecyclerView(){
        rvTransactions = findViewById(R.id.rvTransactions);
        //recyclerView.setHasFixedSize(true);//if I know it won't change in size
        lmTransactions = new LinearLayoutManager(this);
        adapterTransactions = new AdapterTransactions(this, coinTransactions, symbol);
        rvTransactions.setLayoutManager(lmTransactions);
        ItemTouchHelper.Callback ithCallback = new RvItemTouchHelper(adapterTransactions, Boolean.FALSE, Boolean.TRUE);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(ithCallback);
        adapterTransactions.setItemTouchHelper(itemTouchHelper);
        itemTouchHelper.attachToRecyclerView(rvTransactions);
        rvTransactions.setAdapter(adapterTransactions);
        pbTransactions.setVisibility(View.GONE);
    }


    private void addTransaction(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_newtransaction);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView btnClose = dialog.findViewById(R.id.btnClose);
        Button btnAdd = dialog.findViewById(R.id.btnAdd);
        EditText txtFiatValue = dialog.findViewById(R.id.txtFiatValue);
        EditText txtCryptoAmount = dialog.findViewById(R.id.txtCryptoAmount);
        TextView tvDateTime = dialog.findViewById(R.id.tvDateTime);
        RadioGroup radioGroup = dialog.findViewById(R.id.radioGroup);
/*
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = (RadioButton) group.findViewById(checkedId);
            }});
*/

        tvDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar currentDate = Calendar.getInstance();
                date = Calendar.getInstance();
                new DatePickerDialog(TransactionsActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        date.set(year, monthOfYear, dayOfMonth);
                        new TimePickerDialog(TransactionsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                date.set(Calendar.MINUTE, minute);
                                //String strDate = year+"-"+monthOfYear+"-"+dayOfMonth+" "+hourOfDay+":"+minute;
                                String full = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date.getTime());
                                tvDateTime.setText(full);
                            }
                        }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
                    }
                }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
            }
        });


        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtFiatValue.setTextColor(Color.parseColor("#2f2f2f"));
                txtCryptoAmount.setTextColor(Color.parseColor("#2f2f2f"));
                Double fiat, amount;
                int selected = radioGroup.getCheckedRadioButtonId();
                RadioButton radioBtn = (RadioButton) radioGroup.findViewById(selected);
//                System.out.println("RadioBtn "+radioBtn.getText().toString());
                if(txtFiatValue.getText().toString().equals("") || txtCryptoAmount.getText().toString().equals("")){
                    Toast.makeText(dialog.getContext(), "Please, fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                try
                {
                    fiat = Double.parseDouble(txtFiatValue.getText().toString().replace(",","."));
                }
                catch(NumberFormatException e)
                {
                    txtFiatValue.setTextColor(Color.RED);
                    Toast.makeText(dialog.getContext(), "Please, fill in a numeric value", Toast.LENGTH_SHORT).show();
                    return;
                }
                try
                {
                    amount = Double.parseDouble(txtCryptoAmount.getText().toString().replace(",","."));
                }
                catch(NumberFormatException e)
                {
                    txtCryptoAmount.setTextColor(Color.RED);
                    Toast.makeText(dialog.getContext(), "Please, fill in a numeric value", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(tvDateTime.getText().toString().equals("Select Date")){
                    Toast.makeText(dialog.getContext(), "Please, select time of transaction", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(radioBtn.getText().toString().equals("Sell")){
                    fiat = -fiat;
                    amount = -amount;
                }


                CoinTransaction coinTransaction = new CoinTransaction(radioBtn.getText().toString(), tvDateTime.getText().toString(), fiat, amount);
                for(int i=0;i<MainActivity.coins.size();i++){
                    if(MainActivity.coins.get(i).getCryptocurrency().equals(crypto)){
                        MainActivity.coins.get(i).addCoinTransaction(coinTransaction);
                        db.addTransaction(MainActivity.coins.get(i));
                    }
                }

                value += fiat;
                holdings += amount;
                for(int i=0; i<MainActivity.cardItems.size();i++){
                    if(MainActivity.cardItems.get(i).getCoin().getCryptocurrency().equals(crypto)){
                        MainActivity.cardItems.get(i).setHoldings(holdings);
                        MainActivity.cardItems.get(i).setValue(value);
                        MainActivity.adapter.notifyDataSetChanged();
                    }
                }

                tvTotalBalance.setText("$"+String.format("%.2f", holdings*currPrice));
                tvTotalInvested.setText("$"+String.format("%.2f", value)+" Total investment");
                tvHoldings.setText(df.format(holdings)+" "+symbol);
                calculateAvgPNL();
                sortDates();
                adapterTransactions.notifyDataSetChanged();

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void calculateAvgPNL(){
        try{
            Double average = value/holdings;
            Double pnl = holdings*currPrice - holdings*average;


            if(!Double.isNaN(average)){
                tvAvgBuyPrice.setText(String.format("$%.2f", average));
            }
            if(pnl>0){
                llPNL.setBackground(getResources().getDrawable(R.drawable.colour_minty));
                tvPNL.setText(String.format("$%.2f", pnl));
            }
            else if(pnl<0){
                llPNL.setBackground(getResources().getDrawable(R.drawable.colour_red));
                tvPNL.setText(String.format("$%.2f", pnl).replace("$-","-$"));
            }
        }
        catch(ArithmeticException e){
            e.printStackTrace();
            tvAvgBuyPrice.setText("$0");
            tvPNL.setText("$0");
        }
    }

    private void sortDates(){
        Collections.sort(coinTransactions, new Comparator<CoinTransaction>() {
            DateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            @Override
            public int compare(CoinTransaction o1, CoinTransaction o2) {
                try {
                    return f.parse(o2.getDate()).compareTo(f.parse(o1.getDate()));
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });
    }
}