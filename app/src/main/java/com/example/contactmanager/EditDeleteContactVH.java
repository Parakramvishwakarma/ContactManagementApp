package com.example.contactmanager;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EditDeleteContactVH extends RecyclerView.ViewHolder  {
    ImageView contactIcon;
    TextView contactNameTextBox;
    ImageButton contactEditButton;
    ImageButton contactDeleteButton;

    public EditDeleteContactVH(@NonNull View itemView, ViewGroup parent) {
        super(itemView);
        // Define the content inside the view holder
        contactIcon = itemView.findViewById(R.id.contact_icon);
        contactNameTextBox = itemView.findViewById(R.id.contact_name);
        contactEditButton = itemView.findViewById(R.id.editButton);
        contactDeleteButton = itemView.findViewById(R.id.deleteButton);
    }
}
