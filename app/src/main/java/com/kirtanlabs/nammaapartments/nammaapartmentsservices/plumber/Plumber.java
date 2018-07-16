package com.kirtanlabs.nammaapartments.nammaapartmentsservices.plumber;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.R;

import static com.kirtanlabs.nammaapartments.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartments.Constants.setLatoLightFont;
import static com.kirtanlabs.nammaapartments.Constants.setLatoRegularFont;

public class Plumber extends BaseActivity implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final int[] buttonIds = new int[]{R.id.buttonImmediately,
            R.id.buttonMorningSlot,
            R.id.buttonNoonSlot,
            R.id.buttonEveningSlot};

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_plumber;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.plumber;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Getting Id's for all the views*/
        TextView textSelectProblem = findViewById(R.id.textSelectProblem);
        Spinner spinnerSelectProblem = findViewById(R.id.spinnerSelectProblem);
        TextView textSelectSlot = findViewById(R.id.textSelectSlot);
        Button buttonImmediately = findViewById(R.id.buttonImmediately);
        Button buttonMorningSlot = findViewById(R.id.buttonMorningSlot);
        Button buttonNoonSlot = findViewById(R.id.buttonNoonSlot);
        Button buttonEveningSlot = findViewById(R.id.buttonEveningSlot);
        Button buttonRequestPlumber = findViewById(R.id.buttonRequestPlumber);

        /*Setting font for all the views*/
        textSelectProblem.setTypeface(setLatoBoldFont(this));
        textSelectSlot.setTypeface(setLatoBoldFont(this));
        buttonImmediately.setTypeface(setLatoRegularFont(this));
        buttonMorningSlot.setTypeface(setLatoRegularFont(this));
        buttonNoonSlot.setTypeface(setLatoRegularFont(this));
        buttonEveningSlot.setTypeface(setLatoRegularFont(this));
        buttonRequestPlumber.setTypeface(setLatoLightFont(this));

        // We want Button Immediately should be selected on start of activity
        selectButton(R.id.buttonImmediately);

        /*Setting font for all the items in the list*/
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, getResources().getStringArray(R.array.plumbing_issues_list)) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textProblem = view.findViewById(android.R.id.text1);
                textProblem.setTypeface(Constants.setLatoRegularFont(Plumber.this));
                return view;
            }
        };
        //Setting adapter to Spinner view
        spinnerSelectProblem.setAdapter(adapter);

        /*Setting event for views*/
        buttonImmediately.setOnClickListener(this);
        buttonMorningSlot.setOnClickListener(this);
        buttonNoonSlot.setOnClickListener(this);
        buttonEveningSlot.setOnClickListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClick Listener
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonImmediately:
                selectButton(R.id.buttonImmediately);
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
                button.setBackgroundResource(R.drawable.selected_button_design);
            } else {
                button.setBackgroundResource(R.drawable.valid_for_button_design);
            }
        }
    }
}