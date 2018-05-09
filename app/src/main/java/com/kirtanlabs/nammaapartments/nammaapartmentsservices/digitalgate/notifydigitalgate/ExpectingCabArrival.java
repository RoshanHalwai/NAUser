package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.notifydigitalgate;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.R;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Locale;

public class ExpectingCabArrival extends BaseActivity {

    /*Declaring all variables in this activity*/
    TextView textCabNumber, textDateTime, textValidFor;
    EditText editCabNumber, editDateTime;
    Button button1hr, button2hr, button4hr, button6hr, button8hr, button12hr, button16hr, button24hr, buttonNotifyGate;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    String concatenatedDateAndTime;
    String selectedDate;
    String selectedTime;

    /*An integer array for storing button id's*/
    int[] buttonIds = new int[]{R.id.button1Hr,
            R.id.button2Hr,
            R.id.button4Hr,
            R.id.button6Hr,
            R.id.button8Hr,
            R.id.button12Hr,
            R.id.button16Hr,
            R.id.button24Hr};

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_expecting_cab_arrival;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.expecting_cab_arrival;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*We need Info Button in this screen*/
        showInfoButton();

        /*Getting Id's for all the views*/
        textCabNumber = findViewById(R.id.textCabNumber);
        textDateTime = findViewById(R.id.textDateTime);
        textValidFor = findViewById(R.id.textValidFor);
        editCabNumber = findViewById(R.id.editCabNumber);
        editDateTime = findViewById(R.id.editDateTime);
        button1hr = findViewById(R.id.button1Hr);
        button2hr = findViewById(R.id.button2Hr);
        button4hr = findViewById(R.id.button4Hr);
        button6hr = findViewById(R.id.button6Hr);
        button8hr = findViewById(R.id.button8Hr);
        button12hr = findViewById(R.id.button12Hr);
        button16hr = findViewById(R.id.button16Hr);
        button24hr = findViewById(R.id.button24Hr);
        buttonNotifyGate = findViewById(R.id.buttonNotifyGate);

        /*Setting font for all the views*/
        textCabNumber.setTypeface(Constants.setLatoBoldFont(this));
        textDateTime.setTypeface(Constants.setLatoBoldFont(this));
        textValidFor.setTypeface(Constants.setLatoBoldFont(this));
        editCabNumber.setTypeface(Constants.setLatoRegularFont(this));
        editDateTime.setTypeface(Constants.setLatoRegularFont(this));
        buttonNotifyGate.setTypeface(Constants.setLatoLightFont(this));
        button1hr.setTypeface(Constants.setLatoRegularFont(this));
        button2hr.setTypeface(Constants.setLatoRegularFont(this));
        button4hr.setTypeface(Constants.setLatoRegularFont(this));
        button6hr.setTypeface(Constants.setLatoRegularFont(this));
        button8hr.setTypeface(Constants.setLatoRegularFont(this));
        button12hr.setTypeface(Constants.setLatoRegularFont(this));
        button16hr.setTypeface(Constants.setLatoRegularFont(this));
        button24hr.setTypeface(Constants.setLatoRegularFont(this));

        /*Setting event for Valid period Buttons*/
        button1hr.setOnClickListener(v -> selectButton(R.id.button1Hr));

        button2hr.setOnClickListener(v -> selectButton(R.id.button2Hr));

        button4hr.setOnClickListener(v -> selectButton(R.id.button4Hr));

        button6hr.setOnClickListener(v -> selectButton(R.id.button6Hr));

        button8hr.setOnClickListener(v -> selectButton(R.id.button8Hr));

        button12hr.setOnClickListener(v -> selectButton(R.id.button12Hr));

        button16hr.setOnClickListener(v -> selectButton(R.id.button16Hr));

        button24hr.setOnClickListener(v -> selectButton(R.id.button24Hr));

        /*Setting event for  Displaying Date & Time*/
        editDateTime.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                displayDateAndTime();
            }
        });
        editDateTime.setOnClickListener(v -> displayDateAndTime());
    }

    /*Method for ValidFor 8 Button clicks*/
    private void selectButton(int id) {
        for (int buttonId : buttonIds) {
            Button button = findViewById(buttonId);
            if (buttonId == id) {
                button.setBackgroundResource(R.drawable.selected_button_design);
            } else {
                button.setBackgroundResource(R.drawable.valid_for_button_design);
            }
        }
    }

    /**
     * This method is invoked when user clicks on pick date and time icon.
     */
    private void displayDateAndTime() {
        Calendar calendar = Calendar.getInstance();
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        int mHour = calendar.get(Calendar.HOUR_OF_DAY);
        int mMinute = calendar.get(Calendar.MINUTE);

        // Date Picker Dialog
        datePickerDialog = new DatePickerDialog(this,
                (DatePicker view, int year, int month, int dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    selectedDate = "";
                    selectedDate = new DateFormatSymbols().getMonths()[month].substring(0, 3) + " " + dayOfMonth + ", " + year;
                    datePickerDialog.cancel();
                    timePickerDialog.show();
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();

        // Time Picker Dialog
        timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    selectedTime = "";
                    concatenatedDateAndTime = "";
                    selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                    timePickerDialog.cancel();
                    concatenatedDateAndTime = selectedDate + "\t\t" + " " + selectedTime;
                    editDateTime.setText(concatenatedDateAndTime);
                }, mHour, mMinute, true);
    }

}

