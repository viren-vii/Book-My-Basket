package com.example.gocorona;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onStart() {

        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
            System.exit(0);
        }

        Toast.makeText(MainActivity.this , "Main " , Toast.LENGTH_SHORT).show();
            SharedPreferences check = getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
            boolean isCustomer = check.getBoolean("isCustomer", false);
            boolean isSeller = check.getBoolean("isSeller", false);
            if (isCustomer) {
                Intent intToCustomerHome = new Intent(MainActivity.this, Home.class);
                startActivity(intToCustomerHome);
            } else if (isSeller) {
                Intent intToSellerHome = new Intent(MainActivity.this, Seller_Home.class);
                startActivity(intToSellerHome);
            }else {
                Toast.makeText(MainActivity.this , "Choose between two options!" , Toast.LENGTH_SHORT).show();
            }
//                Toast.makeText(this  , "hi" +isSeller + isCustomer , Toast.LENGTH_SHORT).show();

        super.onStart();
            }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        final Loader loader = new Loader(MainActivity.this);

        Button btnCustomer = findViewById(R.id.Customer_button);
        Button btnSeller = findViewById(R.id.Seller_button);

        btnSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intToSellerMain = new Intent(MainActivity.this , Seller_Main.class);
                startActivity(intToSellerMain);
                finish();
            }
        });
        btnCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intToCustomerMain = new Intent(MainActivity.this , Customer_Main.class);
                startActivity(intToCustomerMain);
                finish();
            }
        });
//        Toast.makeText(this  , "hi" +isSeller + isCustomer , Toast.LENGTH_SHORT).show();
    }
}

