package com.kirtanlabs.nammaapartments.services.societyservices.othersocietyservices.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;

import java.text.DateFormatSymbols;

import static com.kirtanlabs.nammaapartments.utilities.Constants.SCREEN_TITLE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoLightFont;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoRegularFont;

public class EventManagement extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener, DatePickerDialog.OnDateSetListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final int[] buttonIds = new int[]{R.id.buttonMorningSlot,
            R.id.buttonNoonSlot,
            R.id.buttonEveningSlot,
            R.id.buttonNightSlot};
    private EditText editPickDate, editEventTitle;
    private Button buttonParties, buttonConcerts, buttonMeetings, buttonSeminarsOrWorkshops;
    private String societyServiceType;
    private TextView textErrorEventDate;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

    @Override

    protected int getLayoutResourceId() {
        return R.layout.activity_event_management;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.event_management;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Getting Id's for all the views*/
        TextView textEventTitle = findViewById(R.id.textEventTitle);
        TextView textChooseCategory = findViewById(R.id.textChooseCategory);
        TextView textEventDate = findViewById(R.id.textEventDate);
        TextView textChooseTimeSlot = findViewById(R.id.textChooseTimeSlot);
        TextView textTimeSlotQuery = findViewById(R.id.textTimeSlotQuery);
        textErrorEventDate = findViewById(R.id.textErrorEventDate);
        editEventTitle = findViewById(R.id.editEventTitle);
        editPickDate = findViewById(R.id.editPickDate);
        buttonParties = findViewById(R.id.buttonParties);
        buttonConcerts = findViewById(R.id.buttonConcerts);
        buttonMeetings = findViewById(R.id.buttonMeetings);
        buttonSeminarsOrWorkshops = findViewById(R.id.buttonSeminarsOrWorkshops);
        Button buttonMorningSlot = findViewById(R.id.buttonMorningSlot);
        Button buttonNoonSlot = findViewById(R.id.buttonNoonSlot);
        Button buttonEveningSlot = findViewById(R.id.buttonEveningSlot);
        Button buttonNightSlot = findViewById(R.id.buttonNightSlot);
        Button buttonBook = findViewById(R.id.buttonBook);

        /*Setting Fonts for all the views*/
        textEventTitle.setTypeface(setLatoBoldFont(this));
        textChooseCategory.setTypeface(setLatoBoldFont(this));
        textEventDate.setTypeface(setLatoBoldFont(this));
        textChooseTimeSlot.setTypeface(setLatoBoldFont(this));
        textTimeSlotQuery.setTypeface(setLatoBoldFont(this));
        textErrorEventDate.setTypeface(setLatoBoldFont(this));
        editPickDate.setTypeface(setLatoRegularFont(this));
        editEventTitle.setTypeface(setLatoRegularFont(this));
        buttonParties.setTypeface(setLatoRegularFont(this));
        buttonConcerts.setTypeface(setLatoRegularFont(this));
        buttonMeetings.setTypeface(setLatoRegularFont(this));
        buttonSeminarsOrWorkshops.setTypeface(setLatoRegularFont(this));
        buttonMorningSlot.setTypeface(setLatoRegularFont(this));
        buttonNoonSlot.setTypeface(setLatoRegularFont(this));
        buttonEveningSlot.setTypeface(setLatoRegularFont(this));
        buttonNightSlot.setTypeface(setLatoRegularFont(this));
        buttonBook.setTypeface(setLatoLightFont(this));

        /*We don't want the keyboard to be displayed when user clicks on the pick date and time edit field*/
        editPickDate.setInputType(InputType.TYPE_NULL);

        societyServiceType = getString(R.string.event_management).toLowerCase();

        /*Since we have History button here, we would want users to navigate to history and take a look at their
         * History of that particular Society Service*/
        ImageView historyButton = findViewById(R.id.historyButton);
        historyButton.setVisibility(View.VISIBLE);

        /*Setting event for views */
        editPickDate.setOnClickListener(this);
        editPickDate.setOnFocusChangeListener(this);
        buttonParties.setOnClickListener(this);
        buttonConcerts.setOnClickListener(this);
        buttonMeetings.setOnClickListener(this);
        buttonSeminarsOrWorkshops.setOnClickListener(this);
        buttonMorningSlot.setOnClickListener(this);
        buttonNoonSlot.setOnClickListener(this);
        buttonEveningSlot.setOnClickListener(this);
        buttonNightSlot.setOnClickListener(this);
        historyButton.setOnClickListener(this);
        buttonBook.setOnClickListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding OnDateSet Listener
     * ------------------------------------------------------------- */

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (view.isShown()) {
            String selectedDate = new DateFormatSymbols().getMonths()[month].substring(0, 3) + " " + dayOfMonth + ", " + year;
            editPickDate.setText(selectedDate);
            textErrorEventDate.setVisibility(View.GONE);
        }
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClick  and FocusChange Listener Methods
     * ------------------------------------------------------------- */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editPickDateTime:
                pickDate(this, this);
                break;
            case R.id.buttonMorningSlot:
                selectButton(R.id.buttonMorningSlot);
                break;
            case R.id.buttonNoonSlot:
                selectButton(R.id.buttonNoonSlot);
                break;
            case R.id.buttonEveningSlot:
                selectButton(R.id.buttonEveningSlot);
                break;
            case R.id.buttonNightSlot:
                selectButton(R.id.buttonNightSlot);
                break;
            case R.id.buttonParties:
                buttonParties.setBackgroundResource(R.drawable.selected_button_design);
                buttonConcerts.setBackgroundResource(R.drawable.valid_for_button_design);
                buttonMeetings.setBackgroundResource(R.drawable.valid_for_button_design);
                buttonSeminarsOrWorkshops.setBackgroundResource(R.drawable.valid_for_button_design);
                break;
            case R.id.buttonConcerts:
                buttonConcerts.setBackgroundResource(R.drawable.selected_button_design);
                buttonParties.setBackgroundResource(R.drawable.valid_for_button_design);
                buttonMeetings.setBackgroundResource(R.drawable.valid_for_button_design);
                buttonSeminarsOrWorkshops.setBackgroundResource(R.drawable.valid_for_button_design);
                break;
            case R.id.buttonMeetings:
                buttonMeetings.setBackgroundResource(R.drawable.selected_button_design);
                buttonParties.setBackgroundResource(R.drawable.valid_for_button_design);
                buttonConcerts.setBackgroundResource(R.drawable.valid_for_button_design);
                buttonSeminarsOrWorkshops.setBackgroundResource(R.drawable.valid_for_button_design);
                break;
            case R.id.buttonSeminarsOrWorkshops:
                buttonSeminarsOrWorkshops.setBackgroundResource(R.drawable.selected_button_design);
                buttonParties.setBackgroundResource(R.drawable.valid_for_button_design);
                buttonConcerts.setBackgroundResource(R.drawable.valid_for_button_design);
                buttonMeetings.setBackgroundResource(R.drawable.valid_for_button_design);
                break;
            case R.id.buttonBook:
                validateFields();
                /*TODO:To Discuss Book Functionality and to Implement the Logic Later */
                break;
            case R.id.historyButton:
                Intent societyServiceHistoryIntent = new Intent(EventManagement.this, SocietyServicesHistory.class);
                societyServiceHistoryIntent.putExtra(SCREEN_TITLE, societyServiceType);
                startActivity(societyServiceHistoryIntent);
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
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * Method to change colour of selected buttons
     *
     * @param id - of selected button
     */
    private void selectButton(int id) {
        for (int buttonId : buttonIds) {
            Button button = findViewById(buttonId);
            if (buttonId == id) {
                Button selectedButton = button;
                button.setBackgroundResource(R.drawable.selected_button_design);
            } else {
                button.setBackgroundResource(R.drawable.valid_for_button_design);
            }
        }
    }

    /**
     * This method gets invoked to check all the validation fields.
     */
    private void validateFields() {
        String problemValue = editEventTitle.getText().toString();
        String eventDate = editPickDate.getText().toString().trim();
        Boolean fieldsFilled = isAllFieldsFilled(new EditText[]{editEventTitle, editPickDate});
        /*This condition checks if all fields are not filled and if user presses book button it will
         *then display proper error messages.*/
        if (!fieldsFilled) {
            if (TextUtils.isEmpty(problemValue)) {
                editEventTitle.setError(getString(R.string.event_title_error));
            }
            if (TextUtils.isEmpty(eventDate)) {
                textErrorEventDate.setVisibility(View.VISIBLE);
            }
        }
        /*This condition checks for if user has filled all the fields and navigates to appropriate screen.*/
        if (fieldsFilled) {
            /*TODO:Discuss the Storage Part in Firebase for Event Management*/
        }
    }

}
