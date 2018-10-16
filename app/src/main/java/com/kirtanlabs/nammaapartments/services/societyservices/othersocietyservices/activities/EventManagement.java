package com.kirtanlabs.nammaapartments.services.societyservices.othersocietyservices.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.navigationdrawer.mywallet.activities.MyPaymentsActivity;
import com.kirtanlabs.nammaapartments.navigationdrawer.mywallet.pojo.Transaction;
import com.kirtanlabs.nammaapartments.services.societyservices.othersocietyservices.pojo.NammaApartmentSocietyServices;
import com.kirtanlabs.nammaapartments.userpojo.UserPersonalDetails;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static com.kirtanlabs.nammaapartments.utilities.Constants.ALL_SOCIETYSERVICENOTIFICATION_REFERENCE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.EIGHTEEN_HOURS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.EIGHTH_TIME_SLOT;
import static com.kirtanlabs.nammaapartments.utilities.Constants.ELEVENTH_TIME_SLOT;
import static com.kirtanlabs.nammaapartments.utilities.Constants.ELEVEN_HOURS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.EVENT_MANAGEMENT_REFERENCE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIFTEEN_HOURS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIFTH_TIME_SLOT;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_BOOKING_AMOUNT;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_CONVENIENCE_CHARGES;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_EVENT_MANAGEMENT;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_PRIVATE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_SOCIETYSERVICENOTIFICATION;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_TIMESTAMP;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_TIME_SLOTS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_TRANSACTIONS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIRST_TIME_SLOT;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FOURTEENTH_TIME_SLOT;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FOURTEEN_HOURS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FOURTH_TIME_SLOT;
import static com.kirtanlabs.nammaapartments.utilities.Constants.INDIAN_RUPEE_CURRENCY_CODE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.IN_PROGRESS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.NINETEEN_HOURS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.NINE_HOURS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.NINTH_TIME_SLOT;
import static com.kirtanlabs.nammaapartments.utilities.Constants.PAYMENT_CANCELLED_ERROR_CODE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.PAYMENT_FAILURE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.PAYMENT_SUCCESSFUL;
import static com.kirtanlabs.nammaapartments.utilities.Constants.PRIVATE_TRANSACTION_REFERENCE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.SCREEN_TITLE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.SECOND_TIME_SLOT;
import static com.kirtanlabs.nammaapartments.utilities.Constants.SEVENTEEN_HOURS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.SEVENTH_TIME_SLOT;
import static com.kirtanlabs.nammaapartments.utilities.Constants.SIXTEEN_HOURS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.SIXTH_TIME_SLOT;
import static com.kirtanlabs.nammaapartments.utilities.Constants.SOCIETYSERVICENOTIFICATION_REFERENCE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.TENTH_TIME_SLOT;
import static com.kirtanlabs.nammaapartments.utilities.Constants.TEN_HOURS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.THIRD_TIME_SLOT;
import static com.kirtanlabs.nammaapartments.utilities.Constants.THIRTEENTH_TIME_SLOT;
import static com.kirtanlabs.nammaapartments.utilities.Constants.THIRTEEN_HOURS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.TIME_SLOT_FULL_DAY;
import static com.kirtanlabs.nammaapartments.utilities.Constants.TOTAL_NUMBER_OF_TIME_SLOTS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.TRANSACTION_REFERENCE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.TWELFTH_TIME_SLOT;
import static com.kirtanlabs.nammaapartments.utilities.Constants.TWELVE_HOURS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.TWENTY_HOURS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.TWENTY_ONE_HOURS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.TWENTY_THREE_HOURS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.TWENTY_TWO_HOURS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoLightFont;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoRegularFont;

