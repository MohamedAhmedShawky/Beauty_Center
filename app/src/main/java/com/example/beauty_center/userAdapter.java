package com.example.beauty_center;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
/*
public class userAdapter extends ArrayAdapter<User> {


    public userAdapter(Context context, ArrayList <User> users) {
        super(context, 0, users);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final User user = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_salon, parent, false);
        }


        TextView Beauty_name, Distance, WaitingNumber;


        Beauty_name = (TextView) convertView.findViewById(R.id.SalonName);
        Distance = (TextView) convertView.findViewById(R.id.Distance);
        WaitingNumber = (TextView) convertView.findViewById(R.id.Waiting);

        Beauty_name.setText(user.getName());
        Distance.setText(user.getAddress());
        WaitingNumber.setText(user.getOpenAt());

        return convertView;


    }
}*/


public class userAdapter extends ArrayAdapter<User> {

    private Context mContext;
    private List<User> userList = new ArrayList<>();

    public userAdapter(@NonNull Context context, @SuppressLint("SupportAnnotationUsage") @LayoutRes ArrayList<User> users) {
        super(context, 0 , users);
        mContext = context;
        userList = users;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.item_salon,parent,false);

        final User currentUser = userList.get(position);

        TextView name = (TextView) listItem.findViewById(R.id.SalonName);
        name.setText(currentUser.getBeauty_Center_name());

        TextView services = (TextView) listItem.findViewById(R.id.services);
        services.setText( "services "+currentUser.getFirstService()+","+currentUser.getSecondService());



        TextView Waiting = (TextView) listItem.findViewById(R.id.Waiting);
        Waiting.setText("waiting "+currentUser.NumberOfReservation);


        RelativeLayout ID=(RelativeLayout)listItem.findViewById(R.id.list_item);

        ID.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v) {

                Intent book=new Intent(getContext(),salonPofile.class);

                book.putExtra("name",currentUser.getBeauty_Center_name());
                book.putExtra("address",currentUser.getAddress());
                book.putExtra("email",currentUser.getEmail());
                book.putExtra("phone",currentUser.getPhone());
                book.putExtra("distance",currentUser.getCloseAt());
                book.putExtra("offer",currentUser.getOffer());
                book.putExtra("id",currentUser.getID());
                book.putExtra("firstService",currentUser.getFirstService());
                book.putExtra("secondService",currentUser.getSecondService());

                getContext().startActivity(book);
            }
        });




        return listItem;
    }
}
