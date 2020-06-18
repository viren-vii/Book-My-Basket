package com.example.gocorona;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Comment;

import java.util.List;

public class DetailedActivity extends AppCompatActivity {

    TextView City, area;
    FirebaseDatabase firebaseDatabase;
    Button addShop;
    EditText shopName;
    RecyclerView recyclerView;
    CommentAdapter commentAdapter;
    List<Comment> listComment;
    String PostKey,key;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        //recyclerView =(RecyclerView)findViewById(R.id.rvdetailed);
        City=(TextView)findViewById(R.id.textViewcity);
        addShop=(Button)findViewById(R.id.button5addShop);
        shopName=(EditText)findViewById(R.id.editTextshopName);
        area= (TextView)findViewById(R.id.textViewarea);
        firebaseDatabase=FirebaseDatabase.getInstance();

        final String city=getIntent().getExtras().getString("Name");
        City.setText(city);
        String Area=getIntent().getExtras().getString("Quantity");
        area.setText(Area);
        PostKey=getIntent().getExtras().getString("postKey");
        databaseReference=firebaseDatabase.getReference();

        addShop.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                SharedPreferences id = getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
                String Id = id.getString("IdDB", "1");
                Id = Id.replace(".","-");
                String Category = id.getString("Category" , "1");

                addShop.setVisibility(View.INVISIBLE);
                DatabaseReference commentRef= firebaseDatabase.getInstance().getReference("Sellers").child(Category).child(Id).child("orders").child(city);
                String content=shopName.getText().toString();

                commentRef.setValue(content).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showMsg("Order Sent");
                        shopName.setText("");
                        addShop.setVisibility(View.VISIBLE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showMsg("Failed" +e.getMessage());
                    }
                });
            }
        });
//        iniRV();
//         letsCheck();
    }
    private void showMsg(String s) {
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();

    }
}