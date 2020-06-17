package com.example.gocorona;

public class NewCustomer {
     String longitude, latitude;

    public NewCustomer(){

    }

    public NewCustomer(String longitude, String latitude) {

        this.latitude = latitude;
        this.longitude = longitude;
    }

    public  String getLongitude(){return longitude;}
    public String getLatitude(){ return  latitude; }
}
