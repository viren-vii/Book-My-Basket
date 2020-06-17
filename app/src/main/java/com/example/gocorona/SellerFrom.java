package com.example.gocorona;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class SellerFrom extends AppCompatActivity {
    String id,EmailAsId;

    private  static  final  int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
    EditText nameEntry,phoneEntry,pincodeEntry,addressEntry;
    Button btnSubmit;
    double Latitude, Longitude;
    Geocoder geocoder;
    List<Address> addresses;
    DatabaseReference databaseSellers;
    //------------------------------SPINNER-------------------------
    ArrayList<String> States = new ArrayList<>();
    ArrayList<String> Districts = new ArrayList<>();
    ArrayList<String> Categories = new ArrayList<>();
    SpinnerDialog spinnerDialogStates;
    SpinnerDialog spinnerDialogDistricts;
    SpinnerDialog spinnerDialogCategories;
    Button btnSearchState;
    Button btnSearchDistrict;
    Button btnSelectCategory;
    String State,District,Category;
    //---------------------------------------------------------------

    FusedLocationProviderClient fusedLocationClient;
    TextView locationView;
    Button btnLoc;

    @Override
    protected void onStart() {
        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(SellerFrom.this);
        id = googleSignInAccount.getEmail();
        EmailAsId = id.replace("." , "-");
        Toast.makeText(SellerFrom.this , "On Start"+ EmailAsId , Toast.LENGTH_SHORT).show();
        nameEntry = findViewById(R.id.NameEntryS);
        phoneEntry = findViewById(R.id.PhoneEntryS);
        pincodeEntry = findViewById(R.id.PincodeEntryS);
        addressEntry = findViewById(R.id.AddressEntryS);
        btnSelectCategory = findViewById(R.id.category);
        btnSearchState = findViewById(R.id.SelectStateS);
        btnSearchDistrict = findViewById(R.id.SelectDistrictS);


        final DatabaseReference toCheckRef = FirebaseDatabase.getInstance().getReference().child("Sellers").child("ALL").child(EmailAsId);
        toCheckRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    Toast.makeText(SellerFrom.this , "I am here" , Toast.LENGTH_SHORT).show();
                    String name = Objects.requireNonNull(dataSnapshot.child("name").getValue()).toString();
                    String phone = Objects.requireNonNull(dataSnapshot.child("phone").getValue()).toString();
                    String pincode= Objects.requireNonNull(dataSnapshot.child("pincode").getValue()).toString();
                    String address = Objects.requireNonNull(dataSnapshot.child("address").getValue()).toString();
                    String category = Objects.requireNonNull(dataSnapshot.child("category").getValue()).toString();
                    String state = Objects.requireNonNull(dataSnapshot.child("state").getValue()).toString();
                    String district = Objects.requireNonNull(dataSnapshot.child("district").getValue()).toString();
                    String longitude = Objects.requireNonNull(dataSnapshot.child("longitude").getValue()).toString();
                    String latitude = Objects.requireNonNull(dataSnapshot.child("latitude").getValue()).toString();
                    Longitude = Double.parseDouble(longitude);
                    Latitude = Double.parseDouble(latitude);
                    District = district;
                    State = state;
                    Category = category;

                    nameEntry.setText(name);
                    phoneEntry.setText(phone);
                    pincodeEntry.setText(pincode);
                    addressEntry.setText(address);
                    btnSelectCategory.setText(category);
                    btnSearchDistrict.setText(district);
                    btnSearchState.setText(state);
                }
                else
                {
                    Toast.makeText(SellerFrom.this , "Please fill all the details!" , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_from);

        //+++++++++++++++++++++++++++++++++++++++++++++++++
        databaseSellers = FirebaseDatabase.getInstance().getReference("Sellers");

        nameEntry = findViewById(R.id.NameEntryS);
        phoneEntry = findViewById(R.id.PhoneEntryS);
        pincodeEntry = findViewById(R.id.PincodeEntryS);
        addressEntry = findViewById(R.id.AddressEntryS);
        btnSubmit = findViewById(R.id.SellerDataSubmit);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEntry.getText().toString().trim().toUpperCase();
                String phone = phoneEntry.getText().toString().trim();
                String pincode = pincodeEntry.getText().toString().trim();
                String address = addressEntry.getText().toString().trim().toUpperCase();

                String longitude = String.valueOf(Longitude);
                String latitude = String.valueOf(Latitude);
                if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(pincode) && !TextUtils.isEmpty(address)  && !TextUtils.isEmpty(longitude)&& !TextUtils.isEmpty(latitude))
                {
                    addSeller();
                    Intent goToHome = new Intent(SellerFrom.this , Seller_Home.class);
                    startActivity(goToHome);
                    finish();
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SellerFrom.this);
// Add the buttons
                    builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button
                        }
                    });

// Set other dialog properties

// Create the AlertDialog
                    AlertDialog dialog = builder.create();
                    dialog.setMessage("Please Fill All Fields Correctly");
                    dialog.show();

                }
            }
        });

