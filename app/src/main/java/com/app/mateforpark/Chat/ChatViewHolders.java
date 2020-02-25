package com.app.mateforpark.Chat;

import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.mateforpark.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChatViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {


    public TextView mMessage;
    public LinearLayout mContainer;
    public ImageView mChatImage;

    public ChatViewHolders(@NonNull final View itemView) {

        super(itemView);

        itemView.setOnClickListener(this);

        mMessage = itemView.findViewById(R.id.userMessage);
        mContainer = itemView.findViewById(R.id.container);
        mChatImage = itemView.findViewById(R.id.chatProfileImage);

        mContainer.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {


                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {


    }
}
