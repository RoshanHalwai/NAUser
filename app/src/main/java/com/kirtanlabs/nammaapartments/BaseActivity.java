package com.kirtanlabs.nammaapartments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartments.userpojo.NammaApartmentUser;
import com.kirtanlabs.nammaapartments.utilities.Constants;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.Calendar;
import java.util.Objects;
import java.util.regex.Pattern;

import pl.aprilapps.easyphotopicker.EasyImage;

import static com.kirtanlabs.nammaapartments.utilities.Constants.ALL_USERS_REFERENCE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.CAMERA_PERMISSION_REQUEST_CODE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.ENABLE_LOCATION_PERMISSION_CODE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_LATITUDE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_LONGITUDE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_OTHER_DETAILS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.GALLERY_PERMISSION_REQUEST_CODE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.PHONE_NUMBER_MAX_LENGTH;
import static com.kirtanlabs.nammaapartments.utilities.Constants.PLACE_CALL_PERMISSION_REQUEST_CODE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.PRIVATE_USERS_REFERENCE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.READ_CONTACTS_PERMISSION_REQUEST_CODE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.SEND_SMS_PERMISSION_REQUEST_CODE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoItalicFont;

/**
 * Root activity for most of the Activities of this project.
 * Responsible for creating toolbar by getting title from the activity
 * and implementing events on back button.
 */
