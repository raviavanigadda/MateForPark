package com.app.mateforpark;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class UserProfile extends AppCompatActivity {

    private String username, usercountry, useremail, usergender, userage, userimage;
    private TextView tvname, tvcountry, tvemail, tvgender, tvage, mBack;
    private ImageView ivuserimage, iemail, iage, igender;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private DatabaseReference mUserDatabase;
    String userid, otheruserid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Intent intent = getIntent();
        otheruserid = intent.getStringExtra("id");


        tvname = findViewById(R.id.tv_name);
        tvcountry = findViewById(R.id.tv_country);
        tvemail = findViewById(R.id.tv_email);
        tvgender = findViewById(R.id.tv_gender);
        tvage = findViewById(R.id.tv_age);
        ivuserimage = findViewById(R.id.userimage);

        iemail = findViewById(R.id.iv_email);
        iage = findViewById(R.id.iv_age);
        igender = findViewById(R.id.iv_gender);

        mBack = findViewById(R.id.back);

        mAuth = FirebaseAuth.getInstance();
        //gets current userid
        userid = mAuth.getCurrentUser().getUid();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(otheruserid);

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {

                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                    //get the child you want
                    if (map.get("name") != null) {
                        username = map.get("name").toString();
                        tvname.setText(username);
                    }

                    if (map.get("email") != null) {
                        useremail = map.get("email").toString();
                        tvemail.setText(useremail);
                    }

                    if (map.get("name") != null) {
                        userage = map.get("age").toString();
                        tvage.setText(userage);
                    }

                    if (map.get("gender") != null) {
                        usergender = map.get("gender").toString();
                        tvgender.setText(usergender);
                    }

                    if (map.get("country") != null) {
                        usercountry = map.get("country").toString();
                        tvcountry.setText(usercountry);
                    }

                    if (map.get("photo") != null) {
                        userimage = map.get("photo").toString();
                        switch (userimage) {

                            case "default":
                                Glide.with(getApplication()).load(R.drawable.default_image).into(ivuserimage);
                                break;
                            default:
                                Glide.with(getApplication()).load(userimage).into(ivuserimage);
                                break;

                        }
                    }
                }

                }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                return;
            }
        });

    }

}
