package com.example.sale;

public class Product {
    private int pid;
    private String name;
    private int price;
    private int cost;
    private byte[] image;

    public Product(int pid,String name, int price,int cost,byte[] image) {
        this.pid=pid;
        this.name = name;

        this.price=price;
        this.cost=cost;
        this.image = image;
    }
    public int getPrice() {
        return price;
    }
    public int getPid() {
        return pid;
    }
    public int getCost() {
        return cost;
    }
    public String getName() {
        return name;
    }

    public byte[] getImage() {return image;}

}
