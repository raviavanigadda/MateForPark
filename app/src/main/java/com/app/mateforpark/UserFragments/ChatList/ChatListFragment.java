package com.app.mateforpark.UserFragments.ChatList;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.mateforpark.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatListFragment extends Fragment {

    private RecyclerView mChatRecyclerView;
    private RecyclerView.Adapter mChatAdapter;
    private RecyclerView.LayoutManager mChatLayoutManager;
    private String currentUserId;
    TextView mErrorDisplay;

    public ChatListFragment() {
        //Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat_profile, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Inflate the layout for this fragment
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mChatRecyclerView = getView().findViewById(R.id.recyclerViewChat);
        mErrorDisplay = getView().findViewById(R.id.errorChat);

        //allows to scroll freely through recycler view with no hickups
        mChatRecyclerView.setNestedScrollingEnabled(false);
        mChatRecyclerView.setHasFixedSize(true);

        mChatLayoutManager = new LinearLayoutManager(getActivity());
        mChatRecyclerView.setLayoutManager(mChatLayoutManager);

        mChatAdapter = new ChatListViewAdapter(getDataSetMatches(), getActivity());
        mChatRecyclerView.setAdapter(mChatAdapter);


        getUserMatchId();

    }

    private void getUserMatchId() {

        DatabaseReference matchDb = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("connections").child("matches");
        matchDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()) {
                    for (DataSnapshot match : dataSnapshot.getChildren()) {
                        FetchMatchInformation(match.getKey());
                    }
                    mErrorDisplay.setVisibility(View.GONE);
                }
                else{
                        mErrorDisplay.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void FetchMatchInformation(String key) {
        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("Users").child(key);
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String userId = dataSnapshot.getKey();
                    String name = "";
                    String profileImageUrl = "";
                    String country = "";
                    String bio = "";

                    if(dataSnapshot.child("name").getValue()!=null){
                        name = dataSnapshot.child("name").getValue().toString();
                    }

                    if(dataSnapshot.child("country").getValue()!=null){
                        country = dataSnapshot.child("country").getValue().toString();
                    }

                    if(dataSnapshot.child("bio").getValue()!=null){
                        bio = dataSnapshot.child("bio").getValue().toString();
                    }

                    if(!dataSnapshot.child("photo").getValue().equals("default")){

                        profileImageUrl = dataSnapshot.child("photo").getValue().toString();
                    }

                    ChatListObject obj = new ChatListObject(userId, name, profileImageUrl, country, bio);
                    resultsChat.add(obj);

                    mChatAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private ArrayList<ChatListObject> resultsChat= new ArrayList<ChatListObject>();

    private List<ChatListObject> getDataSetMatches() {
        return resultsChat;
    }

}
