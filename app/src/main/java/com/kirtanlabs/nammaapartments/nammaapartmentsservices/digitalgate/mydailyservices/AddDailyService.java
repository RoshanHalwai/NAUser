package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.mydailyservices;


import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.R;

import java.util.Calendar;
import java.util.Locale;

public class AddDailyService extends BaseActivity {

    private final int RESULT_PICK_CONTACT = 1;
    private TextView textDescription;
    private EditText editPickTime;
    private EditText editName;
    private EditText editMobile;

    private Button buttonAdd;
    private String selectedTime;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_my_service;
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

        /*We don't want the keyboard to be displayed when user clicks on the pick date and time edit field*/
        editPickTime.setInputType(InputType.TYPE_NULL);

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

        /*Setting event for Select From Contacts button*/
        buttonSelectFromContact.setOnClickListener(view -> {
            Intent i = new Intent(Intent.ACTION_PICK);
            i.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
            startActivityForResult(i, RESULT_PICK_CONTACT);
        });

        /*Setting event for  Displaying Date & Time*/
        editPickTime.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                displayTime();
            }
        });
        editPickTime.setOnClickListener(View -> displayTime());

        /*Getting type of service*/
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String service_type;
        if (bundle != null) {
            service_type = bundle.getString(Constants.SERVICE_TYPE);
            String description = getText(R.string.we_will_send_an_otp_to_your_visitor_allowing_them_to_enter_into_your_society).toString();
            assert service_type != null;
            description = description.replace("visitor", service_type);
            textDescription.setText(description);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
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
                                if(formattedPhoneNumber.startsWith("91") && formattedPhoneNumber.length()>10) {
                                    formattedPhoneNumber = formattedPhoneNumber.substring(2,12);
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
            }
        }
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
