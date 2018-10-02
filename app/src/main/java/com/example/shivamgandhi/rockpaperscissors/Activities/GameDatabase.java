package com.example.shivamgandhi.rockpaperscissors.Activities;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.shivamgandhi.rockpaperscissors.Utils.Player;
import com.example.shivamgandhi.rockpaperscissors.Utils.Vars;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class GameDatabase {

    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;
    Vars mVars;
    Player mPlayer;
    int point_r = 0;
    int point_p = 0;
    int point_s = 0;
    int count_r = 0, count_p = 0, count_s = 0, count_none = 0;
    ArrayList<String> winingStatus = new ArrayList<>(); // IN ORDER OF RPS
    ArrayList<String> userChoice = new ArrayList<>(); // STORE RPS OF EACH PLAYER
    String getWiningStatus_R, getWiningStatus_P, getWiningStatus_S;

    public void GameDatabase(){}

    public void initializeVariable(){

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
        mVars = Vars.getInstance();
    }

    // --------------------------------------------------------------------------------------------- //

    /**
     * Function to create new game
     * @return gameID
     */
    public String createGame(){

        initializeVariable();
        DatabaseReference games = mDatabaseReference.child("Games");
        DatabaseReference game = games.child(generateRandomNumber());
        game.child("status").setValue("waiting");
        game.child("latitude").setValue("0");
        game.child("longitude").setValue("0");
        return game.getKey();
    }

    // --------------------------------------------------------------------------------------------- //

    /**
     * Function to create random number
     * @return random number [6 digit]
     */
    public String generateRandomNumber(){

        String randomNumber = "";
        Random rnd = new Random();
        int n = 100000 + rnd.nextInt(900000);
        randomNumber = String.valueOf(n);
        return randomNumber;
    }

    // --------------------------------------------------------------------------------------------- //

    /**
     * Function to join(player) into the existing game
     * @return PlayerID
     */
    public String joinGame(){

        initializeVariable();
        mPlayer = new Player("player name", "default", "none", "not ready",0.0,0.0);
        DatabaseReference games = mDatabaseReference.child("Games");
        DatabaseReference game = games.child(mVars.getGameID());
        DatabaseReference players = game.child("Players");
        DatabaseReference player = players.push();
        player.setValue(mPlayer);

        return player.getKey();
    }

    // --------------------------------------------------------------------------------------------- //

    /**
     * Function to set RPSvalue of player
     * @param playerName
     * @param RPSvalue
     */
    public void setRPSvalue(String playerName, String RPSvalue){

        mVars = Vars.getInstance();
        mPlayer = new Player(playerName, mVars.getStatus(), RPSvalue, mVars.getReady(),mVars.getLatitude(),mVars.getLongitude());
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
        mDatabaseReference.child("Games").child(mVars.getGameID()).child("Players").child(mVars.getPlayerID()).setValue(mPlayer);
    }

    // --------------------------------------------------------------------------------------------- //

    /**
     *
     * @param cnt_r
     * @param cnt_p
     * @param cnt_s
     * @param cnt_none
     * @return
     */
    public ArrayList<String> calResult(int cnt_r, int cnt_p, int cnt_s, int cnt_none){

        Log.d("val/cnt_r: ", "" + cnt_r);
        Log.d("val/cnt_p: ", "" + cnt_p);
        Log.d("val/cnt_s: ", "" + cnt_s);
        Log.d("val/cnt_r: ", "" + cnt_none);

        /**
         * get count of R/P/S -> count points of team R/P/S
         */
        point_r = cnt_s;
        point_p = cnt_r;
        point_s = cnt_p;

        Log.d("val/point_r: ", "" + point_r);
        Log.d("val/point_p: ", "" + point_p);
        Log.d("val/point_s: ", "" + point_s);

        /**
         * get max point of team R/P/S
         */
        int max_point = point_p;
        if (max_point < point_r) {
            max_point = point_r;
        }
        if (max_point < point_s) {
            max_point = point_s;
        }

        Log.d("val/max_point: ", "" + max_point);

        /**
         * count how many teams have max_point score
         */
        int no_max_point = 0;
        if (point_r == max_point) {
            no_max_point++;
        }
        if (point_p == max_point) {
            no_max_point++;
        }
        if (point_s == max_point) {
            no_max_point++;
        }

        Log.d("val/no_max_point: ", "" + no_max_point);

        String winingStatus_rock = "lose";
        String winingStatus_paper = "lose";
        String winingStatus_scissors = "lose";

        /**
         * case - every player got same option [R/P/S]
         */
        // ------------------------------------ //
        if (max_point == (cnt_r + cnt_p + cnt_s + cnt_none)) {
            winingStatus_rock = "draw";
            winingStatus_paper = "draw";
            winingStatus_scissors = "draw";
            no_max_point = 4;
        }

        /**
         * case - one [team] definite winner
         */
        // ------------------------------------ //
        if (no_max_point == 1) {
            Log.d("val/inside-1: ", "" + 1);

            if (max_point == cnt_r) {
                winingStatus_rock = "win";
                Log.d("val/inside-1-R: ", "" + 1);
            }
            if (max_point == cnt_p) {
                winingStatus_paper = "win";
                Log.d("val/inside-1-P: ", "" + 1);
            }
            if (max_point == cnt_s) {
                winingStatus_scissors = "win";
                Log.d("val/inside-1-s: ", "" + 1);
            }
            if ((cnt_r > 0) && (winingStatus_scissors.equals("win"))) {
                winingStatus_scissors = "lose";
                winingStatus_rock = "win";
            } else if ((cnt_p > 0) && (winingStatus_rock.equals("win"))) {
                winingStatus_rock = "lose";
                winingStatus_paper = "win";
            } else if ((cnt_s > 0) && (winingStatus_paper.equals("win"))) {
                winingStatus_paper = "lose";
                winingStatus_scissors = "win";
            }
        }

        /**
         * case - two [team] possible winners
         */
        // ------------------------------------ //
        else if (no_max_point == 2) {
            Log.d("val/inside-2: ", "" + 2);

            if (point_r == max_point) {
                if (cnt_r > 0) {
                    winingStatus_rock = "win";
                }
            }
            if (point_p == max_point) {
                if (cnt_p > 0) {
                    winingStatus_paper = "win";
                }
            }
            if (point_s == max_point) {
                if (cnt_s > 0) {
                    winingStatus_scissors = "win";
                }
            }
        }

        /**
         * case - three [team] definite winners = draw
         */
        // ------------------------------------ //
        else if (no_max_point == 3) {
            Log.d("val/inside-3: ", "" + 3);
            if (cnt_none > 0) {
                winingStatus_scissors = "win";
                winingStatus_rock = "win";
                winingStatus_paper = "win";
            } else {
                Log.d("val/inside-3-draw: ", "" + 3);
                winingStatus_scissors = "draw";
                winingStatus_rock = "draw";
                winingStatus_paper = "draw";
            }
        }

        winingStatus.add(winingStatus_rock);
        winingStatus.add(winingStatus_paper);
        winingStatus.add(winingStatus_scissors);

        Log.d("val/Rock:- ", winingStatus_rock);
        Log.d("val/Paper:- ", winingStatus_paper);
        Log.d("val/Scissors:- ", winingStatus_scissors);

        return winingStatus;
    }

    // --------------------------------------------------------------------------------------------- //

    /**
     * Function to get RPS value of each player
     * @return
     */
    public ArrayList<String> getRPSValue() {
        mVars = Vars.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();

        mDatabaseReference.child("Games").child(mVars.getGameID()).child("Players").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                userChoice.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Player player = postSnapshot.getValue(Player.class);
                    userChoice.add(player.RPS);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Log.d("cal/userValue", "RPS value:- " + userChoice);
        return userChoice;
    }

    // --------------------------------------------------------------------------------------------- //

    /**
     * Function to set value of R/P/S/None
     * @param userChoice
     */
    public void setCount_rps(ArrayList<String> userChoice) {
        mVars = Vars.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();

        this.userChoice = userChoice;

        for (int i = 0; i < this.userChoice.size(); i++) {
            if (this.userChoice.get(i).equals("rock")) {
                count_r++;
                Log.d("CalculateResult/cnt_r:-", "" + count_r);
            } else if (this.userChoice.get(i).equals("paper")) {
                count_p++;
                Log.d("CalculateResult/cnt_p:-", "" + count_p);
            } else if (this.userChoice.get(i).equals("scissors")) {
                count_s++;
                Log.d("CalculateResult/cnt_s:-", "" + count_s);
            } else {
                count_none++;
                Log.d("CalculateResult/cnt_n:-", "" + count_none);
            }
        }
    }

    // --------------------------------------------------------------------------------------------- //

    /**
     * function that returns[get] count of ROCK
     */
    public int getCount_r() {
        return count_r;
    }

    // --------------------------------------------------------------------------------------------- //

    /**
     * function that returns[get] count of PAPER
     */
    public int getCount_p() {
        return count_p;
    }

    // --------------------------------------------------------------------------------------------- //

    /**
     * function that returns[get] count of SCISSORS
     */
    public int getCount_s() {
        return count_s;
    }

    // --------------------------------------------------------------------------------------------- //

    /**
     * function that returns[get] number of None
     */
    public int getCount_n() {
        return count_none;
    }

    // --------------------------------------------------------------------------------------------- //

    /**
     * Function to Update Player Status in Database
     * @param getWiningStatus_r
     * @param getWiningStatus_p
     * @param getWiningStatus_s
     */
    public void updatePlayerStatus(String getWiningStatus_r, String getWiningStatus_p, String getWiningStatus_s) {
        mVars = Vars.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();

        getWiningStatus_R = getWiningStatus_r;
        getWiningStatus_P = getWiningStatus_p;
        getWiningStatus_S = getWiningStatus_s;

        mDatabaseReference.child("Games").child(mVars.getGameID()).child("Players").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Player player = postSnapshot.getValue(Player.class);

                    /**
                     * change status[win/lose/none] of player in Player class
                     */
                    if (player.RPS.equals("rock")) {
                        player.status = getWiningStatus_R;
                        Log.d("Calcula/np/R", player.status);
                    } else if (player.RPS.equals("paper")) {
                        player.status = getWiningStatus_P;
                        Log.d("Calcula/np/P", player.status);
                    } else if (player.RPS.equals("scissors")) {
                        player.status = getWiningStatus_S;
                        Log.d("Calcula/np/S", player.status);
                    } else {
                        player.status = "lose";
                    }

                    String key = postSnapshot.getKey();
                    /**
                     * update the status of player in Firebase database
                     */
                    mDatabaseReference.child("Games").child(mVars.getGameID()).child("Players").child(key).setValue(player);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // --------------------------------------------------------------------------------------------- //

    /**
     * Function to remove player from Game
     */
    public void removePlayerFromGame(){

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
        mDatabaseReference.child("Games").child(mVars.getGameID()).child("Players").child(mVars.getPlayerID()).removeValue();
    }

    // --------------------------------------------------------------------------------------------- //

    /**
     * Function to update location of Player
     * @param lat
     * @param log
     */
    public void updateLocation(Double lat, Double log){

        mVars = Vars.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
        mPlayer = new Player(mVars.getPlayerName(),mVars.getStatus(),mVars.getRPSvalue(),mVars.getReady(),lat,log);

        mDatabaseReference.child("Games").child(mVars.getGameID()).child("Players").child(mVars.getPlayerID()).setValue(mPlayer);

    }
}
