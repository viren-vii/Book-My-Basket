package com.example.gocorona;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class MapsMain extends FragmentActivity implements OnMapReadyCallback  {
    public double a,b,x;
    public String Ca,Cb;
    public boolean St;
    float zoomLevel = 16.0f;
    String address,idForOrder;

    public double d =  75.341601;
    public double c =  19.189257;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    GoogleMap map;
    Button bt;

//    @Override
//    protected void onStart() {
//        Toast.makeText(MapsMain.this , "App started!", Toast.LENGTH_SHORT).show();
//
//        SharedPreferences id = getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
//        final String Id = id.getString("IdDB", "1");
//        Toast.makeText(MapsMain.this, Id , Toast.LENGTH_SHORT).show();
//
//        DatabaseReference databaseReferenceC = FirebaseDatabase.getInstance().getReference().child("Customers");
//        Toast.makeText(MapsMain.this , "1", Toast.LENGTH_SHORT).show();
//        databaseReferenceC.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshotCustomer) {
//                Ca = Double.parseDouble(Objects.requireNonNull(dataSnapshotCustomer.child("virubhosale112@gmail-com").child("latitude").getValue()).toString());
//                Cb = Double.parseDouble(Objects.requireNonNull(dataSnapshotCustomer.child("virubhosale112@gmail-com").child("longitude").getValue()).toString());
//                Toast.makeText(MapsMain.this , Ca+"\n"+Cb+"\nIn customer DB" , Toast.LENGTH_SHORT).show();
//
////                Ca = Double.parseDouble(CustomerLat);
////                Cb = Double.parseDouble(CustomerLng);
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//        super.onStart();
//
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapsMain.this);

    }

        @Override
        public void onMapReady(final GoogleMap googleMap) {


            SharedPreferences id = getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
            Ca = id.getString("Ca", "0");
            Cb = id.getString("Cb","0");
            final String Cat = getIntent().getStringExtra("Category");


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Sellers").child(Cat);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String A,B,name;
                String status;
                boolean noMark = true;
                for (DataSnapshot shopSnapshot: dataSnapshot.getChildren()) {
                    A = Objects.requireNonNull(shopSnapshot.child("latitude").getValue()).toString();
                    B = Objects.requireNonNull(shopSnapshot.child("longitude").getValue()).toString();
                    name = Objects.requireNonNull(shopSnapshot.child("name").getValue()).toString();
                    status = Objects.requireNonNull(shopSnapshot.child("status").getValue()).toString();
                    address = Objects.requireNonNull(shopSnapshot.child("address").getValue()).toString();
                    idForOrder = Objects.requireNonNull(shopSnapshot.child("id").getValue()).toString();
                    String CategoryFromDB = (String) Objects.requireNonNull(shopSnapshot.child("category").getValue());
                    a = Double.parseDouble(A);
                    b = Double.parseDouble(B);
                    St = Boolean.parseBoolean(status);

                    double CA = Double.parseDouble(Ca);
                    double CB = Double.parseDouble(Cb);

                    map= googleMap;
                    LatLng Maharshtra=new LatLng(a,b);
//                    Toast.makeText(MapsMain.this , a+"\n"+b+"\n"+name , Toast.LENGTH_SHORT).show();
                    x = distFrom(a,b,CA,CB);
                    Toast.makeText(MapsMain.this , x+"\n" , Toast.LENGTH_SHORT).show();
                    if(St)
                    {
                         Marker Maha = map.addMarker(new MarkerOptions()
                                .position(Maharshtra)
                                .title(name)
                                .snippet(address));
                         Maha.setTag(idForOrder);
//                        map.addMarker(new MarkerOptions().position(Maharshtra).title(name).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_edit)));
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(Maharshtra , 10));
                        noMark = false;

                        switch (Cat)
                        {
                            case "GROCERIES":
                                Maha.setIcon(bitmapDescriptorFromVector(MapsMain.this , R.drawable.gro_12dp));
                                break;
                            case "HEALTH CARE":
                                Maha.setIcon(bitmapDescriptorFromVector(MapsMain.this , R.drawable.health_care));
                                break;
                            case "GENERAL STORE":
                                Maha.setIcon(bitmapDescriptorFromVector(MapsMain.this , R.drawable.gen_12));
                                break;
                            case "OTHER":
                                Maha.setIcon(bitmapDescriptorFromVector(MapsMain.this , R.drawable.other));
                                break;
                            case "FOOD PRODUCTS":
                                Maha.setIcon(bitmapDescriptorFromVector(MapsMain.this , R.drawable.food_12dp));
                                break;
                            case "ALL":
                                switch (CategoryFromDB){
                                    case "GROCERIES":
                                        Maha.setIcon(bitmapDescriptorFromVector(MapsMain.this , R.drawable.gro_12dp));
                                        break;
                                    case "HEALTH CARE":
                                        Maha.setIcon(bitmapDescriptorFromVector(MapsMain.this , R.drawable.health_care));
                                        break;
                                    case "GENERAL STORE":
                                        Maha.setIcon(bitmapDescriptorFromVector(MapsMain.this , R.drawable.gen_12));
                                        break;
                                    case "OTHER":
                                        Maha.setIcon(bitmapDescriptorFromVector(MapsMain.this , R.drawable.other));
                                        break;
                                    case "FOOD PRODUCTS":
                                        Maha.setIcon(bitmapDescriptorFromVector(MapsMain.this , R.drawable.food_12dp));
                                        break;
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }
                if(noMark)
                {
                    Toast.makeText(MapsMain.this , "There is no Shop Open nearby" , Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(MapsMain.this);
// Add the buttons
                    builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button
                        }
                    });

// Set other dialog properties

// Create the AlertDialog
                    AlertDialog dialog = builder.create();
                    dialog.setMessage("There is no Shop OPEN near by!");
                    dialog.show();

                }
                else
                {
                    map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(final Marker marker) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(MapsMain.this);
// Add the buttons
                        builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked OK button
                            }
                        });
                        builder.setNeutralButton(R.string.order, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked OK button
                                String idOrder = marker.getTag().toString();
                                Intent intToOrder = new Intent(MapsMain.this , OrderTest.class);
                                intToOrder.putExtra("idForOrder" , idOrder);
                                startActivity(intToOrder);
                            }
                        });

// Set other dialog properties

// Create the AlertDialog
                        AlertDialog dialog = builder.create();
                        dialog.setMessage("For Directions Click on Directions Icon in Lower Right Corner OR place an order!");
                        dialog.show();

                        return false;
                    }
                });
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public static float distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371; //meters
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        float dist = (float) (earthRadius * c);

        return dist;
    }
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorDrawableResourceId) {
        Drawable background = ContextCompat.getDrawable(context, R.drawable.ic_no_bg);
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        vectorDrawable.setBounds(20, 10, vectorDrawable.getIntrinsicWidth() + 20, vectorDrawable.getIntrinsicHeight() + 10);
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
