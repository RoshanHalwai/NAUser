package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.invitevisitors;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.R;

import java.util.Calendar;

public class InvitingVisitors extends BaseActivity {

    final int RESULT_PICK_CONTACT = 1;
    TextView textVisitorName;
    TextView textVisitorMobile;
    TextView textOr;
    TextView textDateTime;
    TextView textCalendar;
    TextView textDescription;
    EditText editVisitorName;
    EditText editVisitorMobile;
    public int mYear, mMonth, mDay, mHour, mMinute;
    String concatenatedString = "";

    String selectedDate = "";
    String selectedTime = "";

    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;

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

        /*Getting Id's for all the views*/
        textVisitorName = findViewById(R.id.textVisitorName);
        textVisitorMobile = findViewById(R.id.textInvitorMobile);
        textOr = findViewById(R.id.textOr);
        textDateTime = findViewById(R.id.textDateTime);
        textCalendar = findViewById(R.id.textCalendar);
        textDescription = findViewById(R.id.textDescription);
        editVisitorName = findViewById(R.id.editVisitorName);
        editVisitorMobile = findViewById(R.id.editMobileNumber);
        Button buttonSelectFromContact = findViewById(R.id.buttonSelectFromContact);
        Button buttonInvite = findViewById(R.id.buttonInvite);

        /*Setting font for all the views*/
        textVisitorName.setTypeface(Constants.setLatoBoldFont(this));
        textVisitorMobile.setTypeface(Constants.setLatoBoldFont(this));
        textOr.setTypeface(Constants.setLatoBoldFont(this));
        textDateTime.setTypeface(Constants.setLatoBoldFont(this));
        textCalendar.setTypeface(Constants.setLatoBoldFont(this));
        textDescription.setTypeface(Constants.setLatoBoldFont(this));
        editVisitorName.setTypeface(Constants.setLatoRegularFont(this));
        editVisitorMobile.setTypeface(Constants.setLatoRegularFont(this));
        buttonSelectFromContact.setTypeface(Constants.setLatoLightFont(this));
        buttonInvite.setTypeface(Constants.setLatoLightFont(this));

        /*Setting event for Select From Contacts button*/
        buttonSelectFromContact.setOnClickListener(view -> {
            Intent i = new Intent(Intent.ACTION_PICK);
            i.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
            startActivityForResult(i, RESULT_PICK_CONTACT);
        });
        /*Setting event for  Displaying Date & Time*/
        textCalendar.setOnClickListener(View ->
        {
            final Calendar mycalendar = Calendar.getInstance();

            mYear = mycalendar.get(Calendar.YEAR);
            mMonth = mycalendar.get(Calendar.MONTH);
            mDay = mycalendar.get(Calendar.DAY_OF_MONTH);

            mHour = mycalendar.get(Calendar.HOUR_OF_DAY);
            mMinute = mycalendar.get(Calendar.MINUTE);

            // date picker
            datePickerDialog = new DatePickerDialog(this,
                    (view, year, month, dayOfMonth) -> {
                        String[] selectedMonth = {getString(R.string.Jan), getString(R.string.Feb), getString(R.string.Mar), getString(R.string.Apr), getString(R.string.May), getString(R.string.Jun), getString(R.string.Jul), getString(R.string.Aug), getString(R.string.Sep), getString(R.string.Oct), getString(R.string.Nov), getString(R.string.Dec)};
                        int selectedYear = year % 100;
                        selectedDate = "";
                        selectedDate = selectedDate + selectedMonth[month] + " " + dayOfMonth + " " + selectedYear;
                        datePickerDialog.cancel();
                        timePickerDialog.show();
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();

            // Launch Time Picker Dialog
            timePickerDialog = new TimePickerDialog(this,
                    (view, hourOfDay, minute) -> {
                        selectedTime = "";
                        if (minute < 10 && hourOfDay < 10) {
                            selectedTime = "0" + hourOfDay + ":0" + minute;
                        } else if (minute < 10 && hourOfDay > 10) {
                            selectedTime = hourOfDay + ":0" + minute;
                        } else if (minute > 10 && hourOfDay < 10) {
                            selectedTime = "0" + hourOfDay + ":" + minute;
                        } else {
                            selectedTime = hourOfDay + ":" + minute;
                        }
                        timePickerDialog.cancel();
                        concatenatedString = selectedDate + "," + " " + selectedTime;
                        textCalendar.setText(concatenatedString);
                    }, mHour, mMinute, true);
        });

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
                                editVisitorName.setText(name);
                                editVisitorMobile.setText(formattedPhoneNumber);
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
}