//--------------------------------------------------------------------Spiiner-------------------------------------------------
        initializeState();
        initialzeDisctrict();
        initializeCategory();

        spinnerDialogStates = new SpinnerDialog(SellerFrom.this , States , "Select State");
        spinnerDialogDistricts = new SpinnerDialog(SellerFrom.this , Districts , "Select District");
        spinnerDialogCategories = new SpinnerDialog(SellerFrom.this , Categories , "Select Category");


        spinnerDialogStates.setCancellable(true);
        spinnerDialogStates.setShowKeyboard(false);
        spinnerDialogDistricts.setCancellable(true);
        spinnerDialogDistricts.setShowKeyboard(false);
        spinnerDialogCategories.setCancellable(true);
        spinnerDialogCategories.setShowKeyboard(false);


        spinnerDialogStates.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String state, int position) {
                State = state;
                Toast.makeText(SellerFrom.this , "Selected:"+ State , Toast.LENGTH_SHORT).show();
                btnSearchState.setText(state);

            }
        });
        spinnerDialogDistricts.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String district, int position) {
                District = district;
                Toast.makeText(SellerFrom.this , "Selected:"+ District , Toast.LENGTH_SHORT).show();
                btnSearchDistrict.setText(district);

            }
        });
        spinnerDialogCategories.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String category, int position) {
                Category = category;
                Toast.makeText(SellerFrom.this , "Selected:"+Category , Toast.LENGTH_SHORT).show();
                btnSelectCategory.setText(category);
            }
        });
        btnSearchState = findViewById(R.id.SelectStateS);
        btnSearchDistrict = findViewById(R.id.SelectDistrictS);
        btnSelectCategory = findViewById(R.id.category);

        btnSearchState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerDialogStates.showSpinerDialog();
            }
        });
        btnSearchDistrict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerDialogDistricts.showSpinerDialog();
            }
        });
        btnSelectCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerDialogCategories.showSpinerDialog();
            }
        });
        //--------------------------------------------------------------------------------------------------------------------
        btnLoc = findViewById(R.id.btnLocateS);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(SellerFrom.this);

        btnLoc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                fetchLocation();
            }

        });
