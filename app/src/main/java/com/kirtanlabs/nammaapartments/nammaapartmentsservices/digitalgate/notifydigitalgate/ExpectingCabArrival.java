package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.notifydigitalgate;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Build;
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
    Button button1hr;
    Button button2hr;
    Button button4hr;
    Button button6hr;
    Button button8hr;
    Button button12hr;
    Button button16hr;
    Button button24hr;
    TextView textCabNumber, textDateTime, textValidFor;
    private EditText editDateTime;
    EditText editCabNumber;
    Button buttonNotifyGate;
    int[] buttonIds;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private String concatenatedDateAndTime = "";
    private String selectedDate = "";
    private String selectedTime = "";

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

        /*Getting Id's for all the views*/
        button1hr = findViewById(R.id.button1Hr);
        button2hr = findViewById(R.id.button2Hr);
        button4hr = findViewById(R.id.button4Hr);
        button6hr = findViewById(R.id.button6Hr);
        button8hr = findViewById(R.id.button8Hr);
        button12hr = findViewById(R.id.button12Hr);
        button16hr = findViewById(R.id.button16Hr);
        button24hr = findViewById(R.id.button24Hr);
        textCabNumber = findViewById(R.id.textCabNumber);
        textDateTime = findViewById(R.id.textDateTime);
        textValidFor = findViewById(R.id.textValidFor);
        editCabNumber = findViewById(R.id.editCabNumber);
        editDateTime = findViewById(R.id.editDateTime);
        buttonNotifyGate = findViewById(R.id.buttonNotifyGate);

        /*Created an integer array for storing button id's*/
        buttonIds = new int[]{R.id.button1Hr,
                R.id.button2Hr,
                R.id.button4Hr,
                R.id.button6Hr,
                R.id.button8Hr,
                R.id.button12Hr,
                R.id.button16Hr,
                R.id.button24Hr};

        /*Setting font for all the views*/
        textCabNumber.setTypeface(Constants.setLatoBoldFont(this));
        textDateTime.setTypeface(Constants.setLatoBoldFont(this));
        textValidFor.setTypeface(Constants.setLatoBoldFont(this));
        editCabNumber.setTypeface(Constants.setLatoRegularFont(this));
        editDateTime.setTypeface(Constants.setLatoRegularFont(this));
        buttonNotifyGate.setTypeface(Constants.setLatoLightFont(this));
        button1hr.setTypeface(Constants.setLatoLightFont(this));
        button2hr.setTypeface(Constants.setLatoLightFont(this));
        button4hr.setTypeface(Constants.setLatoLightFont(this));
        button6hr.setTypeface(Constants.setLatoLightFont(this));
        button8hr.setTypeface(Constants.setLatoLightFont(this));
        button12hr.setTypeface(Constants.setLatoLightFont(this));
        button16hr.setTypeface(Constants.setLatoLightFont(this));
        button24hr.setTypeface(Constants.setLatoLightFont(this));

        /*Setting event for 8 buttons*/
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

    private void selectButton(int id) {
        for (int buttonId : buttonIds) {
            Button button = findViewById(buttonId);
            if (buttonId == id) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    button.setBackground(getDrawable(R.drawable.selected_button_design));
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    button.setBackground(getDrawable(R.drawable.button_design));
                }
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
                    selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                    timePickerDialog.cancel();
                    concatenatedDateAndTime = selectedDate + "\t\t" + " " + selectedTime;
                    editDateTime.setText(concatenatedDateAndTime);
                }, mHour, mMinute, true);
    }
}

