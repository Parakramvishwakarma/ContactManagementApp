package com.example.contactmanager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class ViewContactsFragment extends Fragment {

    private NavigationData navigationData;
    private EditText searchBar;
    private EditContact editContactModel;
    private ContactData contactModel;
    private List<Contact> data;

    private RecyclerView recyclerView;

    public ViewContactsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navigationData = new ViewModelProvider(getActivity()).get(NavigationData.class);
        editContactModel = new ViewModelProvider(getActivity()).get(EditContact.class);
        contactModel = new ViewModelProvider(getActivity()).get(ContactData.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_view_contacts, container, false);

        //get the elements
        recyclerView = view.findViewById(R.id.users_recycler);
        searchBar = view.findViewById(R.id.users_search);
        //set the Gridlayout manager for the recycler view
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1,
                GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        //get the data from te DB
        data = getContactData();
        //set the data in the adapter
        EditDeleteContactAdapter editDeleteContactAdapter = new EditDeleteContactAdapter(data, navigationData, editContactModel, contactModel);
        recyclerView.setAdapter(editDeleteContactAdapter);

        /* -----------------------------------------------------------------------------------------
                Function: deleteContactID Observer
                Author: Ryan
                Description: Deletes the contact from the database
         ---------------------------------------------------------------------------------------- */

        editContactModel.deleteContactId.observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(Long integer) {
                if (integer != 0) {
                    ContactDao contactDao = initialiseDB();
                    //delete from db
                    contactDao.deleteContact(integer);
                    int deletePosition = editContactModel.getDeleteContactPosition();

                    editContactModel.setContactCount(deletePosition - 1);
                    //delete from data
                    data.remove(deletePosition);
                    //notify adapter
                    editDeleteContactAdapter.notifyItemRemoved(editContactModel.getDeleteContactPosition());
                    //restore state
                    editContactModel.setDeleteContactPosition(0);
                    editContactModel.setDeleteContactId(0L);
                }
            }
        });

        return view;
    }

    public List<Contact> getContactData() {
        ContactDao contactDao = initialiseDB();
        data = null;
        data = contactDao.getAllContacts();
        return data;
    }
    public ContactDao initialiseDB() {
        return ContactDbInstance.getDatabase(getContext()).contactDao();
    }

}