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

        mSignup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                userGender = mySpinner.getSelectedItem().toString().trim();

                final String email = mUserEmail.getText().toString();
                final String password = mUserPassword.getText().toString();
                final String confirmPassword = mConfirmPassword.getText().toString();
                final String name = mUserName.getText().toString();

                final String age = mUserAge.getText().toString();
                final String country = mCountryCodePicker.getSelectedCountryEnglishName();

                if (TextUtils.isEmpty(name)) {
                    mUserName.setError("Please enter your name");
                }
                else if(TextUtils.isEmpty(email))
                {
                    mUserEmail.setError("Please enter your Email ID");
                }
                else if (TextUtils.isEmpty(password)) {
                    mUserPassword.setError("Please enter your password");
                }

                else if(TextUtils.isEmpty(confirmPassword)){
                    mConfirmPassword.setError("Please confirm your password");
                }

                else if(!(TextUtils.isEmpty(confirmPassword))&&(!confirmPassword.equals(password))){
                    mConfirmPassword.setError("Passwords do not match!");
                }

                else if(password.length() <6){
                    mUserPassword.setError("Password length is minimum 6 characters.");
                }

                else if(TextUtils.isEmpty(age)){
                    mUserAge.setError("Please enter your age");
                }

                else if(!TextUtils.isEmpty(age) && (Integer.parseInt(age) < 18 )||(Integer.parseInt(age) == 0)){
                    mUserAge.setError("This app is only for 18 and above");
                }

                else if(userGender.equals("Do not specify"))
                {
                    Toast.makeText(RegisterActivity.this, "Please select your gender", Toast.LENGTH_SHORT).show();
                }

                else {
                    checkEmail(email,mUserEmail);

                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){

                                String userId = mAuth.getCurrentUser().getUid();
                                DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

                                //To store the user information easily we use hashmaps
                                Map userInfo = new HashMap<>();

                                userInfo.put("name", name);
                                userInfo.put("email", email);
                                userInfo.put("gender", userGender);
                                userInfo.put("age", age);
                                userInfo.put("country", country);
                                userInfo.put("photo","default");

                                currentUserDb.updateChildren(userInfo);

                                Toast.makeText(RegisterActivity.this, "Registration Successful. Please wait...", Toast.LENGTH_SHORT).show();
                                clearEditTextFields();

                                String flag = "false";
                                final Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);

                                //Create a bundle
                                Bundle b = new Bundle();

                                //add data to bundle
                                b.putString("flag",flag);
                                intent.putExtras(b);

                                new MyTask().execute();
                                mAuth.signOut();
                                startActivity(intent);
                                finish();

                   /* handler.postDelayed(new Runnable() {
                       @Override
                       public void run() {
                           // Do something after 3s = 3000ms

                       }
                   }, 4000);*/

                            }
                        }
                    });
                }
            }
        });
       //mSignup.setVisibility(View.VISIBLE);

        mBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });


    }
    public void checkEmail(String email, final EditText mUserEmail){
        mAuth.fetchSignInMethodsForEmail(this.mUserEmail.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {

                        boolean check = !task.getResult().getSignInMethods().isEmpty();
                        if(!check)
                        {
                            Log.d("Message","Email does not exist. You can proceed to create a new one.");

                        }
                        else
                        {
                            mUserEmail.setError("Email already exists! Please try another email address.");
                        }
                    }
                });
    }
    private void clearEditTextFields() {
        mUserName.getText().clear();
        mUserPassword.getText().clear();
        mConfirmPassword.getText().clear();
        mUserAge.getText().clear();
        mUserEmail.getText().clear();
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
