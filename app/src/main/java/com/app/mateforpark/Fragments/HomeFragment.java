package com.app.mateforpark.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.mateforpark.Fragments.Cards.Cards;
import com.app.mateforpark.Fragments.Cards.CustomArrayAdapter;
import com.app.mateforpark.R;
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


public class HomeFragment extends Fragment{

    private CustomArrayAdapter arrayAdapter;
    private FirebaseAuth mAuth;

    private String currentUId;
    private String userGender;
    private String otheruserGender;
    Button mRightButton,mLeftButton;
    TextView mTextDisplay, mUserName;


    public DatabaseReference usersDb = FirebaseDatabase.getInstance().getReference().child("Users");

    List<Cards> rowItems;
    private Object dataObject;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final SwipeFlingAdapterView flingContainer = getView().findViewById(R.id.frame);


       mLeftButton = getView().findViewById(R.id.leftSwipe);
       mRightButton = getView().findViewById(R.id.rightSwipe);
       mTextDisplay = getView().findViewById(R.id.errorHome);
       mAuth = FirebaseAuth.getInstance();

       currentUId = mAuth.getCurrentUser().getUid();



       rowItems = new ArrayList<Cards>();

       arrayAdapter = new CustomArrayAdapter(getActivity(), R.layout.item, rowItems);

       Bundle bundle = this.getArguments();

       if(bundle != null)
       {
           String value = bundle.getString("param_country");
           String value1 = bundle.getString("param_gender");

           filterUsers(value, value1);
           Toast.makeText(getActivity(), "Data are "+value+" "+ value1, Toast.LENGTH_SHORT).show();
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
                refreshFragmentUI(HomeFragment.this);
                Cards obj = (Cards) dataObject;

                //Cards parameters should appear
                String userId = obj.getUserID();
                usersDb.child(userId).child("connections").child("decline").child(currentUId).setValue(true);

            }

            @Override
            public void onRightCardExit(Object dataObject) {

                refreshFragmentUI(HomeFragment.this);
                Cards obj = (Cards) dataObject;
                String userId = obj.getUserID();
                usersDb.child(userId).child("connections").child("accept").child(currentUId).setValue(true);
                isConnectionMatch(userId);

            }

            @Override
            public void onAdapterAboutToEmpty(int i) {



                if(i==0){

                    mRightButton.setVisibility(View.GONE);
                    mLeftButton.setVisibility(View.GONE);
                    mTextDisplay.setVisibility(View.VISIBLE);

                }
                else
                {
                    mRightButton.setVisibility(View.VISIBLE);
                    mLeftButton.setVisibility(View.VISIBLE);
                    mTextDisplay.setVisibility(View.GONE);

                }

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

    }

    private void filterUsers(final String country, final String gender) {

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference userDb = usersDb.child(user.getUid());

        userDb.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    if (dataSnapshot.child("gender").getValue() != null) {

                       // userGender = dataSnapshot.child("gender").getValue().toString();

                        switch (gender) {
                            case "Male":
                                otheruserGender = "Male";
                                break;
                            case "Female":
                                otheruserGender = "Female";
                                break;

                        }

                        if(dataSnapshot.child("gender").getValue().equals("Do not specify")){
                            Toast.makeText(getActivity(), "Please change your gender to view profiles!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else
                        {
                            getFilterOppositeUsers(country);
                        }

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

                        }

                        if(dataSnapshot.child("gender").getValue().equals("Do not specify")){
                            Toast.makeText(getActivity(), "Please change your gender to view profiles!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else
                        {
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


                    //adds name and profile to the card from the database
                    Cards item = new Cards(dataSnapshot.getKey(), dataSnapshot.child("name").getValue().toString(),
                            profileImageUrl, dataSnapshot.child("age").getValue().toString(),dataSnapshot.child("country").getValue().toString());

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
                            profileImageUrl, dataSnapshot.child("age").getValue().toString(),dataSnapshot.child("country").getValue().toString());

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


    public void isConnectionMatch(String userId) {

        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");
        DatabaseReference  currentUserConnectionDb = usersDb.child(currentUId).child("connections").child("accept").child(userId);

        currentUserConnectionDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {

                    Toast.makeText(getActivity(), "New Match!", Toast.LENGTH_SHORT).show();

                    //for chat id
                    String key = FirebaseDatabase.getInstance().getReference().child("chat").push().getKey();

                    //usersDb.child(dataSnapshot.getKey()).child("connections").child("matches").child(currentUId).setValue(true);
                    usersDb.child(dataSnapshot.getKey()).child("connections").child("matches").child(currentUId).child("chatId").setValue(key);

                    //usersDb.child(currentUId).child("connections").child("matches").child(dataSnapshot.getKey()).setValue(true);
                    usersDb.child(currentUId).child("connections").child("matches").child(dataSnapshot.getKey()).child("chatId").setValue(key);

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
