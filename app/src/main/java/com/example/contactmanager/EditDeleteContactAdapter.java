package com.example.contactmanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EditDeleteContactAdapter extends RecyclerView.Adapter<EditDeleteContactVH>  {

    private List<Contact> data;
    private NavigationData navModel;
    private EditContact editContactModel;

    public EditDeleteContactAdapter(List<Contact> data, NavigationData navModel, EditContact editContactModel, ContactData contactModel){
        this.data = data;
        this.navModel = navModel;
        this.editContactModel = editContactModel;
    }

    @NonNull
    @Override
    public EditDeleteContactVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.contact_list_item,parent,false);
        return new EditDeleteContactVH(view, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull EditDeleteContactVH holder, int position) {
        Contact singleRow = data.get(position);
        holder.contactIcon.setImageResource(singleRow.getImage());
        holder.contactNameTextBox.setText(singleRow.getFirstName() + " " + singleRow.getLastName());

        /* -----------------------------------------------------------------------------------------
                Function: contactDelete Click Listener
                Author: Ryan
         ---------------------------------------------------------------------------------------- */
        holder.contactDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteContact(position, singleRow.getId(), holder, singleRow);
            }
        });

        /* -----------------------------------------------------------------------------------------
                Function: contactEdit Click Listener
                Author: Ryan
         ---------------------------------------------------------------------------------------- */
        holder.contactEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navModel.setClickedValue(1);
                System.out.println("CONTACT DATA - First Name: " + singleRow.getFirstName());
                System.out.println("CONTACT DATA - Last Name: " + singleRow.getLastName());
                System.out.println("CONTACT DATA - Email: " + singleRow.getEmail());
                System.out.println("CONTACT DATA - Phone: " + singleRow.getPhoneNumber());
                System.out.println("CONTACT DATA - ID: " + singleRow.getId());
                System.out.println("CONTACT DATA - Nav: " + navModel.getClickedValue());

            }
        });
    }


    public void deleteContact(int index, long id, EditDeleteContactVH holder, Contact singleRow) {
        editContactModel.setDeleteContactPosition(holder.getAdapterPosition());
        editContactModel.setDeleteContactId(id);

    }
    @Override
    public int getItemCount() {
        return data.size();
    }
}
