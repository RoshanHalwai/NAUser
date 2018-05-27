package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.invitevisitors;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.R;

import java.io.IOException;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.kirtanlabs.nammaapartments.Constants.CAMERA_PERMISSION_REQUEST_CODE;
import static com.kirtanlabs.nammaapartments.Constants.GALLERY_PERMISSION_REQUEST_CODE;
import static com.kirtanlabs.nammaapartments.Constants.READ_CONTACTS_PERMISSION_REQUEST_CODE;

public class InvitingVisitors extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private CircleImageView circleImageInvitingVisitors;
    private EditText editPickDateTime;
    private EditText editVisitorName;
    private EditText editVisitorMobile;
    private TextView textDescription;
    private Button buttonInvite;
    private AlertDialog imageSelectingOptions;
    private ListView listView;
    private String selectedDate;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_inviting_visitors;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.inviting_visitors;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*We need Info Button in this screen*/
        showInfoButton();

        /*Custom DialogBox with list of all image services*/
        setupCustomDialog();

        /*Getting Id's for all the views*/
        circleImageInvitingVisitors = findViewById(R.id.invitingVisitorsProfilePic);
        TextView textVisitorName = findViewById(R.id.textVisitorAndServiceName);
        TextView textVisitorMobile = findViewById(R.id.textInvitorMobile);
        TextView textOr = findViewById(R.id.textOr);
        TextView textDateTime = findViewById(R.id.textDateTime);
        TextView textCountryCode = findViewById(R.id.textCountryCode);
        editPickDateTime = findViewById(R.id.editPickDateTime);
        textDescription = findViewById(R.id.textDescription);
        editVisitorName = findViewById(R.id.editVisitorName);
        editVisitorMobile = findViewById(R.id.editMobileNumber);
        Button buttonSelectFromContact = findViewById(R.id.buttonSelectFromContact);
        buttonInvite = findViewById(R.id.buttonInvite);

        /*We don't want the keyboard to be displayed when user clicks on the pick date and time edit field*/
        editPickDateTime.setInputType(InputType.TYPE_NULL);

        /*Setting font for all the views*/
        textVisitorName.setTypeface(Constants.setLatoBoldFont(this));
        textVisitorMobile.setTypeface(Constants.setLatoBoldFont(this));
        textOr.setTypeface(Constants.setLatoBoldFont(this));
        textDateTime.setTypeface(Constants.setLatoBoldFont(this));
        textCountryCode.setTypeface(Constants.setLatoItalicFont(this));
        editPickDateTime.setTypeface(Constants.setLatoRegularFont(this));
        textDescription.setTypeface(Constants.setLatoBoldFont(this));
        editVisitorName.setTypeface(Constants.setLatoRegularFont(this));
        editVisitorMobile.setTypeface(Constants.setLatoRegularFont(this));
        buttonSelectFromContact.setTypeface(Constants.setLatoLightFont(this));
        buttonInvite.setTypeface(Constants.setLatoLightFont(this));

        /*Setting event for views */
        circleImageInvitingVisitors.setOnClickListener(this);
        buttonSelectFromContact.setOnClickListener(this);
        editPickDateTime.setOnClickListener(this);
        editPickDateTime.setOnFocusChangeListener(this);

        /*This method gets invoked when user is trying to capture their profile photo either by clicking on camera and gallery.*/
        setupViewsForProfilePhoto();
        
    }

    /* ------------------------------------------------------------- *
     * Overriding onActivityResult
     * ------------------------------------------------------------- */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case READ_CONTACTS_PERMISSION_REQUEST_CODE:
                    Cursor cursor;
                    try {
                        Uri uri = data.getData();
                        if (uri != null) {
                            cursor = getContentResolver().query(uri, null, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                                int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                                String phoneNo = cursor.getString(phoneIndex);
                                String name = cursor.getString(nameIndex);
                                String formattedPhoneNumber = phoneNo.replaceAll("\\D+", "");
                                if (formattedPhoneNumber.startsWith("91") && formattedPhoneNumber.length() > 10) {
                                    formattedPhoneNumber = formattedPhoneNumber.substring(2, 12);
                                }
                                editVisitorName.setText(name);
                                editVisitorMobile.setText(formattedPhoneNumber);
                                cursor.close();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case CAMERA_PERMISSION_REQUEST_CODE:
                    if (data.getExtras() != null) {
                        Bitmap bitmapProfilePic = (Bitmap) data.getExtras().get("data");
                        circleImageInvitingVisitors.setImageBitmap(bitmapProfilePic);
                        onSuccessfulUpload();
                        imageSelectingOptions.cancel();
                    } else {
                        onFailedUpload();
                        imageSelectingOptions.cancel();
                    }
                    break;

                case GALLERY_PERMISSION_REQUEST_CODE:
                    if (data != null && data.getData() != null) {
                        Uri selectedImage = data.getData();
                        try {
                            Bitmap bitmapProfilePic = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                            circleImageInvitingVisitors.setImageBitmap(bitmapProfilePic);
                            onSuccessfulUpload();
                            imageSelectingOptions.cancel();
                        } catch (IOException exception) {
                            exception.getStackTrace();
                        }
                    } else {
                        onFailedUpload();
                        imageSelectingOptions.cancel();
                    }
                    break;
            }
        }
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClick and OnFocusChange Listeners
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.invitingVisitorsProfilePic:
                imageSelectingOptions.show();
                break;
            case R.id.buttonSelectFromContact:
                showUserContacts();
                break;
            case R.id.editPickDateTime:
                pickDate(this, this);
                break;
        }

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            pickDate(this, this);
        }
    }

    /* ------------------------------------------------------------- *
     * Overriding OnDateSet & OnTimeSet Listener
     * ------------------------------------------------------------- */

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (view.isShown()) {
            selectedDate = new DateFormatSymbols().getMonths()[month].substring(0, 3) + " " + dayOfMonth + ", " + year;
            pickTime(this, this);
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (view.isShown()) {
            String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
            String concatenatedDateAndTime = selectedDate + "\t\t" + " " + selectedTime;
            editPickDateTime.setText(concatenatedDateAndTime);
            textDescription.setVisibility(View.VISIBLE);
            buttonInvite.setVisibility(View.VISIBLE);
        }
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * Creates a custom dialog with a list view which contains the list of inbuilt apps such as Camera and Gallery. This
     * imageSelectingOptions is displayed when user clicks on profile image which is on top of the screen.
     */
    private void setupCustomDialog() {
        AlertDialog.Builder addImageDialog = new AlertDialog.Builder(this);
        View listAddImageServices = View.inflate(this, R.layout.list_add_image_services, null);
        addImageDialog.setView(listAddImageServices);
        imageSelectingOptions = addImageDialog.create();
        listView = listAddImageServices.findViewById(R.id.listAddImageService);
    }

    /**
     * This method gets invoked when user is trying to capture their profile photo either by clicking on camera and gallery.
     */
    private void setupViewsForProfilePhoto() {
        /*Creating an array list of selecting images from camera and gallery*/
        ArrayList<String> pickImageList = new ArrayList<>();

        /*Adding pick images services to the list*/
        pickImageList.add(getString(R.string.camera));
        pickImageList.add(getString(R.string.gallery));
        pickImageList.add(getString(R.string.cancel));

        /*Creating the Adapter*/
        ArrayAdapter<String> adapter = new ArrayAdapter<>(InvitingVisitors.this, android.R.layout.simple_list_item_1, pickImageList);

        /*Attaching adapter to the listView*/
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        /*Setting event for listview items*/
        listView.setOnItemClickListener((parent, view, position, id) -> {
            switch (position) {
                case 0:
                    launchCamera();
                    break;
                case 1:
                    pickImageFromGallery();
                    break;
                case 2:
                    imageSelectingOptions.cancel();
            }
        });
    }

    /**
     * This method will get invoked when user successfully uploaded image from gallery and camera.
     */
    private void onSuccessfulUpload() {
        circleImageInvitingVisitors.setVisibility(View.VISIBLE);
    }

    /**
     * This method will get invoked when user fails in uploading image from gallery and camera.
     */
    private void onFailedUpload() {
        circleImageInvitingVisitors.setVisibility(View.INVISIBLE);
        imageSelectingOptions.cancel();
    }
}

