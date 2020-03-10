package com.app.mateforpark.UserFragments.ChatList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.mateforpark.UserFragments.Chat.ChatActivity;
import com.app.mateforpark.R;

public class ChatListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView mChatId, mChatName, mChatCountry, mChatBio;
    public ImageView mChatImage;
    Context context;

    public ChatListViewHolder(@NonNull View itemView) {

        super(itemView);

        itemView.setOnClickListener(this);

        mChatId = itemView.findViewById(R.id.chatId);
        mChatName = itemView.findViewById(R.id.chatName);
        mChatImage = itemView.findViewById(R.id.chatProfileImage);
        mChatBio = itemView.findViewById(R.id.bioChat);
        mChatCountry = itemView.findViewById(R.id.userCountry);
    }

    @Override
    public void onClick(View view) {

        Intent intent = new Intent(view.getContext(), ChatActivity.class);
        Bundle b = new Bundle();

        b.putString("matchId",mChatId.getText().toString());


        intent.putExtras(b);

        view.getContext().startActivity(intent);
    }


}
