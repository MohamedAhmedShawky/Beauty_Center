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

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class serviceAdapter extends ArrayAdapter<User> {
    private Context mContext;
    private List <User> userList = new ArrayList <>();

    public serviceAdapter(@NonNull Context context, @SuppressLint("SupportAnnotationUsage") @LayoutRes ArrayList<User> services) {
        super(context, 0 , services);
        mContext = context;
        userList = services;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.item_service,parent,false);

        final User currentUser = userList.get(position);

        TextView service = (TextView) listItem.findViewById(R.id.ServiceName);
        service.setText(currentUser.getBeauty_Center_name());

        TextView waiting = (TextView) listItem.findViewById(R.id.waiting);
        waiting.setText(currentUser.getBeauty_Center_name());


        TextView completed = (TextView) listItem.findViewById(R.id.completed);
        completed.setText( "Distance "+currentUser.Completed);

        TextView current = (TextView) listItem.findViewById(R.id.current);
        current.setText("waiting "+currentUser.Current);


        RelativeLayout ID=(RelativeLayout)listItem.findViewById(R.id.list_item);



        return listItem;
    }
}
