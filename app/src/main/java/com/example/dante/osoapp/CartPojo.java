package com.example.dante.osoapp;

public class CartPojo {
    private  int number;
    private String name;
    private String price;
    private  String image;
    private int image1;


    public CartPojo(int number, String name, String price, int image1) {
        this.number = number;
        this.name = name;
        this.price = price;
        this.image1 = image1;
    }
public  CartPojo(int number,String name,String price){
        this.number=number;
    this.name = name;
    this.price = price;
}
    public CartPojo(int number, String name, String price, String image) {
        this.number = number;
        this.name = name;
        this.price = price;
        this.image = image;
    }
    public int getImage1() {
        return image1;
    }

    public void setImage1(int image1) {
        this.image1 = image1;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
