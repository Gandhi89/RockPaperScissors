package com.example.shivamgandhi.rockpaperscissors.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.shivamgandhi.rockpaperscissors.R;
import com.example.shivamgandhi.rockpaperscissors.Utils.Vars;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseDatabase mFirebaseDatabase;
    Button createGameBtn,joinGameBtn,showNearByPlayers;
    DatabaseReference mDatabaseReference;
    GameDatabase mGameDatabase;
    FirebaseAuth mFirebaseAuth;
    Vars mVars;
    HashMap<String,String> credential;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private static final int RC_SIGN_IN = 123;
    Boolean isRegistered = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_database);

        initializeAll();
        onClickEvents();
        addAuthListner();

        Log.d("MainScreen/Log","failure");
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                Log.d("MainScreen/Log","inside onAuthChange");

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){

                    Log.d("MainScreen/Log","inside user!=null");
                    Set set = credential.entrySet();
                    Iterator iterator = set.iterator();
                    while(iterator.hasNext()) {
                        Map.Entry mentry = (Map.Entry)iterator.next();

                         if(mentry.getValue().equals(user.getEmail())){
                            Log.d("MainAct/foundUser", "true");
                            mVars.setPlayerName(user.getDisplayName());
                            isRegistered = true;

                            mVars.setPrimaryKey((String) mentry.getKey());
                            mGameDatabase.setupVars((String) mentry.getKey());

                        }
                    }
                    /**
                     * user is signed in
                     */
                    if (isRegistered){
                        Log.d("MainScreen/Log","inside isRegestered");
                        /**
                         * update current location and change status to online
                         */
                        mGameDatabase.updateLocation(mVars.getLatitude(),mVars.getLongitude());
                        mGameDatabase.updateUserStatus("online");

                    }
                    else {
                        Log.d("MainScreen/Log","inside unRegistered");
                        mVars.setTitle("beginner");
                        mVars.setWon(0);
                        mVars.setPlayed(0);
                        mVars.setStatus("online");
                        onSignInInitiaize(user.getEmail(), user.getDisplayName(), mVars.getWon(),mVars.getPlayed(), mVars.getTitle(), mVars.getLatitude(), mVars.getLongitude(), mVars.getStatus());
                    }

                }
                else{
                    Log.d("MainScreen/Log","inside UI.buid");
                    // Choose authentication providers
                    List providers = Arrays.asList(
                            new AuthUI.IdpConfig.EmailBuilder().build(),
                            new AuthUI.IdpConfig.GoogleBuilder().build());

                    // Create and launch sign-in intent
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(providers)
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };

    }// end of onCreate()


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MainActivity/Resume","Activity in foreground");
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("MainActivity/Pause","Activity in backgroung");
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    /**
     * check user is signed in or not
     */
    private void addAuthListner() {
    }

    private void onClickEvents() {
        createGameBtn.setOnClickListener(this);
        joinGameBtn.setOnClickListener(this);
        showNearByPlayers.setOnClickListener(this);
    }

    private void initializeAll() {
        mVars = Vars.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
        mGameDatabase = new GameDatabase();
        mFirebaseAuth = FirebaseAuth.getInstance();
        createGameBtn = findViewById(R.id.MainActivity_createGameBtn);
        joinGameBtn = findViewById(R.id.MainActivity_joinGameBtn);
        showNearByPlayers = findViewById(R.id.MainActivity_showNearByPlayers);

        credential = mVars.getUserCredential();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.MainActivity_createGameBtn:
                /**
                 * create new game
                 */
                mVars.setGameID(mGameDatabase.createGame());
                Log.d("MainActivity/GameID",mVars.getGameID());
                /**
                 * join player into existing game
                 */
                mVars.setPlayerID(mGameDatabase.joinGame());
                Log.d("MainActivity/PlayerID",mVars.getPlayerID());

                Intent i = new Intent(MainActivity.this,PlayersInGame.class);
                startActivity(i);
                break;
            case R.id.MainActivity_joinGameBtn:
                Intent intent = new Intent(MainActivity.this,JoinGame.class);
                startActivity(intent);
                break;
            case R.id.MainActivity_showNearByPlayers:
                break;
        }
    }

    private void onSignInInitiaize(String email, String name, int won,int played, String title, double lat, double log, String status) {

        mVars.setPlayerName(name);
        mVars.setPrimaryKey(mGameDatabase.addUser(email, name, won,played, title, lat, log, status));
        this.credential.put(mVars.getPrimaryKey(),email);
        mVars.setUserCredential(this.credential);

        mGameDatabase.setupVars(mVars.getPrimaryKey());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Log.d("MainAct/ResultOk", ">>>>>>>");
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

    }
}
