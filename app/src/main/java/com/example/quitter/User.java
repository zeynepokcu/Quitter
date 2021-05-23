package com.example.quitter;

public class User {
    public String email;
    public String password;
    public int amount;
    public int time;
    public int price;

    public User(){

    }

    public User(String email, String password, int amount, int time, int price) {
        this.email = email;
        this.password = password;
        this.amount = amount;
        this.time = time;
        this.price = price;
    }


}
