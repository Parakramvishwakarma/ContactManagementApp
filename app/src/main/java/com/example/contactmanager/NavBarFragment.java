package com.example.contactmanager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

public class NavBarFragment extends Fragment {

    /* -----------------------------------------------------------------------------------------
            Function: Initialise View models + Elements
            Author: Parakram + Ryan
     ---------------------------------------------------------------------------------------- */
    NavigationData navigationData;
    private ImageButton backButton, addButton;

    public NavBarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        navigationData = new ViewModelProvider(getActivity()).get(NavigationData.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /* The navigation integers describe the following fragments:
            navigationData == 0 -> View Contacts Fragment
            navigationData == 1 -> Add Contacts Fragment
         */

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nav_bar, container, false);

        /* -----------------------------------------------------------------------------------------
            Function: Initialise layout elements
            Author: Ryan
            Description: Defines layout elements from their layout IDs
         ---------------------------------------------------------------------------------------- */
        backButton = view.findViewById(R.id.backButton);
        addButton = view.findViewById(R.id.addButton);

        /* -----------------------------------------------------------------------------------------
            Function: Add Click Listener
            Author: Ryan
            Description: Navigates to the add contacts fragment.
         ---------------------------------------------------------------------------------------- */
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigationData.setClickedValue(1);
                navigationData.setHistoricalClickedValue(1);
            }
        });

        /* -----------------------------------------------------------------------------------------
            Function: Back Click Listener
            Author: Ryan
            Description: Navigates to the last known fragment (or alternate fragment in special
                cases).
         ---------------------------------------------------------------------------------------- */
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (navigationData.getHistoricalClickedValue() == 1) {
                    navigationData.setClickedValue(0);
                    navigationData.setHistoricalClickedValue(0);
                }
            }
        });

        return view;
    }
}