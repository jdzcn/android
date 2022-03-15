package com.example.sale;

public class Product {

    private String name;
    private int price;
    private int cost;
    private byte[] image;

    public Product(String name, int price,int cost,byte[] image) {
        this.name = name;
        this.price=price;
        this.cost=cost;
        this.image = image;
    }
    public int getPrice() {
        return price;
    }

    public int getCost() {
        return cost;
    }
    public String getName() {
        return name;
    }

    public byte[] getImage() {return image;}

}
