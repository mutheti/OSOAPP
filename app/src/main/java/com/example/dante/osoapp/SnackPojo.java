package com.example.dante.osoapp;

public class SnackPojo {
    private String image;
    private String name;
    private String price;
    private String desription;

    public SnackPojo(String image, String name, String price, String desription) {
        this.image = image;
        this.name = name;
        this.price = price;
        this.desription = desription;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDesription() {
        return desription;
    }

    public void setDesription(String desription) {
        this.desription = desription;
    }
}
