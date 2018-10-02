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

    public User(String a, String b, int c,int h, String d, double e, double f, String g) {
        Email = a;
        userName = b;
        won = c;
        played = h;
        title = d;
        lat = e;
        log = f;
        Status = g;
    }
}
