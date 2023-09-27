package com.example.contactmanager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewContactsFragment extends Fragment {

    private NavigationData navigationData;
    private EditText searchBar;
    private SelectContactModel selectContactModel;
    private List<Contact> data;

    private RecyclerView recyclerView;

    public ViewContactsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navigationData = new ViewModelProvider(this).get(NavigationData.class);
        selectContactModel = new ViewModelProvider(this).get(SelectContactModel.class);
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
        ContactAdapter contactAdapter  =new ContactAdapter(data, navigationData, selectContactModel);
        recyclerView.setAdapter(contactAdapter);
        return view;
    }

    public List<Contact> getContactData() {
        List<Contact> data = new ArrayList<Contact>();
        Contact test = new Contact();
        test.setId(1);
        test.setFirstName("Parakram");
        test.setLastName("Vishwakarma");
        test.setImage(R.drawable.mock_contact_image);
        data.add(test);
        return data;
    }
}