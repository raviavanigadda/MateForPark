package com.app.mateforpark.UserFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.app.mateforpark.UserFragments.Cards.Cards;
import com.app.mateforpark.UserFragments.Cards.CustomArrayAdapter;
import com.app.mateforpark.R;
import com.app.mateforpark.UserMainActivities.User_Settings_Activity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class Home_Dashboard_Fragment extends Fragment{

    private CustomArrayAdapter arrayAdapter;
    private FirebaseAuth mAuth;
    ViewFlipper v_flipper;
    private String currentUId;
    private String userGender;
    private String otheruserGender;
    Button mRightButton,mLeftButton, mSettings;
    TextView mTextDisplay, mUserName;
    private RelativeLayout rl_flipper;
    ImageView onHomeEmpty, onGmailFirst;

    public DatabaseReference senderDb, receiverDb;
    private DatabaseReference usersDb = FirebaseDatabase.getInstance().getReference().child("Users");
    //int images[] = {R.drawable.image, R.drawable.image1, R.drawable.image1};
    List<Cards> rowItems;
    private Object dataObject;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public Home_Dashboard_Fragment() {
        // Required empty public constructor
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final SwipeFlingAdapterView flingContainer = getView().findViewById(R.id.frame);

        mAuth = FirebaseAuth.getInstance();

        mTextDisplay = getView().findViewById(R.id.errorHome);
        //onHomeEmpty = getView().findViewById(R.id.home_empty);
        onGmailFirst = getView().findViewById(R.id.gmail_first);
        //rl_flipper = getView().findViewById(R.id.rl_flipper);
        mSettings = getView().findViewById(R.id.editprofile);
        currentUId = mAuth.getCurrentUser().getUid();
        rowItems = new ArrayList<Cards>();
        arrayAdapter = new CustomArrayAdapter(getActivity(), R.layout.item, rowItems);

        //for Filtering the profiles
        Bundle bundle = this.getArguments();

        if(bundle != null)
        {
            String value = bundle.getString("param_country");
            String value1 = bundle.getString("param_gender");
            filterUsers(value, value1);
            //Toast.makeText(getActivity(), "Data are "+value+" "+ value1, Toast.LENGTH_SHORT).show();

        }
        else
        {
            checkUserGender();
        }

        flingContainer.setAdapter(arrayAdapter);

        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {

            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)

            }

            @Override
            public void onLeftCardExit(Object dataObject) {

                //Do something on the left!
                refreshFragmentUI(Home_Dashboard_Fragment.this);
                Cards obj = (Cards) dataObject;

                //Cards parameters should appear
                String userId = obj.getUserID();
                usersDb.child(userId).child("connections").child("decline").child(currentUId).setValue(true);

            }

            @Override
            public void onRightCardExit(Object dataObject) {

                refreshFragmentUI(Home_Dashboard_Fragment.this);

                Cards obj = (Cards) dataObject;
                String userId = obj.getUserID();
                usersDb.child(userId).child("connections").child("accept").child(currentUId).setValue(true);
                isConnectionMatch(userId);

            }

            @Override
            public void onAdapterAboutToEmpty(int i) {
                // refreshFragmentUI(Home_Dashboard_Fragment.this);
            }


            @Override
            public void onScroll(float v) {

            }



        });




        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {

                /*
                refreshFragmentUI(HomeFragment.this);
                Cards obj = (Cards) dataObject;
                String userId = obj.getUserID();
                final Intent intent = new Intent(getActivity(), UserProfile.class);
                //Create a bundle
                Bundle b = new Bundle();
                //add data to bundle
                b.putString("id",userId);
                intent.putExtras(b);
                startActivity(intent);
                */

            }
        });