public abstract class BaseActivity extends AppCompatActivity implements LocationListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private ImageView infoButton;
    private ImageView backButton;
    private Intent callIntent, msgIntent, readContactsIntent;
    private AVLoadingIndicatorView progressIndicator;
    private ProgressDialog progressDialog;

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

    private void setInfoButtonListener() {
        infoButton = findViewById(R.id.infoButton);
        infoButton.setOnClickListener(view -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.faq_url)))));
    }

    /* ------------------------------------------------------------- *
     * Overriding Location Listener Methods
     * ------------------------------------------------------------- */

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        NammaApartmentUser currentNammaApartmentUser = ((NammaApartmentsGlobal) getApplicationContext()).getNammaApartmentUser();
        /*Getting the reference till the flatDetails under users->private*/
        DatabaseReference usersFlatDetailsReference = PRIVATE_USERS_REFERENCE.child(currentNammaApartmentUser.getUID())
                .child(FIREBASE_CHILD_OTHER_DETAILS);
        /*Setting the value of Latitude and Longitude under flatDetails*/
        usersFlatDetailsReference.child(FIREBASE_CHILD_LATITUDE).setValue(latitude);
        usersFlatDetailsReference.child(FIREBASE_CHILD_LONGITUDE).setValue(longitude);
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

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
        setInfoButtonListener();
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
                    EasyImage.openCamera(this, 0);
                }
                break;
            case GALLERY_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    EasyImage.openGallery(this, 0);
                }
                break;
            case ENABLE_LOCATION_PERMISSION_CODE:
                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    getLocation();
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

    protected void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * We check if permissions are granted to Read Contacts if granted then we directly start Read Contacts Activity with Result
     * else we show Request permission dialog to allow users to give access.
     */
    protected void showUserContacts() {
        readContactsIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        else {
            EasyImage.openCamera(this, 0);
        }
    }

    /**
     * We check if permissions are granted to Pick Image from Gallery if already granted then or if API Level is lower than 15 we directly
     * start Gallery Activity with Result, else we show Request permission dialog to allow users to give access.
     */
    protected void pickImageFromGallery() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            EasyImage.openGallery(this, 0);
        else
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_PERMISSION_REQUEST_CODE);
    }

    /**
     * Displays Feature Unavailable layout along with a message passed by the activity
     *
     * @param text feature unavailable message
     */
    public void showFeatureUnavailableLayout(int text) {
        LinearLayout featureUnavailableLayout = findViewById(R.id.layoutFeatureUnavailable);
        featureUnavailableLayout.setVisibility(View.VISIBLE);
        TextView textView = findViewById(R.id.textFeatureUnavailable);
        textView.setTypeface(setLatoItalicFont(this));
        textView.setText(text);
    }

    /**
     * Displays Feature Unavailable Layout along with message passed by the activity.
     *
     * @param text contains message passed based on the context.
     */
    protected void showFeatureUnAvailableLayout(String text) {
        LinearLayout featureUnavailableLayout = findViewById(R.id.layoutFeatureUnavailable);
        featureUnavailableLayout.setVisibility(View.VISIBLE);
        TextView textView = findViewById(R.id.textFeatureUnavailable);
        textView.setTypeface(setLatoItalicFont(this));
        textView.setText(text);
    }

    /**
     * This Method is used to Hide Feature Unavailable layout from the activity whenever it is called
     */
    protected void hideFeatureUnavailableLayout() {
        LinearLayout featureUnavailableLayout = findViewById(R.id.layoutFeatureUnavailable);
        featureUnavailableLayout.setVisibility(View.GONE);
    }

    /**
     * This method checks if all the editTexts are filled or not.
     *
     * @param fields consists of array of EditTexts.
     * @return consists of boolean variable based on the context.
     */
    protected boolean isAllFieldsFilled(EditText[] fields) {
        for (EditText currentField : fields) {
            if (TextUtils.isEmpty(currentField.getText().toString())) {
                currentField.requestFocus();
                return false;
            }
        }
        return true;
    }

    /**
     * This method checks if user is entering proper phone number or not.
     *
     * @param phone consists of string value of mobile number of that particular activity.
     * @return returns a boolean variable based on the context.
     */
    protected boolean isValidPhone(String phone) {
        return !Pattern.matches("[a-zA-Z]+", phone) && phone.length() >= PHONE_NUMBER_MAX_LENGTH;
    }

    /**
     * This method gets invoked when user is trying to enter improper format of entering name.
     *
     * @param name contains that particular editText of name
     * @throws NumberFormatException because if user tries to enter number in place of name.
     */
    protected boolean isValidPersonName(String name) throws NumberFormatException {
        return !Pattern.matches("[a-zA-Z0-9.@() ]+", name);
    }

    /**
     * This method gets invoked when user is trying to enter improper format of entering email.
     *
     * @param email consists of string value of email of that particular activity.
     * @return returns a boolean  variable based on that context.
     */
    protected boolean isValidEmail(String email) {
        return !Pattern.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+", email);
    }

    protected void showProgressIndicator() {
        progressIndicator = findViewById(R.id.animationWaitingForCustomers);
        progressIndicator.smoothToShow();
    }

    protected void hideProgressIndicator() {
        progressIndicator.smoothToHide();
    }

    protected void showProgressDialog(Context context, String title, String message) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    protected void hideProgressDialog() {
        progressDialog.dismiss();
    }

    /**
     * This method is invoked to check whether the mobile number entered in particular editText is
     * Already existing User's Mobile Number or not
     *
     * @param mobileNumber - that needs to be checked in Users Mobile Number List
     * @param editTextID   - Id of the EditText
     * @param context      - of the calling Activity
     */
    protected void checkEnteredMobileNumberInUsersList(String mobileNumber, int editTextID, Context context) {
        EditText editTextMobileNumber = findViewById(editTextID);
        DatabaseReference familyMemberMobileNumberReference = ALL_USERS_REFERENCE.child(mobileNumber);
        familyMemberMobileNumberReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    editTextMobileNumber.requestFocus();
                    Drawable drawableWrap = DrawableCompat.wrap(
                            Objects.requireNonNull(ContextCompat.getDrawable(
                                    context,
                                    android.R.drawable.stat_sys_warning)
                            )
                    );
                    drawableWrap.mutate();
                    DrawableCompat.setTint(drawableWrap, ContextCompat.getColor(context, R.color.nmBlack));
                    drawableWrap.setBounds(0, 0, drawableWrap.getIntrinsicWidth(), drawableWrap.getIntrinsicHeight());
                    editTextMobileNumber.setError(getString(R.string.mobile_number_exists), drawableWrap);
                    editTextMobileNumber.setSelection(editTextMobileNumber.length());
                } else {
                    editTextMobileNumber.setError(null);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    /* ------------------------------------------------------------- *
     * Public Methods
     * ------------------------------------------------------------- */

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
     * We check if permissions are granted to access location of the user, if granted user's loatitude and longitude can be fetched
     * else we show Request permission dialog to allow users to give access.
     */
    protected void enableLocationService() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ENABLE_LOCATION_PERMISSION_CODE);
        } else {
            getLocation();
        }
    }

    /**
     * We are using the Location Manager class to get the latitude and longitude coordinates of the user
     */
    private void getLocation() {
        try {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Objects.requireNonNull(locationManager).requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    /**
     * We check if permissions are granted to send SMS if granted then we directly start SMS Activity
     * else we show Request permission dialog to allow users to give access.
     *
     * @param MobileNumber - to which message needs to be sent
     */
    public void sendTextMessage(String MobileNumber) {
        sendTextMessage(MobileNumber, "");
    }

    /**
     * We check if permissions are granted to send SMS if granted then we directly start SMS Activity
     * else we show Request permission dialog to allow users to give access.
     *
     * @param MobileNumber - to which message needs to be sent
     * @param message      - This is the message that would be displayed in the SMS application body
     */
    public void sendTextMessage(String MobileNumber, String message) {
        msgIntent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", MobileNumber, null));
        msgIntent.putExtra(getApplicationContext().getString(R.string.message_name), message);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQUEST_CODE);
        } else {
            startActivity(msgIntent);
        }
    }

    /**
     * Shows Date picker dialog allowing users to select date. The selected date is
     * displayed in the view which calls this activity
     *
     * @param context           of the calling activity
     * @param onDateSetListener callback for the event
     */
    public final void pickDate(Context context, DatePickerDialog.OnDateSetListener onDateSetListener) {
        final Calendar c = Calendar.getInstance();
        int myYear = c.get(Calendar.YEAR);
        int myMonth = (c.get(Calendar.MONTH));
        int myDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, onDateSetListener, myYear, myMonth, myDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
        datePickerDialog.setCanceledOnTouchOutside(false);
    }

    /**
     * Shows Time picker dialog allowing users to select time. The selected time is then
     * displayed in the view which calls this activity
     *
     * @param context           of the calling activity
     * @param onTimeSetListener callback for the event
     */
    public final void pickTime(Context context, TimePickerDialog.OnTimeSetListener onTimeSetListener) {
        final Calendar calendarTime = Calendar.getInstance();
        int mHour = calendarTime.get(Calendar.HOUR_OF_DAY);
        int mMinute = calendarTime.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(context, onTimeSetListener, mHour, mMinute, true);
        timePickerDialog.setCanceledOnTouchOutside(false);
        timePickerDialog.show();
    }

    /**
     * Shows message box with title, message and method to be executed when user
     * clicks on Ok button
     *
     * @param title   - Title of the message
     * @param message - Body of the message
     * @param method  - Method to execute after click of OK button
     */
    public void showConfirmDialog(String title, String message, Runnable method) {
        AlertDialog.Builder alertNotifyGateDialog = new AlertDialog.Builder(this);
        alertNotifyGateDialog.setCancelable(false);
        alertNotifyGateDialog.setTitle(title);
        alertNotifyGateDialog.setMessage(message);
        alertNotifyGateDialog.setPositiveButton("YES", (dialog, which) -> {
            dialog.cancel();
            method.run();
        });
        alertNotifyGateDialog.setNegativeButton("NO", (dialog, which) -> dialog.cancel());
        new Dialog(this);
        alertNotifyGateDialog.show();
    }

    /**
     * Shows message box with title, message and activity to be called when user
     * clicks on Ok button
     *
     * @param title   - Title of the message
     * @param message - Body of the message
     * @param intent  - If null then on click of Ok, the dialog will disappear
     *                else intent activity will be called
     */
    public void showNotificationDialog(String title, String message, Intent intent) {
        AlertDialog.Builder alertNotifyGateDialog = new AlertDialog.Builder(this);
        alertNotifyGateDialog.setCancelable(false);
        alertNotifyGateDialog.setTitle(title);
        alertNotifyGateDialog.setMessage(message);
        if (intent == null) {
            alertNotifyGateDialog.setPositiveButton("OK", (dialog, which) -> dialog.cancel());
        } else {
            alertNotifyGateDialog.setPositiveButton("OK", (dialog, which) -> startActivity(intent));
        }

        new Dialog(this);
        alertNotifyGateDialog.show();
    }

}
