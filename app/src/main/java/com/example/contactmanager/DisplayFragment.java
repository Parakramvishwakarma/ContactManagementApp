package com.example.contactmanager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class DisplayFragment extends Fragment {


    private EditContact editContactModel;

    private TextView firstName, lastName, email, phone, contactName;

    private ImageView contactPhoto;
    private NavigationData navModel;
    public DisplayFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navModel = new ViewModelProvider(getActivity()).get(NavigationData.class);
        editContactModel = new ViewModelProvider(getActivity()).get(EditContact.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_display, container, false);
                /* -----------------------------------------------------------------------------------------
            Function: Initialise layout elements
            Author: Ryan
            Description: Defines layout elements from their layout IDs
         ---------------------------------------------------------------------------------------- */
        firstName = view.findViewById(R.id.firstName);
        lastName = view.findViewById(R.id.lastName);
        email = view.findViewById(R.id.emailBox);
        phone = view.findViewById(R.id.phoneBox);
        contactName = view.findViewById(R.id.contactName);
        contactPhoto = view.findViewById(R.id.contactPhoto);
        contactName.setText(editContactModel.getFirstName() + " " + editContactModel.getLastName());

               /* -----------------------------------------------------------------------------------------
            Function: Initialise setTexts
            Author: Ryan
            Description: Maintains data across orientation changes
         ---------------------------------------------------------------------------------------- */
        if (editContactModel.getFirstName() != null) {
            firstName.setText(editContactModel.getFirstName());
        }

        if (editContactModel.getLastName() != null) {
            lastName.setText(editContactModel.getLastName());
        }



        if (editContactModel.getEmail() != null) {
            email.setText(editContactModel.getEmail());
        }

        if (editContactModel.getPhoneNumber() != null) {
            phone.setText(editContactModel.getPhoneNumber());
        }
        if (editContactModel.getContactIcon() != null) {
            contactPhoto.setImageBitmap(editContactModel.getContactIcon());
        }
        return view;
    }
}