/*
        mRightButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                flingContainer.getTopCardListener().selectRight();
                arrayAdapter.notifyDataSetChanged();
            }
        });

        mLeftButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                flingContainer.getTopCardListener().selectLeft();
                arrayAdapter.notifyDataSetChanged();
            }
        });
*/
    }

    //Image slider
   /* public void flipperImages(int image){
        ImageView imageView = new ImageView(getContext());
        imageView.setBackgroundResource(image);

        v_flipper.addView(imageView);
        v_flipper.setFlipInterval(4000);
        v_flipper.setAutoStart(true);

        v_flipper.setInAnimation(getContext(),android.R.anim.slide_in_left);
        v_flipper.setOutAnimation(getContext(),android.R.anim.slide_out_right);

    }
*/
    //For filtering single user
    private void filterUsers(final String country, final String gender) {

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference userDb = usersDb.child(user.getUid());

        userDb.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    if (dataSnapshot.child("gender").getValue() != null) {

                        //userGender = dataSnapshot.child("gender").getValue().toString();

                        switch (gender) {
                            case "Male":
                                otheruserGender = "Male";
                                break;
                            case "Female":
                                otheruserGender = "Female";
                                break;
                            case "Others":
                                otheruserGender = "Others";
                                break;

                        }

                        getFilterOppositeUsers(country);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void checkUserGender() {

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userDb = usersDb.child(user.getUid());

        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    if (dataSnapshot.child("gender").getValue() != null) {

                        userGender = dataSnapshot.child("gender").getValue().toString();

                        switch (userGender) {
                            case "Male":
                                otheruserGender = "Female";
                                break;
                            case "Female":
                                otheruserGender = "Male";
                                break;
                            case "Others":
                                otheruserGender = "Others";
                                break;

                        }

                        if(dataSnapshot.child("gender").getValue().equals("Do not specify")){
                            //Toast.makeText(getActivity(), "Please change your gender to view profiles!", Toast.LENGTH_SHORT).show();

                            mSettings.setVisibility(View.VISIBLE);
                            //  onGmailFirst.setVisibility(View.GONE);
                            mSettings.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(getActivity(), User_Settings_Activity.class);
                                    startActivity(intent);
                                }
                            });

                            return;
                        }
                        else

                        {
                            mSettings.setVisibility(View.GONE);
                            getOppositeGenderUsers();
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getOppositeGenderUsers() {
        usersDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                if(dataSnapshot.exists() && !dataSnapshot.child("connections").child("decline").hasChild(currentUId) &&
                        !dataSnapshot.child("connections").child("accept").hasChild(currentUId) && dataSnapshot.child("gender").getValue().toString().equals(otheruserGender)){

                    String profileImageUrl = "default";

                    if(!dataSnapshot.child("photo").getValue().equals("default")){

                        profileImageUrl = dataSnapshot.child("photo").getValue().toString();
                    }

                    String bio="";

                    if(!dataSnapshot.child("bio").getValue().equals(null)){
                        bio = dataSnapshot.child("bio").getValue().toString();
                    }
                    //adds name and profile to the card from the database
                    Cards item = new Cards(dataSnapshot.getKey(), dataSnapshot.child("name").getValue().toString(),
                            profileImageUrl, dataSnapshot.child("age").getValue().toString(),dataSnapshot.child("country").getValue().toString()
                            ,bio);

                    rowItems.add(item);

                    arrayAdapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getFilterOppositeUsers(final String country) {
        usersDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                if(dataSnapshot.exists() && !dataSnapshot.child("connections").child("decline").hasChild(currentUId) &&
                        !dataSnapshot.child("connections").child("accept").hasChild(currentUId) &&
                        dataSnapshot.child("gender").getValue().toString().equals(otheruserGender) &&
                        !dataSnapshot.getKey().equals(currentUId) &&
                        dataSnapshot.child("country").getValue().toString().equals(country)){

                    String profileImageUrl = "default";

                    if(!dataSnapshot.child("photo").getValue().equals("default")){

                        profileImageUrl = dataSnapshot.child("photo").getValue().toString();
                    }


                    //adds name and profile to the card from the database
                    Cards item = new Cards(dataSnapshot.getKey(), dataSnapshot.child("name").getValue().toString(),
                            profileImageUrl, dataSnapshot.child("age").getValue().toString(),dataSnapshot.child("country").getValue().toString()
                            ,dataSnapshot.child("bio").getValue().toString());

                    rowItems.add(item);

                    arrayAdapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void isConnectionMatch(final String userId) {

        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");

        //for Accept Data
        DatabaseReference  currentUserConnectionDb = usersDb.child(currentUId).child("connections").child("accept").child(userId);

        currentUserConnectionDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {

                    Toast.makeText(getActivity(), "You are Matched!", Toast.LENGTH_SHORT).show();

                    //for chat id
                    String key = FirebaseDatabase.getInstance().getReference().child("chat").push().getKey();

                    //usersDb.child(dataSnapshot.getKey()).child("connections").child("matches").child(currentUId).setValue(true);

                    //For other user
                    usersDb.child(dataSnapshot.getKey()).child("connections").child("matches").child(currentUId).child("chatId").setValue(key);
                    usersDb.child(dataSnapshot.getKey()).child("connections").child("matches").child(currentUId).child("chatId").setValue(key);


                    //usersDb.child(currentUId).child("connections").child("matches").child(dataSnapshot.getKey()).setValue(true);
                    // For Current user
                    usersDb.child(currentUId).child("connections").child("matches").child(dataSnapshot.getKey()).child("chatId").setValue(key);
                    usersDb.child(dataSnapshot.getKey()).child("connections").child("matches").child(currentUId).child("chatId").setValue(key);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void refreshFragmentUI(Fragment fragment){

        getFragmentManager().beginTransaction().detach(fragment).attach(fragment).commit();

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;

    }



}
