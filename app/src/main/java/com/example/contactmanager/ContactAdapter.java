package com.example.contactmanager;

import android.nfc.tech.NfcV;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactVH> {
    private List<Contact> data;
    private NavigationData navigationData;
    private SelectContactModel selectContactModel;

    public ContactAdapter(List<Contact> data, NavigationData navModel, SelectContactModel selectContactModel) {
        this.data = data;
        this.navigationData = navModel;
        this.selectContactModel = selectContactModel;
    }
    @NonNull
    @Override
    public ContactVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.contact_list_item,parent,false);
        return new ContactVH(view, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactVH holder, int position) {
        Contact singleRow = data.get(position);
        holder.userIcon.setImageResource(singleRow.getImage());
        holder.userNameTextBox.setText(String.format("%s, %s", singleRow.getLastName(), singleRow.getFirstName()));
        holder.userIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick(holder, singleRow);
            }
        });
        holder.userNameTextBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick(holder, singleRow);
            }
        });
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick(holder, singleRow);
            }
        });
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigationData.setClickedValue(2);
                selectContactModel.setContactId(singleRow.getId()); //set the selected users
                navigationData.setHistoricalClickedValue(navigationData.getHistoricalClickedValue()); // set the historical value this will allow us to come back to it
            }
        });
    }
    private void onItemClick(@NonNull ContactVH holder, Contact singleRow) {
        navigationData.setClickedValue(2); // go to the edit contact page
        selectContactModel.setContactId(singleRow.getId()); //set the selected users
        navigationData.setHistoricalClickedValue(navigationData.getHistoricalClickedValue()); // set the historical value this will allow us to come back to it

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}


