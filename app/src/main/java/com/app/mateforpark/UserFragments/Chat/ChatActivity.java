package com.app.mateforpark.UserFragments.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.mateforpark.R;
import com.app.mateforpark.UserMainActivities.User_Profile_Activity;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ChatActivity extends AppCompatActivity {


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mChatAdapter;
    private RecyclerView.LayoutManager mChatLayoutManager;
    String currentPhotoURL, matchPhotoURL;
    private EditText mSendEditText;
    private TextView mMatchName;
    private ImageButton mSendButton, mBackButton, mViewProfile;
    int position;
    private String currentUserId, matchId, chatId, senderImageUrl, receiverImgUrl;

    DatabaseReference mDatabaseUser,mDatabaseChat, mDatabaseProfile,mDatabaseMatchProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        matchId = getIntent().getExtras().getString("matchId");

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();


        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("connections").child("matches").child(matchId).child("chatId");
        mDatabaseChat = FirebaseDatabase.getInstance().getReference().child("Chat");

        mDatabaseProfile = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("photo");

        mDatabaseMatchProfile = FirebaseDatabase.getInstance().getReference().child("Users").child(matchId);

        mRecyclerView = findViewById(R.id.recyclerView);
        mBackButton = findViewById(R.id.back);
        mViewProfile = findViewById(R.id.viewmatchprofile);

        //allows to scroll freely through recycler view with no hickups
        mRecyclerView.setNestedScrollingEnabled(false);

        mRecyclerView.setHasFixedSize(false);

        mChatLayoutManager = new LinearLayoutManager(ChatActivity.this);
        mRecyclerView.setLayoutManager(mChatLayoutManager);

        mChatAdapter = new ChatAdapter(getDataSetChat(), ChatActivity.this);

        mRecyclerView.setAdapter(mChatAdapter);
        mSendButton = findViewById(R.id.send);
        mSendEditText = findViewById(R.id.editTextMessage);
        mMatchName = findViewById(R.id.matchName);

        mDatabaseMatchProfile.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    String username;
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                    if (map.get("name") != null) {
                        username = map.get("name").toString();
                        mMatchName.setText(username);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mViewProfile.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), User_Profile_Activity.class);
                //Create a bundle
                Bundle b = new Bundle();
                //add data to bundle
                b.putString("id",matchId);
                intent.putExtras(b);
                view.getContext().startActivity(intent);
            }
        });

        getChatId();


        mSendButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

        mBackButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    private void sendMessage() {
        String sendMessageText = mSendEditText.getText().toString();

        if(!sendMessageText.isEmpty()){
            DatabaseReference newMessageDb = mDatabaseChat.push();

            Map newMessage = new HashMap();
            newMessage.put("senderId", currentUserId);
            newMessage.put("message", sendMessageText);

            // newMessage.put("senderUrl", sendMessageText);
            // newMessage.put("receiverUrl", sendMessageText);

            newMessageDb.setValue(newMessage);
        }

        // to clear added text to null
        mSendEditText.setText(null);
        mRecyclerView.scrollToPosition(position);

    }

    private void getChatId(){
        mDatabaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    chatId = dataSnapshot.getValue().toString();
                    mDatabaseChat = mDatabaseChat.child(chatId);
                    //after getting chat id get the messages
                    getChatMessages();
                    // getImage();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabaseProfile.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    currentPhotoURL = dataSnapshot.getValue(String.class);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void getChatMessages() {
        mDatabaseChat.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()){
                    String message = null;
                    String createdByUser = null;

                    if(dataSnapshot.child("message").getValue()!= null){
                        message = dataSnapshot.child("message").getValue().toString();
                    }

                    if(dataSnapshot.child("senderId").getValue()!= null){
                        createdByUser = dataSnapshot.child("senderId").getValue().toString();

                    }

                    if(message!=null && createdByUser !=null){
                        Boolean currentUserBoolean = false;

                        if(createdByUser.equals(currentUserId)){
                            currentUserBoolean = true;
                        }

                        ChatObject newMessage = new ChatObject(message, currentUserBoolean);


                        resultsChat.add(newMessage);
                        mChatAdapter.notifyDataSetChanged();
                    }

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


    private ArrayList<ChatObject> resultsChat = new ArrayList<ChatObject>();

    private List<ChatObject> getDataSetChat() {
        return resultsChat;
    }
}
