package com.app.mateforpark.Fragments.ChatScreenFragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.mateparktest.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutParams;

//this function takes care of populating data in xml layout
public class ChatScreenAdapter extends RecyclerView.Adapter<ChatScreenViewHolders> {

    private List<ChatScreenObject> chatList;
    private Context context;

    private FirebaseAuth mAuth;
    private String currentUId;

    private DatabaseReference usersDb = FirebaseDatabase.getInstance().getReference().child("Users");


    //passes information between matchesactivity and matchesadapter and matcheslist we want
    public ChatScreenAdapter(List<ChatScreenObject> chatList, Context context){

        this.chatList = chatList;
        this.context = context;

    }

    @NonNull
    @Override
    public ChatScreenViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //always some for recycler view
        //part that controls the layout
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_profile, null, false);

        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        layoutView.setLayoutParams(lp);

        ChatScreenViewHolders rcv = new ChatScreenViewHolders((layoutView));

        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatScreenViewHolders holder, final int position) {

        //this takes care of populating the layout we pass above
        //position is to check if item is in the recyler view. views outside the screen are destroyed.
        holder.mChatId.setText(chatList.get(position).getUserId());
        holder.mChatName.setText(chatList.get(position).getUserName());

        if(!chatList.get(position).getProfileImageUrl().equals("default")){
            Glide.with(context).load(chatList.get(position).getProfileImageUrl()).into(holder.mChatImage);
        }
        

    }

    private void removeMatchFromDb() {
        mAuth = FirebaseAuth.getInstance();

        currentUId = mAuth.getCurrentUser().getUid();

        DatabaseReference removeMatchDb = usersDb.child(currentUId).child("connections").child("matches");
        removeMatchDb.removeValue();
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }
}
