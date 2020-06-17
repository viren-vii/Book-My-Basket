package com.example.gocorona;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddArea extends AppCompatActivity {
    Button add;
    EditText area,city;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_area);
        area =(EditText)findViewById(R.id.editText2);
        city=(EditText)findViewById(R.id.editText);
        add=(Button)findViewById(R.id.buttonAddToFire);


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Deal deal= new Deal(area.getText().toString(),city.getText().toString());
                addArea(deal);

            }
        });


    }

    private void addArea(Deal deal) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("").push();
        String key = myRef.getKey();
        deal.setPostKey(key);


        myRef.setValue(deal).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(AddArea.this,"added",Toast.LENGTH_LONG).show();
                area.setText("");
                city.setText("");
            }
        });

    }
}