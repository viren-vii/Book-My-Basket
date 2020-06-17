package com.example.gocorona;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //    double a,b;
    DatabaseReference databaseCustomers;
    String id, EmailAsId;
    double latitude;
    double longitude;
    private Boolean exit = false;

//    Button groceries, genStore, healthCare, supMarket, foodPro, all;

    TextView TXTgroceries, TXTgenStore, TXThealthCare, TXTfoodPro, TXTall, TXTother;
    String Cat;

    Button btnMaps;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListner;

    TextView tv;
    Button loc;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
    FusedLocationProviderClient fusedLocationClient;

    Button getLocationBtn;
    TextView locationText;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;


     Button buttonGet;
     TextView textView;
     LocationManager locationManager;
     LocationListener listener;

SharedPreferences preferences;
SharedPreferences.Editor editor;

    @Override
    protected void onStart() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(Home.this);
        fetchLocation();
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        preferences = getApplicationContext().getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putBoolean("isCustomer", true);
        editor.putBoolean("isSeller",false);
        editor.apply();
////**************************************************************************************
//        Button btnGet = findViewById(R.id.get);
//        btnGet.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Shop");
//                databaseReference.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        String A = Objects.requireNonNull(dataSnapshot.child("latitude").getValue()).toString();
//                        String B = Objects.requireNonNull(dataSnapshot.child("longitude").getValue()).toString();
//                        String name = Objects.requireNonNull(dataSnapshot.child("name").getValue()).toString();
//                         a = Double.parseDouble(A);
//                         b = Double.parseDouble(B);
//                        Toast.makeText(Home.this , a+"\n"+b , Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//            }
//        });

//-----------------------------------------------hook--------------------------------------------
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
///////////////////////////---------------------toolbar-------------------------------------------

        setSupportActionBar(toolbar);

////////////-------------------------------------Drawer Menu -------------------------------------
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(Home.this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_Home);
//-------------------------------------------------------------------------------------------------------------
//        Button button = (Button) findViewById(R.id.signout);
        mAuth = FirebaseAuth.getInstance();

        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(Home.this, Customer_Main.class));
                }
            }
        };

        //-------------------------------------------------------------KANDYA


        //*************************************************************************************


//        getLocationBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getLocation();
//            }
//        });
//        btnMaps = findViewById(R.id.maps);
//        btnMaps.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intToMaps = new Intent(Home.this, MapsMain.class);
//                startActivity(intToMaps);
//            }
//        });

        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(Home.this);
        if (googleSignInAccount != null) {
            id = googleSignInAccount.getEmail();
            EmailAsId = id.replace(".", "-");

        }
//        Spinner spinner = findViewById(R.id.spinner);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Home.this , R.array.CategorySpinner , android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);
//        spinner.setOnItemSelectedListener(Home.this);
        CardView updates = findViewById(R.id.layUPDATES);
        CardView groceries = findViewById(R.id.layGROCERIES);
        CardView genStore = findViewById(R.id.layGENERALst);
        CardView healthCare = findViewById(R.id.layMEDICAL);
        CardView foodPro = findViewById(R.id.layFOODpro);
        CardView all = findViewById(R.id.layALL);
        CardView other = findViewById(R.id.layOTHER);
         TXTgroceries = findViewById(R.id.txtGROCERIES);
         TXTgenStore = findViewById(R.id.txtGENERALst);
         TXThealthCare = findViewById(R.id.txtMEDICAL);
         TXTfoodPro = findViewById(R.id.txtFOODpro);
         TXTall = findViewById(R.id.txtALL);
         TXTother = findViewById(R.id.txtOTHER);

//        groceries = findViewById(R.id.gro);
//        genStore = findViewById(R.id.genSt);
//        healthCare = findViewById(R.id.healthCare);
//        supMarket = findViewById(R.id.supMarket);
//        foodPro = findViewById(R.id.foodPro);
//        all = findViewById(R.id.all);
        updates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intToWeb = new Intent(Home.this , Corona_updates.class);
                startActivity(intToWeb);
            }
        });

        groceries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cat = (String) TXTgroceries.getText();
                Toast.makeText(Home.this, Cat, Toast.LENGTH_SHORT).show();
                Intent intToMaps = new Intent(Home.this, MapsMain.class);
                intToMaps.putExtra("Category", Cat);
                startActivity(intToMaps);
            }
        });
        genStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cat = (String) TXTgenStore.getText();
                Toast.makeText(Home.this, Cat, Toast.LENGTH_SHORT).show();
                Intent intToMaps = new Intent(Home.this, MapsMain.class);
                intToMaps.putExtra("Category", Cat);
                startActivity(intToMaps);
            }
        });
        healthCare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cat = (String) TXThealthCare.getText();
                Toast.makeText(Home.this, Cat, Toast.LENGTH_SHORT).show();
                Intent intToMaps = new Intent(Home.this, MapsMain.class);
                intToMaps.putExtra("Category", Cat);
                startActivity(intToMaps);
            }
        });
        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cat = (String) TXTother.getText();
                Toast.makeText(Home.this, Cat, Toast.LENGTH_SHORT).show();
                Intent intToMaps = new Intent(Home.this, MapsMain.class);
                intToMaps.putExtra("Category", Cat);
                startActivity(intToMaps);
            }
        });
        foodPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cat = (String) TXTfoodPro.getText();
                Toast.makeText(Home.this, Cat, Toast.LENGTH_SHORT).show();
                Intent intToMaps = new Intent(Home.this, MapsMain.class);
                intToMaps.putExtra("Category", Cat);
                startActivity(intToMaps);
            }
        });
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cat = (String) TXTall.getText();
                Toast.makeText(Home.this, Cat, Toast.LENGTH_SHORT).show();
                Intent intToMaps = new Intent(Home.this, MapsMain.class);
                intToMaps.putExtra("Category", Cat);
                startActivity(intToMaps);
            }
        });

        }
    //  --------------------------------drawer--------------------------------------------------------
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Intent intToMain= new Intent(Home.this , MainActivity.class);
            intToMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intToMain.putExtra("EXIT", true);
            startActivity(intToMain);
