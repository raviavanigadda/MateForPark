package com.app.mateforpark;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.mateparktest.Fragments.Cards.Cards;
import com.app.mateparktest.Fragments.Cards.CustomArrayAdapter;
import com.app.mateparktest.R;
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


public class HomeFragment extends Fragment {

    private Cards cardData[];
    private CustomArrayAdapter arrayAdapter;
    private int i;

    private FirebaseAuth mAuth;

    private String currentUId;
    private String userGender;
    private String otheruserGender;

    private DatabaseReference usersDb = FirebaseDatabase.getInstance().getReference().child("Users");

    List<Cards> rowItems;

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

        SwipeFlingAdapterView flingContainer = getView().findViewById(R.id.frame);

        mAuth = FirebaseAuth.getInstance();

        currentUId = mAuth.getCurrentUser().getUid();

        checkUserGender();
        rowItems = new ArrayList<Cards>();

        arrayAdapter = new CustomArrayAdapter(getActivity(), R.layout.item, rowItems );

        flingContainer.setAdapter(arrayAdapter);

        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                //  arrayAdapter.notify();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                Cards obj = (Cards) dataObject;
                //Cards parameters should appear
                String userId = obj.getUserID();
                usersDb.child(userId).child("connections").child("decline").child(currentUId).setValue(true);
                Toast.makeText(getActivity(),"Left!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Toast.makeText(getActivity(),"Right!",Toast.LENGTH_SHORT).show();
                //arrayAdapter.notify();

                Cards obj = (Cards) dataObject;
                String userId = obj.getUserID();
                usersDb.child(userId).child("connections").child("accept").child(currentUId).setValue(true);
                isConnectionMatch(userId);

            }

            @Override
            public void onAdapterAboutToEmpty(int i) {

            }


            @Override
            public void onScroll(float v) {

            }

        });

        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(getActivity(),"Clicked!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void isConnectionMatch(String userId) {
        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");
        DatabaseReference  currentUserConnectionDb = usersDb.child(currentUId).child("connections").child("accept").child(userId);

        currentUserConnectionDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    Toast.makeText(getActivity(),"New Match!", Toast.LENGTH_SHORT).show();

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
                        getOppositeGenderUsers();
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
                    Cards item = new Cards(dataSnapshot.getKey(), dataSnapshot.child("name").getValue().toString(), profileImageUrl, dataSnapshot.child("age").getValue().toString(),dataSnapshot.child("country").getValue().toString());

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;

    }



}