package com.kirtanlabs.nammaapartments.services.societyservices.othersocietyservices.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.utilities.Constants;

public class SocietyServiceProblemList extends BaseActivity implements SearchView.OnQueryTextListener, AdapterView.OnItemClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private String[] problemsList;
    private ListView listView;
    private ArrayAdapter<String> adapter;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Methods
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_society_service_problem_list;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.select_problem;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Here we are hiding the keyboard on launch of screen since user has to see the UI first.*/
        hideKeyboard();

        /*Getting Id's for all the views*/
        SearchView searchProblem = findViewById(R.id.searchProblem);
        listView = findViewById(R.id.listView);

        int previousScreenTitle = getIntent().getIntExtra(Constants.SCREEN_TITLE, 0);

        /*We display list of issues according to previous screen title*/
        switch (previousScreenTitle) {
            case R.string.plumber:
                problemsList = getResources().getStringArray(R.array.plumbing_issues_list);
                break;
            case R.string.carpenter:
                problemsList = getResources().getStringArray(R.array.carpentry_issues_list);
                break;
            case R.string.electrician:
                problemsList = getResources().getStringArray(R.array.electrical_issues_list);
                break;
            case R.string.society_services:
                problemsList = getResources().getStringArray(R.array.society_services_list);
                break;
            case R.string.apartment_services:
                problemsList = getResources().getStringArray(R.array.apartment_services_list);
                break;
            case R.string.scrap_collection:
                problemsList = getResources().getStringArray(R.array.scrap_list);
                break;
        }

        /*Setting font for all the items in the list*/
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, problemsList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textProblem = view.findViewById(android.R.id.text1);
                textProblem.setTypeface(Constants.setLatoRegularFont(SocietyServiceProblemList.this));
                return view;
            }
        };
        /*Setting adapter to list view*/
        listView.setAdapter(adapter);

        /*Setting listeners to the view*/
        searchProblem.setOnQueryTextListener(this);
        listView.setOnItemClickListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding OnQueryText and OnItemClick Listener Methods
     * ------------------------------------------------------------- */

    @Override
    public boolean onQueryTextSubmit(String query) {
        returnSelectedProblem(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String problem = (String) listView.getItemAtPosition(position);
        returnSelectedProblem(problem);
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * This method is invoked to send problem back to Society Service Screen that user has selected from the list.
     *
     * @param problem that is selected by the user.
     */
    private void returnSelectedProblem(String problem) {
        Intent intent = new Intent();
        intent.putExtra(Constants.SOCIETY_SERVICE_PROBLEM, problem);
        setResult(RESULT_OK, intent);
        finish();
    }
}
