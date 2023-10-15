package com.example.contactmanager;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ViewContactsFragment extends Fragment {

    /* -----------------------------------------------------------------------------------------
            Function: Initialise View models + Elements
            Author: Parakram + Ryan
     ---------------------------------------------------------------------------------------- */
    private NavigationData navigationData;
    private EditContact editContactModel;
    private ContactData contactModel;
    private List<Contact> data;
    EditDeleteContactAdapter editDeleteContactAdapter;

    private SearchView searchView;
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
        searchView = view.findViewById(R.id.users_search);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return false;
            }
        });
        //set the Gridlayout manager for the recycler view
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1,
                GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        //get the data from te DB
        data = getContactData();
        //set the data in the adapter
        editDeleteContactAdapter = new EditDeleteContactAdapter(data, navigationData, editContactModel, contactModel);
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

    /* -----------------------------------------------------------------------------------------
        Function: filterList()
        Author: Parakram
        Description: Filter by the search key of the search element
     ---------------------------------------------------------------------------------------- */
    private void filterList(String text) {
        List<Contact> filteredList =  new ArrayList<Contact>();
        for (Contact contact: data) {
            String contactName = contact.getFirstName() + " " + contact.getLastName();
            if (contactName.toLowerCase().contains(text.toLowerCase())){
                filteredList.add(contact);
            }
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(getActivity(), "No related contacts", Toast.LENGTH_SHORT).show();
        }
        else {
            editDeleteContactAdapter.setFilteredData(filteredList);
        }

    }

    /* -----------------------------------------------------------------------------------------
        Function: getContactData()
        Author: Parakram
        Description: get all contact
     ---------------------------------------------------------------------------------------- */
    public List<Contact> getContactData() {
        ContactDao contactDao = initialiseDB();
        data = null;
        data = contactDao.getAllContacts();
        return data;
    }

    /* -----------------------------------------------------------------------------------------
        Function: initialiseDB
        Author: Ryan
        Description: Initialises the contact database
     ---------------------------------------------------------------------------------------- */
    public ContactDao initialiseDB() {
        return ContactDbInstance.getDatabase(getContext()).contactDao();
    }

}