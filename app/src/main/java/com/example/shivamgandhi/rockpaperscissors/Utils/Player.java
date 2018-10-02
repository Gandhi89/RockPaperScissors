package com.example.shivamgandhi.rockpaperscissors.Utils;

public class Player {
    public String name;
    public String status;
    public String RPS;
    public String ready;
    public double lat;
    public double log;

    public Player() {

    }

    public Player(String name, String status, String rps, String ready, Double lat, Double log) {
        this.name = name;
        this.status = status;
        this.RPS = rps;
        this.ready = ready;
        this.lat = lat;
        this.log = log;
    }
}
