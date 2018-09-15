package com.kirtanlabs.nammaapartments.navigationdrawer.myfood;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.home.activities.NammaApartmentsHome;
import com.kirtanlabs.nammaapartments.utilities.Constants;

public class MyFoodActivity extends BaseActivity implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final int[] buttonIds = new int[]{R.id.buttonFoodQtyLess,
            R.id.buttonFoodQtyMore};
    private EditText editFoodType;
    private TextView textErrorFoodQuantity;
    private Boolean isFoodQtySelected = false;

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
        Button buttonCollectFood = findViewById(R.id.buttonCollectFood);

        /*Setting Fonts for all the views*/
        textFoodType.setTypeface(Constants.setLatoBoldFont(this));
        textFoodQty.setTypeface(Constants.setLatoBoldFont(this));
        textErrorFoodQuantity.setTypeface(Constants.setLatoRegularFont(this));
        editFoodType.setTypeface(Constants.setLatoRegularFont(this));
        buttonFoodQtyLess.setTypeface(Constants.setLatoRegularFont(this));
        buttonFoodQtyMore.setTypeface(Constants.setLatoRegularFont(this));
        buttonCollectFood.setTypeface(Constants.setLatoLightFont(this));

        /*Setting OnClick Listeners to the views*/
        buttonFoodQtyLess.setOnClickListener(this);
        buttonFoodQtyMore.setOnClickListener(this);
        buttonCollectFood.setOnClickListener(this);
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
            case R.id.buttonCollectFood:
                /*This method gets invoked to check all the editText fields and button validations.*/
                validateFields();
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
        /*This condition checks if all fields are not filled and if user presses collect food button
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
            Intent naHomeIntent = new Intent(MyFoodActivity.this, NammaApartmentsHome.class);
            naHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            naHomeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            showNotificationDialog(getString(R.string.request_raised),
                    getString(R.string.food_request_service_dialog_message),
                    naHomeIntent);
        }
    }
}
