package com.kirtanlabs.nammaapartments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import static com.kirtanlabs.nammaapartments.Constants.CAMERA_PERMISSION_REQUEST_CODE;
import static com.kirtanlabs.nammaapartments.Constants.GALLERY_PERMISSION_REQUEST_CODE;
import static com.kirtanlabs.nammaapartments.Constants.PLACE_CALL_PERMISSION_REQUEST_CODE;
import static com.kirtanlabs.nammaapartments.Constants.READ_CONTACTS_PERMISSION_REQUEST_CODE;
import static com.kirtanlabs.nammaapartments.Constants.SEND_SMS_PERMISSION_REQUEST_CODE;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 5/1/2018
 */

/**
 * Root activity for most of the Activities of this project.
 * Responsible for creating toolbar by getting title from the activity
 * and implementing events on back button.
 */
public abstract class BaseActivity extends AppCompatActivity {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private ImageView infoButton;
    private ImageView backButton;
    private Intent callIntent, msgIntent, readContactsIntent, cameraIntent, galleryIntent;

    /* ------------------------------------------------------------- *
     * Abstract Methods
     * ------------------------------------------------------------- */

    protected abstract int getLayoutResourceId();

    protected abstract int getActivityTitle();

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    private void setActivityTitle(int resourceId) {
        TextView activityTitle = findViewById(R.id.textActivityTitle);
        activityTitle.setTypeface(Constants.setLatoRegularFont(this));
        activityTitle.setText(resourceId);
    }

    private void setBackButtonListener() {
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> onBackPressed());
    }

    private void hideInfoButton() {
        infoButton.setVisibility(View.GONE);
    }

    private void showBackButton() {
        backButton.setVisibility(View.VISIBLE);
    }

    /* ------------------------------------------------------------- *
     * Overriding AppCompatActivity Methods
     * ------------------------------------------------------------- */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
        backButton = findViewById(R.id.backButton);
        infoButton = findViewById(R.id.infoButton);
        hideInfoButton();
        showBackButton();
        setActivityTitle(getActivityTitle());
        setBackButtonListener();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PLACE_CALL_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    startActivity(callIntent);
                }
                break;
            case SEND_SMS_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    startActivity(msgIntent);
                }
                break;
            case READ_CONTACTS_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    startActivityForResult(readContactsIntent, READ_CONTACTS_PERMISSION_REQUEST_CODE);
                }
                break;
            case CAMERA_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    startActivityForResult(cameraIntent, CAMERA_PERMISSION_REQUEST_CODE);
                }
                break;
            case GALLERY_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    startActivityForResult(galleryIntent, GALLERY_PERMISSION_REQUEST_CODE);
                }
                break;
        }
    }

    /* ------------------------------------------------------------- *
     * Protected Methods
     * ------------------------------------------------------------- */

    protected void hideBackButton() {
        backButton.setVisibility(View.INVISIBLE);
    }

    protected void showInfoButton() {
        infoButton.setVisibility(View.VISIBLE);
    }

    /**
     * We check if permissions are granted to Read Contacts if granted then we directly start Read Contacts Activity with Result
     * else we show Request permission dialog to allow users to give access.
     */
    protected void showUserContacts() {
        readContactsIntent = new Intent(Intent.ACTION_PICK);
        readContactsIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, READ_CONTACTS_PERMISSION_REQUEST_CODE);
        else {
            startActivityForResult(readContactsIntent, READ_CONTACTS_PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * We check if permissions are granted to take photos using camera if already granted then we directly start Camera Activity with Result
     * else we show Request permission dialog to allow users to give access.
     */
    protected void launchCamera() {
        cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        else {
            startActivityForResult(cameraIntent, CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * We check if permissions are granted to Pick Image from Gallery if already granted then we directly start Gallery Activity with Result
     * else we show Request permission dialog to allow users to give access.
     */
    protected void pickImageFromGallery() {
        galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_PERMISSION_REQUEST_CODE);
        else {
            startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), GALLERY_PERMISSION_REQUEST_CODE);
        }
    }

    /* ------------------------------------------------------------- *
     * Public Methods
     * ------------------------------------------------------------- */

    /**
     * Displays Feature Unavailable layout along with a message passed by the activity
     *
     * @param text feature unavailable message
     */
    public void showFeatureUnavailableLayout(int text) {
        LinearLayout featureUnavailableLayout = findViewById(R.id.layoutFeatureUnavailable);
        featureUnavailableLayout.setVisibility(View.VISIBLE);
        TextView textView = findViewById(R.id.textFeatureUnavailable);
        textView.setText(text);
    }

    /**
     * We check if permissions are granted to place calls if granted then we directly start Dialer Activity
     * else we show Request permission dialog to allow users to give access.
     *
     * @param MobileNumber - to which call needs to be placed
     */
    public void makePhoneCall(String MobileNumber) {
        callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + MobileNumber));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, PLACE_CALL_PERMISSION_REQUEST_CODE);
        } else {
            startActivity(callIntent);
        }
    }

    /**
     * We check if permissions are granted to send SMS if granted then we directly start SMS Activity
     * else we show Request permission dialog to allow users to give access.
     *
     * @param MobileNumber - to which message needs to be sent
     */
    public void sendTextMessage(String MobileNumber) {
        msgIntent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", MobileNumber, null));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQUEST_CODE);
        } else {
            startActivity(msgIntent);
        }
    }

}
