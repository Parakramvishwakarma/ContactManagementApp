package com.example.contactmanager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class AddContactFragment extends Fragment {

    /* -----------------------------------------------------------------------------------------
            Function: Initialise View models + Elements
            Author: Ryan
     ---------------------------------------------------------------------------------------- */
    private NavigationData navModel;
    private ContactData contactModel;
    private EditText firstName, lastName;
    private Button addContactButton;
    private String firstNameString, lastNameString;
    public AddContactFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        navModel = new ViewModelProvider(getActivity()).get(NavigationData.class);
        contactModel = new ViewModelProvider(getActivity()).get(ContactData.class);
        ContactDao contactDao = initialiseDB();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_contact, container, false);

        /* -----------------------------------------------------------------------------------------
            Function: Initialise layout elements
            Author: Ryan
            Description: Defines layout elements from their layout IDs
         ---------------------------------------------------------------------------------------- */
        firstName = view.findViewById(R.id.firstName);
        lastName = view.findViewById(R.id.lastName);
        addContactButton = view.findViewById(R.id.saveContactButton);

        /* -----------------------------------------------------------------------------------------
            Function: firstName Text Change Listener
            Author: Ryan
         ---------------------------------------------------------------------------------------- */
        firstName.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                firstNameString = String.valueOf(s);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        /* -----------------------------------------------------------------------------------------
            Function: lastName Text Change Listener
            Author: Ryan
         ---------------------------------------------------------------------------------------- */
        lastName.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                lastNameString = String.valueOf(s);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveContact();
                int contactCount = contactModel.getContactCount();
                contactModel.setContactCount(contactCount + 1);

                navModel.setClickedValue(0);
                navModel.setHistoricalClickedValue(0);
            }
        });

        return view;
    }

    public ContactDao initialiseDB() {
        return ContactDbInstance.getDatabase(getContext()).contactDao();
    }

    public void saveContact() {
        ContactDao contactDao = initialiseDB();
        Contact contact = new Contact();
        contact.setUserName(contactModel.getUserName());
        contact.setUserIcon(userModel.getUserIcon());
        contact.setUserLosses(0);
        contact.setUserWins(0);
        contactDao.insert(user);
        userModel.setUserIcon(0);
        userModel.setUserName("");
    }
}