package com.app.mateforpark;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuth.AuthStateListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RegisterActivity extends AppCompatActivity {

    private EditText mUserName, mUserPassword, mUserEmail, mUserAge, mConfirmPassword;
    private Button mSignup, mBack;
    private String userGender;
    CountryCodePicker mCountryCodePicker;

    FirebaseAuth mAuth;
    private AuthStateListener firebaseAuthStateListener;
    Handler handler;

    //For progressbar after clicking signup
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    FrameLayout progressBarHolder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        mUserName = findViewById(R.id.username);
        mUserPassword = findViewById(R.id.userpassword);
        mConfirmPassword = findViewById(R.id.confirmpassword);

        mUserEmail = findViewById(R.id.useremail);
        mUserAge = findViewById(R.id.userage);
        mCountryCodePicker = (CountryCodePicker) findViewById(R.id.ccp);

        mSignup = findViewById(R.id.signup);
        mBack = findViewById(R.id.back);

        final Spinner mySpinner = findViewById(R.id.spinner);

        //create data to show inside spinner
       final ArrayAdapter<String> myAdapter = new ArrayAdapter<>(RegisterActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.gender));



        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //allow the adapter to show the data inside the spinner.
        mySpinner.setAdapter(myAdapter);


        mAuth = FirebaseAuth.getInstance();
        handler = new Handler();
        progressBarHolder = (FrameLayout)findViewById(R.id.progressBarHolder);

        firebaseAuthStateListener = new AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if(user != null){
                    Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }

            }
        };

       //mSignup.setVisibility(View.VISIBLE);


    }


    private class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mSignup.setEnabled(false);
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(300);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
            mSignup.setEnabled(true);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                    TimeUnit.SECONDS.sleep(3);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }


    }
}
