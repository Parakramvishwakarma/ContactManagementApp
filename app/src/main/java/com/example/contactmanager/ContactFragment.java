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

public class ContactFragment extends Fragment {

    /* -----------------------------------------------------------------------------------------
            Function: Initialise View models + Elements
            Author: Ryan
     ---------------------------------------------------------------------------------------- */
    private NavigationData navModel;
    private EditContact editContact;
    private CreateContact contactModel;
    private EditText firstName, lastName, email, phone;
    private Button saveContactButton;
    private Long num;
    public ContactFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        navModel = new ViewModelProvider(getActivity()).get(NavigationData.class);
        contactModel = new ViewModelProvider(getActivity()).get(CreateContact.class);
        editContact = new ViewModelProvider(getActivity()).get(EditContact.class);
        ContactDao contactDao = initialiseDB();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact, container, false);

        // Set isEdit boolean
        boolean isEdit = editContact.getContactId() != 0;

        /* -----------------------------------------------------------------------------------------
            Function: Initialise layout elements
            Author: Ryan
            Description: Defines layout elements from their layout IDs
         ---------------------------------------------------------------------------------------- */
        firstName = view.findViewById(R.id.firstName);
        lastName = view.findViewById(R.id.lastName);
        email = view.findViewById(R.id.emailBox);
        phone = view.findViewById(R.id.phoneBox);
        saveContactButton = view.findViewById(R.id.saveContactButton);

        // Initialise defaults
        firstName.setText("");
        lastName.setText("");
        email.setText("");
        phone.setText("");

        /* -----------------------------------------------------------------------------------------
            Function: firstName Text Change Listener
            Author: Ryan
         ---------------------------------------------------------------------------------------- */
        firstName.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (isEdit) {
                    editContact.setFirstName(String.valueOf(s));
                }
                contactModel.setFirstName(String.valueOf(s));
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
                if (isEdit) {
                    editContact.setLastName(String.valueOf(s));
                }
                contactModel.setLastName(String.valueOf(s));
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        /* -----------------------------------------------------------------------------------------
            Function: email Text Change Listener
            Author: Ryan
         ---------------------------------------------------------------------------------------- */
        email.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (isEdit) {
                    editContact.setEmail(String.valueOf(s));
                }
                contactModel.setEmail(String.valueOf(s));
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        /* -----------------------------------------------------------------------------------------
            Function: phone Text Change Listener
            Author: Ryan
         ---------------------------------------------------------------------------------------- */
        phone.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                try {
                    num = Long.parseLong(String.valueOf(s));
                    System.out.println("Converted long value: " + num);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid format for long");
                }
                if (isEdit) {
                    editContact.setPhoneNumber(num);
                }
                contactModel.setPhoneNumber(num);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });


        saveContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isEdit) {
                    updateContact();
                }
                else {
                    saveContact();
                    int contactCount = contactModel.getContactCount();
                    contactModel.setContactCount(contactCount + 1);
                }
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
        contact.setFirstName(contactModel.getFirstName());
        contact.setLastName(contactModel.getLastName());
        contact.setEmail(contactModel.getEmail());
        //contact.setImage(contactModel.getContactIcon());
        contactDao.insert(contact);

        // Once added, wipes from short term data
        //contactModel.setContactIcon(0);
        contactModel.setFirstName("");
        contactModel.setLastName("");
        contactModel.setEmail("");
    }
    public void updateContact() {
        ContactDao contactDao = initialiseDB();
        contactDao.updateFirstName(editContact.getContactId(), editContact.getFirstName());
        contactDao.updateLastName(editContact.getContactId(), editContact.getLastName());
        contactDao.updateEmail(editContact.getContactId(), editContact.getEmail());

        // Once added, wipes from short term data
        //contactModel.setContactIcon(0);
        editContact.setFirstName("");
        editContact.setLastName("");
        editContact.setEmail("");
    }

}