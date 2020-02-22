package com.app.mateforpark.Fragments.Cards;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.mateparktest.R;
import com.app.mateparktest.UserProfile;
import com.bumptech.glide.Glide;

import java.util.List;

public class CustomArrayAdapter extends ArrayAdapter<Cards> {

    //which will pass inside the arrayadapter
    private Context context;
    private CustomArrayAdapter arrayAdapter;
    List<Cards> rowItems;
    private List<Cards> items;
    private int item;
    TextView name,age,country;
    Button mRightButton,mLeftButton;

    //when we start populating the cards right in the beginning
    public CustomArrayAdapter(Context context, int resourceId, List<Cards> items) {
        super(context, resourceId, items);
        this.items = items;
        this.context = context;

    }

    //whats going to populate each card
    //we add name, image
    public View getView(int position, View convertView, ViewGroup parent) {
        //everything in card is what we get here



        //get the item in the adapter
        final Cards cardItem = getItem(position); //this will give us the name, image and everything. position is current position that view is building

        if (convertView == null) {
            //we inflate the xml which gives us a view
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);

        }



        //setting up data from layout
        name = convertView.findViewById(R.id.name);
        age = convertView.findViewById(R.id.age);
        country = convertView.findViewById(R.id.country);

       // mLeftButton = convertView.findViewById(R.id.leftSwipe);
       // mRightButton = convertView.findViewById(R.id.rightSwipe);

        ImageView image = convertView.findViewById(R.id.Cardimage);

        //populate rows xml using with info from the item
        name.setText(cardItem.getUserName());
        age.setText(cardItem.getUserAge());
        country.setText(cardItem.getUserCountry());

        name.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                String userId = cardItem.getUserID();
                Intent intent = new Intent(context, UserProfile.class);
                //Create a bundle
                Bundle b = new Bundle();
                //add data to bundle
                b.putString("id",userId);
                intent.putExtras(b);
                context.startActivity(intent);

            }
        });


        switch (cardItem.getPhotoUrl()) {

            case "default":
                //  image.setImageResource(R.drawable.default_image);
                Glide.with(convertView.getContext()).load(R.drawable.default_image).into(image);
                break;
            default:
                Glide.clear(image);
                Glide.with(convertView.getContext()).load(cardItem.getPhotoUrl()).into(image);
                break;

        }


        //  image.setImageResource(R.mipmap.ic_launcher);

        return convertView;
    }
}

