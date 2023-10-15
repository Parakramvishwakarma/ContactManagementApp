package com.example.contactmanager;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
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
import android.widget.Toast;

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
    private String num;
    private boolean isEdit;
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
        System.out.println("Test " + navModel.getHistoricalClickedValue());
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

        /* -----------------------------------------------------------------------------------------
            Function: Initialise setTexts
            Author: Ryan
            Description: Maintains data across orientation changes
         ---------------------------------------------------------------------------------------- */
        if (contactModel.getFirstName() != null) {
            firstName.setText(contactModel.getFirstName());
        }

        if (contactModel.getLastName() != null) {
            lastName.setText(contactModel.getLastName());
        }

        if (contactModel.getEmail() != null) {
            email.setText(contactModel.getEmail());
        }

        if (contactModel.getPhoneNumber() != null) {
            phone.setText(contactModel.getPhoneNumber());
        }
        if (contactModel.getContactIcon() != null) {
            contactPhoto.setImageBitmap(contactModel.getContactIcon());
        }
        contactModel.setSaveToggle(contactModel.getSaveToggle());
        System.out.println("RDebug - saveToggle, Or: " + contactModel.getSaveToggle());

        /* -----------------------------------------------------------------------------------------
            Function: firstName Text Change Listener
            Author: Ryan
            Description: Sets the first name
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
            Description: Sets the last name
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
            Description: Sets the email
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
            Description: Sets the phone number and checks if it is valid
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
                        num = s.toString();
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
            // Sets the relevant data fields to their existing data parameters
            contactName.setText(editContactModel.getFirstName() + " " + editContactModel.getLastName());
            firstName.setText(editContactModel.getFirstName());
            lastName.setText(editContactModel.getLastName());
            email.setText(editContactModel.getEmail());
            phone.setText(editContactModel.getPhoneNumber());
            contactPhoto.setImageBitmap(editContactModel.getContactIcon());

        }

        /* -----------------------------------------------------------------------------------------
            Function: saveContactButton click listener
            Author: Ryan
            Description: saves/updates the contact data
         ---------------------------------------------------------------------------------------- */
        saveContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isEdit) {
                    // If we are editing the contact, update
                    updateContact();
                }
                else {
                    // If we are creating a new contact, save
                    saveContact();
                }
                System.out.println("RDebug - saveToggle: " + contactModel.getSaveToggle());

                if (contactModel.getSaveToggle() == 0) {
                    if (navModel.getHistoricalClickedValue() == 2) {
                        navModel.setClickedValue(2);
                        navModel.setHistoricalClickedValue(0);
                    }
                    else {
                        navModel.setClickedValue(0);
                        navModel.setHistoricalClickedValue(0);
                    }
                }

            }
        });

        /* -----------------------------------------------------------------------------------------
            Function: addPhotoButton click listener
            Author: Ryan
            Description: Launches the photo launcher intent
         ---------------------------------------------------------------------------------------- */
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

    /* -----------------------------------------------------------------------------------------
            Function: initialiseDB
            Author: Ryan
            Description: Initialises the contact database
         ---------------------------------------------------------------------------------------- */
    public ContactDao initialiseDB() {
        return ContactDbInstance.getDatabase(getContext()).contactDao();
    }

    /* -----------------------------------------------------------------------------------------
            Function: saveContact
            Author: Ryan
            Description: Updates the contact model by retrieving all data from the CreateContact
                view model. Cleans setters and getters after saving.
         ---------------------------------------------------------------------------------------- */
    public void saveContact() {
        ContactDao contactDao = initialiseDB();
        Contact contact = new Contact();

        // Ensures data fields are filled before saving
        if (contactModel.getFirstName() == null || contactModel.getFirstName().isEmpty()) {
            contactModel.setSaveToggle(contactModel.getSaveToggle() + 1);
        }

        if (contactModel.getLastName() == null || contactModel.getLastName().isEmpty()) {
            contactModel.setSaveToggle(contactModel.getSaveToggle() + 1);
        }

        if (contactModel.getEmail() == null || contactModel.getEmail().isEmpty()) {
            contactModel.setSaveToggle(contactModel.getSaveToggle() + 1);
        }

        if (contactModel.getPhoneNumber() == null || contactModel.getPhoneNumber().isEmpty()) {
            contactModel.setSaveToggle(contactModel.getSaveToggle() + 1);
        }

        System.out.println("RDebug - saveToggle: " + contactModel.getSaveToggle());
        // Checks all data fields
        if ((!contactModel.getFirstName().isEmpty() && !contactModel.getLastName().isEmpty() &&
                !contactModel.getEmail().isEmpty() && !contactModel.getPhoneNumber().isEmpty()) &&
                (contactModel.getFirstName() != null && contactModel.getLastName() != null &&
                contactModel.getEmail() != null && contactModel.getPhoneNumber() != null)) {
            System.out.println("RDebug - First Name: " + contactModel.getFirstName());
            System.out.println("RDebug - Last Name: " + contactModel.getLastName());
            System.out.println("RDebug - Email: " + contactModel.getEmail());
            System.out.println("RDebug - Phone: " + contactModel.getPhoneNumber());
            contactModel.setSaveToggle(0); // resets save toggle
        }

        if (contactModel.getSaveToggle() == 0) {
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

            // Increment contact count
            int contactCount = contactModel.getContactCount();
            contactModel.setContactCount(contactCount + 1);

            // Once added, wipes from short term data
            contactModel.setContactIcon(null);
            contactModel.setFirstName("");
            contactModel.setLastName("");
            contactModel.setEmail("");
            contactModel.setPhoneNumber("");
        }
        else {
            Toast.makeText(getContext(), "Contact Fields Blank", Toast.LENGTH_SHORT).show();
        }

    }

    /* -----------------------------------------------------------------------------------------
            Function: updateContact
            Author: Ryan
            Description: Updates the contact model by retrieving all data from the EditContact view
                model. Cleans setters and getters after saving.
         ---------------------------------------------------------------------------------------- */
    public void updateContact() {
        ContactDao contactDao = initialiseDB();

        // Ensures data fields are filled before saving
        if (editContactModel.getFirstName() == null || editContactModel.getFirstName().isEmpty()) {
            contactModel.setSaveToggle(contactModel.getSaveToggle() + 1);
        }

        if (editContactModel.getLastName() == null || editContactModel.getLastName().isEmpty()) {
            contactModel.setSaveToggle(contactModel.getSaveToggle() + 1);
        }

        if (editContactModel.getEmail() == null || editContactModel.getEmail().isEmpty()) {
            contactModel.setSaveToggle(contactModel.getSaveToggle() + 1);
        }

        if (editContactModel.getPhoneNumber() == null || editContactModel.getPhoneNumber().isEmpty()) {
            contactModel.setSaveToggle(contactModel.getSaveToggle() + 1);
        }

        System.out.println("RDebug - saveToggle: " + contactModel.getSaveToggle());
        // Checks all data fields
        if ((!contactModel.getFirstName().isEmpty() && !contactModel.getLastName().isEmpty() &&
                !contactModel.getEmail().isEmpty() && !contactModel.getPhoneNumber().isEmpty()) &&
                (contactModel.getFirstName() != null && contactModel.getLastName() != null &&
                        contactModel.getEmail() != null && contactModel.getPhoneNumber() != null)) {
            System.out.println("RDebug - First Name: " + contactModel.getFirstName());
            System.out.println("RDebug - Last Name: " + contactModel.getLastName());
            System.out.println("RDebug - Email: " + contactModel.getEmail());
            System.out.println("RDebug - Phone: " + contactModel.getPhoneNumber());
            contactModel.setSaveToggle(0); // resets save toggle
        }

        if (contactModel.getSaveToggle() == 0) {
            contactDao.updateFirstName(editContactModel.getContactId(), editContactModel.getFirstName());
            contactDao.updateLastName(editContactModel.getContactId(), editContactModel.getLastName());
            contactDao.updateEmail(editContactModel.getContactId(), editContactModel.getEmail());
            contactDao.updatePhoneNumber(editContactModel.getContactId(), editContactModel.getPhoneNumber());
            contactDao.updateContactIcon(editContactModel.getContactId(), editContactModel.getContactIcon());

            // Once added, wipes from short term data
            if (navModel.getHistoricalClickedValue() != 2) {
                editContactModel.setContactIcon(null);
                editContactModel.setFirstName("");
                editContactModel.setLastName("");
                editContactModel.setEmail("");
                editContactModel.setPhoneNumber("");
                editContactModel.setContactId(0);
            }


            // Once added, wipes from short term data (this has been added to ensure no excess data has carried over)
            contactModel.setContactIcon(null);
            contactModel.setFirstName("");
            contactModel.setLastName("");
            contactModel.setEmail("");
            contactModel.setPhoneNumber("");
        }
        else {
            Toast.makeText(getContext(), "Contact Fields Blank", Toast.LENGTH_SHORT).show();
        }
    }

    /* -----------------------------------------------------------------------------------------
            Function: photoLauncher
            Author: Ryan
            Description: Launches the intent for the camera app and saves it as a bitmap
         ---------------------------------------------------------------------------------------- */
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
        Bitmap contactImage = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(contactImage);
        canvas.drawColor(backgroundColor);

        // Puts first letter of the name on the Bitmap
        Paint textPaint = new Paint();
        textPaint.setColor(Color.WHITE); // Text color
        textPaint.setTextSize(100); // Text size

        // Calculates the position to center the text
        Rect bounds = new Rect();
        textPaint.getTextBounds(name, 0, 1, bounds);
        int x = (contactImage.getWidth() - bounds.width()) / 2;
        int y = (contactImage.getHeight() + bounds.height()) / 2;

        canvas.drawText(name.substring(0, 1).toUpperCase(), x, y, textPaint);

        return contactImage;
    }

}