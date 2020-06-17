package com.example.gocorona;

public class Deal {
    private String Quantity;
    private String Name;
    private String price;
    private String PostKey;

    public Deal() {
    }

    public String getPostKey() {
        return PostKey;
    }

    public void setPostKey(String postKey) {
        PostKey = postKey;
    }

    public Deal(String Quantity, String Name) {
        this.Quantity = Quantity;
        this.Name = Name;
        //this.price = price;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String Quantity) {
        this.Quantity = Quantity;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}