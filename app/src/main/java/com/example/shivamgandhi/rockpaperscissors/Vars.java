package com.example.shivamgandhi.rockpaperscissors;

public class Vars {
    private static Vars singleton = new Vars();

    private Vars() {
    }

    public static Vars getInstance() {
        return singleton;
    }

    // -------------------------------------------------------------------------------------------- //

    // ServerEnd
    private String gameID = "";

    // UserEnd
    private String playerName = "p";
    private String playerID = "";
    private String RPSvalue = "none";
    private Double latitude = 0.0;
    private Double longitude = 0.0;
    private String ready = "not ready";
    private String status = "default";


    // -------------------------------------------------------------------------------------------- //
    public String getGameID() {
        return gameID;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerID() {
        return playerID;
    }

    public void setPlayerID(String playerID) {
        this.playerID = playerID;
    }

    public String getRPSvalue() {
        return RPSvalue;
    }

    public void setRPSvalue(String RPSvalue) {
        this.RPSvalue = RPSvalue;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getReady() {
        return ready;
    }

    public void setReady(String ready) {
        this.ready = ready;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
