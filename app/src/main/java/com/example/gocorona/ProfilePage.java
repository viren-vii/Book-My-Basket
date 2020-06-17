package com.example.gocorona;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ProfilePage extends AppCompatActivity {
    private Button btnBack;
    String id,EmailAsId;
    TextView Name,Email,PhoneNo,Address,District,State,Pincode;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        id = account.getEmail();
        EmailAsId = id.replace("." , "-");
//        String personName = account.getDisplayName();
//        String personEmail = account.getEmail();
//        Uri img = account.getPhotoUrl();

//        ImageView Image = findViewById(R.id.image);
        Name = findViewById(R.id.nameTextView);
        Email = findViewById(R.id.emailId);
        PhoneNo = findViewById(R.id.phone);
        Address = findViewById(R.id.address);

//
//        SharedPreferences id = getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
//        String Id = id.getString("IdDB", "1");
//        boolean isCustomer = id.getBoolean("isCustomer", false);
//        boolean isSeller = id.getBoolean("isSeller", false);

            databaseReference = FirebaseDatabase.getInstance().getReference().child("Sellers").child("ALL").child(EmailAsId);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String name = Objects.requireNonNull(dataSnapshot.child("name").getValue()).toString().toUpperCase();
                    String phone = Objects.requireNonNull(dataSnapshot.child("phone").getValue()).toString();
                    String address = Objects.requireNonNull(dataSnapshot.child("address").getValue()).toString().toUpperCase();
                    String state = Objects.requireNonNull(dataSnapshot.child("state").getValue()).toString().toUpperCase();
                    String district = Objects.requireNonNull(dataSnapshot.child("district").getValue()).toString().toUpperCase();
                    String pincode  = Objects.requireNonNull(dataSnapshot.child("pincode").getValue()).toString();
                    String email = Objects.requireNonNull(dataSnapshot.child("email").getValue()).toString();
                    Name.setText(name);
                    Email.setText(email);
                    PhoneNo.setText(phone);
                    address = address+","+state+","+district+"-"+pincode;
                    Address.setText(address);
//                State.setText(state);
////                District.setText(district);
////                Pincode.setText(pincode);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ProfilePage.this , "Some error has occurred!" , Toast.LENGTH_SHORT).show();
                }
            });

        }





//        Image.setImageURI(img);
//        Name.setText(personName);
//        Email.setText(personEmail);

//        Button btnLoc = findViewById(R.id.buttonToGetLoc);
//
//        btnLoc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(ProfilePage.this);
//
//                if(ActivityCompat.checkSelfPermission(ProfilePage.this , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
//                    getLocation();
//                }else {
//                    ActivityCompat.requestPermissions(ProfilePage.this , new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
//                }
//            }
//
//            private void getLocation() {
//                fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Location> task) {
//                        Location location = task.getResult();
//                        if(location != null)
//                        {
//                            try {
//                                Geocoder geocoder = new Geocoder(ProfilePage.this , Locale.getDefault());
//                                List<Address> addresses = geocoder.getFromLocation(
//                                        location.getLatitude(),location.getLongitude(), 1
//                                );
//                                Add.setText(addresses.get(0).getAddressLine(0));
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                });
//            }
//
//        });
}


