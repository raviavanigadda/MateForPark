package com.app.mateforpark.UserFragments.Chat;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.mateforpark.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutParams;

//this function takes care of populating data in xml layout
public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolders> {

    private List<ChatObject> chatList;
    private Context context;

    public ChatAdapter(List<ChatObject> chatList, Context context){

        this.chatList = chatList;
        this.context = context;

    }

    private FirebaseAuth mAuth;
    private String currentUId;

    private DatabaseReference usersDb = FirebaseDatabase.getInstance().getReference().child("Users");

    @NonNull
    @Override
    public ChatViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        //always some for recycler view
        //part that controls the layout
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, null, false);

        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        layoutView.setLayoutParams(lp);

        ChatViewHolders rcv = new ChatViewHolders((layoutView));

        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolders holder, int position) {



            //Adapter update
        holder.mMessage.setText(chatList.get(position).getMessage());


        if(chatList.get(position).getCurrentUser()){
            //set the gravity to the end. same as right of the screen
            holder.mMessage.setGravity(Gravity.END);
            holder.mMessage.setTextColor(Color.parseColor("#404040"));
            holder.mContainer.setBackgroundColor(Color.parseColor("#f4f4f4"));
        }
        else
        {


            holder.mMessage.setGravity(Gravity.START);
            holder.mMessage.setTextColor(Color.parseColor("#FFFFFF"));
            //blue color
            holder.mContainer.setBackgroundColor(Color.parseColor("#2DB4C8"));

        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }
}
