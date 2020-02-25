package com.app.mateforpark.Fragments.ChatScreenFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.app.mateforpark.Chat.ChatActivity;
import com.app.mateforpark.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChatScreenViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView mChatId, mChatName;
    public ImageView mChatImage;
    Context context;

    public ChatScreenViewHolders(@NonNull View itemView) {

        super(itemView);

        itemView.setOnClickListener(this);

        mChatId = itemView.findViewById(R.id.chatId);
        mChatName = itemView.findViewById(R.id.chatName);
        mChatImage = itemView.findViewById(R.id.chatProfileImage);
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
