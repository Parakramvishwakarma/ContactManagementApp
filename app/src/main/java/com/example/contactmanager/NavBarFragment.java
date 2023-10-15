package com.example.contactmanager;

import com.example.contactmanager.ContactDao;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.List;

public class NavBarFragment extends Fragment {

    /* -----------------------------------------------------------------------------------------
            Function: Initialise View models + Elements
            Author: Parakram + Ryan
     ---------------------------------------------------------------------------------------- */
    NavigationData navigationData;
    private ImageButton backButton, addButton, importButton;
    byte[] photo = null;
    Bitmap contactPhoto = null;
    CreateContact contactModel;
    EditContact editContactModel;
    int contactId;

    private static final int REQUEST_READ_CONTACT_PERMISSION = 3;

    private static final int CONTACT_PICKER_REQUEST = 1001;

    public NavBarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        navigationData = new ViewModelProvider(getActivity()).get(NavigationData.class);
        contactModel = new ViewModelProvider(getActivity()).get(CreateContact.class);
        editContactModel = new ViewModelProvider(getActivity()).get(EditContact.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /* The navigation integers describe the following fragments:
            navigationData == 0 -> View Contacts Fragment
            navigationData == 1 -> Add/Edit Contacts Fragment
            navigationData == 2 -> Camera Fragment
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
        importButton = view.findViewById(R.id.importButton);

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
                    editContactModel.resetEditContact();
                }
            }
        });

        /* -----------------------------------------------------------------------------------------
            Function: Import Click Listener
            Author: Ryan
            Description: Navigates to ...
         ---------------------------------------------------------------------------------------- */
        importButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(requireContext(),
                        android.Manifest.permission.READ_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(requireActivity(),
                            new String[]{Manifest.permission.READ_CONTACTS},
                            REQUEST_READ_CONTACT_PERMISSION);
                }
                else {
                    openContactsPicker();
                }


                navigationData.setClickedValue(2);
                navigationData.setHistoricalClickedValue(2);

            }
        });

        navigationData.clickedValue.observe(getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer == 0) {
                    backButton.setVisibility(View.GONE);
                    addButton.setVisibility(View.VISIBLE);
                    importButton.setVisibility(View.VISIBLE);
                }
                if (integer == 1) {
                    backButton.setVisibility(View.VISIBLE);
                    addButton.setVisibility(View.GONE);
                    importButton.setVisibility(View.GONE);
                }
            }});

        return view;
    }

    private final ActivityResultLauncher<Intent> pickContactLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();

                }
            }
    );

    private void openContactsPicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, CONTACT_PICKER_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CONTACT_PICKER_REQUEST && resultCode == Activity.RESULT_OK) {
            // Handle the selected contact data here
            if (data != null) {
                Uri contactUri = data.getData();
                contactId = getContactIdFromUri(contactUri);

                // Use the contactUri to retrieve contact details
                retrieveName();

                if(isDuplicateContact(contactModel.getFirstName(), contactModel.getLastName()) == false) {
                    retrievePhoneNumber();
                    retrieveEmail();
                    retrieveImage();

                    saveContact();
                    int contactCount = contactModel.getContactCount();
                    contactModel.setContactCount(contactCount + 1);
                    navigationData.setClickedValue(0);
                }
                else {
                    Toast.makeText(getContext(), "Contact is already in database", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    // Helper method to extract contact ID from contactUri
    private int getContactIdFromUri(Uri contactUri) {
        Cursor cursor = requireContext().getContentResolver().query(
                contactUri, null, null, null, null);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    int contactIdColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
                    return cursor.getInt(contactIdColumnIndex);
                }
            } finally {
                cursor.close();
            }
        }
        return -1; // Return -1 if contact ID cannot be retrieved
    }

    private void retrieveName() {
        String firstName = "";
        String lastName = "";
        Uri nameUri = ContactsContract.Data.CONTENT_URI;

        String[] projection = new String[] {
                ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,
                ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME
        };

        String selection = ContactsContract.Data.CONTACT_ID + " = ? AND " +
                ContactsContract.Data.MIMETYPE + " = ?";
        String[] selectionArgs = new String[] {
                String.valueOf(contactId),
                ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
        };

        Cursor cursor = requireContext().getContentResolver().query(
                nameUri, projection, selection, selectionArgs, null);

        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    int givenNameIndex = cursor.getColumnIndex(
                            ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME);
                    int familyNameIndex = cursor.getColumnIndex(
                            ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME);

                    firstName = cursor.getString(givenNameIndex);
                    lastName = cursor.getString(familyNameIndex);
                }
            } finally {
                cursor.close();
            }
        }

        contactModel.setFirstName(firstName);
        contactModel.setLastName(lastName);
    }


    private void retrievePhoneNumber() {
        String result = "";
        Uri phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] queryFields = new String[] {
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };

        String whereClause = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?";
        String[] whereValues = new String[]{
                String.valueOf(this.contactId)
        };
        Cursor cursor = requireContext().getContentResolver().query(
                phoneUri, queryFields, whereClause, whereValues, null);

        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    do {
                        String phoneNumber = cursor.getString(0);
                        if (!phoneNumber.trim().isEmpty()) { // Check if the phone number is not empty
                            result = result + phoneNumber + " ";
                        }
                    } while (cursor.moveToNext());
                }
            } finally {
                cursor.close();
            }
        }

        if (!result.trim().isEmpty()) { // Check if the result is not empty
            contactModel.setPhoneNumber(Long.parseLong(result.trim()));
        }
    }

    private void retrieveEmail() {
        String result = "";
        Uri emailUri = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        String[] queryFields = new String[] {
                ContactsContract.CommonDataKinds.Email.ADDRESS
        };

        String whereClause = ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=?";
        String[] whereValues = new String[]{
                String.valueOf(this.contactId)
        };
        Cursor cursor = requireContext().getContentResolver().query(
                emailUri, queryFields, whereClause, whereValues, null);

        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    do {
                        String email = cursor.getString(0);
                        result = result + email + " ";
                        System.out.print("RDebug - Email: " + result);
                    } while (cursor.moveToNext());
                }
            } finally {
                cursor.close();
            }
        }

        contactModel.setEmail(result);
    }
    private void retrieveImage() {
        Bitmap contactPhoto = null;
        String[] projection = new String[] {
                ContactsContract.CommonDataKinds.Photo.PHOTO
        };

        String selection = ContactsContract.Data.CONTACT_ID + " = ? AND " +
                ContactsContract.Data.MIMETYPE + " = ?";

        String[] selectionArgs = new String[] {
                String.valueOf(contactId),
                ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE
        };

        Cursor cursor = requireContext().getContentResolver().query(
                ContactsContract.Data.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null
        );

        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {

                    int photoIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO);
                    byte[] photoData = cursor.getBlob(photoIndex);

                    if (photoData != null) {
                        // Create a Bitmap from the photoData
                        contactPhoto = BitmapFactory.decodeByteArray(photoData, 0, photoData.length);
                    }
                }
            } finally {
                cursor.close();
            }
        }
        contactModel.setContactIcon(contactPhoto);
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

        if (contactModel.getContactIcon() == null) {
            Bitmap generatedIcon = generateContactImage(contactModel.getFirstName());
            contactModel.setContactIcon(generatedIcon);
            contact.setImage(contactModel.getContactIcon());
        }
        else {
            contact.setImage(contactModel.getContactIcon());
        }

        contactDao.insert(contact);

        // Once added, wipes from short term data
        contactModel.setContactIcon(null);
        contactModel.setFirstName("");
        contactModel.setLastName("");
        contactModel.setEmail("");
        contactModel.setPhoneNumber(0L);
    }

    /* -----------------------------------------------------------------------------------------
            Function: generateContactImage
            Author: Ryan
            Description: If no user photo is chosen, we will generate one with a random RGB colour
                background and the first letter of their first name.
         ---------------------------------------------------------------------------------------- */
    public Bitmap generateContactImage(String name) {

        // We can generate a random background colour for the icon
        int r = (int) (Math.random() * 256);
        int g = (int) (Math.random() * 256);
        int b = (int) (Math.random() * 256);

        int backgroundColor = Color.rgb(r, g, b); // Defines the colour by mapping to RGB

        // Creates the Bitmap with the given color background
        Bitmap contactImage = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(contactImage);
        canvas.drawColor(backgroundColor);

        // Puts first letter of the name on the Bitmap
        Paint textPaint = new Paint();
        textPaint.setColor(Color.WHITE); // Text color
        textPaint.setTextSize(40); // Text size

        // Calculates the position to center the text
        Rect bounds = new Rect();
        textPaint.getTextBounds(name, 0, 1, bounds);
        int x = (contactImage.getWidth() - bounds.width()) / 2;
        int y = (contactImage.getHeight() + bounds.height()) / 2;

        canvas.drawText(name.substring(0, 1).toUpperCase(), x, y, textPaint);

        return contactImage;
    }

    public boolean isDuplicateContact(String firstName, String lastName) {
        ContactDao contactDao = initialiseDB();
        List<Contact> contacts = contactDao.getAllContacts();

        // Iterate through the list of contacts
        for (Contact contact : contacts) {
            if (contact.getFirstName().equalsIgnoreCase(firstName) && contact.getLastName().equalsIgnoreCase(lastName)) {
                // If a contact with the same first name and last name is found, it's a duplicate
                return true;
            }
        }

        // If no duplicates are found, return false
        return false;
    }


}