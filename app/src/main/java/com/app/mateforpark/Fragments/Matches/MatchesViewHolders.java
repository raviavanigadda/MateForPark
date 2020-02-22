package com.app.mateforpark.Fragments.Matches;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.mateparktest.R;
import com.app.mateparktest.UserProfile;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MatchesViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView mMatchId, mMatchName;
    public ImageView mMatchImage;
    Context context;

    public MatchesViewHolders(@NonNull View itemView) {

        super(itemView);

        itemView.setOnClickListener(this);

        mMatchId = itemView.findViewById(R.id.matchId);
        mMatchName = itemView.findViewById(R.id.matchName);
        mMatchImage = itemView.findViewById(R.id.matchProfileImage);
    }

    @Override
    public void onClick(View view) {
        final Intent intent = new Intent(view.getContext(), UserProfile.class);
        //Create a bundle
        Bundle b = new Bundle();
        //add data to bundle
        b.putString("id",mMatchId.getText().toString());
        intent.putExtras(b);
        view.getContext().startActivity(intent);
        /*

        Intent intent = new Intent(view.getContext(), ChatActivity.class);
        Bundle b = new Bundle();

        b.putString("matchId",mMatchId.getText().toString());

        intent.putExtras(b);

        view.getContext().startActivity(intent);*/
    }


}