public class EventManagement extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener, DatePickerDialog.OnDateSetListener, PaymentResultListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private static final String TAG = MyPaymentsActivity.class.getSimpleName();
    private final int[] buttonIds = new int[]{R.id.buttonParties,
            R.id.buttonConcerts,
            R.id.buttonMeetings,
            R.id.buttonSeminarsOrWorkshops};
    private EditText editPickDate, editEventTitle;
    private Button buttonFirstTimeSlot, buttonSecondTimeSlot, buttonThirdTimeSlot, buttonFourthTimeSlot, buttonFifthTimeSlot,
            buttonSixthTimeSlot, buttonSeventhTimeSlot, buttonEighthTimeSlot, buttonNinthTimeSlot, buttonTenthTimeSlot,
            buttonEleventhTimeSlot, buttonTwelfthTimeSlot, buttonThirteenthTimeSlot, buttonFourteenthTimeSlot, buttonFullDay;
    private String societyServiceType, category, selectedEventDate;
    private TextView textErrorEventDate, textErrorChooseCategory, textErrorChooseTimeSlot, textTimeSlotQuery, textChooseTimeSlot;
    private Calendar calendar;
    private Boolean isCategorySelected = false, isFullDayTimeSlotSelected = false;
    private LinearLayout layoutTimeSlot, layoutLegend;
    private List<String> selectedTimeSlotsList;
    private int selectedButtonId;
    private float conveniencePercentage, convenienceCharge, totalAmount, bookingAmountPerSlot;

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

        /*To Retrieve the booking amount per slot from server, if booking charges is 0.0 then it indicates
         * the society do not have Event Booking facilities*/
        showProgressDialog(this, getString(R.string.event_booking), getString(R.string.please_wait_a_moment));
        retrieveBookingAmountPerSlot(bookingAmount -> {
            hideProgressDialog();
            if (bookingAmount == 0.0f) {
                showFeatureUnAvailableLayout(getString(R.string.event_feature_unavailable));
            } else {
                ScrollView eventManagementLayout = findViewById(R.id.eventManagementLayout);
                eventManagementLayout.setVisibility(View.VISIBLE);
                bookingAmountPerSlot = bookingAmount;
                onCreateUtil();
            }
        });
    }

    private void onCreateUtil() {
        /*We need Info Button in this screen*/
        showInfoButton();

        /*Since we have History button here, we would want users to navigate to history and take a look at their
         * History of that particular Society Service*/
        ImageView historyButton = findViewById(R.id.historyButton);
        historyButton.setVisibility(View.VISIBLE);

        /*Getting Id's for all the views*/
        TextView textEventTitle = findViewById(R.id.textEventTitle);
        TextView textChooseCategory = findViewById(R.id.textChooseCategory);
        TextView textEventDate = findViewById(R.id.textEventDate);
        textChooseTimeSlot = findViewById(R.id.textChooseTimeSlot);
        textTimeSlotQuery = findViewById(R.id.textTimeSlotQuery);
        textErrorEventDate = findViewById(R.id.textErrorEventDate);
        textErrorChooseCategory = findViewById(R.id.textErrorChooseCategory);
        textErrorChooseTimeSlot = findViewById(R.id.textErrorChooseTimeSlot);
        TextView textAvailableSlotInfo = findViewById(R.id.textAvailableSlotInfo);
        TextView textUnavailableSlotInfo = findViewById(R.id.textUnavailableSlotInfo);
        editEventTitle = findViewById(R.id.editEventTitle);
        editPickDate = findViewById(R.id.editPickDate);
        Button buttonParties = findViewById(R.id.buttonParties);
        Button buttonConcerts = findViewById(R.id.buttonConcerts);
        Button buttonMeetings = findViewById(R.id.buttonMeetings);
        Button buttonSeminarsOrWorkshops = findViewById(R.id.buttonSeminarsOrWorkshops);
        buttonFirstTimeSlot = findViewById(R.id.buttonFirstTimeSlot);
        buttonSecondTimeSlot = findViewById(R.id.buttonSecondTimeSlot);
        buttonThirdTimeSlot = findViewById(R.id.buttonThirdTimeSlot);
        buttonFourthTimeSlot = findViewById(R.id.buttonFourthTimeSlot);
        buttonFifthTimeSlot = findViewById(R.id.buttonFifthTimeSlot);
        buttonSixthTimeSlot = findViewById(R.id.buttonSixthTimeSlot);
        buttonSeventhTimeSlot = findViewById(R.id.buttonSeventhTimeSlot);
        buttonEighthTimeSlot = findViewById(R.id.buttonEighthTimeSlot);
        buttonNinthTimeSlot = findViewById(R.id.buttonNinthTimeSlot);
        buttonTenthTimeSlot = findViewById(R.id.buttonTenthTimeSlot);
        buttonEleventhTimeSlot = findViewById(R.id.buttonEleventhTimeSlot);
        buttonTwelfthTimeSlot = findViewById(R.id.buttonTwelfthTimeSlot);
        buttonThirteenthTimeSlot = findViewById(R.id.buttonThirteenthTimeSlot);
        buttonFourteenthTimeSlot = findViewById(R.id.buttonFourteenthTimeSlot);
        buttonFullDay = findViewById(R.id.buttonFullDay);
        Button buttonBook = findViewById(R.id.buttonBook);
        layoutLegend = findViewById(R.id.layoutLegend);
        layoutTimeSlot = findViewById(R.id.layoutTimeSlot);

        /*Setting Fonts for all the views*/
        textEventTitle.setTypeface(setLatoBoldFont(this));
        textChooseCategory.setTypeface(setLatoBoldFont(this));
        textEventDate.setTypeface(setLatoBoldFont(this));
        textChooseTimeSlot.setTypeface(setLatoBoldFont(this));
        textTimeSlotQuery.setTypeface(setLatoBoldFont(this));
        textErrorChooseCategory.setTypeface(setLatoRegularFont(this));
        textErrorChooseTimeSlot.setTypeface(setLatoRegularFont(this));
        textAvailableSlotInfo.setTypeface(setLatoBoldFont(this));
        textUnavailableSlotInfo.setTypeface(setLatoBoldFont(this));
        editPickDate.setTypeface(setLatoRegularFont(this));
        editEventTitle.setTypeface(setLatoRegularFont(this));
        buttonParties.setTypeface(setLatoRegularFont(this));
        buttonConcerts.setTypeface(setLatoRegularFont(this));
        buttonMeetings.setTypeface(setLatoRegularFont(this));
        buttonSeminarsOrWorkshops.setTypeface(setLatoRegularFont(this));
        buttonSeminarsOrWorkshops.setTypeface(setLatoRegularFont(this));
        buttonFirstTimeSlot.setTypeface(setLatoRegularFont(this));
        buttonSecondTimeSlot.setTypeface(setLatoRegularFont(this));
        buttonThirdTimeSlot.setTypeface(setLatoRegularFont(this));
        buttonFourthTimeSlot.setTypeface(setLatoRegularFont(this));
        buttonFifthTimeSlot.setTypeface(setLatoRegularFont(this));
        buttonSixthTimeSlot.setTypeface(setLatoRegularFont(this));
        buttonSeventhTimeSlot.setTypeface(setLatoRegularFont(this));
        buttonEighthTimeSlot.setTypeface(setLatoRegularFont(this));
        buttonNinthTimeSlot.setTypeface(setLatoRegularFont(this));
        buttonTenthTimeSlot.setTypeface(setLatoRegularFont(this));
        buttonEleventhTimeSlot.setTypeface(setLatoRegularFont(this));
        buttonTwelfthTimeSlot.setTypeface(setLatoRegularFont(this));
        buttonThirteenthTimeSlot.setTypeface(setLatoRegularFont(this));
        buttonFourteenthTimeSlot.setTypeface(setLatoRegularFont(this));
        buttonFullDay.setTypeface(setLatoRegularFont(this));
        buttonBook.setTypeface(setLatoLightFont(this));

        selectedTimeSlotsList = new ArrayList<>();

        /*To Retrieve the convenienceCharge from server*/
        retrieveConvenienceCharge();

        /*We don't want the keyboard to be displayed when user clicks on the pick date and time edit field*/
        editPickDate.setInputType(InputType.TYPE_NULL);

        societyServiceType = FIREBASE_CHILD_EVENT_MANAGEMENT;

        /*Setting event for views */
        editPickDate.setOnClickListener(this);
        editPickDate.setOnFocusChangeListener(this);
        buttonParties.setOnClickListener(this);
        buttonConcerts.setOnClickListener(this);
        buttonMeetings.setOnClickListener(this);
        buttonSeminarsOrWorkshops.setOnClickListener(this);
        buttonFirstTimeSlot.setOnClickListener(this);
        buttonSecondTimeSlot.setOnClickListener(this);
        buttonThirdTimeSlot.setOnClickListener(this);
        buttonFourthTimeSlot.setOnClickListener(this);
        buttonFifthTimeSlot.setOnClickListener(this);
        buttonSixthTimeSlot.setOnClickListener(this);
        buttonSeventhTimeSlot.setOnClickListener(this);
        buttonEighthTimeSlot.setOnClickListener(this);
        buttonNinthTimeSlot.setOnClickListener(this);
        buttonTenthTimeSlot.setOnClickListener(this);
        buttonEleventhTimeSlot.setOnClickListener(this);
        buttonTwelfthTimeSlot.setOnClickListener(this);
        buttonThirteenthTimeSlot.setOnClickListener(this);
        buttonFourteenthTimeSlot.setOnClickListener(this);
        buttonFullDay.setOnClickListener(this);
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

            calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            selectedEventDate = new SimpleDateFormat("dd-MM-yyyy", Locale.US).format(calendar.getTime());
            showSlotLayout(selectedDate);
        }
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClick  and FocusChange Listener Methods
     * ------------------------------------------------------------- */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editPickDate:
                pickDate(this, this);
                break;
            case R.id.buttonParties:
                category = getString(R.string.parties);
                selectButton(R.id.buttonParties);
                textErrorChooseCategory.setVisibility(View.GONE);
                break;
            case R.id.buttonConcerts:
                category = getString(R.string.concerts);
                selectButton(R.id.buttonConcerts);
                textErrorChooseCategory.setVisibility(View.GONE);
                break;
            case R.id.buttonMeetings:
                category = getString(R.string.meetings);
                selectButton(R.id.buttonMeetings);
                textErrorChooseCategory.setVisibility(View.GONE);
                break;
            case R.id.buttonSeminarsOrWorkshops:
                category = getString(R.string.seminar_workshops);
                selectButton(R.id.buttonSeminarsOrWorkshops);
                textErrorChooseCategory.setVisibility(View.GONE);
                break;
            case R.id.buttonFirstTimeSlot:
                selectMultipleTimeSlots(R.id.buttonFirstTimeSlot);
                break;
            case R.id.buttonSecondTimeSlot:
                selectMultipleTimeSlots(R.id.buttonSecondTimeSlot);
                break;
            case R.id.buttonThirdTimeSlot:
                selectMultipleTimeSlots(R.id.buttonThirdTimeSlot);
                break;
            case R.id.buttonFourthTimeSlot:
                selectMultipleTimeSlots(R.id.buttonFourthTimeSlot);
                break;
            case R.id.buttonFifthTimeSlot:
                selectMultipleTimeSlots(R.id.buttonFifthTimeSlot);
                break;
            case R.id.buttonSixthTimeSlot:
                selectMultipleTimeSlots(R.id.buttonSixthTimeSlot);
                break;
            case R.id.buttonSeventhTimeSlot:
                selectMultipleTimeSlots(R.id.buttonSeventhTimeSlot);
                break;
            case R.id.buttonEighthTimeSlot:
                selectMultipleTimeSlots(R.id.buttonEighthTimeSlot);
                break;
            case R.id.buttonNinthTimeSlot:
                selectMultipleTimeSlots(R.id.buttonNinthTimeSlot);
                break;
            case R.id.buttonTenthTimeSlot:
                selectMultipleTimeSlots(R.id.buttonTenthTimeSlot);
                break;
            case R.id.buttonEleventhTimeSlot:
                selectMultipleTimeSlots(R.id.buttonEleventhTimeSlot);
                break;
            case R.id.buttonTwelfthTimeSlot:
                selectMultipleTimeSlots(R.id.buttonTwelfthTimeSlot);
                break;
            case R.id.buttonThirteenthTimeSlot:
                selectMultipleTimeSlots(R.id.buttonThirteenthTimeSlot);
                break;
            case R.id.buttonFourteenthTimeSlot:
                selectMultipleTimeSlots(R.id.buttonFourteenthTimeSlot);
                break;
            case R.id.buttonFullDay:
                fullDayTimeSlotSelected();
                break;
            case R.id.buttonBook:
                /* This method gets invoked to check all the editText fields for validations.*/
                validateFields();
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
     * Implementing PaymentResultListener Methods
     * ------------------------------------------------------------- */

    /**
     * On Payment Success, method gets called containing payment ID of the transaction
     *
     * @param paymentID - Id of Successful transaction
     */
    @Override
    public void onPaymentSuccess(String paymentID) {
        try {
            Toast.makeText(this, getString(R.string.payment_success), Toast.LENGTH_SHORT).show();
            /*This method stores event details given by user to firebase*/
            storeEventManagementDetailsInFirebase();
            storeEventManagementTransactionDetails(paymentID, PAYMENT_SUCCESSFUL);
            Intent societyServiceHistoryIntent = new Intent(EventManagement.this, SocietyServicesHistory.class);
            societyServiceHistoryIntent.putExtra(SCREEN_TITLE, societyServiceType);
            showNotificationDialog(getString(R.string.payment_success_title), getString(R.string.payment_success_message), societyServiceHistoryIntent);

            /*Clearing the editTexts values and deselecting the user selected buttons*/
            isCategorySelected = false;
            Button eventCategoryButton = findViewById(selectedButtonId);
            eventCategoryButton.setBackgroundResource(R.drawable.valid_for_button_design);
            editEventTitle.getText().clear();
            editPickDate.getText().clear();
            layoutTimeSlot.setVisibility(View.GONE);
            layoutLegend.setVisibility(View.GONE);
            textChooseTimeSlot.setVisibility(View.GONE);
            textTimeSlotQuery.setVisibility(View.GONE);
        } catch (Exception e) {
            Log.e(TAG, getString(R.string.payment_success_exception), e);
        }
    }

    /**
     * On Payment Failure, method gets called containing error code and gateway response
     *
     * @param code     - Error Code
     * @param response - Payment Gateway Response
     */
    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, getString(R.string.payment_failed), Toast.LENGTH_SHORT).show();
            if (!(code == PAYMENT_CANCELLED_ERROR_CODE) || !(response.equals(getString(R.string.payment_cancelled)))) {
                storeEventManagementTransactionDetails("", PAYMENT_FAILURE);
            }
        } catch (Exception e) {
            Log.e(TAG, getString(R.string.payment_failure_exception), e);
        }
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * This method retrieves the conveniencePercentage value from server.
     */
    private void retrieveConvenienceCharge() {
        TRANSACTION_REFERENCE.child(FIREBASE_CHILD_CONVENIENCE_CHARGES).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                convenienceCharge = Objects.requireNonNull(dataSnapshot.getValue(Float.class));
                conveniencePercentage = (convenienceCharge / 100);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * This method is invoked to select multiple time slots.
     *
     * @param buttonTimeSlotID - Id of button
     */
    private void selectMultipleTimeSlots(int buttonTimeSlotID) {
        Button button = findViewById(buttonTimeSlotID);
        String timeSlotValue = button.getText().toString().trim();

        /*if that particular time slot is already selected than deselect that time slot*/
        if (selectedTimeSlotsList.contains(timeSlotValue)) {
            button.setBackgroundResource(R.drawable.valid_for_button_design);
            selectedTimeSlotsList.remove(timeSlotValue);
        } else {
            button.setBackgroundResource(R.drawable.selected_button_design);
            selectedTimeSlotsList.add(timeSlotValue);
        }
        /*Deselecting "Full day" time slot if it is selected*/
        textErrorChooseTimeSlot.setVisibility(View.GONE);
        isFullDayTimeSlotSelected = false;
        buttonFullDay.setBackgroundResource(R.drawable.valid_for_button_design);
    }

    /**
     * This method is invoked when user selects on "Full day" time slot.
     */
    private void fullDayTimeSlotSelected() {
        if (isFullDayTimeSlotSelected) {
            isFullDayTimeSlotSelected = false;
            buttonFullDay.setBackgroundResource(R.drawable.valid_for_button_design);
        } else {
            selectedTimeSlotsList.clear();
            isFullDayTimeSlotSelected = true;
            buttonFullDay.setBackgroundResource(R.drawable.selected_button_design);
            deselectAllTimeSlots();
            textErrorChooseTimeSlot.setVisibility(View.GONE);
        }
    }

    /**
     * This method is invoked to deselect all time slots which user has selected
     */
    private void deselectAllTimeSlots() {
        int[] buttonTimeSlotIDs = new int[]{R.id.buttonFirstTimeSlot,
                R.id.buttonSecondTimeSlot,
                R.id.buttonThirdTimeSlot,
                R.id.buttonFourthTimeSlot,
                R.id.buttonFifthTimeSlot,
                R.id.buttonSixthTimeSlot,
                R.id.buttonSeventhTimeSlot,
                R.id.buttonEighthTimeSlot,
                R.id.buttonNinthTimeSlot,
                R.id.buttonTenthTimeSlot,
                R.id.buttonEleventhTimeSlot,
                R.id.buttonTwelfthTimeSlot,
                R.id.buttonThirteenthTimeSlot,
                R.id.buttonFourteenthTimeSlot};

        for (int buttonId : buttonTimeSlotIDs) {
            Button button = findViewById(buttonId);
            button.setBackgroundResource(R.drawable.valid_for_button_design);
        }
    }

    /**
     * Method to change colour of selected buttons
     *
     * @param id - of selected button
     */
    private void selectButton(int id) {
        isCategorySelected = true;
        selectedButtonId = id;
        for (int buttonId : buttonIds) {
            Button button = findViewById(buttonId);
            if (buttonId == id) {
                button.setBackgroundResource(R.drawable.selected_button_design);
                textErrorChooseCategory.setVisibility(View.GONE);
            } else {
                button.setBackgroundResource(R.drawable.valid_for_button_design);
            }
        }
    }

    /**
     * This method gets invoked to check all the validation fields.
     */
    private void validateFields() {
        String eventTitle = editEventTitle.getText().toString();
        String eventDate = editPickDate.getText().toString().trim();
        Boolean fieldsFilled = isAllFieldsFilled(new EditText[]{editEventTitle, editPickDate}) && isCategorySelected;
        /*This condition checks if all fields are not filled and if user presses book button it will
         *then display proper error messages.*/
        if (!fieldsFilled) {
            if (TextUtils.isEmpty(eventTitle)) {
                editEventTitle.setError(getString(R.string.event_title_error));
            }
            if (TextUtils.isEmpty(eventDate)) {
                textErrorEventDate.setVisibility(View.VISIBLE);
            }
            if (!isCategorySelected) {
                textErrorChooseCategory.setVisibility(View.VISIBLE);
            }
        } else if (selectedTimeSlotsList.isEmpty() && !isFullDayTimeSlotSelected) {
            textErrorChooseTimeSlot.setVisibility(View.VISIBLE);
        }

        /*This condition checks for if user has filled all the fields and have chosen the time slot
         *and navigates to appropriate screen.*/
        if (fieldsFilled && (!selectedTimeSlotsList.isEmpty() || isFullDayTimeSlotSelected)) {
            showEventPaymentDialog();
        }
    }

    /**
     * This is invoked to call a Dialog box which is used to display total amount that user has to for booking
     * an event in particular time slots for that date.
     */
    private void showEventPaymentDialog() {
        /*Inflating the layout in the dialog box*/
        View eventPaymentDialog = View.inflate(this, R.layout.layout_event_bill_and_maintenance_payment_dialog, null);

        /*Getting Id's for all the views*/
        TextView textEventBillAndMaintenanceBill = eventPaymentDialog.findViewById(R.id.textEventBillAndMaintenanceBill);
        TextView textBookedSlotsNumberAndMaintenanceAmount = eventPaymentDialog.findViewById(R.id.textBookedSlotsNumberAndMaintenanceAmount);
        TextView textAmountPerSlot = eventPaymentDialog.findViewById(R.id.textAmountPerSlot);
        TextView textEstimatedAmount = eventPaymentDialog.findViewById(R.id.textEstimatedAmount);
        TextView textConvenienceFee = eventPaymentDialog.findViewById(R.id.textConvenienceFee);
        TextView textTotalAmount = eventPaymentDialog.findViewById(R.id.textTotalAmount);
        TextView textBookedSlotsNumberAndMaintenanceCostValue = eventPaymentDialog.findViewById(R.id.textBookedSlotsNumberAndMaintenanceCostValue);
        TextView textAmountPerSlotValue = eventPaymentDialog.findViewById(R.id.textAmountPerSlotValue);
        TextView textEstimatedAmountValue = eventPaymentDialog.findViewById(R.id.textEstimatedAmountValue);
        TextView textConvenienceFeeValue = eventPaymentDialog.findViewById(R.id.textConvenienceFeeValue);
        TextView textTotalAmountValue = eventPaymentDialog.findViewById(R.id.textTotalAmountValue);
        Button buttonCancel = eventPaymentDialog.findViewById(R.id.buttonCancel);
        Button buttonPayNow = eventPaymentDialog.findViewById(R.id.buttonPayNow);

        /*Setting fonts for all the views*/
        textEventBillAndMaintenanceBill.setTypeface(setLatoBoldFont(this));
        textBookedSlotsNumberAndMaintenanceAmount.setTypeface(setLatoRegularFont(this));
        textAmountPerSlot.setTypeface(setLatoRegularFont(this));
        textEstimatedAmount.setTypeface(setLatoRegularFont(this));
        textConvenienceFee.setTypeface(setLatoRegularFont(this));
        textTotalAmount.setTypeface(setLatoBoldFont(this));
        textBookedSlotsNumberAndMaintenanceCostValue.setTypeface(setLatoBoldFont(this));
        textAmountPerSlotValue.setTypeface(setLatoBoldFont(this));
        textEstimatedAmountValue.setTypeface(setLatoBoldFont(this));
        textConvenienceFeeValue.setTypeface(setLatoBoldFont(this));
        textTotalAmountValue.setTypeface(setLatoBoldFont(this));
        buttonCancel.setTypeface(setLatoLightFont(this));
        buttonPayNow.setTypeface(setLatoLightFont(this));

        /*Calculating total amount based on number of time slots selected by the user*/
        int totalNumberOfTimeSlotsSelected;
        if (isFullDayTimeSlotSelected) {
            totalNumberOfTimeSlotsSelected = TOTAL_NUMBER_OF_TIME_SLOTS;
        } else {
            totalNumberOfTimeSlotsSelected = selectedTimeSlotsList.size();
        }
        textBookedSlotsNumberAndMaintenanceCostValue.setText(String.valueOf(totalNumberOfTimeSlotsSelected));

        /*Setting booking amount per slot retrieved from server*/
        String bookingAmountPerSlotStr = getString(R.string.rupees_symbol) + " " + String.valueOf(bookingAmountPerSlot);
        textAmountPerSlotValue.setText(bookingAmountPerSlotStr);

        /*Setting convenience fee  amount from server*/
        String convenienceFee = getString(R.string.convenience_fee);
        convenienceFee = convenienceFee.replace(getString(R.string.num_value), String.valueOf(convenienceCharge));
        textConvenienceFee.setText(convenienceFee);

        /*Mathematical Calculations for setting the convenience fee and adding it to the total amount.*/
        float estimatedAmount = (totalNumberOfTimeSlotsSelected * bookingAmountPerSlot);
        String estimatedAmountValue = getString(R.string.rupees_symbol) + " " + estimatedAmount;
        textEstimatedAmountValue.setText(estimatedAmountValue);

        /*Deriving Convenience Fee from the estimated cost value and slots selected by the user*/
        float convenienceAmount = (conveniencePercentage * estimatedAmount);
        String convenienceAmountValue = getString(R.string.rupees_symbol) + " " + convenienceAmount;
        textConvenienceFeeValue.setText(convenienceAmountValue);

        /*Setting the total amount which includes convenience fee plus estimated amount*/
        totalAmount = estimatedAmount + convenienceAmount;
        String totalAmountValue = getString(R.string.rupees_symbol) + " " + String.valueOf(totalAmount);
        textTotalAmountValue.setText(totalAmountValue);

        /*Creating the Alert Dialog and setting the view*/
        AlertDialog.Builder alertValidationDialog = new AlertDialog.Builder(this);
        alertValidationDialog.setView(eventPaymentDialog);
        AlertDialog dialog = alertValidationDialog.create();
        dialog.setCancelable(false);

        /*Showing the dialog*/
        new Dialog(this);
        dialog.show();

        /*Setting Listeners to the views*/
        buttonCancel.setOnClickListener(v -> dialog.cancel());
        buttonPayNow.setOnClickListener(v -> {
            /*converting Rupees into paise*/
            float totalAmountInPaise = totalAmount * 100;
            startPayment(totalAmountInPaise);
            dialog.cancel();
        });
    }

    /**
     * This method is invoked to open razor pay payment gateway
     *
     * @param amount - that user has to pay to book an event
     */
    private void startPayment(float amount) {
        final Activity activity = this;
        final Checkout co = new Checkout();
        try {
            UserPersonalDetails userPersonalDetails = ((NammaApartmentsGlobal) getApplicationContext()).getNammaApartmentUser().getPersonalDetails();
            JSONObject options = new JSONObject();
            options.put(getString(R.string.name).toLowerCase(), getString(R.string.app_name));
            options.put(getString(R.string.payment_description), getString(R.string.event_management));
            options.put(getString(R.string.currency), INDIAN_RUPEE_CURRENCY_CODE);
            options.put(getString(R.string.amount), String.valueOf(amount));

            JSONObject preFill = new JSONObject();
            preFill.put(getString(R.string.email), userPersonalDetails.getEmail());
            preFill.put(getString(R.string.contact), userPersonalDetails.getPhoneNumber());
            options.put(getString(R.string.prefill), preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, getString(R.string.payment_error) + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /**
     * Store the details of Event Management Notifications to Firebase
     */
    private void storeEventManagementDetailsInFirebase() {
        /*Generating the societyServiceUID*/
        DatabaseReference eventManagementNotificationReference = ALL_SOCIETYSERVICENOTIFICATION_REFERENCE;
        String notificationUID = eventManagementNotificationReference.push().getKey();

        /*Getting the data entered by user while booking the Society Service */
        String userUID = NammaApartmentsGlobal.userUID;
        String eventTitle = editEventTitle.getText().toString();
        String eventDate = editPickDate.getText().toString();

        /*Storing Society Service data entered by user under new parent 'societyServiceNotifications' in Firebase*/
        NammaApartmentSocietyServices nammaApartmentSocietyServices = new NammaApartmentSocietyServices(null, null,
                userUID, societyServiceType, notificationUID, IN_PROGRESS, null, null);

        /*Setting Event Title Entered By User*/
        nammaApartmentSocietyServices.setEventTitle(eventTitle);

        /*Setting Event Date Entered By User*/
        nammaApartmentSocietyServices.setEventDate(eventDate);

        /*Setting Event Category Entered By User*/
        nammaApartmentSocietyServices.setCategory(category);

        /*adding event data under ALL_SOCIETYSERVICENOTIFICATION_REFERENCE->Notification UID*/
        eventManagementNotificationReference.child(notificationUID).setValue(nammaApartmentSocietyServices);

        /*Storing time stamp to keep track of notifications*/
        eventManagementNotificationReference.child(notificationUID).child(FIREBASE_CHILD_TIMESTAMP).setValue(System.currentTimeMillis());

        /*Mapping Time Slot with value in eventManagement under selected Event Date and (SocietyServiceNotifications->notificationUID->timeSlots) in firebase */
        DatabaseReference eventTimeSlotReference = EVENT_MANAGEMENT_REFERENCE.child(FIREBASE_CHILD_PRIVATE).child(selectedEventDate);
        if (isFullDayTimeSlotSelected) {
            eventManagementNotificationReference.child(notificationUID).child(FIREBASE_CHILD_TIME_SLOTS).child(TIME_SLOT_FULL_DAY).setValue(true);
            eventTimeSlotReference.child(TIME_SLOT_FULL_DAY).setValue(true);
        } else {
            for (String timeSlotValue : selectedTimeSlotsList) {
                eventManagementNotificationReference.child(notificationUID).child(FIREBASE_CHILD_TIME_SLOTS).child(timeSlotValue).setValue(true);
                eventTimeSlotReference.child(timeSlotValue).setValue(true);
            }
        }

        /*Mapping Society Service UID with value in userData under Flat Number*/
        DatabaseReference societyServiceUserDataReference = ((NammaApartmentsGlobal) getApplicationContext())
                .getUserDataReference()
                .child(FIREBASE_CHILD_SOCIETYSERVICENOTIFICATION);
        societyServiceUserDataReference.child(societyServiceType).child(notificationUID).setValue(true);

        /*Creating new key under societyServicesNotifications->eventManagement->notificationUid->true*/
        DatabaseReference eventManagementReference = SOCIETYSERVICENOTIFICATION_REFERENCE.child(FIREBASE_CHILD_EVENT_MANAGEMENT);
        eventManagementReference.child(notificationUID).setValue(true);
    }


    /**
     * This method invokes to check whether there are any slots booked or not.
     *
     * @param selectedDate contains the user selected date
     */
    private void showSlotLayout(String selectedDate) {
        showProgressDialog(this,
                getString(R.string.event_management_dialog_title),
                getString(R.string.event_management_dialog_message));

        if (!TextUtils.isEmpty(selectedDate)) {
            layoutTimeSlot.setVisibility(View.VISIBLE);
            layoutLegend.setVisibility(View.VISIBLE);
            textChooseTimeSlot.setVisibility(View.VISIBLE);
            textTimeSlotQuery.setVisibility(View.VISIBLE);
        }

        /*Deselecting all previous time slots*/
        deselectAllTimeSlots();
        buttonFirstTimeSlot.setEnabled(true);
        buttonSecondTimeSlot.setEnabled(true);
        buttonThirdTimeSlot.setEnabled(true);
        buttonFourthTimeSlot.setEnabled(true);
        buttonFifthTimeSlot.setEnabled(true);
        buttonSixthTimeSlot.setEnabled(true);
        buttonSeventhTimeSlot.setEnabled(true);
        buttonEighthTimeSlot.setEnabled(true);
        buttonNinthTimeSlot.setEnabled(true);
        buttonTenthTimeSlot.setEnabled(true);
        buttonEleventhTimeSlot.setEnabled(true);
        buttonTwelfthTimeSlot.setEnabled(true);
        buttonThirteenthTimeSlot.setEnabled(true);
        buttonFourteenthTimeSlot.setEnabled(true);
        buttonFullDay.setEnabled(true);
        buttonFullDay.setBackgroundResource(R.drawable.valid_for_button_design);

        calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentYear = calendar.get(Calendar.YEAR);
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        /*Getting Current date here*/
        String currentDate = new DateFormatSymbols().getMonths()[currentMonth].substring(0, 3) + " " + currentDay + ", " + currentYear;
        /*Disabling Time Slot if current time exceeds the last hour of particular time slot for current date*/
        if (selectedDate.equals(currentDate)) {
            switch (currentHour) {
                case NINE_HOURS:
                    disableTimeSlots(R.id.buttonFirstTimeSlot);
                    break;
                case TEN_HOURS:
                    disableTimeSlots(R.id.buttonSecondTimeSlot);
                    break;
                case ELEVEN_HOURS:
                    disableTimeSlots(R.id.buttonThirdTimeSlot);
                    break;
                case TWELVE_HOURS:
                    disableTimeSlots(R.id.buttonFourthTimeSlot);
                    break;
                case THIRTEEN_HOURS:
                    disableTimeSlots(R.id.buttonFifthTimeSlot);
                    break;
                case FOURTEEN_HOURS:
                    disableTimeSlots(R.id.buttonSixthTimeSlot);
                    break;
                case FIFTEEN_HOURS:
                    disableTimeSlots(R.id.buttonSeventhTimeSlot);
                    break;
                case SIXTEEN_HOURS:
                    disableTimeSlots(R.id.buttonEighthTimeSlot);
                    break;
                case SEVENTEEN_HOURS:
                    disableTimeSlots(R.id.buttonNinthTimeSlot);
                    break;
                case EIGHTEEN_HOURS:
                    disableTimeSlots(R.id.buttonTenthTimeSlot);
                    break;
                case NINETEEN_HOURS:
                    disableTimeSlots(R.id.buttonEleventhTimeSlot);
                    break;
                case TWENTY_HOURS:
                    disableTimeSlots(R.id.buttonTwelfthTimeSlot);
                    break;
                case TWENTY_ONE_HOURS:
                    disableTimeSlots(R.id.buttonThirteenthTimeSlot);
                    break;
                case TWENTY_TWO_HOURS:
                    disableTimeSlots(R.id.buttonFourteenthTimeSlot);
                    break;
                case TWENTY_THREE_HOURS:
                    disableTimeSlots(R.id.buttonFullDay);
                    break;
            }
        }

        /*Disabling Time slot which are already booked for particular Date*/
        disableBookedSlots(selectedEventDate);
    }

    /**
     * This method is invoked to disable Time slot which are already booked by another user of that particular selected date
     *
     * @param date selected by the user.
     */
    private void disableBookedSlots(String date) {
        DatabaseReference eventBookingReference = EVENT_MANAGEMENT_REFERENCE.child(FIREBASE_CHILD_PRIVATE).child(date);

        /*Retrieving Booked Time slot of particular date from (eventManagement->selectedDate->timeSlot) in firebase*/
        eventBookingReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    buttonFullDay.setEnabled(false);
                    for (DataSnapshot timeSlotDataSnapshot : dataSnapshot.getChildren()) {
                        String timeSlot = timeSlotDataSnapshot.getKey();
                        switch (timeSlot) {
                            case FIRST_TIME_SLOT:
                                buttonFirstTimeSlot.setEnabled(false);
                                break;
                            case SECOND_TIME_SLOT:
                                buttonSecondTimeSlot.setEnabled(false);
                                break;
                            case THIRD_TIME_SLOT:
                                buttonThirdTimeSlot.setEnabled(false);
                                break;
                            case FOURTH_TIME_SLOT:
                                buttonFourthTimeSlot.setEnabled(false);
                                break;
                            case FIFTH_TIME_SLOT:
                                buttonFifthTimeSlot.setEnabled(false);
                                break;
                            case SIXTH_TIME_SLOT:
                                buttonSixthTimeSlot.setEnabled(false);
                                break;
                            case SEVENTH_TIME_SLOT:
                                buttonSeventhTimeSlot.setEnabled(false);
                                break;
                            case EIGHTH_TIME_SLOT:
                                buttonEighthTimeSlot.setEnabled(false);
                                break;
                            case NINTH_TIME_SLOT:
                                buttonNinthTimeSlot.setEnabled(false);
                                break;
                            case TENTH_TIME_SLOT:
                                buttonTenthTimeSlot.setEnabled(false);
                                break;
                            case ELEVENTH_TIME_SLOT:
                                buttonEleventhTimeSlot.setEnabled(false);
                                break;
                            case TWELFTH_TIME_SLOT:
                                buttonTwelfthTimeSlot.setEnabled(false);
                                break;
                            case THIRTEENTH_TIME_SLOT:
                                buttonThirteenthTimeSlot.setEnabled(false);
                                break;
                            case FOURTEENTH_TIME_SLOT:
                                buttonFourteenthTimeSlot.setEnabled(false);
                                break;
                            case TIME_SLOT_FULL_DAY:
                                disableTimeSlots(R.id.buttonFullDay);
                                break;
                        }
                    }
                }
                hideProgressDialog();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * This method is invoked to disable the time slots if current time exceeds the time slots last hour value
     *
     * @param buttonTimeSlotId of the button
     */
    private void disableTimeSlots(int buttonTimeSlotId) {
        switch (buttonTimeSlotId) {
            case R.id.buttonFullDay:
            case R.id.buttonFourteenthTimeSlot:
                buttonFourteenthTimeSlot.setEnabled(false);
            case R.id.buttonThirteenthTimeSlot:
                buttonThirteenthTimeSlot.setEnabled(false);
            case R.id.buttonTwelfthTimeSlot:
                buttonTwelfthTimeSlot.setEnabled(false);
            case R.id.buttonEleventhTimeSlot:
                buttonEleventhTimeSlot.setEnabled(false);
            case R.id.buttonTenthTimeSlot:
                buttonTenthTimeSlot.setEnabled(false);
            case R.id.buttonNinthTimeSlot:
                buttonNinthTimeSlot.setEnabled(false);
            case R.id.buttonEighthTimeSlot:
                buttonEighthTimeSlot.setEnabled(false);
            case R.id.buttonSeventhTimeSlot:
                buttonSeventhTimeSlot.setEnabled(false);
            case R.id.buttonSixthTimeSlot:
                buttonSixthTimeSlot.setEnabled(false);
            case R.id.buttonFifthTimeSlot:
                buttonFifthTimeSlot.setEnabled(false);
            case R.id.buttonFourthTimeSlot:
                buttonFourthTimeSlot.setEnabled(false);
            case R.id.buttonThirdTimeSlot:
                buttonThirdTimeSlot.setEnabled(false);
            case R.id.buttonSecondTimeSlot:
                buttonSecondTimeSlot.setEnabled(false);
            case R.id.buttonFirstTimeSlot:
                buttonFirstTimeSlot.setEnabled(false);
                buttonFullDay.setEnabled(false);
                break;
        }
    }

    /**
     * This method gets invoked to check if the booking amount key is present or not,if present retrieves
     * the value from firebase.
     *
     * @param bookingAmountCallback receiving result of the booking amount value.
     */
    private void retrieveBookingAmountPerSlot(BookingAmountCallback bookingAmountCallback) {
        EVENT_MANAGEMENT_REFERENCE.child(FIREBASE_CHILD_BOOKING_AMOUNT).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    bookingAmountCallback.onCallback(dataSnapshot.getValue(Float.class));
                } else {
                    bookingAmountCallback.onCallback(0.0f);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Stores the details of transaction for event in firebase
     *
     * @param paymentId Unique Id to identify each transaction
     * @param result    of the transaction
     */
    private void storeEventManagementTransactionDetails(final String paymentId, final String result) {
        DatabaseReference userTransactionReference = ((NammaApartmentsGlobal) getApplicationContext()).
                getUserDataReference().child(FIREBASE_CHILD_TRANSACTIONS);
        final String transactionUID = userTransactionReference.push().getKey();
        final Transaction transactionDetails = new Transaction(totalAmount, paymentId, result,
                getString(R.string.event_management), NammaApartmentsGlobal.userUID, transactionUID, System.currentTimeMillis());
        PRIVATE_TRANSACTION_REFERENCE.child(transactionUID).setValue(transactionDetails).addOnCompleteListener(task ->
                userTransactionReference.child(transactionUID).setValue(true));
    }

    /* ------------------------------------------------------------- *
     * Interface
     * ------------------------------------------------------------- */

    private interface BookingAmountCallback {
        void onCallback(Float bookingAmount);
    }

}
