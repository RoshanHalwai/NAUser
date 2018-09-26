package com.kirtanlabs.nammaapartments.navigationdrawer.myfood.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.navigationdrawer.myfood.pojo.DonateFood;
import com.kirtanlabs.nammaapartments.utilities.Constants;

import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_FOOD_DONATIONS;

public class MyFoodActivity extends BaseActivity implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final int[] buttonIds = new int[]{R.id.buttonFoodQtyLess,
            R.id.buttonFoodQtyMore};
    private EditText editFoodType;
    private TextView textErrorFoodQuantity;
    private Boolean isFoodQtySelected = false;
    private Button selectedButton;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_my_food;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.my_food;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Getting Id's for all the views*/
        TextView textFoodType = findViewById(R.id.textFoodType);
        TextView textFoodQty = findViewById(R.id.textFoodQty);
        textErrorFoodQuantity = findViewById(R.id.textErrorFoodQuantity);
        editFoodType = findViewById(R.id.editFoodType);
        Button buttonFoodQtyLess = findViewById(R.id.buttonFoodQtyLess);
        Button buttonFoodQtyMore = findViewById(R.id.buttonFoodQtyMore);
        Button buttonDonateFood = findViewById(R.id.buttonDonateFood);

        /*Since we have History button here, we would want users to navigate to history and take a look at their
         * history of that particular food donations raised by user*/
        ImageView historyButton = findViewById(R.id.historyButton);
        historyButton.setVisibility(View.VISIBLE);

        /*Setting Fonts for all the views*/
        textFoodType.setTypeface(Constants.setLatoBoldFont(this));
        textFoodQty.setTypeface(Constants.setLatoBoldFont(this));
        textErrorFoodQuantity.setTypeface(Constants.setLatoRegularFont(this));
        editFoodType.setTypeface(Constants.setLatoRegularFont(this));
        buttonFoodQtyLess.setTypeface(Constants.setLatoRegularFont(this));
        buttonFoodQtyMore.setTypeface(Constants.setLatoRegularFont(this));
        buttonDonateFood.setTypeface(Constants.setLatoLightFont(this));

        /*Setting OnClick Listeners to the views*/
        buttonFoodQtyLess.setOnClickListener(this);
        buttonFoodQtyMore.setOnClickListener(this);
        buttonDonateFood.setOnClickListener(this);
        historyButton.setOnClickListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClick Listener Methods
     * ------------------------------------------------------------- */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonFoodQtyLess:
                selectButton(R.id.buttonFoodQtyLess);
                break;
            case R.id.buttonFoodQtyMore:
                selectButton(R.id.buttonFoodQtyMore);
                break;
            case R.id.buttonDonateFood:
                /*This method gets invoked to check all the editText fields and button validations.*/
                validateFields();
                break;
            case R.id.historyButton:
                Intent donateFoodHistoryIntent = new Intent(MyFoodActivity.this, FoodDonationHistory.class);
                startActivity(donateFoodHistoryIntent);
                break;
        }
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * This method changes the background of two buttons.
     *
     * @param id contains the id of that particular button.
     */
    private void selectButton(int id) {
        isFoodQtySelected = true;
        for (int buttonId : buttonIds) {
            Button button = findViewById(buttonId);
            if (buttonId == id) {
                selectedButton = button;
                button.setBackgroundResource(R.drawable.selected_button_design);
                textErrorFoodQuantity.setVisibility(View.GONE);
            } else {
                button.setBackgroundResource(R.drawable.valid_for_button_design);
            }
        }
    }

    /**
     * This method gets invoked to check all the validation fields.
     */
    private void validateFields() {
        boolean fieldsFilled;
        fieldsFilled = isAllFieldsFilled(new EditText[]{editFoodType}) && isFoodQtySelected;
        /*This condition checks if all fields are not filled and if user presses donate food button
         *it will then display appropriate error messages.*/
        if (!fieldsFilled) {
            if (!isFoodQtySelected) {
                textErrorFoodQuantity.setVisibility(View.VISIBLE);
            }
            if (TextUtils.isEmpty(editFoodType.getText().toString())) {
                editFoodType.setError(getString(R.string.please_enter_food_type));
            }
        }
        /*This condition checks for if user has filled all the fields and navigates to appropriate screen.*/
        if (fieldsFilled) {
            /*This method stores the user food request details in Firebase*/
            storeFoodRequestDetailsInFirebase();
            /*Clearing the editText value and deselecting the user selected button*/
            editFoodType.setText("");
            selectedButton.setBackgroundResource(R.drawable.valid_for_button_design);
            isFoodQtySelected = false;
        }
    }

    /**
     * This method stores food request details in firebase whenever user raises a request to
     * donate food.
     */
    private void storeFoodRequestDetailsInFirebase() {
        DatabaseReference donateFoodReference = Constants.DONATE_FOOD_REFERENCE;
        DatabaseReference userDonateFoodReference = ((NammaApartmentsGlobal) getApplicationContext()).getUserDataReference().child(FIREBASE_CHILD_FOOD_DONATIONS);
        String foodRequestUID = userDonateFoodReference.push().getKey();
        userDonateFoodReference.child(foodRequestUID).setValue(true);

        /*Getting the user entered values*/
        String foodType = editFoodType.getText().toString();
        String foodQuantity = selectedButton.getText().toString();
        DonateFood donateFood = new DonateFood(foodType, foodQuantity, foodRequestUID, NammaApartmentsGlobal.userUID, System.currentTimeMillis(), getString(R.string.pending));
        donateFoodReference.child(foodRequestUID).setValue(donateFood);

        /*Navigating users to donate food history screen*/
        Intent foodDonationHistoryIntent = new Intent(MyFoodActivity.this, FoodDonationHistory.class);
        foodDonationHistoryIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        foodDonationHistoryIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        showNotificationDialog(getString(R.string.request_raised),
                getString(R.string.food_request_service_dialog_message),
                foodDonationHistoryIntent);
    }
}
