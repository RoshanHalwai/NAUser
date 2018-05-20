package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.mydailyservices;

import android.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.onboarding.login.OTP;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.kirtanlabs.nammaapartments.Constants.CAMERA_PERMISSION_REQUEST_CODE;
import static com.kirtanlabs.nammaapartments.Constants.GALLERY_PERMISSION_REQUEST_CODE;
import static com.kirtanlabs.nammaapartments.Constants.READ_CONTACTS_PERMISSION_REQUEST_CODE;

public class AddDailyService extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private TextView textDescription;
    private EditText editPickTime;
    private EditText editName;
    private EditText editMobile;
    private Button buttonAdd;
    private CircleImageView circleImageView;
    private String selectedTime;
    private String service_type;
    private AlertDialog dialog;
    private ListView listView;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_add_daily_service;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.add_my_service;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*We need Info Button in this screen*/
        showInfoButton();

        /*Custom DialogBox with list of all image services*/
        AlertDialog.Builder addImageDialog = new AlertDialog.Builder(this);
        View listAddImageServices = View.inflate(this, R.layout.list_add_image_services, null);
        addImageDialog.setView(listAddImageServices);
        dialog = addImageDialog.create();

        /*Getting Id's for all the views*/
        TextView textName = findViewById(R.id.textName);
        TextView textMobile = findViewById(R.id.textMobile);
        TextView textOr = findViewById(R.id.textOr);
        TextView textTime = findViewById(R.id.textTime);
        TextView textCountryCode = findViewById(R.id.textCountryCode);
        textDescription = findViewById(R.id.textDescription);
        editName = findViewById(R.id.editName);
        editMobile = findViewById(R.id.editMobileNumber);
        editPickTime = findViewById(R.id.editPickTime);
        Button buttonSelectFromContact = findViewById(R.id.buttonSelectFromContact);
        buttonAdd = findViewById(R.id.buttonAdd);
        circleImageView = findViewById(R.id.profileImage);
        listView = listAddImageServices.findViewById(R.id.listAddImageService);

        /*Setting font for all the views*/
        textName.setTypeface(Constants.setLatoBoldFont(this));
        textMobile.setTypeface(Constants.setLatoBoldFont(this));
        textOr.setTypeface(Constants.setLatoBoldFont(this));
        textTime.setTypeface(Constants.setLatoBoldFont(this));
        textCountryCode.setTypeface(Constants.setLatoItalicFont(this));
        editPickTime.setTypeface(Constants.setLatoRegularFont(this));
        textDescription.setTypeface(Constants.setLatoBoldFont(this));
        editName.setTypeface(Constants.setLatoRegularFont(this));
        editMobile.setTypeface(Constants.setLatoRegularFont(this));
        buttonSelectFromContact.setTypeface(Constants.setLatoLightFont(this));
        buttonAdd.setTypeface(Constants.setLatoLightFont(this));

        /*We don't want the keyboard to be displayed when user clicks on the pick date and time edit field*/
        editPickTime.setInputType(InputType.TYPE_NULL);

        /*Setting events for views*/
        circleImageView.setOnClickListener(this);
        editPickTime.setOnFocusChangeListener(this);
        editPickTime.setOnClickListener(this);
        buttonAdd.setOnClickListener(this);
        buttonSelectFromContact.setOnClickListener(this);

        updateOTPDescription();
        setupLayoutForProfilePhoto();
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClickListener Objects
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profileImage:
                dialog.show();
                break;
            case R.id.editPickTime:
                displayTime();
                break;
            case R.id.buttonAdd:
                Intent intentButtonAdd = new Intent(AddDailyService.this, OTP.class);
                intentButtonAdd.putExtra(Constants.SCREEN_TITLE, R.string.add_my_service);
                intentButtonAdd.putExtra(Constants.SERVICE_TYPE, service_type);
                startActivity(intentButtonAdd);
                break;
            case R.id.buttonSelectFromContact:
                showUserContacts();
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            displayTime();
        }
    }


    /* ------------------------------------------------------------- *
     * Overriding onActivityResult method
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
                                editName.setText(name);
                                editMobile.setText(formattedPhoneNumber);
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
                        circleImageView.setImageBitmap(bitmapProfilePic);
                        onSuccessfulUpload();
                        dialog.cancel();
                    } else {
                        onFailedUpload();
                        dialog.cancel();
                    }
                    break;

                case GALLERY_PERMISSION_REQUEST_CODE:
                    if (data != null && data.getData() != null) {
                        Uri selectedImage = data.getData();
                        try {
                            Bitmap bitmapProfilePic = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                            circleImageView.setImageBitmap(bitmapProfilePic);
                            onSuccessfulUpload();
                            dialog.cancel();
                        } catch (IOException exception) {
                            exception.getStackTrace();
                        }
                    } else {
                        onFailedUpload();
                        dialog.cancel();
                    }
                    break;
            }
        }
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * We need to update OTP Message description based on Service for which user is entering the
     * details.
     */
    private void updateOTPDescription() {
        if (getIntent().getExtras() != null) {
            service_type = getIntent().getStringExtra(Constants.SERVICE_TYPE);
            String description = getResources().getString(R.string.send_otp_message).replace("visitor", service_type);
            textDescription.setText(description);
        }
    }

    /**
     * Allows users to pick image from Gallery or use camera to create one
     */
    private void setupLayoutForProfilePhoto() {
        /*Creating an array list of selecting images from camera and gallery*/
        ArrayList<String> pickImageList = new ArrayList<>();

        /*Adding pick images services to the list*/
        pickImageList.add(getString(R.string.camera));
        pickImageList.add(getString(R.string.gallery));
        pickImageList.add(getString(R.string.cancel));

        /*Creating the Adapter*/
        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddDailyService.this, android.R.layout.simple_list_item_1, pickImageList);

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
                    dialog.cancel();
                    break;
            }
        });

    }


    /*This method will get invoked when user successfully uploaded pic from gallery and camera*/
    private void onSuccessfulUpload() {
        circleImageView.setVisibility(View.VISIBLE);
    }

    /*This method will get invoked when user fails in uploading image from gallery and camera*/
    private void onFailedUpload() {
        circleImageView.setVisibility(View.INVISIBLE);
        dialog.cancel();
    }

    /**
     * This method is invoked when user clicks on pick time icon.
     */
    private void displayTime() {
        Calendar calendar = Calendar.getInstance();
        int mHour = calendar.get(Calendar.HOUR_OF_DAY);
        int mMinute = calendar.get(Calendar.MINUTE);

        // Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    selectedTime = "";
                    selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                    editPickTime.setText(selectedTime);
                    textDescription.setVisibility(View.VISIBLE);
                    buttonAdd.setVisibility(View.VISIBLE);
                }, mHour, mMinute, true);
        timePickerDialog.show();
    }

}
