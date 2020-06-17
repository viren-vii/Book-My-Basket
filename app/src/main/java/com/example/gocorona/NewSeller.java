package com.example.gocorona;

public class NewSeller {
    String id,name,phone,pincode,address,district,state,email, longitude, latitude, Status, category;

    public NewSeller(){

    }

    public NewSeller(String id, String name, String phone, String pincode, String address, String district, String state , String email, String longitude, String latitude, String Status, String category) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.pincode = pincode;
        this.address = address;
        this.district = district;
        this.state = state;
        this.email = email;
        this.longitude = longitude;
        this.latitude = latitude;
        this.Status = Status;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getPincode() {
        return pincode;
    }

    public String getAddress() {
        return address;
    }
    public  String getDistrict(){
        return district;
    }
    public String getState(){
        return state;
    }
    public String getEmail(){return email;}
    public String getLongitude(){ return  longitude;}
    public  String getLatitude(){return  latitude;}
    public String getStatus(){return Status;}
    public String getCategory(){return category;}
}
