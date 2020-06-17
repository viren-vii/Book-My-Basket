package com.example.gocorona;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddItem extends AppCompatActivity {
    Button add;
    EditText ItQty,ItName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        ItQty =(EditText)findViewById(R.id.ItemQuantity);
        ItName=(EditText)findViewById(R.id.ItemName);
        add=(Button)findViewById(R.id.AddItem);


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Deal deal= new Deal(ItQty.getText().toString(),ItName.getText().toString());
                addItem(deal);

            }
        });
    }

    private void addItem(Deal deal) {

        SharedPreferences id = getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
        String Id = id.getString("IdDB", "1");
        Id = Id.replace(".","-");
        String Category = id.getString("Category" , "1");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Sellers").child(Category).child(Id).child("menu").child(ItName.getText().toString());
        String key = myRef.getKey();
        deal.setPostKey(key);


        myRef.setValue(deal).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(AddItem.this,"Added the item!",Toast.LENGTH_LONG).show();
                ItQty.setText("");
                ItName.setText("");
            }
        });

    }
}