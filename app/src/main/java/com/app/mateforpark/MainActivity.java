package com.app.mateforpark;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.app.mateforpark.Fragments.AccountFragment;
import com.app.mateforpark.Fragments.ChatScreenFragment.ChatScreenFragment;
import com.app.mateforpark.Fragments.ExploreFragment;
import com.app.mateforpark.Fragments.HomeFragment;
import com.app.mateforpark.Fragments.Matches.MatchesFragment;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuth.AuthStateListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private AuthStateListener firebaseAuthStateListener;
    private DatabaseReference usersDb = FirebaseDatabase.getInstance().getReference().child("Users");
    private GoogleSignInClient mGoogleSignInClient;

    private String currentUId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BottomNavigationView navigation = findViewById(R.id.navigationView);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //set home as main screen
        navigation.setSelectedItemId(R.id.home);

        // loadFragment(new HomeFragment());


        //Firebase Connections & id retrieval
        mAuth = FirebaseAuth.getInstance();
        currentUId = mAuth.getCurrentUser().getUid();

        //firebase Auth Listener to check if user is already logged in or not
        firebaseAuthStateListener = new AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null){
                    Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };



    }

    private OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
        = new OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

           switch(menuItem.getItemId()){
               case R.id.home:
                   loadFragment(new HomeFragment());
                   return true;
               case R.id.chat:
                   loadFragment(new ChatScreenFragment());
                   return true;
               case R.id.explore:
                   loadFragment(new ExploreFragment());
                   return true;
               case R.id.Account:
                   loadFragment(new AccountFragment());
                   return true;
               case R.id.matches:
                   loadFragment(new MatchesFragment());
                   return true;
           }

            return false;
        }
    };


    //To load fragments when pressed in bottom navigation bar
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


}
