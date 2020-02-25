package com.app.mateforpark.Fragments.Matches;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.mateforpark.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutParams;

//this function takes care of populating data in xml layout
public class MatchesAdapter extends RecyclerView.Adapter<MatchesViewHolders> {

    private List<MatchesObject> matchesList;
    private Context context;

    private FirebaseAuth mAuth;
    private String currentUId;

    private DatabaseReference usersDb = FirebaseDatabase.getInstance().getReference().child("Users");


    //passes information between matchesactivity and matchesadapter and matcheslist we want
    public MatchesAdapter(List<MatchesObject> matchesList, Context context){

        this.matchesList = matchesList;
        this.context = context;

    }

    @NonNull
    @Override
    public MatchesViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //always some for recycler view
        //part that controls the layout
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_matches, null, false);

        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        layoutView.setLayoutParams(lp);

        MatchesViewHolders rcv = new MatchesViewHolders((layoutView));

        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull MatchesViewHolders holder, final int position) {

        //this takes care of populating the layout we pass above
        //position is to check if item is in the recyler view. views outside the screen are destroyed.
        holder.mMatchId.setText(matchesList.get(position).getUserId());
        holder.mMatchName.setText(matchesList.get(position).getUserName());

        if(!matchesList.get(position).getProfileImageUrl().equals("default")){
            Glide.with(context).load(matchesList.get(position).getProfileImageUrl()).into(holder.mMatchImage);
        }

        /*holder.mMatchImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "OnClick called on position" + position, Toast.LENGTH_SHORT).show();

            }
        });

        holder.mMatchImage.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(context, "OnLongClick called on position" + position, Toast.LENGTH_SHORT).show();

                matchesList.remove(position);
                notifyItemRemoved(position);
                removeMatchFromDb();
                return true;
            }
        });*/

    }

    private void removeMatchFromDb() {
        mAuth = FirebaseAuth.getInstance();

        currentUId = mAuth.getCurrentUser().getUid();
        DatabaseReference removeMatchDb = usersDb.child(currentUId).child("connections").child("matches");
        removeMatchDb.removeValue();
    }

    @Override
    public int getItemCount() {
        return matchesList.size();
    }
}
