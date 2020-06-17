package com.example.gocorona;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SellerMenu extends AppCompatActivity {
    DatabaseReference ref;
    ArrayList<Deal> list;
    RecyclerView recyclerView;
    SearchView searchView;
    Button add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_menu);

        SharedPreferences id = getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
        String Id = id.getString("IdDB", "1");
        Id = Id.replace(".","-");
        String Category = id.getString("Category" , "1");

        ref=FirebaseDatabase.getInstance().getReference("Sellers").child(Category).child(Id).child("menu");
        recyclerView =(RecyclerView)findViewById(R.id.rv);
        searchView=(SearchView)findViewById(R.id.searchView);
        add=(Button)findViewById(R.id.buttonAddAarea);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SellerMenu.this, AddItem.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(ref != null){

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot.exists()){
                        list=new ArrayList<>();

                        for(DataSnapshot ds : dataSnapshot.getChildren()){

                            list.add(ds.getValue(Deal.class));
                        }
                        AdapterClass adapterClass=new AdapterClass(list);
                        recyclerView.setAdapter(adapterClass);

                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Toast.makeText(SellerMenu.this,databaseError.getMessage(),Toast.LENGTH_SHORT).show();

                }
            });


        }


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                return true;
            }
        });



    }
    private  void  search(String str){

        ArrayList<Deal> myList = new ArrayList<>();
        for(Deal object : list){
            if(object.getName().toLowerCase().contains(str.toLowerCase()) || object.getQuantity().toLowerCase().contains(str.toLowerCase()) ){
                myList.add(object);
            }
        }
        AdapterClass adapterClass =new AdapterClass(myList);
        recyclerView.setAdapter(adapterClass);


    }

}