//            if (exit) {
//                finish(); // finish activity
//            } else {
//                Toast.makeText(this, "Press Back again to Exit.",
//                        Toast.LENGTH_SHORT).show();
//                exit = true;
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        exit = false;
//                    }
//                }, 3 * 1000);
//
//            }
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.nav_Home:
                break;
//            case R.id.nav_Profile:
//                Intent intToProfile = new Intent(Home.this, ProfilePage.class);
//                startActivity(intToProfile);
//                break;
//            case R.id.nav_Edit_Profile:
//                Intent intToEdit = new Intent(Home.this, CustomerForm.class);
//                startActivity(intToEdit);
//                break;
            case R.id.nav_Email_Id:
                Intent intToMail = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "virubhosale112@gmail.com", null));
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
                editor.putBoolean("isCustomer",false);
                editor.putBoolean("isSeller",false);
                editor.apply();
                Intent intToMain = new Intent(Home.this, MainActivity.class);
                // set the new task and clear flags
                intToMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intToMain);
                mAuth.signOut();
                finish();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
//
//    private void fetchLocation() {
//        // Here, thisActivity is the current activity
//        if (ContextCompat.checkSelfPermission(Home.this,
//                Manifest.permission.ACCESS_COARSE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            // Permission is not granted
//            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(Home.this,
//                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
//                // Show an explanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//
//                new AlertDialog.Builder(Home.this)
//                        .setTitle("Required Location Permission")
//                        .setMessage("Allow location to feed you location!")
//                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                // No explanation needed; request the permission
//                                ActivityCompat.requestPermissions(Home.this,
//                                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
//                                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
//                            }
//                        })
//                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        })
//                        .create()
//                        .show();
//            } else {
//                // No explanation needed; request the permission
//                ActivityCompat.requestPermissions(Home.this,
//                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
//                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
//
//                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
//                // app-defined int constant. The callback method gets the
//                // result of the request.
//            }
//        } else {
//            // Permission has already been granted
//
//            fusedLocationClient.getLastLocation()
//                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
//                        @Override
//                        public void onSuccess(Location location) {
//                            // Got last known location. In some rare situations this can be null.
//                            if (location != null) {
//                                // Logic to handle location object
//                                double latitude = location.getLatitude();
//                                double longitude = location.getLongitude();
//                                String Latitude = String.valueOf(latitude);
//                                String Longitude = String.valueOf(longitude);
//                                Toast.makeText(Home.this, "Complete", Toast.LENGTH_SHORT).show();
//
//                                Toast.makeText(Home.this, "Successgully Registered your Location!\nYou're at " + latitude + longitude + "\n", Toast.LENGTH_SHORT).show();
//                                //pass to DB
//                                NewCustomer Customer = new NewCustomer(Longitude, Latitude);
//                                databaseCustomers.child(id).setValue(Customer);
//                            }
//                        }
//                    });
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//            } else {
//
//            }
//        }

    private void fetchLocation() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(Home.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(Home.this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                new AlertDialog.Builder(Home.this)
                        .setTitle("Required Location Permission")
                        .setMessage("Allow location to feed you location!")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // No explanation needed; request the permission
                                ActivityCompat.requestPermissions(Home.this,
                                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();
            }
            else
                {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(Home.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else
            {
            // Permission has already been granted

            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();

                                String Latitude = String.valueOf(latitude);
                                String Longitude = String.valueOf(longitude);
                                //pass to DB
                                if (latitude != 0.0 && longitude != 0.0) {
                                    databaseCustomers = FirebaseDatabase.getInstance().getReference("Customers");
                                    NewCustomer Customer = new NewCustomer(Longitude, Latitude);
                                    databaseCustomers.child(EmailAsId).setValue(Customer);
                                    Toast.makeText(Home.this, "Successgully Registered your Location!\nYou're at " + latitude + longitude + "\n", Toast.LENGTH_SHORT).show();

                                    SharedPreferences id = getApplicationContext().getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = id.edit();
                                    editor.putString("Ca", Latitude);
                                    editor.putString("Cb", Longitude);
                                    editor.apply();
                                }
                            }
                        }
                    });
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {

            }
            else
            {

            }
        }
        }

//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        Cat = parent.getItemAtPosition(position).toString().toUpperCase();
//        Toast.makeText(Home.this , Cat , Toast.LENGTH_SHORT).show();
//        Intent intToMaps = new Intent(Home.this , MapsMain.class);
//        intToMaps.putExtra("Category" , Cat);
//        startActivity(intToMaps);
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//
//    }

}
