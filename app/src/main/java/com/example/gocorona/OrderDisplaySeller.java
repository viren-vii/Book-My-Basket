package com.example.gocorona;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.List;

public class OrderDisplaySeller extends AppCompatActivity {


    RecyclerView recyclerView;
    CommentAdapter commentAdapter;
    List<Comment> listComment;
    String key;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_display_seller);

        firebaseDatabase=FirebaseDatabase.getInstance();
        recyclerView =(RecyclerView)findViewById(R.id.rvdetailed);
        databaseReference=firebaseDatabase.getReference();

        iniRV();

    }

    private void iniRV() {

        SharedPreferences id = getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
        String Id = id.getString("IdDB", "1");
        Id = Id.replace(".","-");
        String Category = id.getString("Category" , "1");

        key=databaseReference.getKey();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DatabaseReference commentref=firebaseDatabase.getReference("Sellers").child(Category).child(Id).child("orders");
        commentref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listComment =new ArrayList<>();
                for (DataSnapshot snap:dataSnapshot.getChildren()){

                    org.w3c.dom.Comment comment= snap.getValue(org.w3c.dom.Comment.class);
                    listComment.add(comment);

                }

                commentAdapter= new CommentAdapter(getApplicationContext(),listComment);
                recyclerView.setAdapter(commentAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
