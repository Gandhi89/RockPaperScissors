package com.example.shivamgandhi.rockpaperscissors.Utils;

public class User {

    public String Email;
    public String userName;
    public int won;
    public int played;
    public String title;
    public double lat;
    public double log;
    public String Status;

    public User() {

    }

    public User(String email, String userName, int won,int played, String title, double lat, double log, String status) {
        Email = email;
        this.userName = userName;
        this.won = won;
        this.played = played;
        this.title = title;
        this.lat = lat;
        this.log = log;
        Status = status;
    }
}
