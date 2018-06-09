package com.kirtanlabs.nammaapartments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.mydailyservices.DailyServicesHomeAdapter;
import com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.mysweethome.MySweetHomeAdapter;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.Calendar;
import java.util.regex.Pattern;

import static com.kirtanlabs.nammaapartments.Constants.CAMERA_PERMISSION_REQUEST_CODE;
import static com.kirtanlabs.nammaapartments.Constants.GALLERY_PERMISSION_REQUEST_CODE;
import static com.kirtanlabs.nammaapartments.Constants.PHONE_NUMBER_MAX_LENGTH;
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

    private View cancelDialog;
    private AlertDialog dialog;
    private ImageView infoButton;
    private ImageView backButton;
    private Intent callIntent, msgIntent, readContactsIntent, cameraIntent, galleryIntent;
    private AVLoadingIndicatorView progressIndicator;
    private int screenTitle;

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
     * We check if permissions are granted to Pick Image from Gallery if already granted then or if API Level is lower than 15 we directly
     * start Gallery Activity with Result, else we show Request permission dialog to allow users to give access.
     */
    protected void pickImageFromGallery() {
        galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), GALLERY_PERMISSION_REQUEST_CODE);
        else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_PERMISSION_REQUEST_CODE);
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
        timePickerDialog.show();
    }

    /**
     * This method is invoked when user clicks on cancel or remove icon.
     */
    public void openCancelDialog(int screenTitle) {
        this.screenTitle = screenTitle;
        cancelDialog = View.inflate(this, R.layout.layout_dialog_cancel, null);

        /*Getting Id's for all the views*/
        TextView textCancelDescription = cancelDialog.findViewById(R.id.textCancelDescription);

        /*Setting Fonts for all the views*/
        textCancelDescription.setTypeface(Constants.setLatoRegularFont(this));

        /*This method is used to create cancel dialog */
        createCancelDialog();
    }

    /**
     * This method checks if all the editTexts are filled or not.
     *
     * @param fields consists of array of EditTexts.
     * @return consists of boolean variable based on the context.
     */
    public boolean isAllFieldsFilled(EditText[] fields) {
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
    public boolean isValidPhone(String phone) {
        boolean check;
        check = !Pattern.matches("[a-zA-Z]+", phone) && phone.length() >= PHONE_NUMBER_MAX_LENGTH;
        return check;
    }

    /**
     * This method gets invoked when user is trying to enter improper format of entering name.
     *
     * @param name contains that particular editText of name
     * @throws NumberFormatException because if user tries to enter number in place of name.
     */
    public boolean isValidPersonName(String name) throws NumberFormatException {
        boolean check;
        check = !Pattern.matches("[a-zA-Z ]+", name);
        return check;
    }

    /**
     * This method is invoked to create an NotifyGate dialog when user successfully fills all the details.
     */
    public void createNotifyGateDialog() {
        AlertDialog.Builder alertNotifyGateDialog = new AlertDialog.Builder(this);
        alertNotifyGateDialog.setMessage(R.string.notification_message);
        alertNotifyGateDialog.setTitle("Notification Message");
        alertNotifyGateDialog.setPositiveButton("Ok", (dialog, which) -> dialog.cancel());

        new Dialog(this);
        alertNotifyGateDialog.show();
    }

    public void showProgressIndicator() {
        progressIndicator = findViewById(R.id.animationWaitingForCustomers);
        progressIndicator.smoothToShow();
    }

    public void hideProgressIndicator() {
        progressIndicator.smoothToHide();
    }

    /**
     * This method is invoked to create a cancel dialog.
     */
    private void createCancelDialog() {
        AlertDialog.Builder alertCancelDialog = new AlertDialog.Builder(this);
        alertCancelDialog.setTitle("Delete");
        alertCancelDialog.setPositiveButton("Yes", (dialog, which) -> deleteListData());
        alertCancelDialog.setNegativeButton("No", (dialog, which) -> dialog.cancel());
        alertCancelDialog.setView(cancelDialog);
        dialog = alertCancelDialog.create();

        new Dialog(this);
        dialog.show();
    }

    /**
     * This method is invoked to delete visitor or daily services or family member data.
     */
    private void deleteListData() {
        /*Decrementing the count variable on deletion of one visitor or daily service or family member data.*/
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        switch (screenTitle) {
            case R.string.my_daily_services:
                DailyServicesHomeAdapter adapterDailyServices = new DailyServicesHomeAdapter(this);
                recyclerView.setAdapter(adapterDailyServices);
                --DailyServicesHomeAdapter.count;
                adapterDailyServices.notifyDataSetChanged();
                break;
            case R.string.my_sweet_home:
                MySweetHomeAdapter adapterMySweetHome = new MySweetHomeAdapter(this);
                recyclerView.setAdapter(adapterMySweetHome);
                --MySweetHomeAdapter.count;
                adapterMySweetHome.notifyDataSetChanged();
                break;
        }
        dialog.cancel();
    }
}
