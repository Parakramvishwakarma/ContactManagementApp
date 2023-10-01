package com.example.contactmanager;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class ContactFragment extends Fragment {

    /* -----------------------------------------------------------------------------------------
            Function: Initialise View models + Elements
            Author: Ryan
     ---------------------------------------------------------------------------------------- */
    private NavigationData navModel;
    private EditContact editContactModel;
    private CreateContact contactModel;
    private EditText firstName, lastName, email, phone;
    private TextView contactName;
    private ImageView contactPhoto;
    private Button saveContactButton, addPhotoButton;
    private Long num;
    private boolean isEdit;
    private File photoFile;
    public ContactFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        navModel = new ViewModelProvider(getActivity()).get(NavigationData.class);
        contactModel = new ViewModelProvider(getActivity()).get(CreateContact.class);
        editContactModel = new ViewModelProvider(getActivity()).get(EditContact.class);
        ContactDao contactDao = initialiseDB();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact, container, false);

        // Set isEdit boolean
        isEdit = editContactModel.getContactId() != 0;

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
        saveContactButton = view.findViewById(R.id.saveContactButton);
        addPhotoButton = view.findViewById(R.id.addPhotoButton);
        contactPhoto = view.findViewById(R.id.contactPhoto);

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
                    editContactModel.setFirstName(String.valueOf(s));
                    System.out.println("New name is:" + editContactModel.getFirstName());
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
                    editContactModel.setLastName(String.valueOf(s));
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
                    editContactModel.setEmail(String.valueOf(s));
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

                // Applies a filter to the phone number input to prevent non digits (0-9) from being typed
                InputFilter filter = new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                        for (int i = start; i < end; i++) {
                            if (!Character.isDigit(source.charAt(i))) {
                                return "";
                            }
                        }
                        return null; // Accept the input
                    }
                };
                phone.setFilters(new InputFilter[] { filter });
                String inputText = phone.getText().toString();
                if (inputText != null) {
                    try {
                        num = Long.parseLong(String.valueOf(s));
                        if (isEdit) {
                            editContactModel.setPhoneNumber(num);
                        }
                        contactModel.setPhoneNumber(num);
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid format for long");
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        if (isEdit) {
            contactName.setText(editContactModel.getFirstName() + " " + editContactModel.getLastName());
            firstName.setText(editContactModel.getFirstName());
            lastName.setText(editContactModel.getLastName());
            email.setText(editContactModel.getEmail());
            phone.setText(Long.toString(editContactModel.getPhoneNumber()));
            contactPhoto.setImageBitmap(editContactModel.getContactIcon());

        }


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

        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                photoLauncher.launch(intent);
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
        contact.setPhoneNumber(contactModel.getPhoneNumber());
        contact.setImage(contactModel.getContactIcon());
        contactDao.insert(contact);

        // Once added, wipes from short term data
        //contactModel.setContactIcon(null);
        contactModel.setFirstName("");
        contactModel.setLastName("");
        contactModel.setEmail("");
        contactModel.setPhoneNumber(0L);
    }
    public void updateContact() {
        ContactDao contactDao = initialiseDB();
        contactDao.updateFirstName(editContactModel.getContactId(), editContactModel.getFirstName());
        contactDao.updateLastName(editContactModel.getContactId(), editContactModel.getLastName());
        contactDao.updateEmail(editContactModel.getContactId(), editContactModel.getEmail());
        contactDao.updatePhoneNumber(editContactModel.getContactId(), editContactModel.getPhoneNumber());
        contactDao.updateContactIcon(editContactModel.getContactId(), editContactModel.getContactIcon());

        // Once added, wipes from short term data
        editContactModel.setContactIcon(null);
        editContactModel.setFirstName("");
        editContactModel.setLastName("");
        editContactModel.setEmail("");
        editContactModel.setPhoneNumber(0L);
    }

    private final ActivityResultLauncher<Intent> photoLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    Bitmap image = (Bitmap) data.getExtras().get("data");
                    if (image != null) {
                        contactPhoto.setImageBitmap(image);
                        if (isEdit) {
                            editContactModel.setContactIcon(image);
                        }
                        contactModel.setContactIcon(image);

                    }
                }
            }
    );

}