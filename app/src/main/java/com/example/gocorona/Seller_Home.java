package com.example.gocorona;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Seller_Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TextView Status;
    Button btnStatus;
    String id;
    String shopName;
    DatabaseReference databaseReference;
    DatabaseReference databaseReferenceAll;
    boolean statusBool;

    FirebaseAuth mAuthS;
    FirebaseAuth.AuthStateListener mAuthListner;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onStart() {
        super.onStart();

        mAuthS.addAuthStateListener(mAuthListner);

        changeStatus();
        Toast.makeText(Seller_Home.this , "Status is up to date!" , Toast.LENGTH_SHORT).show();
    }


    DrawerLayout drawerLayoutS;
    NavigationView navigationViewS;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller__home);


        preferences = getApplicationContext().getSharedPreferences(MainActivity.PREFS_NAME , Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putBoolean("isSeller", true);
        editor.putBoolean("isCustomer",false);
        editor.putBoolean("filledForm",true);
        editor.apply();

//-----------------------------------------------hook--------------------------------------------
        drawerLayoutS = findViewById(R.id.drawer_layoutS);
        navigationViewS = findViewById(R.id.nav_view_Seller);
        toolbar = findViewById(R.id.toolbar);
///////////////////////////---------------------toolbar-------------------------------------------

        setSupportActionBar(toolbar);

////////////-------------------------------------Drawer Menu -------------------------------------
        navigationViewS.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(Seller_Home.this , drawerLayoutS , toolbar , R.string.navigation_drawer_open , R.string.navigation_drawer_close);
        drawerLayoutS.addDrawerListener(toggle);
        toggle.syncState();

        navigationViewS.setNavigationItemSelectedListener(this);
        navigationViewS.setCheckedItem(R.id.nav_Home_Seller);



//-------------------------------------------------------------------------------------------------------------
        mAuthS = FirebaseAuth.getInstance();

        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()==null)
                {
                    startActivity(new Intent(Seller_Home.this, Seller_Main.class));
                }
            }
        };

        //-------------------------------------------------DB
        Status = findViewById(R.id.textView);
        btnStatus = findViewById(R.id.button);

        btnStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeStatus();
            }
        });

        //----------------------------------------------------------------------
        TextView shopName = findViewById(R.id.Shop_Name);
        TextView shopAdd = findViewById(R.id.ShowAdd);
        SharedPreferences id = getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
        String shop_name = id.getString("ShopName", "Shop Name");
        String shop_add = id.getString("Address", "Shop Address");
        shopAdd.setText(shop_add);
        shopName.setText(shop_name);
    }

    private void changeStatus() {
        //+++++++++++++++++++++++++++++++++++++++++++++++++++
        //for getting id
        SharedPreferences id = getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
        String Id = id.getString("IdDB", "1");
        String Category = id.getString("Category" , "1");
        //+++++++++++++++++++++++++++++++++++++++++++++++++++
        String EmailAsId = Id.replace("." , "-");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Sellers").child(Category).child(EmailAsId);
        databaseReferenceAll = FirebaseDatabase.getInstance().getReference().child("Sellers").child("ALL").child(EmailAsId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String statusStr = dataSnapshot.child("status").toString();
                if(statusStr.equals("true"))
                {
                    statusBool = true;
                }
                else if(statusStr.equals("false"))
                {
                    statusBool = false;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        databaseReferenceAll.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String statusStr = dataSnapshot.child("status").toString();
                if(statusStr.equals("true"))
                {
                    statusBool = true;
                }
                else if(statusStr.equals("false"))
                {
                    statusBool = false;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
//        if(statusStr.equals("OPEN"))
//        {
//            status = true;
//            Status.setText("OPEN");
//        }
//        else
//        {
//            status = false;
//            Status.setText("CLOSE");
//        }
            statusBool = !statusBool;
            databaseReference.child("status").setValue(statusBool);
            databaseReferenceAll.child("status").setValue(statusBool);
            if(statusBool)
            {
                Status.setText("OPEN");
                Status.setTextColor(this.getResources().getColor(R.color.open));
            }
            else
            {
                Status.setText("CLOSE");
                Status.setTextColor(this.getResources().getColor(R.color.close));
            }

        Toast.makeText(Seller_Home.this , "Status Updated Succesfully!" , Toast.LENGTH_SHORT).show();

    }

    //  --------------------------------drawer--------------------------------------------------------
    @Override
    public void  onBackPressed(){
        if (drawerLayoutS.isDrawerOpen(GravityCompat.START)) {
            drawerLayoutS.closeDrawer(GravityCompat.START);
        } else {
            Intent intToMain= new Intent(Seller_Home.this , MainActivity.class);
            intToMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intToMain.putExtra("EXIT", true);
            startActivity(intToMain);
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){
            case R.id.nav_Home_Seller:
                break;
            case R.id.nav_Profile:
                Intent intToProfile = new Intent(Seller_Home.this , ProfilePage.class);
                startActivity(intToProfile);
                break;
            case R.id.nav_Edit_Profile:
                Intent intToEdit = new Intent(Seller_Home.this , SellerFrom.class);
                startActivity(intToEdit);
                break;
            case R.id.nav_Verified:
                Intent intToVerification = new Intent(Seller_Home.this , VerificationActivity.class);
                startActivity(intToVerification);
                break;
            case R.id.nav_set_menu:
                Intent intToMenu = new Intent(Seller_Home.this , SellerMenu.class);
                startActivity(intToMenu);
                break;
            case R.id.nav_Email_Id:
                Intent intToMail = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","virubhosale112@gmail.com", null));
                intToMail.putExtra(Intent.EXTRA_SUBJECT, "GoCorona Help");
                intToMail.putExtra(Intent.EXTRA_TEXT, "message");
                startActivity(Intent.createChooser(intToMail, "Choose an Email client :"));
                break;
            case R.id.nav_Phone_Number:
                Intent intToDialer = new Intent(Intent.ACTION_DIAL);
                intToDialer.setData(Uri.parse("tel:9423587762"));
                startActivity(intToDialer);
                break;
//            case R.id.nav_Help:--------------------------------------------------------help page------------------------------
//                Intent intToHelp = new Intent(Home.this , Help.class);
//                startActivity(intToHelp);
//                break;
            case R.id.nav_LogOut:
                editor.putBoolean("isSeller",false);
                editor.putBoolean("isCustomer",false);
                editor.apply();
                Intent intToMain = new Intent(Seller_Home.this, MainActivity.class);
                // set the new task and clear flags
                intToMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intToMain);
                mAuthS.signOut();
                finish();
//                mAuthS.signOut();
//                editor.clear();
//                editor.apply();
                break;

        }
        drawerLayoutS.closeDrawer(GravityCompat.START);
        return true;
    }
}