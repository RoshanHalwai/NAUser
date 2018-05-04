package com.kirtanlabs.nammaapartments.onboarding.flatdetails;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.R;

import java.util.Arrays;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 5/2/2018
 */

public class MyFlatDetails extends BaseActivity {

    ListView listView;
    EditText inputSearch;
    ArrayAdapter<String> adapter;

    EditText editCity;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_my_flat_details;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.my_flat_details;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Getting Id's for all the views*/
        TextView textCity = findViewById(R.id.textCity);
        TextView textSociety = findViewById(R.id.textSociety);
        TextView textApartment = findViewById(R.id.textApartment);
        TextView textFlat = findViewById(R.id.textFlat);
        TextView textResidentType = findViewById(R.id.textResidentType);
        TextView textVerificationMessage = findViewById(R.id.textVerificationMessage);
        editCity = findViewById(R.id.editCity);
        EditText editSociety = findViewById(R.id.editSociety);
        EditText editApartment = findViewById(R.id.editApartment);
        EditText editFlat = findViewById(R.id.editFlat);
        Button buttonContinue = findViewById(R.id.buttonContinue);

        /*Setting font for all the views*/
        textCity.setTypeface(Constants.setLatoBoldFont(this));
        textSociety.setTypeface(Constants.setLatoBoldFont(this));
        textApartment.setTypeface(Constants.setLatoBoldFont(this));
        textFlat.setTypeface(Constants.setLatoBoldFont(this));
        textResidentType.setTypeface(Constants.setLatoBoldFont(this));
        textVerificationMessage.setTypeface(Constants.setLatoRegularFont(this));
        editCity.setTypeface(Constants.setLatoRegularFont(this));
        editSociety.setTypeface(Constants.setLatoRegularFont(this));
        editApartment.setTypeface(Constants.setLatoRegularFont(this));
        editFlat.setTypeface(Constants.setLatoRegularFont(this));
        buttonContinue.setTypeface(Constants.setLatoLightFont(this));

        /*Attaching listeners to EditText*/
        editCity.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                searchCitiesList();
            }
        });
        editCity.setOnClickListener(view -> searchCitiesList());

    }

    public void searchCitiesList() {
        final Dialog dialog = new Dialog(MyFlatDetails.this);
        dialog.setContentView(R.layout.cities_listview);
        listView = dialog.findViewById(R.id.list);
        inputSearch = dialog.findViewById(R.id.inputSearch);
        inputSearch.setTypeface(Constants.setLatoBoldFont(MyFlatDetails.this));

        String[] values = new String[]{"Delhi", "Bengaluru", "Chennai", "Lucknow", "Goa", "Pune",
                "Agra", "Dehradun", "Hyderabad", "Gurgaon", "Kerala", "Mumbai", "Gujarat"};
        Arrays.sort(values);

        dialog.show();

        /*Setting font for all the items in the list view*/
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setTypeface(Constants.setLatoRegularFont(MyFlatDetails.this));
                return view;
            }
        };
        listView.setAdapter(adapter);

        /*Attaching listeners to ListView*/
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String itemValue = (String) listView.getItemAtPosition(position);
            editCity.setText(itemValue);
            dialog.cancel();
        });

        /*Attaching listeners to Search Field*/
        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user search for the Text
                MyFlatDetails.this.adapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });

    }

}
