package com.example.gocorona;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class OrderTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_test);

        TextView idTv = findViewById(R.id.Id);
        Bundle extras = getIntent().getExtras();
        if(extras != null)
            idTv.setText(extras.getString("idForOrder"));
    }
}
