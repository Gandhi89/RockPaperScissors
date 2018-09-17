package com.example.shivamgandhi.rockpaperscissors;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseDatabase mFirebaseDatabase;
    Button createGameBtn,joinGameBtn,showNearByPlayers;
    DatabaseReference mDatabaseReference;
    GameDatabase mGameDatabase;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private static final int RC_SIGN_IN = 123;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_database);

        initializeAll();
        onClickEvents();
        addAuthListner();


        /**
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){

                    Toast.makeText(MainActivity.this, "You are now signed in!", Toast.LENGTH_SHORT).show();
                }
                else{
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
        **/
    }// end of onCreate()

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MainActivity/Resume","Activity in foreground");
        //mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("MainActivity/Pause","Activity in backgroung");
        //mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
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
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
        mGameDatabase = new GameDatabase();
        mFirebaseAuth = FirebaseAuth.getInstance();
        createGameBtn = findViewById(R.id.MainActivity_createGameBtn);
        joinGameBtn = findViewById(R.id.MainActivity_joinGameBtn);
        showNearByPlayers = findViewById(R.id.MainActivity_showNearByPlayers);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.MainActivity_createGameBtn:
                /**
                 * create new game
                 */

                Log.d("MainActivity/GameID",mGameDatabase.createGame());

                break;
            case R.id.MainActivity_joinGameBtn:
                break;
            case R.id.MainActivity_showNearByPlayers:
                break;
        }
    }
}
