package com.example.contactmanager;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ContactVH extends RecyclerView.ViewHolder{
    LinearLayout layout;
    ImageView userIcon;
    TextView userNameTextBox;


    public ContactVH(@NonNull View itemView, ViewGroup parent) {
        super(itemView);
        userIcon = itemView.findViewById(R.id.user_icon);
        userNameTextBox = itemView.findViewById(R.id.user_name);
        layout = itemView.findViewById(R.id.user_layout);
    }
}
