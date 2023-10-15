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

    public void setFilteredData (List<Contact> filteredData) {
        this.data = filteredData;
        notifyDataSetChanged();
    }
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
        holder.contactIcon.setImageBitmap(singleRow.getImage());
        holder.contactNameTextBox.setText(singleRow.getFirstName() + " " + singleRow.getLastName());

        holder.contactIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navModel.setClickedValue(2);

                // Sets the data in the adapter from the view holder
                editContactModel.setFirstName(singleRow.getFirstName());
                editContactModel.setLastName(singleRow.getLastName());
                editContactModel.setEmail(singleRow.getEmail());
                editContactModel.setPhoneNumber(singleRow.getPhoneNumber());
                editContactModel.setContactIcon(singleRow.getImage());
                editContactModel.setContactId(singleRow.getId());

            }});



        holder.contactNameTextBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navModel.setClickedValue(2);

                // Sets the data in the adapter from the view holder
                editContactModel.setFirstName(singleRow.getFirstName());
                editContactModel.setLastName(singleRow.getLastName());
                editContactModel.setEmail(singleRow.getEmail());
                editContactModel.setPhoneNumber(singleRow.getPhoneNumber());
                editContactModel.setContactIcon(singleRow.getImage());
                editContactModel.setContactId(singleRow.getId());

            }});
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
                navModel.setHistoricalClickedValue(1);

                // Sets the data in the adapter from the view holder
                editContactModel.setFirstName(singleRow.getFirstName());
                editContactModel.setLastName(singleRow.getLastName());
                editContactModel.setEmail(singleRow.getEmail());
                editContactModel.setPhoneNumber(singleRow.getPhoneNumber());
                editContactModel.setContactIcon(singleRow.getImage());
                editContactModel.setContactId(singleRow.getId());

            }
        });
    }


    /* -----------------------------------------------------------------------------------------
            Function: deleteContact()
            Author: Ryan
            Description: Deletes the contact from the view holder
     ---------------------------------------------------------------------------------------- */
    public void deleteContact(int index, long id, EditDeleteContactVH holder, Contact singleRow) {
        editContactModel.setDeleteContactPosition(holder.getAdapterPosition());
        editContactModel.setDeleteContactId(id);

    }
    @Override
    public int getItemCount() {
        return data.size();
    }
}