//        btnLoc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(ActivityCompat.checkSelfPermission(SellerFrom.this , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
//                    getLocation();
//                }else {
//                    ActivityCompat.requestPermissions(SellerFrom.this , new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
//                }
//            }
//        });

        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(SellerFrom.this);
        id = googleSignInAccount.getEmail();
        EmailAsId = id.replace("." , "-");

    }

    private void setInfo() throws IOException {
        geocoder = new Geocoder(this, Locale.getDefault());
        addresses = geocoder.getFromLocation(Latitude, Longitude, 1);
        String district = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String pincode = addresses.get(0).getPostalCode();
        String address = addresses.get(0).getAddressLine(0);
        addressEntry.setText(address);
        pincodeEntry.setText(pincode);
        District = district;
        State = state;

        btnSearchDistrict.setText(district);
        btnSearchState.setText(state);
    }

    private void addSeller(){
        String name = nameEntry.getText().toString().trim().toUpperCase();
        String phone = phoneEntry.getText().toString().trim();
        String pincode = pincodeEntry.getText().toString().trim();
        String address = addressEntry.getText().toString().trim().toUpperCase();
        String district = District.toUpperCase();
        String state = State.toUpperCase();
        String email =  id;
        String category = Category.toUpperCase();
        String longitude = String.valueOf(Longitude);
        String latitude = String.valueOf(Latitude);
        String Status = "false";

        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(phone)  && !TextUtils.isEmpty(address))
        {
            //+++++++++++++++++++++++++++++++++++++++++++++++++++
            SharedPreferences preferences = getApplicationContext().getSharedPreferences(MainActivity.PREFS_NAME , Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("IdDB", id);
            editor.putString("Category",category);
            editor.putString("ShopName", name);
            editor.putString("Address",address);
            editor.apply();
            //+++++++++++++++++++++++++++++++++++++++++++++++++++

            NewSeller seller = new NewSeller(id , name , phone , pincode , address , district , state, email, longitude, latitude, Status, category);

            databaseSellers.child(category).child(EmailAsId).setValue(seller);
            databaseSellers.child("ALL").child(EmailAsId).setValue(seller);

            Toast.makeText(SellerFrom.this , "Your Information is Stored Successfully!" , Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(SellerFrom.this , "Please Fill All the Fields" , Toast.LENGTH_SHORT).show();
        }
    }
//-----------------------------------------------
    private void initialzeDisctrict() {
        Districts.add("Anantapur");
        Districts.add("Chittoor");
        Districts.add("East Godavari");
        Districts.add("Guntur");
        Districts.add("Kadapa");
        Districts.add("Krishna");
        Districts.add("Kurnool");
        Districts.add("Nellore");
        Districts.add("Prakasam");
        Districts.add("Srikakulam");
        Districts.add("Visakhapatnam");
        Districts.add("Vizianagaram");
        Districts.add("West Godavari");
        Districts.add("Anjaw");
        Districts.add("Siang");
        Districts.add("Changlang");
        Districts.add("Dibang Valley");
        Districts.add("East Kameng");
        Districts.add("East Siang");
        Districts.add("Kamle");
        Districts.add("Kra Daadi");
        Districts.add("Kurung Kumey");
        Districts.add("Lepa Rada ");
        Districts.add("Lohit");
        Districts.add("Longding");
        Districts.add("Lower Dibang Valley");
        Districts.add("Lower Siang");
        Districts.add("Lower Subansiri");
        Districts.add("Namsai");
        Districts.add("Pakke Kessang ");
        Districts.add("Papum Pare");
        Districts.add("Shi Yomi ");
        Districts.add("Tawang");
        Districts.add("Tirap");
        Districts.add("Upper Siang");
        Districts.add("Upper Subansiri");
        Districts.add("West Kameng");
        Districts.add("West Siang");
        Districts.add("Baksa");
        Districts.add("Barpeta");
        Districts.add("Biswanath");
        Districts.add("Bongaigaon");
        Districts.add("Cachar");
        Districts.add("Charaideo");
        Districts.add("Chirang");
        Districts.add("Darrang");
        Districts.add("Dhemaji");
        Districts.add("Dhubri");
        Districts.add("Dibrugarh");
        Districts.add("Dima Hasao");
        Districts.add("Goalpara");
        Districts.add("Golaghat");
        Districts.add("Hailakandi");
        Districts.add("Hojai");
        Districts.add("Jorhat");
        Districts.add("Kamrup");
        Districts.add("Kamrup Metropolitan");
        Districts.add("Karbi Anglong");
        Districts.add("Karimganj");
        Districts.add("Kokrajhar");
        Districts.add("Lakhimpur");
        Districts.add("Majuli");
        Districts.add("Morigaon");
        Districts.add("Nagaon");
        Districts.add("Nalbari");
        Districts.add("Sivasagar");
        Districts.add("Sonitpur");
        Districts.add("South Salmara-Mankachar");
        Districts.add("Tinsukia");
        Districts.add("Udalguri");
        Districts.add("West Karbi Anglong");
        Districts.add("Araria");
        Districts.add("Arwal");
        Districts.add("Aurangabad");
        Districts.add("Banka");
        Districts.add("Begusarai");
        Districts.add("Bhagalpur");
        Districts.add("Bhojpur");
        Districts.add("Buxar");
        Districts.add("Darbhanga");
        Districts.add("East Champaran");
        Districts.add("Gaya");
        Districts.add("Gopalganj");
        Districts.add("Jamui");
        Districts.add("Jehanabad");
        Districts.add("Kaimur");
        Districts.add("Katihar");
        Districts.add("Khagaria");
        Districts.add("Kishanganj");
        Districts.add("Lakhisarai");
        Districts.add("Madhepura");
        Districts.add("Madhubani");
        Districts.add("Munger");
        Districts.add("Muzaffarpur");
        Districts.add("Nalanda");
        Districts.add("Nawada");
        Districts.add("Patna");
        Districts.add("Purnia");
        Districts.add("Rohtas");
        Districts.add("Saharsa");
        Districts.add("Samastipur");
        Districts.add("Saran");
        Districts.add("Sheikhpura");
        Districts.add("Sheohar");
        Districts.add("Sitamarhi");
        Districts.add("Siwan");
        Districts.add("Supaul");
        Districts.add("Vaishali");
        Districts.add("West Champaran");
        Districts.add("Balod");
        Districts.add("Baloda Bazar");
        Districts.add("Balrampur Ramanujganj");
        Districts.add("Bastar");
        Districts.add("Bemetara");
        Districts.add("Bijapur");
        Districts.add("Bilaspur");
        Districts.add("Dantewada");
        Districts.add("Dhamtari");
        Districts.add("Durg");
        Districts.add("Gariaband");
        Districts.add("Gaurela Pendra Marwahi");
        Districts.add("Janjgir Champa");
        Districts.add("Jashpur");
        Districts.add("Kabirdham");
        Districts.add("Kanker");
        Districts.add("Kondagaon");
        Districts.add("Korba");
        Districts.add("Koriya");
        Districts.add("Mahasamund");
        Districts.add("Mungeli");
        Districts.add("Narayanpur");
        Districts.add("Raigarh");
        Districts.add("Raipur");
        Districts.add("Rajnandgaon");
        Districts.add("Sukma");
        Districts.add("Surajpur");
        Districts.add("Surguja");
        Districts.add("North Goa");
        Districts.add("South Goa");
        Districts.add("Ahmedabad");
        Districts.add("Amreli");
        Districts.add("Anand");
        Districts.add("Aravalli");
        Districts.add("Banaskantha");
        Districts.add("Bharuch");
        Districts.add("Bhavnagar");
        Districts.add("Botad");
        Districts.add("Chhota Udaipur");
        Districts.add("Dahod");
        Districts.add("Dang");
        Districts.add("Devbhoomi Dwarka");
        Districts.add("Gandhinagar");
        Districts.add("Gir Somnath");
        Districts.add("Jamnagar");
        Districts.add("Junagadh");
        Districts.add("Kheda");
        Districts.add("Kutch");
        Districts.add("Mahisagar");
        Districts.add("Mehsana");
        Districts.add("Morbi");
        Districts.add("Narmada");
        Districts.add("Navsari");
        Districts.add("Panchmahal");
        Districts.add("Patan");
        Districts.add("Porbandar");
        Districts.add("Rajkot");
        Districts.add("Sabarkantha");
        Districts.add("Surat");
        Districts.add("Surendranagar");
        Districts.add("Tapi");
        Districts.add("Vadodara");
        Districts.add("Valsad");
        Districts.add("Ambala");
        Districts.add("Bhiwani");
        Districts.add("Charkhi Dadri");
        Districts.add("Faridabad");
        Districts.add("Fatehabad");
        Districts.add("Gurugram ");
        Districts.add("Hisar");
        Districts.add("Jhajjar");
        Districts.add("Jind");
        Districts.add("Kaithal");
        Districts.add("Karnal");
        Districts.add("Kurukshetra");
        Districts.add("Mahendragarh");
        Districts.add("Mewat");
        Districts.add("Palwal");
        Districts.add("Panchkula");
        Districts.add("Panipat");
        Districts.add("Rewari");
        Districts.add("Rohtak");
        Districts.add("Sirsa");
        Districts.add("Sonipat");
        Districts.add("Yamunanagar");
        Districts.add("Bilaspur");
        Districts.add("Chamba");
        Districts.add("Hamirpur");
        Districts.add("Kangra");
        Districts.add("Kinnaur");
        Districts.add("Kullu");
        Districts.add("Lahaul Spiti");
        Districts.add("Mandi");
        Districts.add("Shimla");
        Districts.add("Sirmaur");
        Districts.add("Solan");
        Districts.add("Una");
        Districts.add("Anantnag");
        Districts.add("Bandipora");
        Districts.add("Baramulla");
        Districts.add("Budgam");
        Districts.add("Doda");
        Districts.add("Ganderbal");
        Districts.add("Jammu");
        Districts.add("Kathua");
        Districts.add("Kishtwar");
        Districts.add("Kulgam");
        Districts.add("Kupwara");
        Districts.add("Poonch");
        Districts.add("Pulwama");
        Districts.add("Rajouri");
        Districts.add("Ramban");
        Districts.add("Reasi");
        Districts.add("Samba");
        Districts.add("Shopian");
        Districts.add("Srinagar");
        Districts.add("Udhampur");
        Districts.add("Bokaro");
        Districts.add("Chatra");
        Districts.add("Deoghar");
        Districts.add("Dhanbad");
        Districts.add("Dumka");
        Districts.add("East Singhbhum");
        Districts.add("Garhwa");
        Districts.add("Giridih");
        Districts.add("Godda");
        Districts.add("Gumla");
        Districts.add("Hazaribagh");
        Districts.add("Jamtara");
        Districts.add("Khunti");
        Districts.add("Koderma");
        Districts.add("Latehar");
        Districts.add("Lohardaga");
        Districts.add("Pakur");
        Districts.add("Palamu");
        Districts.add("Ramgarh");
        Districts.add("Ranchi");
        Districts.add("Sahebganj");
        Districts.add("Seraikela Kharsawan");
        Districts.add("Simdega");
        Districts.add("West Singhbhum");
        Districts.add("Bagalkot");
        Districts.add("Bangalore Rural");
        Districts.add("Bangalore Urban");
        Districts.add("Belgaum");
        Districts.add("Bellary");
        Districts.add("Bidar");
        Districts.add("Chamarajanagar");
        Districts.add("Chikkaballapur");
        Districts.add("Chikkamagaluru");
        Districts.add("Chitradurga");
        Districts.add("Dakshina Kannada");
        Districts.add("Davanagere");
        Districts.add("Dharwad");
        Districts.add("Gadag");
        Districts.add("Gulbarga ");
        Districts.add("Hassan");
        Districts.add("Haveri");
        Districts.add("Kodagu");
        Districts.add("Kolar");
        Districts.add("Koppal");
        Districts.add("Mandya");
        Districts.add("Mysore");
        Districts.add("Raichur");
        Districts.add("Ramanagara");
        Districts.add("Shimoga");
        Districts.add("Tumkur");
        Districts.add("Udupi");
        Districts.add("Uttara Kannada");
        Districts.add("Vijayapura ");
        Districts.add("Yadgir");
        Districts.add("Alappuzha");
        Districts.add("Ernakulam");
        Districts.add("Idukki");
        Districts.add("Kannur");
        Districts.add("Kasaragod");
        Districts.add("Kollam");
        Districts.add("Kottayam");
        Districts.add("Kozhikode");
        Districts.add("Malappuram");
        Districts.add("Palakkad");
        Districts.add("Pathanamthitta");
        Districts.add("Thiruvananthapuram");
        Districts.add("Thrissur");
        Districts.add("Wayanad");
        Districts.add("Agar Malwa");
        Districts.add("Alirajpur");
        Districts.add("Anuppur");
        Districts.add("Ashoknagar");
        Districts.add("Balaghat");
        Districts.add("Barwani");
        Districts.add("Betul");
        Districts.add("Bhind");
        Districts.add("Bhopal");
        Districts.add("Burhanpur");
        Districts.add("Chachaura");
        Districts.add("Chhatarpur");
        Districts.add("Chhindwara");
        Districts.add("Damoh");
        Districts.add("Datia");
        Districts.add("Dewas");
        Districts.add("Dhar");
        Districts.add("Dindori");
        Districts.add("Guna");
        Districts.add("Gwalior");
        Districts.add("Harda");
        Districts.add("Hoshangabad");
        Districts.add("Indore");
        Districts.add("Jabalpur");
        Districts.add("Jhabua");
        Districts.add("Katni");
        Districts.add("Khandwa");
        Districts.add("Khargone");
        Districts.add("Maihar");
        Districts.add("Mandla");
        Districts.add("Mandsaur");
        Districts.add("Morena");
        Districts.add("Nagda");
        Districts.add("Narsinghpur");
        Districts.add("Neemuch");
        Districts.add("Niwari ");
        Districts.add("Panna");
        Districts.add("Raisen");
        Districts.add("Rajgarh");
        Districts.add("Ratlam");
        Districts.add("Rewa");
        Districts.add("Sagar");
        Districts.add("Satna");
        Districts.add("Sehore");
        Districts.add("Seoni");
        Districts.add("Shahdol");
        Districts.add("Shajapur");
        Districts.add("Sheopur");
        Districts.add("Shivpuri");
        Districts.add("Sidhi");
        Districts.add("Singrauli");
        Districts.add("Tikamgarh");
        Districts.add("Ujjain");
        Districts.add("Umaria");
        Districts.add("Vidisha");
        Districts.add("Ahmednagar");
        Districts.add("Akola");
        Districts.add("Amravati");
        Districts.add("Aurangabad");
        Districts.add("Beed");
        Districts.add("Bhandara");
        Districts.add("Buldhana");
        Districts.add("Chandrapur");
        Districts.add("Dhule");
        Districts.add("Gadchiroli");
        Districts.add("Gondia");
        Districts.add("Hingoli");
        Districts.add("Jalgaon");
        Districts.add("Jalna");
        Districts.add("Kolhapur");
        Districts.add("Latur");
        Districts.add("Mumbai City");
        Districts.add("Mumbai Suburban");
        Districts.add("Nagpur");
        Districts.add("Nanded");
        Districts.add("Nandurbar");
        Districts.add("Nashik");
        Districts.add("Osmanabad");
        Districts.add("Palghar");
        Districts.add("Parbhani");
        Districts.add("Pune");
        Districts.add("Raigad");
        Districts.add("Ratnagiri");
        Districts.add("Sangli");
        Districts.add("Satara");
        Districts.add("Sindhudurg");
        Districts.add("Solapur");
        Districts.add("Thane");
        Districts.add("Wardha");
        Districts.add("Washim");
        Districts.add("Yavatmal");
        Districts.add("Bishnupur");
        Districts.add("Chandel");
        Districts.add("Churachandpur");
        Districts.add("Imphal East");
        Districts.add("Imphal West");
        Districts.add("Jiribam");
        Districts.add("Kakching");
        Districts.add("Kamjong");
        Districts.add("Kangpokpi");
        Districts.add("Noney");
        Districts.add("Pherzawl");
        Districts.add("Senapati");
        Districts.add("Tamenglong");
        Districts.add("Tengnoupal");
        Districts.add("Thoubal");
        Districts.add("Ukhrul");
        Districts.add("East Garo Hills");
        Districts.add("East Jaintia Hills");
        Districts.add("East Khasi Hills");
        Districts.add("North Garo Hills");
        Districts.add("Ri Bhoi");
        Districts.add("South Garo Hills");
        Districts.add("South West Garo Hills");
        Districts.add("South West Khasi Hills");
        Districts.add("West Garo Hills");
        Districts.add("West Jaintia Hills");
        Districts.add("West Khasi Hills");
        Districts.add("Aizawl");
        Districts.add("Champhai");
        Districts.add("Kolasib");
        Districts.add("Lawngtlai");
        Districts.add("Lunglei");
        Districts.add("Mamit");
        Districts.add("Saiha");
        Districts.add("Serchhip");
        Districts.add("Mon");
        Districts.add("Dimapur");
        Districts.add("Kiphire");
        Districts.add("Kohima");
        Districts.add("Longleng");
        Districts.add("Mokokchung");
        Districts.add("Noklak");
        Districts.add("Peren");
        Districts.add("Phek");
        Districts.add("Tuensang");
        Districts.add("Wokha");
        Districts.add("Zunheboto");
        Districts.add("Angul");
        Districts.add("Balangir");
        Districts.add("Balasore");
        Districts.add("Bargarh");
        Districts.add("Bhadrak");
        Districts.add("Boudh");
        Districts.add("Cuttack");
        Districts.add("Debagarh");
        Districts.add("Dhenkanal");
        Districts.add("Gajapati");
        Districts.add("Ganjam");
        Districts.add("Jagatsinghpur");
        Districts.add("Jajpur");
        Districts.add("Jharsuguda");
        Districts.add("Kalahandi");
        Districts.add("Kandhamal");
        Districts.add("Kendrapara");
        Districts.add("Kendujhar");
        Districts.add("Khordha ");
        Districts.add("Koraput");
        Districts.add("Malkangiri");
        Districts.add("Mayurbhanj");
        Districts.add("Nabarangpur");
        Districts.add("Nayagarh");
        Districts.add("Nuapada");
        Districts.add("Puri");
        Districts.add("Rayagada");
        Districts.add("Sambalpur");
        Districts.add("Subarnapur");
        Districts.add("Sundergarh");
        Districts.add("Amritsar");
        Districts.add("Barnala");
        Districts.add("Bathinda");
        Districts.add("Faridkot");
        Districts.add("Fatehgarh Sahib");
        Districts.add("Fazilka");
        Districts.add("Firozpur");
        Districts.add("Gurdaspur");
        Districts.add("Hoshiarpur");
        Districts.add("Jalandhar");
        Districts.add("Kapurthala");
        Districts.add("Ludhiana");
        Districts.add("Mansa");
        Districts.add("Moga");
        Districts.add("Mohali");
        Districts.add("Muktsar");
        Districts.add("Pathankot");
        Districts.add("Patiala");
        Districts.add("Rupnagar");
        Districts.add("Sangrur");
        Districts.add("Shaheed Bhagat Singh Nagar");
        Districts.add("Tarn Taran");
        Districts.add("Ajmer");
        Districts.add("Alwar");
        Districts.add("Banswara");
        Districts.add("Baran");
        Districts.add("Barmer");
        Districts.add("Bharatpur");
        Districts.add("Bhilwara");
        Districts.add("Bikaner");
        Districts.add("Bundi");
        Districts.add("Chittorgarh");
        Districts.add("Churu");
        Districts.add("Dausa");
        Districts.add("Dholpur");
        Districts.add("Dungarpur");
        Districts.add("Sri Ganganagar");
        Districts.add("Hanumangarh");
        Districts.add("Jaipur");
        Districts.add("Jaisalmer");
        Districts.add("Jalore");
        Districts.add("Jhalawar");
        Districts.add("Jhunjhunu");
        Districts.add("Jodhpur");
        Districts.add("Karauli");
        Districts.add("Kota");
        Districts.add("Nagaur");
        Districts.add("Pali");
        Districts.add("Pratapgarh");
        Districts.add("Rajsamand");
        Districts.add("Sawai Madhopur");
        Districts.add("Sikar");
        Districts.add("Sirohi");
        Districts.add("Tonk");
        Districts.add("Udaipur");
        Districts.add("East Sikkim");
        Districts.add("North Sikkim");
        Districts.add("South Sikkim");
        Districts.add("West Sikkim");
        Districts.add("Ariyalur");
        Districts.add("Chengalpattu ");
        Districts.add("Chennai");
        Districts.add("Coimbatore");
        Districts.add("Cuddalore");
        Districts.add("Dharmapuri");
        Districts.add("Dindigul");
        Districts.add("Erode");
        Districts.add("Kallakurichi ");
        Districts.add("Kanchipuram");
        Districts.add("Kanyakumari");
        Districts.add("Karur");
        Districts.add("Krishnagiri");
        Districts.add("Madurai");
        Districts.add("Mayiladuthurai");
        Districts.add("Nagapattinam");
        Districts.add("Namakkal");
        Districts.add("Nilgiris");
        Districts.add("Perambalur");
        Districts.add("Pudukkottai");
        Districts.add("Ramanathapuram");
        Districts.add("Ranipet");
        Districts.add("Salem");
        Districts.add("Sivaganga");
        Districts.add("Tenkasi ");
        Districts.add("Thanjavur");
        Districts.add("Theni");
        Districts.add("Thoothukudi");
        Districts.add("Tiruchirappalli");
        Districts.add("Tirunelveli");
        Districts.add("Tirupattur");
        Districts.add("Tiruppur");
        Districts.add("Tiruvallur");
        Districts.add("Tiruvannamalai");
        Districts.add("Tiruvarur");
        Districts.add("Vellore");
        Districts.add("Viluppuram");
        Districts.add("Virudhunagar");
        Districts.add("Adilabad");
        Districts.add("Bhadradri Kothagudem");
        Districts.add("Hyderabad");
        Districts.add("Jagtial");
        Districts.add("Jangaon");
        Districts.add("Jayashankar Bhupalpally");
        Districts.add("Jogulamba Gadwal");
        Districts.add("Kamareddy");
        Districts.add("Karimnagar");
        Districts.add("Khammam");
        Districts.add("Komaram Bheem");
        Districts.add("Mahabubabad");
        Districts.add("Mahbubnagar");
        Districts.add("Mancherial");
        Districts.add("Medak");
        Districts.add("Medchal");
        Districts.add("Mulugu ");
        Districts.add("Nagarkurnool");
        Districts.add("Nalgonda");
        Districts.add("Narayanpet ");
        Districts.add("Nirmal");
        Districts.add("Nizamabad");
        Districts.add("Peddapalli");
        Districts.add("Rajanna Sircilla");
        Districts.add("Ranga Reddy");
        Districts.add("Sangareddy");
        Districts.add("Siddipet");
        Districts.add("Suryapet");
        Districts.add("Vikarabad");
        Districts.add("Wanaparthy");
        Districts.add("Warangal Rural");
        Districts.add("Warangal Urban");
        Districts.add("Yadadri Bhuvanagiri");
        Districts.add("Dhalai");
        Districts.add("Gomati");
        Districts.add("Khowai");
        Districts.add("North Tripura");
        Districts.add("Sepahijala");
        Districts.add("South Tripura");
        Districts.add("Unakoti");
        Districts.add("West Tripura");
        Districts.add("Agra");
        Districts.add("Aligarh");
        Districts.add("Prayagraj");
        Districts.add("Ambedkar Nagar");
        Districts.add("Amethi ");
        Districts.add("Amroha ");
        Districts.add("Auraiya");
        Districts.add("Azamgarh");
        Districts.add("Baghpat");
        Districts.add("Bahraich");
        Districts.add("Ballia");
        Districts.add("Balrampur");
        Districts.add("Banda");
        Districts.add("Barabanki");
        Districts.add("Bareilly");
        Districts.add("Basti");
        Districts.add("Bhadohi");
        Districts.add("Bijnor");
        Districts.add("Budaun");
        Districts.add("Bulandshahr");
        Districts.add("Chandauli");
        Districts.add("Chitrakoot");
        Districts.add("Deoria");
        Districts.add("Etah");
        Districts.add("Etawah");
        Districts.add("Ayodhya ");
        Districts.add("Farrukhabad");
        Districts.add("Fatehpur");
        Districts.add("Firozabad");
        Districts.add("Gautam Buddha Nagar");
        Districts.add("Ghaziabad");
        Districts.add("Ghazipur");
        Districts.add("Gonda");
        Districts.add("Gorakhpur");
        Districts.add("Hamirpur");
        Districts.add("Hapur ");
        Districts.add("Hardoi");
        Districts.add("Hathras ");
        Districts.add("Jalaun");
        Districts.add("Jaunpur");
        Districts.add("Jhansi");
        Districts.add("Kannauj");
        Districts.add("Kanpur Dehat ");
        Districts.add("Kanpur Nagar");
        Districts.add("Kasganj ");
        Districts.add("Kaushambi");
        Districts.add("Kheri");
        Districts.add("Kushinagar");
        Districts.add("Lalitpur");
        Districts.add("Lucknow");
        Districts.add("Maharajganj");
        Districts.add("Mahoba");
        Districts.add("Mainpuri");
        Districts.add("Mathura");
        Districts.add("Mau");
        Districts.add("Meerut");
        Districts.add("Mirzapur");
        Districts.add("Moradabad");
        Districts.add("Muzaffarnagar");
        Districts.add("Pilibhit");
        Districts.add("Pratapgarh");
        Districts.add("Raebareli");
        Districts.add("Rampur");
        Districts.add("Saharanpur");
        Districts.add("Sambhal *");
        Districts.add("Sant Kabir Nagar");
        Districts.add("Shahjahanpur");
        Districts.add("Shamli *");
        Districts.add("Shravasti");
        Districts.add("Siddharthnagar");
        Districts.add("Sitapur");
        Districts.add("Sonbhadra");
        Districts.add("Sultanpur");
        Districts.add("Unnao");
        Districts.add("Varanasi");
        Districts.add("Almora");
        Districts.add("Bageshwar");
        Districts.add("Chamoli");
        Districts.add("Champawat");
        Districts.add("Dehradun");
        Districts.add("Haridwar");
        Districts.add("Nainital");
        Districts.add("Pauri");
        Districts.add("Pithoragarh");
        Districts.add("Rudraprayag");
        Districts.add("Tehri");
        Districts.add("Udham Singh Nagar");
        Districts.add("Uttarkashi");
        Districts.add("Alipurduar");
        Districts.add("Bankura");
        Districts.add("Birbhum");
        Districts.add("Cooch Behar");
        Districts.add("Dakshin Dinajpur");
        Districts.add("Darjeeling");
        Districts.add("Hooghly");
        Districts.add("Howrah");
        Districts.add("Jalpaiguri");
        Districts.add("Jhargram");
        Districts.add("Kalimpong");
        Districts.add("Kolkata");
        Districts.add("Malda");
        Districts.add("Murshidabad");
        Districts.add("Nadia");
        Districts.add("North 24 Parganas");
        Districts.add("Paschim Bardhaman");
        Districts.add("Paschim Medinipur");
        Districts.add("Purba Bardhaman");
        Districts.add("Purba Medinipur");
        Districts.add("Purulia");
        Districts.add("South 24 Parganas");
        Districts.add("Uttar Dinajpur");
        Districts.add("Nicobar");
        Districts.add("North Middle Andaman");
        Districts.add("South Andaman");
        Districts.add("Chandigarh");
        Districts.add("Dadra Nagar Haveli");
        Districts.add("Daman");
        Districts.add("Diu");
        Districts.add("Central Delhi");
        Districts.add("East Delhi");
        Districts.add("New Delhi");
        Districts.add("North Delhi");
        Districts.add("North East Delhi");
        Districts.add("North West Delhi");
        Districts.add("Shahdara");
        Districts.add("South Delhi");
        Districts.add("South East Delhi");
        Districts.add("South West Delhi");
        Districts.add("West Delhi");
        Districts.add("Lakshadweep");
        Districts.add("Kargil");
        Districts.add("Leh");
        Districts.add("Karaikal");
        Districts.add("Mahe");
        Districts.add("Puducherry");
        Districts.add("Yanam");
    }

    private void initializeState() {
        States.add("Andhra Pradesh (AP)");
        States.add("Arunachal Pradesh (AR)");
        States.add("Assam (AS)");
        States.add("Bihar (BR)");
        States.add("Chhattisgarh (CG)");
        States.add("Goa (GA)");
        States.add("Gujarat (GJ)");
        States.add("Haryana (HR)");
        States.add("Himachal Pradesh (HP)");
        States.add("Jammu and Kashmir (JK)");
        States.add("Jharkhand (JH)");
        States.add("Karnataka (KA)");
        States.add("Kerala (KL)");
        States.add("Madhya Pradesh (MP)");
        States.add("Maharashtra (MH)");
        States.add("Manipur (MN)");
        States.add("Meghalaya (ML)");
        States.add("Mizoram (MZ)");
        States.add("Nagaland (NL)");
        States.add("Odisha(OR)");
        States.add("Punjab (PB)");
        States.add("Rajasthan (RJ)");
        States.add("Sikkim (SK)");
        States.add("Tamil Nadu (TN)");
        States.add("Telangana (TS)");
        States.add("Tripura (TR)");
        States.add("Uttar Pradesh (UP)");
        States.add("Uttarakhand (UK)");
        States.add("West Bengal (WB)");
        States.add("Andaman and Nicobar Islands(AN)");
        States.add("Chandigarh (CH)");
        States.add("Dadra and Nagar Haveli (DN)");
        States.add("Daman and Diu (DD)");
        States.add("National Capital Territory of Delhi (DL)");
        States.add("Lakshadweep (LD)");
        States.add("Pondicherry (PY)");
    }

    private void initializeCategory(){
        Categories.add("GROCERIES");
        Categories.add("FOOD PRODUCTS");
        Categories.add("HEALTH CARE");
        Categories.add("GENERAL STORE");
        Categories.add("OTHER");
    }
//------------------------------------------------

//    private void getLocation() {
//        Toast.makeText(this , "getting location" , Toast.LENGTH_SHORT).show();
//
//        fusedLocationClient.getLastLocation().addOnCompleteListener(this , new OnCompleteListener<Location>() {
//            @Override
//            public void onComplete(@NonNull Task<Location> task) {
//                Location location =  task.getResult();
//                Toast.makeText(SellerFrom.this , "we are inside onComplete" + location , Toast.LENGTH_SHORT).show();
//                if(location != null)
//                {
//                    Toast.makeText(SellerFrom.this , "Location is not null" , Toast.LENGTH_SHORT).show();
//                    try {
//                        Geocoder geocoder = new Geocoder(SellerFrom.this , Locale.getDefault());
//                        Toast.makeText(SellerFrom.this , "We are inside try" , Toast.LENGTH_SHORT).show();
//
//                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(), 1);
//                        Toast.makeText(SellerFrom.this , "Got the location" + addresses.get(0).getLatitude() , Toast.LENGTH_SHORT).show();
//                        locationView.setText(Html.fromHtml("<font color = '#ff1234' <b> Something: </b></font> " + addresses.get(0).getLatitude()));
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//    }
    private void fetchLocation()
    {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(SellerFrom.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(SellerFrom.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                new AlertDialog.Builder(SellerFrom.this)
                        .setTitle("Required Location Permission")
                        .setMessage("Allow location to feed you location!")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // No explanation needed; request the permission
                                ActivityCompat.requestPermissions(SellerFrom.this,
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
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(SellerFrom.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted

            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                Latitude = location.getLatitude();
                                Longitude = location.getLongitude();

                                Toast.makeText(SellerFrom.this, "Successgully Registered your Location!\nYou're at "+Latitude+Longitude+"\n", Toast.LENGTH_SHORT).show();

                                try {
                                    setInfo();
                                } catch (IOException e) {
                                    e.printStackTrace();
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

}
